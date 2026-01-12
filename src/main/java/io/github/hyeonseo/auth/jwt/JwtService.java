package io.github.hyeonseo.auth.jwt;

import io.github.hyeonseo.auth.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties props;

    public String issueAccessToken(String subject, UserRole role) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(props.accessTtlSeconds());

        byte[] keyBytes = props.secret().getBytes(StandardCharsets.UTF_8);
        var key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .issuer(props.issuer())
                .subject(subject)
                .claim("role", role.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    public long accessTtlSeconds() {
        return props.accessTtlSeconds();
    }
}
