package com.example.polzunovfeastserver.route.exception;

public class RouteUpdateRestrictedException extends RuntimeException {
    public RouteUpdateRestrictedException(String message) {
        super(message);
    }
}
