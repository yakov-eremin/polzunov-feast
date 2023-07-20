package com.example.polzunovfeastserver.route;

import com.example.polzunovfeastserver.event.exception.EventNotFoundException;
import com.example.polzunovfeastserver.route.exception.RouteUpdateRestrictedException;
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
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onEventNotFoundException(EventNotFoundException e) {
        log.warn(e.getMessage(), e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(e.getMessage());
        return response;
    }

    @ExceptionHandler(RouteUpdateRestrictedException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onRouteUpdateRestrictedException(RouteUpdateRestrictedException e) {
        log.warn(e.getMessage(), e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(e.getMessage());
        return response;
    }
}
