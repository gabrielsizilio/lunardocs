package io.gitgub.gabrielsizilio.lunardocs.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;
    @Value("${api.security.token.expiration.time}")
    private Integer expirationTime;

    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        try {
            return JWT.create()
                    .withIssuer("lunar")
                    .withSubject(user.getEmail())
                    .withExpiresAt(generateExpirationTime())
                    .sign(algorithm);
        } catch (JWTCreationException ex) {
            throw new RuntimeException("Could not generate token", ex);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("lunar")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTCreationException ex) {
            throw new RuntimeException("Could not validate token", ex);
        }
    }

    private Instant generateExpirationTime() {
        return LocalDateTime.now().plusHours(expirationTime).toInstant(ZoneOffset.of("-03:00"));
    }

}
