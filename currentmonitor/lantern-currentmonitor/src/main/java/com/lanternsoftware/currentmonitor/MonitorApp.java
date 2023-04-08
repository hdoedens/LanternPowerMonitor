package com.lanternsoftware.currentmonitor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lanternsoftware.currentmonitor.api.ApiBreakerConfig;
import com.lanternsoftware.currentmonitor.led.LEDFlasher;
import com.lanternsoftware.datamodel.currentmonitor.Breaker;
import com.lanternsoftware.datamodel.currentmonitor.BreakerConfig;
import com.lanternsoftware.datamodel.currentmonitor.BreakerHub;
import com.lanternsoftware.datamodel.currentmonitor.BreakerPower;
import com.lanternsoftware.datamodel.currentmonitor.BreakerPowerMinute;
import com.lanternsoftware.datamodel.currentmonitor.HubPowerMinute;
import com.lanternsoftware.datamodel.currentmonitor.hub.HubSample;
import com.lanternsoftware.util.CollectionUtils;
import com.lanternsoftware.util.NullUtils;
import com.lanternsoftware.util.ResourceLoader;
import com.lanternsoftware.util.concurrency.ConcurrencyUtils;
import com.lanternsoftware.util.dao.DaoEntity;
import com.lanternsoftware.util.dao.DaoSerializer;

public class MonitorApp {
	private static final Logger LOG = LoggerFactory.getLogger(MonitorApp.class);
	private static final String WORKING_DIR = "/opt/currentmonitor/";
	private static BreakerConfig breakerConfig;
	private static boolean dev = true;

	private static final AtomicBoolean running = new AtomicBoolean(true);
	private static final CurrentMonitor monitor = new CurrentMonitor();
	private static final List<BreakerPower> readings = new ArrayList<>();
	private static String version;
	private static final PowerListener logger = new PowerListener() {
		@Override
		public void onPowerEvent(BreakerPower _power) {
			if (!breakerConfig.getHub().isDebug()) {
				_power.setHubVersion(version);
				synchronized (readings) {
					readings.add(_power);
				}
			} else
				LOG.info("Panel{} - Space{} Power: {}W", _power.getPhaseId(), Breaker.toSpaceDisplay(_power.getSpace()),
						String.format("%.3f", _power.getPower()));
		}

		@Override
		public void onSampleEvent(HubSample _sample) {

		}
	};

	private static MqttPoster mqttPoster;

