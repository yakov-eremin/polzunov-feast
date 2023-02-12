package com.example.polzunovfeastserver.exception.registration;

public class UsernameAlreadyTakenException extends RegistrationException {
    public UsernameAlreadyTakenException(String msg) {
        super(msg);
    }
}