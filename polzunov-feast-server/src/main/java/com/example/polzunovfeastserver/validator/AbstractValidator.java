package com.example.polzunovfeastserver.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractValidator implements Validator {

    protected void rejectIfBlank(Errors errors, String field) {
        String errorCode = "ValidationViolation.message.notBlank";
        String defaultMessage = String.format("Field \"%s\" must not be blank.", field);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, field, errorCode, defaultMessage);
    }

    protected void rejectIfNull(Errors errors, String field) {
        if (valueIsNull(errors, field)) {
            String errorCode = "ValidationViolation.message.notNull";
            String defaultMessage = String.format("Field \"%s\" must not be null.", field);
            errors.rejectValue(field, errorCode, defaultMessage);
        }
    }

    protected void rejectIfBadEmail(Errors errors, String field) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String errorCode = "ValidationViolation.message.email";
        String defaultMessage = String.format("Field \"%s\" must be a well-formed email.", field);
        rejectIfDoesntMatchPattern(errors, field, emailRegex, errorCode, defaultMessage);
    }

    protected void rejectIfBadPhone(Errors errors, String field) {
        String phoneRegex = "^\\+?[0-9]{1,3}[- ]?\\(?[0-9]{3}\\)?[- ]?[0-9]{3}[- ]?[0-9]{4}$";
        String errorCode = "ValidationViolation.message.phone";
        String defaultMessage = String.format("Field \"%s\" must be a well-formed phone number.", field);
        rejectIfDoesntMatchPattern(errors, field, phoneRegex, errorCode, defaultMessage);
    }

    protected void rejectIfContainsWhitespaces(Errors errors, String field) {
        String regex = "^\\S+$";
        String errorCode = "ValidationViolation.message.noWhitespaces";
        String defaultMessage = String.format("Field \"%s\" must not contain whitespace characters", field);
        rejectIfDoesntMatchPattern(errors, field, regex, errorCode, defaultMessage);
    }

    protected void rejectIfDoesntMatchPattern(Errors errors, String field, String regex) {
        String errorCode = "ValidationViolation.message.pattern";
        String defaultMessage = String.format("Field \"%s\" must match patter \"%s\"", field, regex);
        rejectIfDoesntMatchPattern(errors, field, regex, errorCode, new Object[]{regex}, defaultMessage);
    }

    private void rejectIfDoesntMatchPattern(
            Errors errors, String field, String regex, String errorCode, String defaultMessage) {
        rejectIfDoesntMatchPattern(errors, field, regex, errorCode, null, defaultMessage);
    }

    private void rejectIfDoesntMatchPattern(
            Errors errors, String field, String regex,
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
