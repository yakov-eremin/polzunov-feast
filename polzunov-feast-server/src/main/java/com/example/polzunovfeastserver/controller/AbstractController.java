package com.example.polzunovfeastserver.controller;

import com.example.polzunovfeastserver.exception.CorruptedTokenException;
import org.springframework.security.core.Authentication;

public abstract class AbstractController {

    public long retrieveUserId(Authentication authentication) {
        long id;
        String name = authentication.getName();
        try {
            id = Long.parseLong(name);
        } catch (NumberFormatException e) {
            throw new CorruptedTokenException(
                    String.format("Failed to parse id provided in token, provided value=\"%s\"", name), e);
        }

        if (id < 0)
            throw new CorruptedTokenException(
                    String.format("Id=%d provided in token wasn't positive",id)
            );
        return id;
    }
}
