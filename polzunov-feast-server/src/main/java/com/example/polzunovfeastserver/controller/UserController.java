package com.example.polzunovfeastserver.controller;

import com.example.polzunovfeastserver.service.interfaces.UserServiceInterface;
import com.example.polzunovfeastserver.validator.CredentialsValidator;
import com.example.polzunovfeastserver.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.UserApi;
import org.openapitools.model.Credentials;
import org.openapitools.model.Token;
import org.openapitools.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserServiceInterface userService;
    private final UserValidator userValidator;
    private final CredentialsValidator credentialsValidator;

    @Override
    public ResponseEntity<Token> signUpUser(@Valid User user) {
        return ResponseEntity.ok(userService.signUp(user));
    }

    @Override
    public ResponseEntity<Token> signInUser(@Valid Credentials credentials) {
        return ResponseEntity.ok(userService.signIn(credentials));
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        //Назначаем валидатор в зависимости от класса проверяемого объекта
        Optional<Object> validator = Optional.ofNullable(binder.getTarget()).
                filter(field -> field.getClass().equals(User.class));

        if (validator.isPresent())
            binder.setValidator(userValidator);
        else
            binder.setValidator(credentialsValidator);
    }
}