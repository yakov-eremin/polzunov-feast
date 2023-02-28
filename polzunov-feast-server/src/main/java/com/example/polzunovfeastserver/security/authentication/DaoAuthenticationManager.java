package com.example.polzunovfeastserver.security.authentication;

import com.example.polzunovfeastserver.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DaoAuthenticationManager implements AuthenticationManager {

    private final UserEntityLoader userLoader;
    private final PasswordEncoder encoder;

    /**
     * Аутентифицирует объект {@link Authentication}, сравнивая переданный и действительный пароли.
     * <p>
     *
     * @param authentication следует передавать {@link UsernamePasswordAuthenticationToken},
     *                       как самую простую реализацию интерфейса {@link Authentication}.
     * @return новый {@link UsernamePasswordAuthenticationToken} с добавлением authorities аутентифицированного пользователя.
     * @throws UsernameNotFoundException пользователь не найден
     * @throws BadCredentialsException   передан неверный пароль
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserEntity user = userLoader.loadUserByUsername(authentication.getName());

        String rawPresentedPassword = authentication.getCredentials().toString();
        if (!encoder.matches(rawPresentedPassword, user.getPassword()))
            throw new BadCredentialsException(String.format("Wrong password for user \"%s\"", user.getUsername()));

        var result = UsernamePasswordAuthenticationToken.authenticated(
                authentication.getPrincipal(),
                authentication.getCredentials(),
                user.getAuthorities()
        );
        result.setDetails(authentication.getDetails());

        return result;
    }
}
