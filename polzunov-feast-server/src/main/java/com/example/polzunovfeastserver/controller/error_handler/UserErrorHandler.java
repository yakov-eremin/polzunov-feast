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
public class UserErrorHandler extends AbstractErrorHandler {

    public UserErrorHandler(MessageProvider messageProvider) {
        super(messageProvider);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse onBadCredentialsException(BadCredentialsException e) {
        return defaultExceptionHandling(
                "ErrorResponse.message.badCredentials",
                "Incorrect password", e
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onUsernameNotFoundException(UsernameNotFoundException e) {
        return defaultExceptionHandling(
                "ErrorResponse.message.usernameNotFound",
                "Username not found", e
        );
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onUsernameAlreadyTakenException(UsernameAlreadyTakenException e) {
        return defaultExceptionHandling(
                "ErrorResponse.message.alreadyTaken.username",
                "Username was already taken", e
        );
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onEmailAlreadyTakenException(EmailAlreadyTakenException e) {
        return defaultExceptionHandling(
                "ErrorResponse.message.alreadyTaken.email",
                "Email was already taken", e
        );
    }

    @ExceptionHandler(PhoneAlreadyTakenException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onPhoneAlreadyTakenException(PhoneAlreadyTakenException e) {
        return defaultExceptionHandling(
                "ErrorResponse.message.alreadyTaken.phone",
                "Phone number was already taken", e
        );
    }
}
