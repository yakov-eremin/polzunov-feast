package com.example.polzunovfeastserver.user;

import com.example.polzunovfeastserver.user.entity.Role;
import com.example.polzunovfeastserver.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String rootUsername);

    boolean existsByUsernameAndRole(String username, Role role);

    Optional<UserEntity> findByUsernameAndRole(String username, Role role);

    void deleteByUsernameAndRole(String username, Role role);
}