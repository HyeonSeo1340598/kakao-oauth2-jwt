package io.github.hyeonseo.auth.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.auth.jwt")
public record JwtProperties(
        String issuer,
        String secret,
        long accessTtlSeconds
) {}
