package io.github.hyeonseo.auth.common.error;

public record ErrorResponse(
        String status,   // "ERROR"
        String code,
        String message
) {}
