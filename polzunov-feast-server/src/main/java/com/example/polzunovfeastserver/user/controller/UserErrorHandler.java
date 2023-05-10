package com.example.polzunovfeastserver.user.controller;

import com.example.polzunovfeastserver.user.exception.NullPasswordException;
import com.example.polzunovfeastserver.user.exception.UserNotFoundException;
import com.example.polzunovfeastserver.user.exception.WrongPasswordException;
import com.example.polzunovfeastserver.user.exception.already_taken.EmailTakenException;
import com.example.polzunovfeastserver.user.exception.already_taken.PhoneTakenException;
import com.example.polzunovfeastserver.user.exception.already_taken.UsernameTakenException;
import com.example.polzunovfeastserver.util.MessageProvider;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserErrorHandler {

    private final MessageProvider messageProvider;

    public UserErrorHandler(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    @ExceptionHandler(WrongPasswordException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse onWrongPasswordException(WrongPasswordException e) {
        String message = messageProvider.getMessage(
                "ErrorResponse.message.badPassword",
                "Incorrect password");
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(NullPasswordException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse onNullPasswordException(NullPasswordException e) {
        String message = messageProvider.getMessage(
                "ErrorResponse.message.nullPassword",
                "Password cannot be null");
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onUserNotFoundException(UserNotFoundException e) {
        String message = messageProvider.getMessage(
                "ErrorResponse.message.userNotFound",
                "User not found");
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(UsernameTakenException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onUsernameAlreadyTakenException(UsernameTakenException e) {
        String message = messageProvider.getMessage(
                "ErrorResponse.message.alreadyTaken.username",
                "Username was already taken");
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(EmailTakenException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onEmailAlreadyTakenException(EmailTakenException e) {
        String message = messageProvider.getMessage(
                "ErrorResponse.message.alreadyTaken.email",
                "Email was already taken");
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(PhoneTakenException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onPhoneAlreadyTakenException(PhoneTakenException e) {
        String message = messageProvider.getMessage(
                "ErrorResponse.message.alreadyTaken.phone",
                "Phone number was already taken");
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }
}
