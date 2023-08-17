package com.example.polzunovfeastserver.exception;

import com.example.polzunovfeastserver.category.util.CategoriesTableKeys;
import com.example.polzunovfeastserver.event.util.table_key.EventCategoriesTableKeys;
import com.example.polzunovfeastserver.event.util.table_key.EventsTableKeys;
import com.example.polzunovfeastserver.route.node.util.RouteNodesTableKeys;
import com.example.polzunovfeastserver.user.uitl.UsersTableKeys;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JpaExceptionHandler {

    private static final Map<String, ErrorResponse.CodeEnum> CONSTRAINTS_TO_ERROR_CODES = Map.of(
            UsersTableKeys.UNIQUE_EMAIL, ErrorResponse.CodeEnum.EMAIL_ALREADY_EXISTS,
            UsersTableKeys.UNIQUE_PHONE, ErrorResponse.CodeEnum.PHONE_ALREADY_EXISTS,
            RouteNodesTableKeys.FOREIGN_EVENT, ErrorResponse.CodeEnum.EVENT_HAS_ASSOCIATED_ROUTES,
            EventsTableKeys.FOREIGN_PLACE, ErrorResponse.CodeEnum.PLACE_HAS_ASSOCIATED_EVENTS,
            EventCategoriesTableKeys.FOREIGN_EVENT, ErrorResponse.CodeEnum.CATEGORY_HAS_ASSOCIATED_EVENTS,
            CategoriesTableKeys.UNIQUE_NAME, ErrorResponse.CodeEnum.CATEGORY_NAME_ALREADY_EXISTS
    );

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> onDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = "Data integrity violation: " + e.getMessage();
        ErrorResponse.CodeEnum code = ErrorResponse.CodeEnum.INTERNAL_SERVER_ERROR;

        String exceptionMessage = e.getMessage().toLowerCase();
        for (Map.Entry<String, ErrorResponse.CodeEnum> entry : CONSTRAINTS_TO_ERROR_CODES.entrySet()) {
            if (exceptionMessage.contains(entry.getKey())) {
                code = entry.getValue();
                break;
            }
        }

        if (code == ErrorResponse.CodeEnum.INTERNAL_SERVER_ERROR) {
            log.error(message, e);
            return ResponseEntity.internalServerError().body(new ErrorResponse(message, code));
        }

        log.warn(message, e);
        return new ResponseEntity<>(new ErrorResponse(message, code), HttpStatus.CONFLICT);
    }
}
