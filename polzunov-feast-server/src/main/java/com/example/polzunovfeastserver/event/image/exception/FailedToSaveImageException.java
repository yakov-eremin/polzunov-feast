package com.example.polzunovfeastserver.event.image.exception;

public class FailedToSaveImageException extends RuntimeException {
    public FailedToSaveImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
