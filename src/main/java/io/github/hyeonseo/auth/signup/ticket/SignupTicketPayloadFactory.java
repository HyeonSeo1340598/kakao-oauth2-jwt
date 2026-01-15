package io.github.hyeonseo.auth.signup.ticket;

import io.github.hyeonseo.auth.common.types.AuthProvider;
import io.github.hyeonseo.auth.common.types.UserRole;

import java.time.Instant;

public class SignupTicketPayloadFactory {

    public static SignupTicketPayload payload(
            UserRole role,
            AuthProvider providerType,
            String providerId
    ) {
        return new SignupTicketPayload(
                providerType,
                providerId,
                role,
                Instant.now().getEpochSecond()
        );
    }
}
