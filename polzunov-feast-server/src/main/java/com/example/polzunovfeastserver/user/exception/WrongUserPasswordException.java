package com.example.polzunovfeastserver.user.exception;

public class WrongUserPasswordException extends RuntimeException {
    public WrongUserPasswordException(String message) {
        super(message);
    }
}
