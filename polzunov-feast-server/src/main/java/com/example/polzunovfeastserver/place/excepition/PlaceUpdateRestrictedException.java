package com.example.polzunovfeastserver.place.excepition;

public class PlaceUpdateRestrictedException extends RuntimeException {
    public PlaceUpdateRestrictedException(String message) {
        super(message);
    }
}
