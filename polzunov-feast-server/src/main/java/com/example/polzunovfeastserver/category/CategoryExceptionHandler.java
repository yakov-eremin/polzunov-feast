package com.example.polzunovfeastserver.category;

import com.example.polzunovfeastserver.category.exception.CategoryNotFoundException;
import com.example.polzunovfeastserver.category.util.CategoryTableKeys;
import com.example.polzunovfeastserver.event.util.table_key.EventCategoriesTableKeys;
import lombok.extern.slf4j.Slf4j;
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
        return new ErrorResponse(message, ErrorResponse.CodeEnum.CATEGORY_NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = "Data integrity violation: " + e.getMessage();
        ErrorResponse.CodeEnum code = ErrorResponse.CodeEnum.UNKNOWN;

        String exceptionMessage = e.getMessage().toLowerCase();
        if (exceptionMessage.contains(CategoryTableKeys.UNIQUE_NAME)) {
            message = "Category name already exists";
            code = ErrorResponse.CodeEnum.CATEGORY_NAME_ALREADY_EXISTS;
        } else if (exceptionMessage.contains(EventCategoriesTableKeys.FOREIGN_CATEGORY)) {
            message = "Cannot delete category, because there are events associated with it";
            code = ErrorResponse.CodeEnum.CATEGORY_NOT_FOUND;
        }

        log.warn(message, e);
        return new ErrorResponse(message, code);
    }

}
