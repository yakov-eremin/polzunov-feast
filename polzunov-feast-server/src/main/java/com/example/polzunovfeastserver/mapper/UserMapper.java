package com.example.polzunovfeastserver.mapper;

import com.example.polzunovfeastserver.constant.Role;
import com.example.polzunovfeastserver.entity.UserEntity;
import org.openapitools.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder encoder;

    public UserMapper(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * @return {@link UserEntity} with encoded password
     */
    public UserEntity toUserEntityWithEncodedPassword(User user, Role role) {
        return new UserEntity(
                user.getUsername(),
                encoder.encode(user.getPassword()),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                role
        );
    }

}
