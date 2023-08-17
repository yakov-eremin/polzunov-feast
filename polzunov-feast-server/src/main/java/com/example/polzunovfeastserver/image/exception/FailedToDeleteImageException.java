package com.example.polzunovfeastserver.image.exception;

public class FailedToDeleteImageException extends RuntimeException {
    public FailedToDeleteImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
