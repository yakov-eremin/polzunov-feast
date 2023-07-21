package com.example.polzunovfeastserver.user;

import com.example.polzunovfeastserver.security.jwt.TokenService;
import com.example.polzunovfeastserver.user.entity.Role;
import com.example.polzunovfeastserver.user.entity.UserEntity;
import com.example.polzunovfeastserver.user.exception.UserNotFoundException;
import com.example.polzunovfeastserver.user.exception.WrongUserPasswordException;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Credentials;
import org.openapitools.model.Token;
import org.openapitools.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepo;
    private final TokenService tokenService;
    private final PasswordEncoder encoder;

    public Token signUp(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        UserEntity userEntity = UserMapper.toUserEntity(user, null, Role.ROLE_USER);
        return tokenService.generateToken(userEntityRepo.save(userEntity));
    }

    public Token signIn(Credentials credentials) {
        Optional<UserEntity> userOpt = userEntityRepo.findByUsername(credentials.getUsername());
        UserEntity user = userOpt.orElseThrow(() ->
                new UserNotFoundException(String.format("Cannot sign in: user '%s' not found", credentials.getUsername()))
        );

        if (!encoder.matches(credentials.getPassword(), user.getPassword())) {
            throw new WrongUserPasswordException(String.format("Wrong password for user '%s'", user.getUsername()));
        }
        return tokenService.generateToken(user);
    }

    /**
     * If password is null, then set it to previous password
     * @return updated user without password
     */
    public User update(User user, long id) {
        UserEntity previousUser = getEntityById(id);
        if (user.getPassword() == null) {
            user.setPassword(previousUser.getPassword());
        } else {
            user.setPassword(encoder.encode(user.getPassword()));
        }
        return UserMapper.toUserWithoutPassword(
                userEntityRepo.save(UserMapper.toUserEntity(user, id, previousUser.getRole()))
        );
    }

    /**
     * @return user without password
     */
    public User getById(long id) {
        UserEntity userEntity = getEntityById(id);
        return UserMapper.toUserWithoutPassword(userEntity);
    }

    /**
     * @throws UserNotFoundException if user doesn't exist
     */
    public UserEntity getEntityById(long id) {
        return userEntityRepo.findById(id).orElseThrow(() ->
                new UserNotFoundException(String.format("User with id=%d not found", id))
        );
    }

    public void deleteById(long id) {
        //Sprig will complain if user to be deleted doesn't exist, even though docs claim the opposite
        if (!userEntityRepo.existsById(id)) {
            return;
        }
        userEntityRepo.deleteById(id);
    }
}
