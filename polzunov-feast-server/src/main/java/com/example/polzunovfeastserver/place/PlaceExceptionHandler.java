package com.example.polzunovfeastserver.place;

import com.example.polzunovfeastserver.event.util.EventTableKeys;
import com.example.polzunovfeastserver.place.excepition.PlaceHasAssociatedEventsException;
import com.example.polzunovfeastserver.place.excepition.PlaceNotFoundException;
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
@RestControllerAdvice(basePackages = "com.example.polzunovfeastserver.place")
@Order(Ordered.HIGHEST_PRECEDENCE) //needed to not fall into global exception handler
public class PlaceExceptionHandler {

    @ExceptionHandler(PlaceHasAssociatedEventsException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onPlaceHasAssociatedEventsException(PlaceHasAssociatedEventsException e) {
        String message = e.getMessage();
        log.warn(message, e);
        return new ErrorResponse(message, ErrorResponse.CodeEnum.PLACE_HAS_ASSOCIATED_EVENTS);
    }

    @ExceptionHandler(PlaceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onPlaceNotFoundException(PlaceNotFoundException e) {
        String message = "Place not found";
        log.warn(message, e);
        return new ErrorResponse(message, ErrorResponse.CodeEnum.PLACE_NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = "Data integrity violation: " + e.getMessage();
        ErrorResponse.CodeEnum code = ErrorResponse.CodeEnum.UNKNOWN;

        String exceptionMessage = e.getMessage().toLowerCase();
        if (exceptionMessage.contains(EventTableKeys.FOREIGN_PLACE)) {
            message = "Cannot delete place, because there are events associated with it";
            code = ErrorResponse.CodeEnum.PLACE_HAS_ASSOCIATED_EVENTS;
        }

        return new ErrorResponse(message, code);
    }
}
