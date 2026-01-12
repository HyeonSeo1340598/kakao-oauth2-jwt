package io.github.hyeonseo.auth.signup;

import io.github.hyeonseo.auth.AuthProvider;
import io.github.hyeonseo.auth.UserRole;

import java.time.Instant;

public class SignupTicketPayloadFactory {

    public static SignupTicketPayload payload(
            UserRole role,
            AuthProvider providerType,
            String providerId,
            String email
    ) {
        return new SignupTicketPayload(
                providerType,
                providerId,
                role,
                email,
                Instant.now().getEpochSecond()
        );
    }
}
