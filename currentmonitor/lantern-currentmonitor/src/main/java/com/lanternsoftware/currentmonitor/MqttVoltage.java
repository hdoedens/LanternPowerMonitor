package com.lanternsoftware.currentmonitor;

public class MqttVoltage {

    static VoltageListener voltageListener;

    public static void main(String[] args) {

        // Create a flag to control the loop
        boolean keepRunning = true;

        voltageListener = new VoltageListener();

        // Kick off an asynchronous computation in a separate thread
        new Thread(() -> {
            voltageListener.startListening();
        }).start();

        // Use a while loop to continuously process the results of the asynchronous
        // computation
        while (keepRunning) {
            // Use a callback to process the result and create a new CompletableFuture for
            // the next iteration

            // Do other work
            System.out.println("Doing other work..." + voltageListener.voltage);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
