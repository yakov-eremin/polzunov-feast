package com.example.polzunovfeastserver.controller.error_handler;

import com.example.polzunovfeastserver.util.MessageProvider;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ErrorResponse;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public abstract class AbstractErrorHandler {

    protected final MessageProvider messageProvider;

    protected AbstractErrorHandler(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    /**
     * Logs error and returns {@link ErrorResponse} with message.
     * @param errorCode errorCode by which message will be retrieved
     * @param defaultMessage in case errorCode message won't be present
     * @param e error to log
     * @return {@link ErrorResponse} with defaultMessage or message retrieved by errorCode
     */
    protected ErrorResponse defaultExceptionHandling(String errorCode, String defaultMessage, Throwable e) {
        String message = messageProvider.getMessage(errorCode, defaultMessage);
        log.warn(message, e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage(message);
        return response;
    }
}
