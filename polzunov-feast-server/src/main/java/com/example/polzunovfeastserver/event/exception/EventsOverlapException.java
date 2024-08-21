package com.example.polzunovfeastserver.event.exception;

public class EventsOverlapException extends RuntimeException {
    public EventsOverlapException(String message) {
        super(message);
    }
}
