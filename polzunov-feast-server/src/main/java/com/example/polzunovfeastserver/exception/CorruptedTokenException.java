package com.example.polzunovfeastserver.exception;

public class CorruptedTokenException extends RuntimeException {
    public CorruptedTokenException(String message) {
        super(message);
    }
}
