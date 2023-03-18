package com.example.polzunovfeastserver.validator;

import org.openapitools.model.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class UserValidator extends AbstractValidator {

    //TODO разобраться, можно ли использовать разные валидаторы для одно модели в UserController
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        rejectIfBlank(errors, "username");
        rejectIfBlank(errors, "password");
        rejectIfBlank(errors, "name");

        rejectIfNull(errors,"phone");
        rejectIfBadPhone(errors, "phone");

        rejectIfNull(errors,"email");
        rejectIfBadEmail(errors,"email");
    }
}