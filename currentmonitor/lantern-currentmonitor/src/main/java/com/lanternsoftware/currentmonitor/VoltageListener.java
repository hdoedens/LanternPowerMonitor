package com.lanternsoftware.currentmonitor;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class VoltageListener implements MqttCallback {
    // private final Logger LOG = LoggerFactory.getLogger(VoltageListener.class);
    private IMqttClient client;

    private final String BROKER_URL = "tcp://192.168.1.28:1883";
    private final String USERNAME = "mqtt";
    private final String PASSWORD = "passwd";

    public double voltage = 230.0;

    public VoltageListener() {
        try {
            client = new MqttClient(BROKER_URL, "1");
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setUserName(USERNAME);
            options.setPassword(PASSWORD.toCharArray());
            client.connect(options);
        } catch (Exception e) {
            System.out.println("Failed to create MQTT client" + e.getMessage());
        }

    }

    public void startListening() {
        try {
            client.subscribe("dsmr/reading/phase_voltage_l2");
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(this);
    }

    @Override
    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload());
        System.out.println("Current voltage: " + payload);
        voltage = Double.parseDouble(payload);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub

    }
}
