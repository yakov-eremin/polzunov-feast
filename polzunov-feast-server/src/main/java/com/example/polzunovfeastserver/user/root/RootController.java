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

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RootController implements AdminApi {

    private final RootService rootService;

    @Override
    public ResponseEntity<Void> deleteAdminById(@NonNull Long id) {
        rootService.deleteAdminById(id);
        log.info("Admin with id='{}' deleted", id);
        return ResponseEntity.ok().build();
    }

    @Override
    @Validated(OnCreate.class)
    public ResponseEntity<Token> signUpAdmin(User user) {
        Token token = rootService.signUpAdmin(user);
        log.info("Admin with email '{}' signed up", user.getEmail());
        return ResponseEntity.ok(token);
    }

    @Override
    public ResponseEntity<User> updateAdminById(@NonNull Long id, User user) {
        User updatedAdmin = rootService.updateAdminByUsername(id, user);
        log.info("Admin '{}' updated", id);
        return ResponseEntity.ok(updatedAdmin);
    }

    @Override
    public ResponseEntity<List<User>> getAllAdmins() {
        List<User> users = rootService.getAllAdmins();
        log.info("Admins fetched: {}", users);
        return ResponseEntity.ok(users);
    }
}
