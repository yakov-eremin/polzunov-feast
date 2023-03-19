package com.example.polzunovfeastserver.repository;

import com.example.polzunovfeastserver.entity.UserEntity;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserEntityRepository extends Repository<UserEntity, String> {

    Optional<UserEntity> findByUsername(String username);

    UserEntity save(UserEntity user);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}