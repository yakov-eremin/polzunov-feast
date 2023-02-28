package com.example.polzunovfeastserver.controller;

import com.example.polzunovfeastserver.exception.registration.DefaultMessageNotProvidedException;
import com.example.polzunovfeastserver.exception.registration.UsernameAlreadyTakenException;
import lombok.RequiredArgsConstructor;
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

@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse onBadCredentialsException(BadCredentialsException e) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage(
                getErrorMessage("ErrorResponse.message.badCredentials", e)
        );
        return response;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onUsernameNotFoundException(UsernameNotFoundException e) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage(
                getErrorMessage("ErrorResponse.message.usernameNotFound", e)
        );
        return response;
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse UsernameAlreadyTakenException(UsernameAlreadyTakenException e) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage(
                getErrorMessage("ErrorResponse.message.usernameAlreadyTaken", e)
        );
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage(
                getErrorMessage("ErrorResponse.message.validation", e)
        );
        List<ValidationViolation> violations = new ArrayList<>();
        error.setValidationViolations(violations);
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            ValidationViolation violation = new ValidationViolation();
            violation.setFieldName(fieldError.getField());
            String message = getViolationMessage(fieldError);
            violation.setMessage(message);

            violations.add(violation);
        }
        return error;
    }

    private String getViolationMessage(FieldError fieldError) {
        String message;
        try {
            message = messageSource.getMessage(
                    fieldError.getCode(),
                    fieldError.getArguments(),
                    Locale.getDefault()
            );
        } catch (NoSuchMessageException e) {
            //TODO добавить логирование о том, что не нашлось сообщения
            message = fieldError.getDefaultMessage();
        }
        if (message == null)
            throw new DefaultMessageNotProvidedException(
                    String.format(
                            "No default message provided for field \"%s\"", fieldError.getField())
            );

        return message;
    }

    //TODO как передавать аргументы к сообщению об ошибке?
    private String getErrorMessage(String errorCode, Exception e) {
        String message;
        try {
            message = messageSource.getMessage(
                    errorCode, null, Locale.getDefault()
            );
        } catch (NoSuchMessageException ex) {
            //TODO добавить логирование о том, что не нашлось сообщения
            message = e.getMessage();
        }

        if (message == null)
            throw new DefaultMessageNotProvidedException("No message provided for exception", e);

        return message;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse onThrowable(Throwable e) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Internal server error");
        //TODO добавить логирование с сообщением ошибки
        return response;
    }

    //TODO как переопределить, что возвращается при обращении по несуществующему пути (пока что возвращает дефолтное сообщение спринга)
}
