package com.example.polzunovfeastserver.user.root;

import com.example.polzunovfeastserver.util.validation_group.OnCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.AdminApi;
import org.openapitools.model.Token;
import org.openapitools.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RootController implements AdminApi {

    private final RootService rootService;

    @Override
    public ResponseEntity<Void> deleteAdminByUsername(@NonNull String username) {
        rootService.deleteAdminByUsername(username);
        log.info("Admin '{}' deleted", username);
        return ResponseEntity.ok().build();
    }

    @Override
    @Validated(OnCreate.class)
    public ResponseEntity<Token> signUpAdmin(User user) {
        Token token = rootService.signUpAdmin(user);
        log.info("Admin '{}' signed up", user.getUsername());
        return ResponseEntity.ok(token);
    }

    @Override
    public ResponseEntity<User> updateAdminByUsername(@NonNull String username, User user) {
        User updatedAdmin = rootService.updateAdminByUsername(username, user);
        log.info("Admin '{}' updated", username);
        return ResponseEntity.ok(updatedAdmin);
    }
}
