package com.example.polzunovfeastserver.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class AbstractController {

    protected int getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String idStr = authentication.getName();
        return Integer.parseInt(idStr);
    }
}
