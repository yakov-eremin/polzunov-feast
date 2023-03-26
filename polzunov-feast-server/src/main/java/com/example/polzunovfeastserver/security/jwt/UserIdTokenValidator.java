package com.example.polzunovfeastserver.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import static org.springframework.security.oauth2.server.resource.BearerTokenErrorCodes.INVALID_TOKEN;

@Slf4j
@Component
public class UserIdTokenValidator implements OAuth2TokenValidator<Jwt> {

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        OAuth2Error error;

        long id;
        String idStr = token.getSubject();
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            error = new OAuth2Error(
                    INVALID_TOKEN,
                    String.format("provided value='%s' is not a valid id", idStr),
                    null);
            log.warn("Invalid token: {}", error.getDescription());
            return OAuth2TokenValidatorResult.failure(error);
        }

        if (id < 0) {
            error = new OAuth2Error(
                    INVALID_TOKEN,
                    String.format("provided id=%d must be positive", id),
                    null);
            log.warn("Invalid token: {}", error.getDescription());
            return OAuth2TokenValidatorResult.failure(error);
        }

        return OAuth2TokenValidatorResult.success();
    }
}
