package com.example.polzunovfeastserver.user;

import com.example.polzunovfeastserver.user.exception.WrongPasswordException;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserErrorHandler {

    @ExceptionHandler(WrongPasswordException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse onWrongPasswordException(WrongPasswordException e) {
        String message = "Wrong password";
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }
}
