package com.example.polzunovfeastserver.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.Locale;

/**
 * Obtains messages form resource bundles. Logs if message wasn't found.
 */
@Slf4j
@Component
public class MessageProvider {
    private final MessageSource messageSource;

    public MessageProvider(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code, String defaultMessage) {
        return getMessage(code, null, defaultMessage);
    }

    public String getMessage(String code, Object[] args, String defaultMessage) {
        String message;
        try {
            message = messageSource.getMessage(code, args, Locale.getDefault());
        } catch (NoSuchMessageException ex) {
            log.warn("Message for error code = '{}' and args = '{}' wasn't found", code, args);
            return defaultMessage;
        }

        return message;
    }

    public String getValidationViolationMessage(FieldError fieldError) {
        String errorCode = fieldError.getCode();
        Object[] args = fieldError.getArguments();
        String objectName = fieldError.getObjectName();
        String field = fieldError.getField();

        String message;
        try {
            message = messageSource.getMessage(errorCode, args, Locale.getDefault());
        } catch (NoSuchMessageException e) {
            log.warn("Message for error code = '{}' and args = '{}' wasn't found. Object name = '{}', field = '{}'",
                    errorCode, args, objectName, field);
            message = fieldError.getDefaultMessage();
        }
        if (message == null) {
            log.warn("Default message for field = '{}' of object = '{}' wasn't found",
                    field, objectName);
            return "Unknown validation error";
        }

        return message;
    }
}
