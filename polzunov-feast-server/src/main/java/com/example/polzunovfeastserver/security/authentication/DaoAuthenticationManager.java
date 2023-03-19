package com.example.polzunovfeastserver.security.authentication;

import com.example.polzunovfeastserver.entity.UserEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DaoAuthenticationManager implements AuthenticationManager {

    private final UserEntityLoader userLoader;
    private final PasswordEncoder encoder;

    public DaoAuthenticationManager(UserEntityLoader userLoader, PasswordEncoder encoder) {
        this.userLoader = userLoader;
        this.encoder = encoder;
    }

    /**
     * Attempts to authenticate {@link Authentication} object by comparing presented and provided passwords.
     * <p>
     * Pay attention! Returned authentication contains id, not username.
     * @param authentication {@link UsernamePasswordAuthenticationToken} with user's username and password
     * @return new {@link UsernamePasswordAuthenticationToken} with id, password and authorities of authenticated user
     * @throws UsernameNotFoundException username wasn't found
     * @throws BadCredentialsException wrong password
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserEntity user = userLoader.loadUserByUsername(authentication.getName());

        String rawPresentedPassword = authentication.getCredentials().toString();
        if (!encoder.matches(rawPresentedPassword, user.getPassword()))
            throw new BadCredentialsException(String.format("Wrong password for user \"%s\"", user.getUsername()));

        var result = UsernamePasswordAuthenticationToken.authenticated(
                user.getId(),
                authentication.getCredentials(),
                user.getAuthorities()
        );
        result.setDetails(authentication.getDetails());

        return result;
    }
}
