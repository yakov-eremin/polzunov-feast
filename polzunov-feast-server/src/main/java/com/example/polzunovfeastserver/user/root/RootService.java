package com.example.polzunovfeastserver.user.root;

import com.example.polzunovfeastserver.security.jwt.TokenService;
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
    private final TokenService tokenService;
    private final PasswordEncoder encoder;

    public Token signUpAdmin(User admin) {
        admin.setPassword(encoder.encode(admin.getPassword()));
        UserEntity userEntity = UserMapper.toUserEntity(admin, null, ADMIN);
        return tokenService.generateToken(userRepo.save(userEntity));
    }

    public User updateAdminByUsername(String username, User admin) {
        UserEntity previousAdmin = userRepo.findByUsernameAndRole(username, ADMIN).orElseThrow(() ->
                new UserNotFoundException(format("Cannot update admin with username '%s', because admin not found", username)));
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

    public void deleteAdminByUsername(String username) {
        if (!userRepo.existsByUsernameAndRole(username, ADMIN)) {
            return;
        }
        userRepo.deleteByUsernameAndRole(username, ADMIN);
    }
}
