package com.example.polzunovfeastserver.event.image.exception;

public class FailedToDeleteImageException extends RuntimeException {
    public FailedToDeleteImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
