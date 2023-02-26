package com.example.polzunovfeastserver.validation.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * В качестве defaultMessage указывается errorCode.
 */
public abstract class AbstractValidator implements Validator {

    protected void rejectIfBlank(Errors errors, String field) {
        String errorCode = "ValidationViolation.message.notBlank";
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, field, errorCode, errorCode);
    }

    protected void rejectIfNull(Errors errors, String field) {
        if (valueIsNull(errors, field)) {
            String errorCode = "ValidationViolation.message.notNull";
            errors.rejectValue(field, errorCode, errorCode);
        }
    }

    protected void rejectIfBadEmail(Errors errors, String field) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        rejectIfDoesntMatchPattern(errors, field, emailRegex, "ValidationViolation.message.email");
    }

    protected void rejectIfBadPhone(Errors errors, String field) {
        String phoneRegex = "^\\+?[0-9]{1,3}[- ]?\\(?[0-9]{3}\\)?[- ]?[0-9]{3}[- ]?[0-9]{4}$";
        rejectIfDoesntMatchPattern(errors, field, phoneRegex, "ValidationViolation.message.phone");
    }

    protected void rejectIfDoesntMatchPattern(Errors errors, String field, String regex) {
        rejectIfDoesntMatchPattern(errors, field, regex, "ValidationViolation.message.pattern", new Object[]{regex});
    }

    private void rejectIfDoesntMatchPattern(Errors errors, String field, String regex, String errorCode) {
        rejectIfDoesntMatchPattern(errors, field, regex, errorCode, null);
    }

    private void rejectIfDoesntMatchPattern(
            Errors errors, String field, String regex,
            String errorCode, Object[] errorArgs) {
        if (valueIsNull(errors, field)) return;
        String value = errors.getFieldValue(field).toString();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches())
            errors.rejectValue(field, errorCode, errorArgs, errorCode);
    }

    private boolean valueIsNull(Errors errors, String field) {
        Object obj = errors.getFieldValue(field);
        return obj == null;
    }
}
