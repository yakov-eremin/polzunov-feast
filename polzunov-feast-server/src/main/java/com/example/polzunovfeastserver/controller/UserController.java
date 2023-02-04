package com.example.polzunovfeastserver.controller;

import com.example.polzunovfeastserver.exception.UsernameAlreadyExistsException;
import com.example.polzunovfeastserver.service.interfaces.UserServiceInterface;
import org.openapitools.api.UserApi;
import org.openapitools.model.Credentials;
import org.openapitools.model.Token;
import org.openapitools.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {

    private final UserServiceInterface userService;

    public UserController(UserServiceInterface userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<Token> signInUser(Credentials credentials) {
        try {
            return ResponseEntity.ok(userService.signIn(credentials));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<Token> signUpUser(User user) {
        try {
            return ResponseEntity.ok(userService.signUp(user));
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.badRequest().build();
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}