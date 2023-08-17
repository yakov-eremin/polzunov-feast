package com.example.polzunovfeastserver.image.exception;

public class FailedToSaveImageException extends RuntimeException {
    public FailedToSaveImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
