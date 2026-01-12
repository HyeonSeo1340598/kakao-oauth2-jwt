package io.github.hyeonseo.auth.signup;

import io.github.hyeonseo.auth.AuthProvider;
import io.github.hyeonseo.auth.UserRole;

public record SignupTicketPayload(
        AuthProvider providerType,
        String providerId,
        UserRole role,
        String email,
        long issuedAtEpochSec // String nonce 이게 없어지고 들어옴...??
) {
}
