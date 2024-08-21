package com.example.polzunovfeastserver.security;

import com.example.polzunovfeastserver.user.UserEntityRepository;
import com.example.polzunovfeastserver.user.entity.Role;
import com.example.polzunovfeastserver.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RootAdminLoader implements CommandLineRunner {

    @Value("${root.email}")
    private String rootEmail;

    @Value("${root.password}")
    private String rootPassword;

    private final PasswordEncoder passwordEncoder;
    private final UserEntityRepository userRepo;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepo.existsByEmail(rootEmail)) {
            return;
        }

        UserEntity root = new UserEntity(
                null,
                passwordEncoder.encode(rootPassword),
                "root",
                null,
                rootEmail,
                Role.ROOT
        );

        userRepo.save(root);
    }
}
