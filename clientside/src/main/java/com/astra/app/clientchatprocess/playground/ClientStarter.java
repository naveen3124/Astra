package com.astra.app.clientchatprocess.playground;

import com.astra.app.clientchatprocess.client.Client;

public class ClientStarter {

    public static void main(String[] args) {

        new Thread(new Client()).start();

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Client Starter finishing...");
    }
}
