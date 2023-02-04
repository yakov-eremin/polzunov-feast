package com.example.polzunovfeastserver.service;

import com.example.polzunovfeastserver.entity.UserEntity;
import com.example.polzunovfeastserver.exception.UsernameAlreadyExistsException;
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

import static com.example.polzunovfeastserver.constant.Role.ROLE_USER;

@Service
public class UserService implements UserServiceInterface {

    private final UserEntityRepository userEntityRepo;
    private final DaoAuthenticationManager authManager;
    private final TokenService tokenService;


    public UserService(UserEntityRepository userEntityRepo, TokenService tokenService, DaoAuthenticationManager authManager) {
        this.userEntityRepo = userEntityRepo;
        this.tokenService = tokenService;
        this.authManager = authManager;
    }

    /**
     * @throws UsernameAlreadyExistsException пользователь с таким username уже существует
     * @throws BadCredentialsException передан неверный пароль
     */
    @Override
    public Token signUp(User user) throws AuthenticationException {
        if (userEntityRepo.existsByUsername(user.getUsername()))
            throw new UsernameAlreadyExistsException("Пользователь с username " + user.getUsername() + " уже существует.");

        UserEntity userEntity = convertUserToUserEntity(user);
        userEntityRepo.save(userEntity);

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        return tokenService.generateToken(auth);
    }

    /**
     * @throws UsernameNotFoundException пользователь не найден
     * @throws BadCredentialsException передан неверный пароль
     */
    @Override
    public Token signIn(Credentials credentials) throws AuthenticationException {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
        );

        return tokenService.generateToken(auth);
    }

    private UserEntity convertUserToUserEntity(User user) {
        return new UserEntity(
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                ROLE_USER
        );
    }
}
