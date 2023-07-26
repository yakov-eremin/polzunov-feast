package com.example.polzunovfeastserver.user.root_admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.AdminApi;
import org.openapitools.model.Token;
import org.openapitools.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RootController implements AdminApi {

    private final RootService rootService;

    @Override
    public ResponseEntity<Void> deleteAdminByUsername(String username) {
        rootService.deleteAdminByUsername(username);
        log.info("Admin '{}' deleted", username);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Token> signUpAdmin(User user) {
        Token token = rootService.signUpAdmin(user);
        log.info("Admin '{}' signed up", user.getUsername());
        return ResponseEntity.ok(token);
    }

    @Override
    public ResponseEntity<User> updateAdminByUsername(String username, User user) {
        User updatedAdmin = rootService.updateAdminByUsername(username, user);
        log.info("Admin '{}' updated", username);
        return ResponseEntity.ok(updatedAdmin);
    }
}
