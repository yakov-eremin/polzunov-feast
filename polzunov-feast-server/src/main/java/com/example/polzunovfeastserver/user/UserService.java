package com.example.polzunovfeastserver.user;

import com.example.polzunovfeastserver.notification.NotificationService;
import com.example.polzunovfeastserver.security.jwt.JwtService;
import com.example.polzunovfeastserver.user.entity.Role;
import com.example.polzunovfeastserver.user.entity.UserEntity;
import com.example.polzunovfeastserver.user.exception.UserNotFoundException;
import com.example.polzunovfeastserver.user.uitl.UsersTableKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.Credentials;
import org.openapitools.model.LogoutRequest;
import org.openapitools.model.Token;
import org.openapitools.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserEntityRepository userRepo;
    private final JwtService jwtService;
    private final NotificationService notificationService;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;

    public Token signUp(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        UserEntity userEntity = UserMapper.toUserEntity(user, null, Role.USER);
        userEntity = userRepo.save(userEntity);
//        if (user.getNotificationToken().isPresent()) {
//            notificationService.addNotificationToken(userEntity, user.getNotificationToken().get());
//        }
        return jwtService.generateToken(userEntity);
    }

    /**
     * @throws org.springframework.security.authentication.BadCredentialsException wrong password or user's email not found
     */
    public Token signIn(Credentials credentials) {
        var auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));
        UserEntity user = (UserEntity) auth.getPrincipal();

//        if (credentials.getNotificationToken().isPresent()) {
//            notificationService.addNotificationToken(user, credentials.getNotificationToken().get());
//        }
        return jwtService.generateToken(user);
    }

    /**
     * @throws UserNotFoundException user doesn't exist
     */
    public void logoutById(long id, LogoutRequest logoutRequest) {
//        if (!userRepo.existsById(id)) {
//            throw new UserNotFoundException(format("Cannot logout: user with id=%d not found", id));
//        }
//        if (!logoutRequest.getNotificationToken().isPresent()) {
//            log.warn("User with id={} didn't provide notification token when login out", id);
//            return;
//        }
//        notificationService.deleteTokenByUserId(id, logoutRequest.getNotificationToken().get());
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
