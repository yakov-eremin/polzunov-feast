package com.example.polzunovfeastserver.user;

import com.example.polzunovfeastserver.user.entity.Role;
import com.example.polzunovfeastserver.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsById(long id);

    boolean existsByEmail(String email);

    boolean existsByIdAndRole(long id, Role role);

    Optional<UserEntity> findByIdAndRole(long id, Role role);

    void deleteByIdAndRole(long id, Role role);

    boolean existsByPhone(String phone);
}