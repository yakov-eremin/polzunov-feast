package com.example.polzunovfeastserver.event;

import com.example.polzunovfeastserver.event.exception.EventAlreadyStartedException;
import com.example.polzunovfeastserver.event.exception.EventHasAssociatedRoutesException;
import com.example.polzunovfeastserver.event.exception.EventNotFoundException;
import com.example.polzunovfeastserver.event.image.exception.FailedToDeleteImageException;
import com.example.polzunovfeastserver.event.image.exception.FailedToSaveImageException;
import com.example.polzunovfeastserver.event.image.exception.ImageUrlNotFoundException;
import com.example.polzunovfeastserver.event.image.exception.UnsupportedImageTypeException;
import com.example.polzunovfeastserver.place.excepition.PlaceNotFoundException;
import com.example.polzunovfeastserver.route.node.util.RouteNodeTableKeys;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice(basePackages = "com.example.polzunovfeastserver.event")
@Order(Ordered.HIGHEST_PRECEDENCE) //needed to not fall into global exception handler
public class EventExceptionHandler {

    @ExceptionHandler(UnsupportedImageTypeException.class)
    @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
    public ErrorResponse onUnsupportedImageTypeException(UnsupportedImageTypeException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.UNSUPPORTED_IMAGE_TYPE);
    }

    @ExceptionHandler(FailedToSaveImageException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse onFailedToSaveImageException(FailedToSaveImageException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FailedToDeleteImageException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse onFailedToDeleteImageException(FailedToDeleteImageException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ImageUrlNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onImageUrlNotFoundException(ImageUrlNotFoundException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.IMAGE_NOT_FOUND);
    }

    @ExceptionHandler(EventAlreadyStartedException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onEventAlreadyStartedException(EventAlreadyStartedException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.EVENT_ALREADY_STARTED);
    }

    @ExceptionHandler(EventHasAssociatedRoutesException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onEventHasAssociatedRoutesException(EventHasAssociatedRoutesException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.EVENT_HAS_ASSOCIATED_ROUTES);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = "Data integrity violation: " + e.getMessage();
        ErrorResponse.CodeEnum code = ErrorResponse.CodeEnum.UNKNOWN;

        String exceptionMessage = e.getMessage().toLowerCase();
        if (exceptionMessage.contains(RouteNodeTableKeys.FOREIGN_EVENT)) {
            message = "Cannot delete event, because there are routes associated with it";
            code = ErrorResponse.CodeEnum.EVENT_NOT_FOUND;
        }

        log.warn(message, e);
        return new ErrorResponse(message, code);
    }

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onEventNotFoundException(EventNotFoundException e) {
        String message = "Event not found";
        log.warn(message, e);
        return new ErrorResponse(message, ErrorResponse.CodeEnum.EVENT_NOT_FOUND);
    }

    @ExceptionHandler(PlaceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onPlaceNotFoundException(PlaceNotFoundException e) {
        String message = "Place not found";
        log.warn(message, e);
        return new ErrorResponse(message, ErrorResponse.CodeEnum.PLACE_NOT_FOUND);
    }
}
