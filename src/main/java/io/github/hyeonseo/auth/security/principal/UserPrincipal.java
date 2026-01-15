package io.github.hyeonseo.auth.security.principal;

import io.github.hyeonseo.auth.common.types.UserRole;

public record UserPrincipal(
        Long id,
        UserRole role
) {}
