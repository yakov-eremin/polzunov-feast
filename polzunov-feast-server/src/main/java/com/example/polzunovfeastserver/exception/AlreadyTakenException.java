package com.example.polzunovfeastserver.exception;

public abstract class AlreadyTakenException extends RuntimeException {
    public AlreadyTakenException(String message) {
        super(message);
    }
}