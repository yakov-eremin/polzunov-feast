package com.example.polzunovfeastserver.place.excepition;

public class PlaceHasAssociatedEventsException extends RuntimeException {
    public PlaceHasAssociatedEventsException(String message) {
        super(message);
    }
}
