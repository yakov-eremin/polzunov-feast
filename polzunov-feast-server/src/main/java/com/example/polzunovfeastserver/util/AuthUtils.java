package com.example.polzunovfeastserver.util;

import com.example.polzunovfeastserver.exception.CorruptedTokenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthUtils {
    private AuthUtils() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static long extractUserIdFromJwt() throws CorruptedTokenException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String idStr = authentication.getName();
        try {
            return Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            throw new CorruptedTokenException(String.format("Invalid user id='%s' in auth token", idStr));
        }
    }
}
