package com.example.polzunovfeastserver.util;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractValidator implements Validator {

    /**
     * Rejects null, empty or whitespace strings.
     */
    protected void notBlank(Errors errors, String field) {
        String errorCode = "ValidationViolation.message.notBlank";
        String defaultMessage = String.format("Field '%s' must not be blank.", field);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, field, errorCode, defaultMessage);
    }

    protected void notNull(Errors errors, String field) {
        if (valueIsNull(errors, field)) {
            String errorCode = "ValidationViolation.message.notNull";
            String defaultMessage = String.format("Field '%s' must not be null.", field);
            errors.rejectValue(field, errorCode, defaultMessage);
        }
    }

    /**
     * Null elements are considered valid.
     */
    protected void email(Errors errors, String field) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String errorCode = "ValidationViolation.message.email";
        String defaultMessage = String.format("Field '%s' must be a well-formed email.", field);
        pattern(errors, field, emailRegex, errorCode, defaultMessage);
    }

    /**
     * Null elements are considered valid.
     */
    protected void phoneNumber(Errors errors, String field) {
        String phoneRegex = "^\\+?[0-9]{1,3}[- ]?\\(?[0-9]{3}\\)?[- ]?[0-9]{3}[- ]?[0-9]{4}$";
        String errorCode = "ValidationViolation.message.phone";
        String defaultMessage = String.format("Field '%s' must be a well-formed phone number.", field);
        pattern(errors, field, phoneRegex, errorCode, defaultMessage);
    }

    /**
     * Null elements are considered valid.
     */
    protected void noWhitespaces(Errors errors, String field) {
        String regex = "^\\S+$";
        String errorCode = "ValidationViolation.message.noWhitespaces";
        String defaultMessage = String.format("Field '%s' must not contain whitespace characters", field);
        pattern(errors, field, regex, errorCode, defaultMessage);
    }

    /**
     * Field must be a date-time of 'yyy-MM-ddTHH:mm:ss' format. Null elements are considered valid.
     */
    protected void dateTime(Errors errors, String field) {
        String dateTimeStr = (String) errors.getFieldValue(field);
        if (dateTimeStr == null) return;

        try {
            LocalDateTime.parse(dateTimeStr);
        } catch (DateTimeParseException e) {
            String errorCode = "ValidationViolation.message.dateTime";
            String defaultMessage = String.format("Field '%s' must be a date-time of 'yyy-MM-ddTHH:mm:ss' format.", field);
            errors.rejectValue(field, errorCode, defaultMessage);
        }
    }

    /**
     * Null elements are considered valid.
     */
    protected void pattern(Errors errors, String field, String regex) {
        String errorCode = "ValidationViolation.message.pattern";
        String defaultMessage = String.format("Field '%s' must match patter '%s'", field, regex);
        pattern(errors, field, regex, errorCode, new Object[]{regex}, defaultMessage);
    }


    /**
     * Null elements are considered valid.
     */
    private void pattern(Errors errors, String field, String regex,
                         String errorCode, String defaultMessage) {
        pattern(errors, field, regex, errorCode, null, defaultMessage);
    }

    /**
     * Null elements are considered valid.
     */
    private void pattern(Errors errors, String field, String regex,
                         String errorCode, Object[] errorArgs, String defaultMessage) {
        if (valueIsNull(errors, field)) return;
        String value = errors.getFieldValue(field).toString();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches())
            errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }

    private boolean valueIsNull(Errors errors, String field) {
        Object obj = errors.getFieldValue(field);
        return obj == null;
    }
}
