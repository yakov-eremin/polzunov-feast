package com.example.polzunovfeastserver.exception.already_taken;

public class UsernameAlreadyTakenException extends AlreadyTakenException {
    public UsernameAlreadyTakenException(String message) {
        super(message);
    }
}