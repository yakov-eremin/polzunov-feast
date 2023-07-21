package com.example.polzunovfeastserver.category;

import com.example.polzunovfeastserver.category.exception.CategoryNotFoundException;
import com.example.polzunovfeastserver.category.util.CategoryTableKeys;
import com.example.polzunovfeastserver.event.util.EventTableKeys;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.openapitools.model.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice(basePackages = "com.example.polzunovfeastserver.category")
@Order(Ordered.HIGHEST_PRECEDENCE) //needed to not fall into global exception handler
public class CategoryExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onCategoryNotFoundException(CategoryNotFoundException e) {
        String message = e.getMessage();
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

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
        message = switch (cause.getConstraintName()) {
            case CategoryTableKeys.UNIQUE_NAME -> "Category name is not unique";
            case EventTableKeys.FOREIGN_CATEGORY ->
                    "Cannot delete category, because there are events associated with it";
            default -> String.format("Constraint '%s' violation: %s", cause.getConstraintName(), cause.getMessage());
        };
        log.warn(message, e);
        ErrorResponse error = new ErrorResponse();
        error.setMessage(message);
        return error;
    }

}
