package com.example.polzunovfeastserver.user.entity;

import org.openapitools.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserEntity toUserEntity(User user, Long id, Role role) {
        return new UserEntity(
                id,
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                role
        );
    }

    public static User toUserWithoutPassword(UserEntity userEntity) {
        User user = new User();
        user.setUsername(userEntity.getUsername());
        user.setName(userEntity.getName());
        user.setPhone(userEntity.getPhone());
        user.setEmail(userEntity.getEmail());
        return user;
    }
}
