package com.example.polzunovfeastserver.user;

import com.example.polzunovfeastserver.security.jwt.TokenService;
import com.example.polzunovfeastserver.user.entity.Role;
import com.example.polzunovfeastserver.user.entity.UserEntity;
import com.example.polzunovfeastserver.user.exception.UserNotFoundException;
import com.example.polzunovfeastserver.user.exception.WrongUserPasswordException;
import com.example.polzunovfeastserver.user.uitl.UsersTableKeys;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Credentials;
import org.openapitools.model.Token;
import org.openapitools.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.lang.String.format;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userRepo;
    private final TokenService tokenService;
    private final PasswordEncoder encoder;

    public Token signUp(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        UserEntity userEntity = UserMapper.toUserEntity(user, null, Role.USER);
        return tokenService.generateToken(userRepo.save(userEntity));
    }

    public Token signIn(Credentials credentials) {
        Optional<UserEntity> userOpt = userRepo.findByEmail(credentials.getEmail());
        UserEntity user = userOpt.orElseThrow(() ->
                new UserNotFoundException(format("Cannot sign in: user with email '%s' not found", credentials.getPassword()))
        );

        if (!encoder.matches(credentials.getPassword(), user.getPassword())) {
            throw new WrongUserPasswordException(format("Wrong password for user with email '%s'", user.getUsername()));
        }
        return tokenService.generateToken(user);
    }

    /**
     * If password is null, then set it to previous password
     *
     * @return updated user without password
     * @throws UserNotFoundException if user doesn't exist
     */
    public User update(User user, long id) {
        UserEntity previousUser = getEntityById(id);
        if (user.getPassword() == null) {
            user.setPassword(previousUser.getPassword());
        } else {
            user.setPassword(encoder.encode(user.getPassword()));
        }
        return UserMapper.toUser(
                userRepo.save(UserMapper.toUserEntity(user, id, previousUser.getRole()))
        );
    }

    /**
     * @return user without password
     */
    public User getById(long id) {
        UserEntity userEntity = getEntityById(id);
        return UserMapper.toUser(userEntity);
    }

    /**
     * @throws UserNotFoundException if user doesn't exist
     */
    public UserEntity getEntityById(long id) {
        return userRepo.findById(id).orElseThrow(() ->
                new UserNotFoundException(format("User with id=%d not found", id))
        );
    }

    public void deleteById(long id) {
        //Sprig will complain if user to be deleted doesn't exist, even though docs claim the opposite
        if (!userRepo.existsById(id)) {
            return;
        }
        userRepo.deleteById(id);
    }

    /**
     * Check if some of user's fields are unique
     *
     * @throws DataIntegrityViolationException if user with unique fields already exists
     */
    public void checkUser(User user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new DataIntegrityViolationException(
                    format("User with email '%s' already exists, constraint: %s", user.getEmail(), UsersTableKeys.UNIQUE_EMAIL));
        }
        if (user.getPhone() != null && userRepo.existsByPhone(user.getPhone())) {
            throw new DataIntegrityViolationException(
                    format("User with phone '%s' already exists, constraint: %s", user.getPhone(), UsersTableKeys.UNIQUE_PHONE));
        }
    }
}
