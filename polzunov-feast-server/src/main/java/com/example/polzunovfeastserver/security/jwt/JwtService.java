package com.example.polzunovfeastserver.security.jwt;

import com.example.polzunovfeastserver.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Token;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String TOKEN_TYPE = "Bearer";
    private static final int MINUTES_OF_TOKEN_LIFETIME = 60;

    private final JwtEncoder encoder;

    public Token generateToken(UserEntity user) {
        Instant now = Instant.now();
        String scope = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(MINUTES_OF_TOKEN_LIFETIME, MINUTES))
                .subject(user.getId().toString())
                .claim("scope", scope)
                .build();

        String tokenValue = encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new Token(tokenValue, TOKEN_TYPE);
    }
}
