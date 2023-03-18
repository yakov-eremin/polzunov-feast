package com.example.polzunovfeastserver.service;

import com.example.polzunovfeastserver.constant.Role;
import com.example.polzunovfeastserver.entity.UserEntity;
import com.example.polzunovfeastserver.exception.registration.RegistrationException;
import com.example.polzunovfeastserver.exception.registration.UsernameAlreadyTakenException;
import com.example.polzunovfeastserver.mapper.UserMapper;
import com.example.polzunovfeastserver.repository.UserEntityRepository;
import com.example.polzunovfeastserver.security.authentication.DaoAuthenticationManager;
import com.example.polzunovfeastserver.security.jwt.TokenService;
import com.example.polzunovfeastserver.service.interfaces.UserServiceInterface;
import org.openapitools.model.Credentials;
import org.openapitools.model.Token;
import org.openapitools.model.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceInterface {

    private final UserEntityRepository userEntityRepo;
    private final DaoAuthenticationManager authManager;
    private final TokenService tokenService;
    private final UserMapper userMapper;


    public UserService(UserEntityRepository userEntityRepo, TokenService tokenService,
                       DaoAuthenticationManager authManager, UserMapper userMapper) {
        this.userEntityRepo = userEntityRepo;
        this.tokenService = tokenService;
        this.authManager = authManager;
        this.userMapper = userMapper;
    }

    /**
     * @throws UsernameAlreadyTakenException
     */
    @Override
    public Token signUp(User user) throws RegistrationException {
        if (userEntityRepo.existsByUsername(user.getUsername()))
            throw new UsernameAlreadyTakenException(
                    String.format("Username \"%s\" was already taken", user.getUsername()));

        UserEntity userEntity = userMapper.toUserEntityWithEncodedPassword(user, Role.ROLE_USER);
        userEntityRepo.save(userEntity);

        return tokenService.generateToken(userEntity);
    }

    /**
     * @throws BadCredentialsException incorrect password
     * @throws UsernameNotFoundException
     */
    @Override
    public Token signIn(Credentials credentials) throws AuthenticationException {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
        );

        return tokenService.generateToken(auth);
    }
}
