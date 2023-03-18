package com.example.polzunovfeastserver.security.authentication;

import com.example.polzunovfeastserver.entity.UserEntity;
import com.example.polzunovfeastserver.repository.UserEntityRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserEntityLoader implements UserDetailsService {

    private final UserEntityRepository userRepo;

    public UserEntityLoader(UserEntityRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOpt = userRepo.findByUsername(username);
        if (userEntityOpt.isEmpty())
            throw new UsernameNotFoundException(String.format("Username \"%s\" not found", username));

        return userEntityOpt.get();
    }
}
