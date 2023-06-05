package com.example.polzunovfeastserver.util;

import com.example.polzunovfeastserver.global_error.CorruptedTokenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtils {
    public static long extractUserIdFromToken() throws CorruptedTokenException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String idStr = authentication.getName();
        try {
            return Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            throw new CorruptedTokenException(String.format("Invalid user id='%s' in auth token", idStr));
        }
    }
}
