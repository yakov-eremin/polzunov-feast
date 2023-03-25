package com.example.polzunovfeastserver.controller.error_handler;

import com.example.polzunovfeastserver.exception.CorruptedTokenException;
import com.example.polzunovfeastserver.exception.UserIdNotFoundException;
import com.example.polzunovfeastserver.util.MessageProvider;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ErrorResponse;
import org.openapitools.model.ValidationViolation;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @RequestMapping("${server.error.path}")
    public ResponseEntity<ErrorResponse> handleWhitelabelError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String uri = request.getRequestURI();
        ErrorResponse error = new ErrorResponse();

        //if client tries to access '/error'
        if (status == null) {
            error.setMessage(
                    messageProvider.getMessage(
                            "ErrorResponse.message.noSuchPath",
                            "This path doesn't exist")
            );
            log.info("Client tried to access '{}' uri designed only for errors. Response: {}", uri, error);
            return new ResponseEntity<>(error, NOT_FOUND);
        }

        int code = Integer.parseInt(status.toString());
        HttpStatus httpStatus = HttpStatus.resolve(code);
        if (httpStatus == null) {
            throw new RuntimeException(
                    String.format("Error status code = '%d' in uri = '%s' request wasn't a valid http status",
                            code, uri)
            );
        }

        //if client tries to access nonexistent path
        if (httpStatus.equals(NOT_FOUND)) {
            error.setMessage(
                    messageProvider.getMessage(
                            "ErrorResponse.message.noSuchPath",
                            "This path doesn't exist")
            );
            log.info("Client tried to access nonexistent path. Response: {}", error);
            return new ResponseEntity<>(error, NOT_FOUND);
        }

        log.info("Unexpected error occurred at '{}' uri code={}", uri, code);
        return new ResponseEntity<>(httpStatus);
    }

    @ExceptionHandler(CorruptedTokenException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse onCorruptedTokenException(CorruptedTokenException e) {
        String message = messageProvider.getMessage(
                "ErrorResponse.message.tokenUnauthorized",
                "Token authorization failed");
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(UserIdNotFoundException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse onIdNotFoundException(UserIdNotFoundException e) {
        String message = messageProvider.getMessage(
                "ErrorResponse.message.tokenUnauthorized",
                "Token authorization failed");
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
            String message = messageProvider.getValidationViolationMessage(fieldError);
            violation.setMessage(message);

            violations.add(violation);
        }
        ErrorResponse error = new ErrorResponse();
        String message = messageProvider.getMessage(
                "ErrorResponse.message.validation",
                "Validation failed");
        error.setMessage(message);
        error.setValidationViolations(violations);
        log.warn("{}: {}", message, error, e);
        return error;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse onThrowable(Throwable e) {
        String message = messageProvider.getMessage(
                "ErrorResponse.message.internalServerError",
                "Internal server error");
        log.error(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }
}
