package com.lanternsoftware.currentmonitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lanternsoftware.datamodel.currentmonitor.Breaker;
import com.lanternsoftware.datamodel.currentmonitor.BreakerConfig;
import com.lanternsoftware.datamodel.currentmonitor.BreakerHub;
import com.lanternsoftware.datamodel.currentmonitor.BreakerPower;
import com.lanternsoftware.datamodel.currentmonitor.BreakerPowerMinute;
import com.lanternsoftware.datamodel.currentmonitor.HubPowerMinute;
import com.lanternsoftware.datamodel.currentmonitor.hub.HubSample;
import com.lanternsoftware.util.CollectionUtils;
import com.lanternsoftware.util.ResourceLoader;
import com.lanternsoftware.util.concurrency.ConcurrencyUtils;
import com.lanternsoftware.util.dao.DaoEntity;
import com.lanternsoftware.util.dao.DaoSerializer;

class MqttMonitorApp {

    private static final Logger LOG = LoggerFactory.getLogger(MqttMonitorApp.class);
    private static final String WORKING_DIR = "/opt/currentmonitor/";
    private static MonitorConfig config;
    private static BreakerConfig breakerConfig;
    private static final CurrentMonitor monitor = new CurrentMonitor();
    private static final List<BreakerPower> readings = new ArrayList<>();
    private static String version;
    private static MqttPoster mqttPoster;
    private static final AtomicBoolean running = new AtomicBoolean(true);
    private static final PowerListener logger = new PowerListener() {
        @Override
        public void onPowerEvent(BreakerPower _power) {
            if (!config.isDebug()) {
                _power.setHubVersion(version);
                synchronized (readings) {
                    readings.add(_power);
                }
            } else
                LOG.info("Panel{} - Group{} Power: {}W", _power.getPanel(), Breaker.toSpaceDisplay(_power.getGroup()),
                        String.format("%.3f", _power.getPower()));
        }

        @Override
        public void onSampleEvent(HubSample _sample) {
            // post(DaoSerializer.toZipBson(_sample), "sample");
        }
    };

    public static void main(String[] args) {
        try {
            Runtime.getRuntime().exec(new String[] { "systemctl", "restart", "dbus" });
            ConcurrencyUtils.sleep(500);
        } catch (IOException _e) {
            LOG.error("Exception occurred while trying to restart", _e);
        }

        config = DaoSerializer.parse(ResourceLoader.loadFileAsString(WORKING_DIR + "config.json"), MonitorConfig.class);
        if (config == null) {
            config = new MonitorConfig();
            ResourceLoader.writeFile(WORKING_DIR + "config.json", DaoSerializer.toJson(config));
        }
        monitor.setDebug(config.isDebug());
        mqttPoster = new MqttPoster(config);

        breakerConfig = DaoSerializer.parse(ResourceLoader.loadFileAsString(WORKING_DIR + "breaker_config.json"),
                BreakerConfig.class);
        if (breakerConfig == null) {
            breakerConfig = new BreakerConfig();
        }

        if (breakerConfig != null) {
            LOG.info("Breaker Config loaded");
            BreakerHub hub = breakerConfig.getHub(config.getHub());
            if (hub != null) {
                if (config.isNeedsCalibration()) {
                    try {
                        CalibrationResult cal = monitor.calibrateVoltage(hub.getVoltageCalibrationFactor());
                        if (cal != null) {
                            hub.setVoltageCalibrationFactor(cal.getVoltageCalibrationFactor());
                            hub.setFrequency(cal.getFrequency());
                            config.setNeedsCalibration(false);
                            ResourceLoader.writeFile(WORKING_DIR + "config.json", DaoSerializer.toJson(config));
                        }
                    } catch (Throwable t) {
                        LOG.error("Exception trying to read from voltage pin", t);
                    }
                }
                List<Breaker> breakers = breakerConfig.getBreakersForHub(config.getHub());
                monitor.monitorPower(hub, breakers, 1000, logger);
            }
            monitor.submit(new PowerPoster());
        } else {
            LOG.error("Unable to load Breaker Config. Fix config manually and restart PowerMonitor");
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
                            post.put("hub", config.getHub());
                            if (curMinute != lastMinute) {
                                HubPowerMinute minute = new HubPowerMinute();
                                minute.setHub(config.getHub());
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
                                        Breaker.toId(power.getPanel(), power.getGroup()), _i -> new Float[60]);
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
}
