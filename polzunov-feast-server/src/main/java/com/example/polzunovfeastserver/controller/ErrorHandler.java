package com.example.polzunovfeastserver.controller;

import com.example.polzunovfeastserver.exception.CorruptedTokenException;
import com.example.polzunovfeastserver.exception.UserIdNotFoundException;
import com.example.polzunovfeastserver.exception.already_taken.EmailAlreadyTakenException;
import com.example.polzunovfeastserver.exception.already_taken.PhoneAlreadyTakenException;
import com.example.polzunovfeastserver.exception.already_taken.UsernameAlreadyTakenException;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ErrorResponse;
import org.openapitools.model.ValidationViolation;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    private final MessageSource messageSource;

    public ErrorHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(CorruptedTokenException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse onCorruptedTokenException(CorruptedTokenException e) {
        String message = getErrorMessage(
                "ErrorResponse.message.corruptedToken", e, "Provided auth token is corrupted"
        );
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse onBadCredentialsException(BadCredentialsException e) {
        String message = getErrorMessage(
                "ErrorResponse.message.badCredentials", e, "Incorrect password");
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onUsernameNotFoundException(UsernameNotFoundException e) {
        String message = getErrorMessage(
                "ErrorResponse.message.usernameNotFound", e, "Username not found");
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(UserIdNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onIdNotFoundException(UserIdNotFoundException e) {
        String message = getErrorMessage(
                "ErrorResponse.message.userIdNotFound", e, "User id not found");
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onUsernameAlreadyTakenException(UsernameAlreadyTakenException e) {
        String message = getErrorMessage(
                "ErrorResponse.message.alreadyTaken.username",
                e,
                "Username was already taken"
        );
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onEmailAlreadyTakenException(EmailAlreadyTakenException e) {
        String message = getErrorMessage(
                "ErrorResponse.message.alreadyTaken.email",
                e,
                "Email was already taken"
        );
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(PhoneAlreadyTakenException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onPhoneAlreadyTakenException(PhoneAlreadyTakenException e) {
        String message = getErrorMessage(
                "ErrorResponse.message.alreadyTaken.phone",
                e,
                "Phone number was already taken"
        );
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ValidationViolation> violations = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            ValidationViolation violation = new ValidationViolation();
            violation.setFieldName(fieldError.getField());
            String message = getViolationMessage(fieldError);
            violation.setMessage(message);

            violations.add(violation);
        }
        ErrorResponse error = new ErrorResponse();
        String message = getErrorMessage(
                "ErrorResponse.message.validation", e, "Validation failed");
        error.setMessage(message);
        error.setValidationViolations(violations);
        log.warn("{}: {}", message, error, e);
        return error;
    }

    private String getViolationMessage(FieldError fieldError) {
        String errorCode = fieldError.getCode();
        Object[] args = fieldError.getArguments();
        String objectName = fieldError.getObjectName();
        String field = fieldError.getField();

        String message;
        try {
            message = messageSource.getMessage(errorCode, args, Locale.getDefault());
        } catch (NoSuchMessageException e) {
            log.warn("Message for error code = \"{}\" and args = \"{}\" wasn't found. Object name = \"{}\", field = \"{}\"",
                    errorCode, args, objectName, field);
            message = fieldError.getDefaultMessage();
        }
        if (message == null) {
            log.warn("Default message for field = \"{}\" of object = \"{}\" wasn't found",
                    field, objectName);
            return "Unknown validation error";
        }

        return message;
    }

    //TODO how to pass error arguments? What should carry those args? Probably, exceptions
    private String getErrorMessage(String errorCode, Throwable e, String defaultMessage) {
        String message;
        try {
            message = messageSource.getMessage(errorCode, null, Locale.getDefault());
        } catch (NoSuchMessageException ex) {
            log.warn("Message for error code = \"{}\" wasn't found", errorCode);
            message = e.getMessage();
        }

        if (message == null) {
            log.warn("Exception didn't have a message", e);
            message = defaultMessage;
        }

        return message;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse onThrowable(Throwable e) {
        log.error("Unexpected error occurred", e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Internal server error");
        return response;
    }
}
