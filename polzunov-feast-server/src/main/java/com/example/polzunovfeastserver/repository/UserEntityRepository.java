package com.example.polzunovfeastserver.repository;

import com.example.polzunovfeastserver.entity.UserEntity;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserEntityRepository extends Repository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findById(long id);

    UserEntity save(UserEntity user);

    void deleteById(long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}