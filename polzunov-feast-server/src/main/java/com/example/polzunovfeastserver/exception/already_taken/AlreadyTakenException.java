package com.example.polzunovfeastserver.exception.already_taken;

public abstract class AlreadyTakenException extends RuntimeException {
    public AlreadyTakenException(String message) {
        super(message);
    }
}