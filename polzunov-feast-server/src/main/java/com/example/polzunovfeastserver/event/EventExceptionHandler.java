package com.example.polzunovfeastserver.event;

import com.example.polzunovfeastserver.event.exception.EventNotFoundException;
import com.example.polzunovfeastserver.place.excepition.PlaceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice(basePackages = "com.example.polzunovfeastserver.event")
@Order(Ordered.HIGHEST_PRECEDENCE) //needed to not fall into global exception handler
public class EventExceptionHandler {

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onEventNotFoundException(EventNotFoundException e) {
        String message = "Event not found";
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(PlaceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onPlaceNotFoundException(PlaceNotFoundException e) {
        String message = "Place not found";
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }
}
