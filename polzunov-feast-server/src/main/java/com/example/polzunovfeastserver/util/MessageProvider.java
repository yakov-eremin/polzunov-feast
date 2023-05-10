package com.example.polzunovfeastserver.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Retrieves messages form resource bundles. Logs if message wasn't found.
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
            if (message.isBlank())
                log.warn("Message for code='{}' and args='{}' was blank", code, args);
        } catch (NoSuchMessageException ex) {
            log.warn("Message for error code = '{}' and args = '{}' wasn't found", code, args);
            message = defaultMessage;
        }
        if (message == null) {
            log.warn("Default message for code='{}' and args='{}' was null ", code, args);
            message = "Unknown error";
        }
        return message;
    }
}
