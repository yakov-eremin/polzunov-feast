package com.example.polzunovfeastserver.validator;

import org.openapitools.model.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class UserValidator extends AbstractValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        rejectIfBlank(errors, "username");
        rejectIfContainsWhitespaces(errors, "username");

        rejectIfBlank(errors, "password");
        rejectIfContainsWhitespaces(errors,"password");

        rejectIfBlank(errors, "name");

        rejectIfNull(errors,"phone");
        rejectIfBadPhone(errors, "phone");

        rejectIfNull(errors,"email");
        rejectIfBadEmail(errors,"email");
    }
}