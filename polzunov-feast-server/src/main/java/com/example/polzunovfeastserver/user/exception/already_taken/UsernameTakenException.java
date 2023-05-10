package com.example.polzunovfeastserver.user.exception.already_taken;

public class UsernameTakenException extends UserFieldTakenException {
    public UsernameTakenException(String message) {
        super(message);
    }
}