package io.github.hyeonseo.auth.signup.dto;

public record AuthTokenResponse(
        String status,      // "SUCCESS"
        String role,        // "CUSTOMER" | "OWNER"
        String accessToken,
        String tokenType,   // "Bearer"
        long expiresIn
) {}
