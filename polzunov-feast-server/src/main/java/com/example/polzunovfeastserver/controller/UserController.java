package com.example.polzunovfeastserver.controller;

import com.example.polzunovfeastserver.exception.registration.UsernameAlreadyTakenException;
import com.example.polzunovfeastserver.service.interfaces.UserServiceInterface;
import com.example.polzunovfeastserver.validation.validator.CredentialsValidator;
import com.example.polzunovfeastserver.validation.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.UserApi;
import org.openapitools.model.Credentials;
import org.openapitools.model.Token;
import org.openapitools.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        try {
            return ResponseEntity.ok(userService.signUp(user));
        } catch (UsernameAlreadyTakenException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @Override
    public ResponseEntity<Token> signInUser(@Valid Credentials credentials) {
        try {
            return ResponseEntity.ok(userService.signIn(credentials));
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        Optional<Object> validator = Optional.ofNullable(binder.getTarget()).
                filter(field -> field.getClass().equals(User.class));

        if (validator.isPresent())
            binder.setValidator(userValidator);
        else
            binder.setValidator(credentialsValidator);
    }
}