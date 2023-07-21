package com.example.polzunovfeastserver.user;

import com.example.polzunovfeastserver.util.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.UserApi;
import org.openapitools.model.Credentials;
import org.openapitools.model.Token;
import org.openapitools.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    public ResponseEntity<Token> signUpUser(User user) {
        Token token = userService.signUp(user);
        log.info("User '{}' signed up", user.getUsername());
        return ResponseEntity.ok(token);
    }

    @Override
    public ResponseEntity<Token> signInUser(Credentials credentials) {
        Token token = userService.signIn(credentials);
        log.info("User '{}' signed in", credentials.getUsername());
        return ResponseEntity.ok(token);
    }

    @Override
    public ResponseEntity<User> updateUser(User user) {
        long id = AuthenticationUtils.extractUserIdFromToken();
        User updatedUser = userService.update(user, id);
        log.info("User '{}' updated", updatedUser.getUsername());
        return new ResponseEntity<>(updatedUser, OK);
    }

    @Override
    public ResponseEntity<User> getUser() {
        long id = AuthenticationUtils.extractUserIdFromToken();
        User user = userService.getById(id);
        log.info("User '{}' fetched", user.getUsername());
        return new ResponseEntity<>(user, OK);
    }

    @Override
    public ResponseEntity<Void> deleteUser() {
        long id = AuthenticationUtils.extractUserIdFromToken();
        userService.deleteById(id);
        return new ResponseEntity<>(OK);
    }
}