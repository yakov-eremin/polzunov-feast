package com.example.polzunovfeastserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//TODO тестовый контролер, потом удалить
@RestController
public class WithoutAuthController {

    @GetMapping("/without_auth")
    public ResponseEntity<String> withAuth() {
        return ResponseEntity.ok("Without auth works!");
    }
}
