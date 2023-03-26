package com.example.polzunovfeastserver.controller.error_handler;

import com.example.polzunovfeastserver.controller.UserController;
import com.example.polzunovfeastserver.exception.already_taken.EmailAlreadyTakenException;
import com.example.polzunovfeastserver.exception.already_taken.PhoneAlreadyTakenException;
import com.example.polzunovfeastserver.exception.already_taken.UsernameAlreadyTakenException;
import com.example.polzunovfeastserver.util.MessageProvider;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ErrorResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@RestControllerAdvice(assignableTypes = UserController.class)
public class UserErrorHandler {

    private final MessageProvider messageProvider;

    public UserErrorHandler(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    //###########sign in responses:
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse onBadCredentialsException(BadCredentialsException e) {
        String message = messageProvider.getMessage(
                "ErrorResponse.message.badCredentials",
                "Incorrect password");
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onUsernameNotFoundException(UsernameNotFoundException e) {
        String message = messageProvider.getMessage(
                "ErrorResponse.message.usernameNotFound",
                "Username not found");
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }
    //############################


    //##############sign up and update responses:
    @ExceptionHandler(UsernameAlreadyTakenException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onUsernameAlreadyTakenException(UsernameAlreadyTakenException e) {
        String message = messageProvider.getMessage(
                "ErrorResponse.message.alreadyTaken.username",
                "Username was already taken");
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onEmailAlreadyTakenException(EmailAlreadyTakenException e) {
        String message = messageProvider.getMessage(
                "ErrorResponse.message.alreadyTaken.email",
                "Email was already taken");
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(PhoneAlreadyTakenException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onPhoneAlreadyTakenException(PhoneAlreadyTakenException e) {
        String message = messageProvider.getMessage(
                "ErrorResponse.message.alreadyTaken.phone",
                "Phone number was already taken");
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }
    //##########################
}
