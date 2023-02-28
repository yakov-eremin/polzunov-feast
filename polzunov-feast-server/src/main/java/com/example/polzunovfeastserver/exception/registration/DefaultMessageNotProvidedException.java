package com.example.polzunovfeastserver.exception.registration;

public class DefaultMessageNotProvidedException extends RuntimeException {
    public DefaultMessageNotProvidedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DefaultMessageNotProvidedException(String message) {
        super(message);
    }
}