	public static void main(String[] args) {
		if (!dev) {
			try {
				LOG.info("Trying to restart dbus via systemctl");
				Runtime.getRuntime().exec(new String[] { "systemctl", "restart", "dbus" });
				ConcurrencyUtils.sleep(500);
			} catch (IOException _e) {
				LOG.error("Exception occurred while trying to restart", _e);
			}
		}
		version = getVersionNumber();

		int configAttempts = 0;
		while (configAttempts < 5) {
			breakerConfig = DaoSerializer.parse(
					ResourceLoader.loadFileAsString(WORKING_DIR + "breaker_config.json"),
					BreakerConfig.class);
			if ((breakerConfig != null))
				break;
			LOG.error("Failed to load breaker config.  Retrying in 5 seconds...");
			ConcurrencyUtils.sleep(5000);
			configAttempts++;
		}

		monitor.setDebug(breakerConfig.getHub().isDebug());
		monitor.setPostSamples(false);
		LEDFlasher.setLEDOn(false);
		if (NullUtils.isNotEmpty(breakerConfig.getHub().getMqttBrokerUrl()))
			mqttPoster = new MqttPoster(breakerConfig.getHub());

		if (breakerConfig != null) {
			LOG.info("Breaker Config loaded");
			BreakerHub hub = breakerConfig.getHub();
			if (hub != null) {
				if (hub.isNeedsCalibration()) {
					try {
						CalibrationResult cal = monitor.calibrateVoltage(hub.getVoltageCalibrationFactor());
						if (cal != null) {
							hub.setVoltageCalibrationFactor(cal.getVoltageCalibrationFactor());
							hub.setFrequency(cal.getFrequency());
							ApiBreakerConfig.saveCalibration(cal.getVoltageCalibrationFactor(), cal.getFrequency());
						}
					} catch (Throwable t) {
						LOG.error("Exception trying to read from voltage pin", t);
					}
				}
				List<Breaker> breakers = breakerConfig.getBreakers();
				monitor.monitorPower(hub, breakers, 1000, logger);
			}
			monitor.submit(new PowerPoster());
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			synchronized (running) {
				running.set(false);
			}

			monitor.stop();

		}, "Monitor Shutdown"));
		synchronized (monitor) {
			try {
				monitor.wait();
			} catch (InterruptedException _e) {
				LOG.error("Interrupted, shutting down", _e);
			}
		}
	}

	private static final class PowerPoster implements Runnable {
		private final long firstPost;
		private long lastPost;
		private int lastMinute;
		private final Map<Integer, Float[]> breakers = new HashMap<>();

		public PowerPoster() {
			firstPost = (new Date().getTime() / 1000) * 1000;
			lastPost = new Date().getTime();
			lastMinute = (int) (new Date().getTime() / 60000);
		}

		@Override
		public void run() {
			try {
				while (true) {
					synchronized (running) {
						if (!running.get())
							break;
					}
					DaoEntity post = null;
					int curMinute = (int) (new Date().getTime() / 60000);
					List<BreakerPower> mqttReadings = new ArrayList<>();
					synchronized (readings) {
						if (!readings.isEmpty()) {
							mqttReadings.addAll(readings);
							post = new DaoEntity("readings", DaoSerializer.toDaoEntities(readings));
							post.put("hub", breakerConfig.getHub());
							if (curMinute != lastMinute) {
								HubPowerMinute minute = new HubPowerMinute();
								minute.setMinute(lastMinute);
								minute.setBreakers(CollectionUtils.transform(breakers.entrySet(), _e -> {
									BreakerPowerMinute breaker = new BreakerPowerMinute();
									breaker.setPanel(Breaker.toPanel(_e.getKey()));
									breaker.setSpace(Breaker.toSpace(_e.getKey()));
									breaker.setReadings(CollectionUtils.asArrayList(_e.getValue()));
									return breaker;
								}));
								breakers.clear();
								lastMinute = curMinute;
							}
							for (BreakerPower power : readings) {
								Float[] breakerReadings = breakers.computeIfAbsent(
										Breaker.toId(power.getSpace(), power.getPhaseId()), _i -> new Float[60]);
								breakerReadings[(int) ((power.getReadTime().getTime() / 1000) % 60)] = (float) power
										.getPower();
							}
							readings.clear();
						}
					}
					if (mqttPoster != null)
						monitor.submit(() -> mqttPoster.postPower(mqttReadings));
					long now = new Date().getTime();
					long duration = (now - firstPost) % 1000;
					if (now - lastPost < 1000) {
						ConcurrencyUtils.sleep(1000 - duration);
					}
					lastPost = now;
				}
			} catch (Throwable t) {
				LOG.error("Exception in PowerPoster", t);
			}
		}
	}

	public static String getVersionNumber() {
		try {
			Enumeration<URL> resources = MonitorApp.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
			while (resources.hasMoreElements()) {
				InputStream is = null;
				try {
					is = resources.nextElement().openStream();
					Manifest manifest = new Manifest(is);
					Attributes attr = manifest.getMainAttributes();
					if (NullUtils.isEqual(attr.getValue("Specification-Title"), "Lantern Power Monitor")) {
						String version = attr.getValue("Specification-Version");
						LOG.info("Current Version: {}", version);
						return version;
					}
				} finally {
					IOUtils.closeQuietly(is);
				}
			}
		} catch (Exception _e) {
			LOG.error("Failed to get current version number", _e);
		}
		return "";
	}
}
