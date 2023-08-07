package com.example.polzunovfeastserver.event.exception;

public class EventAlreadyStartedException extends RuntimeException {
    public EventAlreadyStartedException(String message) {
        super(message);
    }
}
