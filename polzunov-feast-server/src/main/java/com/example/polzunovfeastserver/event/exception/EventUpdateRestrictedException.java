package com.example.polzunovfeastserver.event.exception;

public class EventUpdateRestrictedException extends RuntimeException {
    public EventUpdateRestrictedException(String message) {
        super(message);
    }
}
