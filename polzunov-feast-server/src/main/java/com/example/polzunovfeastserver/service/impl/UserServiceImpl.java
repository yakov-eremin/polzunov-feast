package com.example.polzunovfeastserver.service.impl;

import com.example.polzunovfeastserver.entity.UserEntity;
import com.example.polzunovfeastserver.exception.already_taken.AlreadyTakenException;
import com.example.polzunovfeastserver.exception.already_taken.EmailAlreadyTakenException;
import com.example.polzunovfeastserver.exception.already_taken.PhoneAlreadyTakenException;
import com.example.polzunovfeastserver.exception.already_taken.UsernameAlreadyTakenException;
import com.example.polzunovfeastserver.mapper.UserMapper;
import com.example.polzunovfeastserver.repository.UserEntityRepository;
import com.example.polzunovfeastserver.security.authentication.DaoAuthenticationManager;
import com.example.polzunovfeastserver.security.authentication.UserEntityLoader;
import com.example.polzunovfeastserver.security.jwt.TokenService;
import com.example.polzunovfeastserver.service.UserService;
import org.openapitools.model.Credentials;
import org.openapitools.model.Token;
import org.openapitools.model.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.polzunovfeastserver.constant.Role.ROLE_USER;

@Service
public class UserServiceImpl implements UserService {

    private final UserEntityRepository userEntityRepo;
    private final DaoAuthenticationManager authManager;
    private final UserEntityLoader userEntityLoader;
    private final TokenService tokenService;
    private final UserMapper userMapper;

    public UserServiceImpl(UserEntityRepository userEntityRepo, TokenService tokenService,
                           DaoAuthenticationManager authManager, UserEntityLoader userEntityLoader, UserMapper userMapper) {
        this.userEntityRepo = userEntityRepo;
        this.tokenService = tokenService;
        this.authManager = authManager;
        this.userEntityLoader = userEntityLoader;
        this.userMapper = userMapper;
    }

    /**
     * @throws UsernameAlreadyTakenException
     */
    @Override
    public Token signUp(User user) throws AlreadyTakenException {
        checkUniqueFields(user, Optional.empty());

        UserEntity userEntity = userMapper.toUserEntityWithEncodedPassword(user, ROLE_USER);
        userEntityRepo.save(userEntity);

        return tokenService.generateToken(userEntity);
    }

    /**
     * @throws BadCredentialsException   incorrect password
     * @throws UsernameNotFoundException
     */
    @Override
    public Token signIn(Credentials credentials) throws AuthenticationException {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
        );

        return tokenService.generateToken(auth);
    }

    /**
     * @param user
     * @param userId
     * @throws com.example.polzunovfeastserver.exception.UserIdNotFoundException
     * user id retrieved from auth token wasn't found
     */
    @Override
    public void update(User user, long userId) {
        UserEntity loadedUser = userEntityLoader.loadById(userId);
        checkUniqueFields(user, Optional.of(loadedUser));

        UserEntity userForUpdate = userMapper.toUserEntityWithEncodedPassword(user, userId, loadedUser.getRole());
        userEntityRepo.save(userForUpdate);
    }

    /**
     * Checks that username, email and phone number are unique.
     * <p>
     * If userEntity is provided, and it has the same value of unique field as user, then this field would not be checked.
     *
     * @param user          user whose fields will be checked for uniqueness
     * @param userEntityOpt
     */
    private void checkUniqueFields(User user, Optional<UserEntity> userEntityOpt) {
        String username = user.getUsername();
        String email = user.getEmail();
        String phone = user.getPhone();

        if (userEntityOpt.isEmpty()) {
            if (userEntityRepo.existsByUsername(username))
                throw new UsernameAlreadyTakenException(String.format("Username \"%s\" already exists", username));
            if (userEntityRepo.existsByEmail(email))
                throw new EmailAlreadyTakenException(String.format("Email \"%s\" was already taken", email));
            if (userEntityRepo.existsByEmail(email))
                throw new PhoneAlreadyTakenException(String.format("Phone number \"%s\" was already taken", phone));
        } else {
            UserEntity userEntity = userEntityOpt.get();
            if (!userEntity.getUsername().equals(username) && userEntityRepo.existsByUsername(username))
                throw new UsernameAlreadyTakenException(String.format("Username \"%s\" already exists", username));
            if (!userEntity.getEmail().equals(email) && userEntityRepo.existsByEmail(email))
                throw new EmailAlreadyTakenException(String.format("Email \"%s\" was already taken", email));
            if (!userEntity.getPhone().equals(phone) && userEntityRepo.existsByPhone(phone))
                throw new PhoneAlreadyTakenException(String.format("Phone number \"%s\" was already taken", phone));
        }
    }
}
