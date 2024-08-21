package com.example.polzunovfeastserver.security;

import org.openapitools.api.AuthApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
public class TokenValidationController implements AuthApi {
    @Override
    public ResponseEntity<Void> checkAuthToken() {
        return ResponseEntity.status(OK).build();
    }
}
