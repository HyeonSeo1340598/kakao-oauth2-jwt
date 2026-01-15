package io.github.hyeonseo.auth.signup.ticket;

import io.github.hyeonseo.auth.common.types.AuthProvider;
import io.github.hyeonseo.auth.common.types.UserRole;

public record SignupTicketPayload(
        AuthProvider providerType,
        String providerId,
        UserRole role,
        long issuedAtEpochSec // String nonce 이게 없어지고 들어옴...??
) {
}
