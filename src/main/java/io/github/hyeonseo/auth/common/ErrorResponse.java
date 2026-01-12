package io.github.hyeonseo.auth.common;

public record ErrorResponse(
        String status,   // "ERROR"
        String message
) {}
