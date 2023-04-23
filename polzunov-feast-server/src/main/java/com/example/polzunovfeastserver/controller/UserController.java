package com.example.polzunovfeastserver.controller;

import com.example.polzunovfeastserver.entity.validator.CredentialsValidator;
import com.example.polzunovfeastserver.entity.validator.UserValidator;
import com.example.polzunovfeastserver.service.UserService;
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

import static org.springframework.http.HttpStatus.OK;

@RestController
public class UserController extends AbstractController implements UserApi {
    private final UserService userService;
    private final UserValidator userValidator;
    private final CredentialsValidator credentialsValidator;

    public UserController(UserService userService, UserValidator userValidator, CredentialsValidator credentialsValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.credentialsValidator = credentialsValidator;
    }

    @Override
    public ResponseEntity<Token> signUpUser(@Valid User user) {
        return ResponseEntity.ok(userService.signUp(user));
    }

    @Override
    public ResponseEntity<Token> signInUser(@Valid Credentials credentials) {
        return ResponseEntity.ok(userService.signIn(credentials));
    }

    @Override
    public ResponseEntity<Void> updateUser(@Valid User user) {
        userService.update(user, getUserId());
        return new ResponseEntity<>(OK);
    }

    @Override
    public ResponseEntity<Void> deleteUser() {
        userService.deleteUserById(getUserId());
        return new ResponseEntity<>(OK);
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        //Assign a validator depending on the class of the object being checked
        Optional<Object> validator = Optional.ofNullable(binder.getTarget()).
                filter(field -> field.getClass().equals(User.class));

        if (validator.isPresent())
            binder.setValidator(userValidator);
        else
            binder.setValidator(credentialsValidator);
    }
}