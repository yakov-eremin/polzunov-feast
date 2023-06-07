package com.example.polzunovfeastserver.user;

import com.example.polzunovfeastserver.user.exception.UserNotFoundException;
import com.example.polzunovfeastserver.user.exception.WrongUserPasswordException;
import com.example.polzunovfeastserver.user.uitl.UserUniqueKey;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.openapitools.model.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice(basePackages = "com.example.polzunovfeastserver.user")
@Order(Ordered.HIGHEST_PRECEDENCE) //needed to not fall into global exception handler
public class UserExceptionHandler {

    @ExceptionHandler(WrongUserPasswordException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse onWrongPasswordException(WrongUserPasswordException e) {
        String message = "Wrong password";
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onUserNotFoundException(UserNotFoundException e) {
        String message = "User not found";
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    //Exception can be thrown when database unique constraints violated
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message;
        if (!(e.getCause() instanceof ConstraintViolationException cause)) {
            message = "Data integrity violation: ";
            log.warn(message, e);
            ErrorResponse error = new ErrorResponse();
            error.setMessage(message + e.getMessage());
            return error;
        }

        //Unique key constraints violation
        switch (cause.getConstraintName()) {
            case UserUniqueKey.USERNAME -> message = "Username is not unique";
            case UserUniqueKey.EMAIL -> message = "Email is not unique";
            case UserUniqueKey.PHONE -> message = "Phone is not unique";
            default -> message = String.format("Constraint '%s' violation: %s", cause.getConstraintName(), cause.getMessage());
        }
        log.warn(message, e);
        ErrorResponse error = new ErrorResponse();
        error.setMessage(message);
        return error;
    }
}
