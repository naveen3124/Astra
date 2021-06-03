package com.astra.app.charprocess.exception;

public class EmptyMessageException extends RuntimeException {

    public EmptyMessageException() {
        super("The message is empty without both header and body");
    }

}
