package com.example.polzunovfeastserver.exception.registration;

public abstract class RegistrationException extends RuntimeException {
    
    public RegistrationException(String message) {
        super(message);
    }
}
