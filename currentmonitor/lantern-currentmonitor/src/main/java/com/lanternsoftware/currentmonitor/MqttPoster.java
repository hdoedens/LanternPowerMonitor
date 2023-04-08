package com.lanternsoftware.currentmonitor;

import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lanternsoftware.datamodel.currentmonitor.BreakerHub;
import com.lanternsoftware.datamodel.currentmonitor.BreakerPower;
import com.lanternsoftware.util.CollectionUtils;
import com.lanternsoftware.util.NullUtils;
import com.lanternsoftware.util.dao.DaoSerializer;

public class MqttPoster {
    private static final Logger LOG = LoggerFactory.getLogger(MqttPoster.class);

    private final IMqttClient client;

    public MqttPoster(BreakerHub _hub) {
        IMqttClient c = null;
        try {
            LOG.info("Attempting to connect to MQTT broker at {}", _hub.getMqttBrokerUrl());
            c = new MqttClient(_hub.getMqttBrokerUrl(), String.format("Lantern_Power_Monitor_Hub"));
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            if (NullUtils.isNotEmpty(_hub.getMqttUsername()))
                options.setUserName(_hub.getMqttUsername());
            if (NullUtils.isNotEmpty(_hub.getMqttPassword()))
                options.setPassword(_hub.getMqttPassword().toCharArray());
            c.connect(options);
        } catch (Exception e) {
            LOG.error("Failed to create MQTT client", e);
        }
        client = c;
    }

    public void postPower(List<BreakerPower> _power) {
        for (BreakerPower power : CollectionUtils.makeNotNull(_power)) {
            String topic = "lantern_power_monitor/breaker_power/" + power.getKey();
            MqttMessage msg = new MqttMessage(NullUtils.toByteArray(DaoSerializer.toJson(power)));
            msg.setQos(2);
            msg.setRetained(true);
            try {
                client.publish(topic, msg);
            } catch (Exception e) {
                LOG.error("Failed to publish message to {}", topic, e);
            }
        }
    }
}
