package com.example.polzunovfeastserver.route;

import com.example.polzunovfeastserver.event.exception.EventAlreadyStartedException;
import com.example.polzunovfeastserver.event.exception.EventCanceledException;
import com.example.polzunovfeastserver.event.exception.EventNotFoundException;
import com.example.polzunovfeastserver.event.exception.EventsOverlapException;
import com.example.polzunovfeastserver.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice(basePackages = "com.example.polzunovfeastserver.route")
@Order(Ordered.HIGHEST_PRECEDENCE) //needed to not fall into global exception handler
public class RouteExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onUserNotFoundException(UserNotFoundException e) {
        String message = "User not found";
        log.warn(message, e);
        return new ErrorResponse(message, ErrorResponse.CodeEnum.USER_NOT_FOUND);
    }

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onEventNotFoundException(EventNotFoundException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.EVENT_NOT_FOUND);
    }

    @ExceptionHandler(EventCanceledException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onEventCanceledException(EventCanceledException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.EVENT_CANCELED);
    }

    @ExceptionHandler(EventAlreadyStartedException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onEventAlreadyStartedException(EventAlreadyStartedException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.EVENT_ALREADY_STARTED);
    }


    @ExceptionHandler(EventsOverlapException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onEventsOverlapException(EventsOverlapException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.EVENTS_TIME_OVERLAP);
    }
}
