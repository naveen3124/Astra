package com.astra.app.clientchatprocess.client;

public class ServerDownException extends RuntimeException {

    public ServerDownException() {
        super("The server is down");
    }
}
