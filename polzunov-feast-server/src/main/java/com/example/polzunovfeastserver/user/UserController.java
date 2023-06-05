package com.example.polzunovfeastserver.user;

import com.example.polzunovfeastserver.user.entity.Role;
import com.example.polzunovfeastserver.user.entity.UserEntity;
import com.example.polzunovfeastserver.util.AuthenticationUtils;
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
public class UserController implements UserApi {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

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
        long userId = AuthenticationUtils.extractUserIdFromToken();
        UserEntity userEntityToUpdate = UserMapper.toUserEntity(user, userId, Role.ROLE_USER);
        UserEntity updatedUserEntity = userService.update(userEntityToUpdate);
        User updatedUser = UserMapper.toUserWithoutPassword(updatedUserEntity);
        return new ResponseEntity<>(updatedUser, OK);
    }

    @Override
    public ResponseEntity<Void> deleteUser() {
        long userId = AuthenticationUtils.extractUserIdFromToken();
        userService.deleteUserById(userId);
        return new ResponseEntity<>(OK);
    }
}