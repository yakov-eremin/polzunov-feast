package com.example.polzunovfeastserver.service;

import com.example.polzunovfeastserver.exception.AlreadyTakenException;
import org.openapitools.model.Credentials;
import org.openapitools.model.Token;
import org.openapitools.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface UserService {

    Token signUp(User user) throws AlreadyTakenException;

    Token signIn(Credentials credentials) throws AuthenticationException;

    void update(User user, Authentication auth) throws AlreadyTakenException;
}
