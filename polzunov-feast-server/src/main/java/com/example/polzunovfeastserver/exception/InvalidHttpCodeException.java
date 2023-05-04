package com.example.polzunovfeastserver.exception;

public class InvalidHttpCodeException extends RuntimeException{
    public InvalidHttpCodeException(String message) {
        super(message);
    }
}
