package com.example.polzunovfeastserver.user.exception;

public class NullPasswordException extends RuntimeException {
    public NullPasswordException(String message) {
        super(message);
    }
}
