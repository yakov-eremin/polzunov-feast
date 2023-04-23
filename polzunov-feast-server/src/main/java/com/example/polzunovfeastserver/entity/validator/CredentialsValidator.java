package com.example.polzunovfeastserver.entity.validator;

import org.openapitools.model.Credentials;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class CredentialsValidator extends AbstractValidator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Credentials.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        rejectIfBlank(errors, "username");
        rejectIfContainsWhitespaces(errors, "username");

        rejectIfBlank(errors, "password");
        rejectIfContainsWhitespaces(errors, "password");
    }
}
