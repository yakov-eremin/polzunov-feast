package com.example.polzunovfeastserver.global_error;

import com.example.polzunovfeastserver.util.MessageProvider;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ErrorResponse;
import org.openapitools.model.FieldValidationViolation;
import org.openapitools.model.HttpAttributeValidationViolation;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RestControllerAdvice
public class GlobalErrorHandler implements ErrorController {

    private final MessageProvider messageProvider;

    protected GlobalErrorHandler(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    //Fields validation violations
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldValidationViolation> violations = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            FieldValidationViolation violation = new FieldValidationViolation();
            violation.setField(fieldError.getField());
            String message = messageProvider
                    .getMessage(fieldError.getCode(), fieldError.getArguments(), fieldError.getDefaultMessage());
            violation.setMessage(message);

            violations.add(violation);
        });
        ErrorResponse error = new ErrorResponse();
        String message = messageProvider.getMessage(
                "ErrorResponse.message.validation",
                "Validation failed");
        error.setMessage(message);
        error.setFieldValidationViolations(violations);
        log.warn("{}: {}", message, error, e);
        return error;
    }

    //Path variables, headers, request parameters violations
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse onConstraintViolationException(final ConstraintViolationException e) {
        List<HttpAttributeValidationViolation> violations = new LinkedList<>();
        e.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String name = propertyPath.substring(propertyPath.lastIndexOf(".") + 1);
            HttpAttributeValidationViolation attributeViolation = new HttpAttributeValidationViolation();
            attributeViolation.setName(name);
            attributeViolation.setMessage(violation.getMessage());
            violations.add(attributeViolation);
        });
        ErrorResponse error = new ErrorResponse();
        String message = messageProvider.getMessage(
                "ErrorResponse.message.validation",
                "Validation failed");
        error.setMessage(message);
        error.setHttpAttributeValidationViolation(violations);
        log.warn("{}: {}", message, error, e);
        return error;
    }

    @ExceptionHandler(CorruptedTokenException.class)
    @ResponseStatus(UNAUTHORIZED)
    public void onCorruptedTokenException(CorruptedTokenException e) {
        log.warn("Authentication token corrupted", e);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse onHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        String message = messageProvider.getMessage(
                "ErrorResponse.message.corruptedHttpRequest",
                "Http request is corrupted");
        log.warn(message, e);
        ErrorResponse error = new ErrorResponse();
        error.setMessage(message);
        return error;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse onThrowable(Throwable e) {
        log.error("Internal server error", e);
        ErrorResponse error = new ErrorResponse();
        error.setMessage(e.getMessage());
        return error;
    }
}
