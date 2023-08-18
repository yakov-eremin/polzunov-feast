package com.example.polzunovfeastserver.exception;

import com.example.polzunovfeastserver.category.exception.CategoryNotFoundException;
import com.example.polzunovfeastserver.event.exception.*;
import com.example.polzunovfeastserver.image.exception.FailedToDeleteImageException;
import com.example.polzunovfeastserver.image.exception.FailedToSaveImageException;
import com.example.polzunovfeastserver.image.exception.ImageUrlNotFoundException;
import com.example.polzunovfeastserver.image.exception.UnsupportedImageTypeException;
import com.example.polzunovfeastserver.place.excepition.PlaceHasAssociatedEventsException;
import com.example.polzunovfeastserver.place.excepition.PlaceNotFoundException;
import com.example.polzunovfeastserver.user.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ErrorResponse;
import org.openapitools.model.FieldValidationViolation;
import org.openapitools.model.HttpAttributeValidationViolation;
import org.openapitools.model.ObjectValidationViolation;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //---user

    /**
     * {@link BadCredentialsException} can be thrown by {@link org.springframework.security.authentication.AuthenticationManager}
     * when authenticating user.
     */
    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse onBadCredentialsException(BadCredentialsException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.WRONG_PASSWORD);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onUsernameNotFoundException(UsernameNotFoundException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.USER_NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onUserNotFoundException(UserNotFoundException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.USER_NOT_FOUND);
    }
    //---

    //---place
    @ExceptionHandler(PlaceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onPlaceNotFoundException(PlaceNotFoundException e) {
        String message = "Place not found";
        log.warn(message, e);
        return new ErrorResponse(message, ErrorResponse.CodeEnum.PLACE_NOT_FOUND);
    }

    @ExceptionHandler(PlaceHasAssociatedEventsException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onPlaceHasAssociatedEventsException(PlaceHasAssociatedEventsException e) {
        String message = e.getMessage();
        log.warn(message, e);
        return new ErrorResponse(message, ErrorResponse.CodeEnum.PLACE_HAS_ASSOCIATED_EVENTS);
    }
    //---

    //---image
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

    @ExceptionHandler(UnsupportedImageTypeException.class)
    @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
    public ErrorResponse onUnsupportedImageTypeException(UnsupportedImageTypeException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.UNSUPPORTED_IMAGE_TYPE);
    }

    @ExceptionHandler(ImageUrlNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onImageUrlNotFoundException(ImageUrlNotFoundException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.IMAGE_NOT_FOUND);
    }
    //---

    //---event
    @ExceptionHandler(EventAlreadyStartedException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onEventAlreadyStartedException(EventAlreadyStartedException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.EVENT_ALREADY_STARTED);
    }

    @ExceptionHandler(EventCanceledException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onEventCanceledException(EventCanceledException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.EVENT_CANCELED);
    }

    @ExceptionHandler(EventsOverlapException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onEventsOverlapException(EventsOverlapException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.EVENTS_TIME_OVERLAP);
    }

    @ExceptionHandler(EventHasAssociatedRoutesException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse onEventHasAssociatedRoutesException(EventHasAssociatedRoutesException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), ErrorResponse.CodeEnum.EVENT_HAS_ASSOCIATED_ROUTES);
    }

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onEventNotFoundException(EventNotFoundException e) {
        String message = "Event not found";
        log.warn(message, e);
        return new ErrorResponse(message, ErrorResponse.CodeEnum.EVENT_NOT_FOUND);
    }
    //---

    //---category
    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse onCategoryNotFoundException(CategoryNotFoundException e) {
        String message = e.getMessage();
        log.warn(message, e);
        return new ErrorResponse(message, ErrorResponse.CodeEnum.CATEGORY_NOT_FOUND);
    }
    //---


    @ExceptionHandler(CorruptedTokenException.class)
    @ResponseStatus(UNAUTHORIZED)
    public void onCorruptedTokenException(CorruptedTokenException e) {
        log.warn("Auth token corrupted", e);
    }

    //Http attributes (path variables, headers, request parameters) violations
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse onConstraintViolationException(final ConstraintViolationException e) {
        String message = "Validation failed";
        List<HttpAttributeValidationViolation> violations = new LinkedList<>();
        e.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String parameter = propertyPath.substring(propertyPath.lastIndexOf(".") + 1);
            violations.add(new HttpAttributeValidationViolation(parameter, violation.getMessage()));
        });
        ErrorResponse error = new ErrorResponse(message, ErrorResponse.CodeEnum.BAD_REQUEST);
        error.setHttpAttributeValidationViolations(violations);
        log.warn("{}: {}", message, error, e);
        return error;
    }

    //Fields validation violations and object validation violations
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        List<FieldValidationViolation> fieldViolations = new LinkedList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldViolations.add(new FieldValidationViolation(fieldError.getField(), fieldError.getDefaultMessage()));
        }

        List<ObjectValidationViolation> objectViolations = new LinkedList<>();
        for (ObjectError objectError : ex.getBindingResult().getGlobalErrors()) {
            objectViolations.add(new ObjectValidationViolation(objectError.getDefaultMessage()));
        }

        String message = "Validation failed";
        ErrorResponse error = new ErrorResponse(message, ErrorResponse.CodeEnum.BAD_REQUEST);
        error.setFieldValidationViolations(fieldViolations);
        error.setObjectValidationViolations(objectViolations);
        log.warn("{}: ", message, ex);
        return handleExceptionInternal(ex, error, headers, status, request);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> createResponseEntity(Object body, @NonNull HttpHeaders headers,
                                                          @NonNull HttpStatusCode statusCode, @NonNull WebRequest request) {
        if (body instanceof ErrorResponse) {
            return ResponseEntity.status(statusCode).headers(headers).body(body);
        }

        HttpStatus httpStatus = HttpStatus.resolve(statusCode.value());
        String message = httpStatus == null ? "Unexpected error" : httpStatus.getReasonPhrase();
        //body is usually an ErrorResponse or ProblemDetail with good detail message, trying to get it:
        if (body instanceof org.springframework.web.ErrorResponse) {
            message = ((org.springframework.web.ErrorResponse) body).getBody().getDetail();
        } else if (body instanceof ProblemDetail) {
            message = ((ProblemDetail) body).getDetail();
        }

        log.warn("MVC exception. {}: {}", message, body);
        return ResponseEntity.status(statusCode).headers(headers).body(new ErrorResponse(message, ErrorResponse.CodeEnum.UNKNOWN));
    }


    @ExceptionHandler(Throwable.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse onThrowable(final Throwable e) {
        log.error("Unexpected error occurred", e);
        return new ErrorResponse("Unexpected error occurred: " + e.getMessage(), ErrorResponse.CodeEnum.INTERNAL_SERVER_ERROR);
    }
}
