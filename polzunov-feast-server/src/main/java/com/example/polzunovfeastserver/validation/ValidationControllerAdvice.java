package com.example.polzunovfeastserver.validation;

import lombok.RequiredArgsConstructor;
import org.openapitools.model.ErrorResponse;
import org.openapitools.model.ValidationViolation;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class ValidationControllerAdvice {
    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage(
                messageSource.getMessage("ErrorResponse.message.validation", null, Locale.getDefault())
        );
        List<ValidationViolation> violations = new ArrayList<>();
        error.setValidationViolations(violations);
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            ValidationViolation violation = new ValidationViolation();
            violation.setFieldName(fieldError.getField());
            String message = getFieldErrorMessage(fieldError);
            violation.setMessage(message);

            violations.add(violation);
        }
        return error;
    }

    private String getFieldErrorMessage(FieldError fieldError) {
        String message;
        if (fieldError.getCode() != null) {
            message = messageSource.getMessage(
                    fieldError.getCode(),
                    fieldError.getArguments(),
                    Locale.getDefault()
            );
        } else {
            message = fieldError.getDefaultMessage();
        }
        return message;
    }
}
