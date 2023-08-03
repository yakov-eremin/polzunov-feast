package com.example.polzunovfeastserver.place;

import com.example.polzunovfeastserver.event.util.EventTableKeys;
import com.example.polzunovfeastserver.place.excepition.PlaceNotFoundException;
import com.example.polzunovfeastserver.place.excepition.PlaceUpdateRestrictedException;
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
@RestControllerAdvice(basePackages = "com.example.polzunovfeastserver.place")
@Order(Ordered.HIGHEST_PRECEDENCE) //needed to not fall into global exception handler
public class PlaceExceptionHandler {

    @ExceptionHandler(PlaceUpdateRestrictedException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onPlaceUpdateRestrictedException(PlaceUpdateRestrictedException e) {
        String message = e.getMessage();
        log.warn(message, e);
        return new ErrorResponse(message);
    }

    @ExceptionHandler(PlaceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onPlaceNotFoundException(PlaceNotFoundException e) {
        String message = "Place not found";
        log.warn(message, e);
        return new ErrorResponse(message);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message;
        if (!(e.getCause() instanceof ConstraintViolationException cause)) {
            message = "Data integrity violation: ";
            log.warn(message, e);
            return new ErrorResponse(message + e.getMessage());
        }

        //Foreign key constraints violation
        if (cause.getConstraintName().equals(EventTableKeys.FOREIGN_PLACE)) {
            message = "Cannot delete place, because there are events associated with it";
        } else {
            message = String.format("Constraint '%s' violation: %s", cause.getConstraintName(), cause.getMessage());
        }
        log.warn(message, e);
        return new ErrorResponse(message);
    }
}
