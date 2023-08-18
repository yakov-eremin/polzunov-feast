package com.example.polzunovfeastserver.user.root;

import com.example.polzunovfeastserver.notification.NotificationService;
import com.example.polzunovfeastserver.security.jwt.JwtService;
import com.example.polzunovfeastserver.user.UserEntityRepository;
import com.example.polzunovfeastserver.user.UserMapper;
import com.example.polzunovfeastserver.user.entity.UserEntity;
import com.example.polzunovfeastserver.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Token;
import org.openapitools.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.polzunovfeastserver.user.entity.Role.ADMIN;
import static java.lang.String.format;

@Service
@Transactional
@RequiredArgsConstructor
public class RootService {

    private final UserEntityRepository userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;
    private final NotificationService notificationService;

    public Token signUpAdmin(User admin) {
        admin.setPassword(encoder.encode(admin.getPassword()));
        UserEntity userEntity = UserMapper.toUserEntity(admin, null, ADMIN);
        userEntity = userRepo.save(userEntity);
        if (admin.getNotificationToken().isPresent()) {
            notificationService.addNotificationToken(userEntity, admin.getNotificationToken().get());
        }
        return jwtService.generateToken(userEntity);
    }

    public User updateAdminByUsername(long id, User admin) {
        UserEntity previousAdmin = userRepo.findByIdAndRole(id, ADMIN).orElseThrow(() ->
                new UserNotFoundException(format("Cannot update admin with id=%d, because admin not found", id)));
        admin.setPassword(encoder.encode(admin.getPassword()));
        if (admin.getPassword() == null) {
            admin.setPassword(previousAdmin.getPassword());
        } else {
            admin.setPassword(encoder.encode(admin.getPassword()));
        }
        return UserMapper.toUser(
                userRepo.save(UserMapper.toUserEntity(admin, previousAdmin.getId(), previousAdmin.getRole()))
        );
    }

    public void deleteAdminById(long id) {
        if (!userRepo.existsByIdAndRole(id, ADMIN)) {
            return;
        }
        userRepo.deleteByIdAndRole(id, ADMIN);
    }
}
