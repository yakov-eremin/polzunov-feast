package com.example.polzunovfeastserver.security.jwt;

import com.example.polzunovfeastserver.user.entity.UserEntity;
import org.openapitools.model.Token;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class TokenService {
    private final JwtEncoder encoder;

    public TokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public Token generateToken(UserEntity user) {
        Instant now = Instant.now();
        String scope = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(user.getId().toString())
                .claim("scope", scope)
                .build();

        String tokenValue = this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        Token token = new Token();
        token.setAccessToken(tokenValue);
        token.setTokenType("Bearer");
        return token;
    }
}
