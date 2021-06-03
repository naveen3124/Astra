package com.astra.app.charprocess.client;

public class ServerDownException extends RuntimeException {

    public ServerDownException() {
        super("The server is down");
    }
}
