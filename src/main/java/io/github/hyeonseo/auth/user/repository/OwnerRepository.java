package io.github.hyeonseo.auth.user.repository;

import io.github.hyeonseo.auth.AuthProvider;
import io.github.hyeonseo.auth.user.domain.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByProviderTypeAndProviderId(AuthProvider providerType, String providerId);
}
