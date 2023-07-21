package com.example.polzunovfeastserver.user;

import com.example.polzunovfeastserver.user.entity.Role;
import com.example.polzunovfeastserver.user.entity.UserEntity;
import org.openapitools.model.User;

public final class UserMapper {
    private UserMapper() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static UserEntity toUserEntity(User user, Long id, Role role) {
        return new UserEntity(
                id,
                user.getUsername().trim(),
                user.getPassword(),
                user.getName().trim(),
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
