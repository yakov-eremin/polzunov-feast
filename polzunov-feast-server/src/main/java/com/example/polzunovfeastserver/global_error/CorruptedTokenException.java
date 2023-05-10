package com.example.polzunovfeastserver.global_error;

public class CorruptedTokenException extends RuntimeException {
    public CorruptedTokenException(String message) {
        super(message);
    }
}
