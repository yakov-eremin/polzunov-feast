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
                user.getPassword(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                role
        );
    }

    public static User toUser(UserEntity userEntity) {
        User user = new User(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail()
        );
        user.setPhone(userEntity.getPhone());
        user.setPassword(userEntity.getPassword());
        return user;
    }
}
