package com.example.polzunovfeastserver.service.interfaces;

import org.openapitools.model.Credentials;
import org.openapitools.model.Token;
import org.openapitools.model.User;
import org.springframework.security.core.AuthenticationException;

public interface UserServiceInterface {

    Token signUp(User user) throws AuthenticationException;

    Token signIn(Credentials credentials) throws AuthenticationException;
}
