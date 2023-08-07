package com.example.polzunovfeastserver.user;

import com.example.polzunovfeastserver.user.exception.UserNotFoundException;
import com.example.polzunovfeastserver.user.exception.WrongUserPasswordException;
import com.example.polzunovfeastserver.user.uitl.UserTableKeys;
import lombok.extern.slf4j.Slf4j;
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
        return new ErrorResponse(message, ErrorResponse.CodeEnum.WRONG_PASSWORD);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onUserNotFoundException(UserNotFoundException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.USER_NOT_FOUND);
    }

    //Exception can be thrown when database unique constraints violated
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = "Data integrity violation: " + e.getMessage();
        ErrorResponse.CodeEnum code = ErrorResponse.CodeEnum.UNKNOWN;

        String exceptionMessage = e.getMessage().toLowerCase();
        if (exceptionMessage.contains(UserTableKeys.UNIQUE_EMAIL)) {
            message = "Email is already in use";
            code = ErrorResponse.CodeEnum.EMAIL_ALREADY_EXISTS;
        } else if (exceptionMessage.contains(UserTableKeys.UNIQUE_PHONE)) {
            message = "Phone is already in use";
            code = ErrorResponse.CodeEnum.PHONE_ALREADY_EXISTS;
        }

        log.warn(message, e);
        return new ErrorResponse(message, code);
    }
}
