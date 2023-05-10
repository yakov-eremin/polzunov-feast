package com.example.polzunovfeastserver.user.controller;

import com.example.polzunovfeastserver.user.entity.Role;
import com.example.polzunovfeastserver.user.UserService;
import com.example.polzunovfeastserver.user.entity.CredentialsValidator;
import com.example.polzunovfeastserver.user.entity.UserEntity;
import com.example.polzunovfeastserver.user.entity.UserMapper;
import com.example.polzunovfeastserver.user.entity.UserValidator;
import com.example.polzunovfeastserver.user.exception.NullPasswordException;
import com.example.polzunovfeastserver.util.AuthenticationUtils;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.UserApi;
import org.openapitools.model.Credentials;
import org.openapitools.model.Token;
import org.openapitools.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
public class UserController implements UserApi {
    private final UserService userService;
    private final UserValidator userValidator;
    private final CredentialsValidator credentialsValidator;

    public UserController(UserService userService, UserValidator userValidator,
                          CredentialsValidator credentialsValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.credentialsValidator = credentialsValidator;
    }

    @Override
    public ResponseEntity<Token> signUpUser(@Valid User user) {
        //Password can be null when updating user, so we need to check it here
        if (user.getPassword() == null) {
            throw new NullPasswordException("Password was null when signing up");
        }

        UserEntity userEntity = UserMapper.toUserEntity(user, null, Role.ROLE_USER);
        Token token = userService.signUp(userEntity);
        log.info("User '{}' signed up", userEntity.getUsername());
        return ResponseEntity.ok(token);
    }

    @Override
    public ResponseEntity<Token> signInUser(@Valid Credentials credentials) {
        Token token = userService.signIn(credentials);
        log.info("User '{}' signed in", credentials.getUsername());
        return ResponseEntity.ok(token);
    }

    @Override
    public ResponseEntity<User> updateUser(@Valid User user) {
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

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        //Assign a validator depending on the class of the object being checked
        Optional<Object> validator = Optional.ofNullable(binder.getTarget())
                .filter(field -> field.getClass().equals(User.class));

        if (validator.isPresent()) {
            binder.setValidator(userValidator);
        } else {
            binder.setValidator(credentialsValidator);
        }
    }
}