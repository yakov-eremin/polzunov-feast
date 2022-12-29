package com.example.polzunovfeastserver.controller;

import com.example.polzunovfeastserver.service.TokenService;
import org.openapitools.api.UserApi;
import org.openapitools.model.Credentials;
import org.openapitools.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public UserController(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ResponseEntity<String> signInUser(Credentials credentials) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getLogin(), credentials.getPassword())
        );
        return ResponseEntity.ok(tokenService.generateToken(authentication));
    }

    @Override
    public ResponseEntity<String> signUpUser(User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword())
        );
        return ResponseEntity.ok(tokenService.generateToken(authentication));
    }
}