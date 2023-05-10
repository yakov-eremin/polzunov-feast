package com.example.polzunovfeastserver.user.exception.already_taken;

public abstract class UserFieldTakenException extends RuntimeException {
    public UserFieldTakenException(String message) {
        super(message);
    }
}