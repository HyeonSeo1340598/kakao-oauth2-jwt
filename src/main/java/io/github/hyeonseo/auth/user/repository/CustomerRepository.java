package io.github.hyeonseo.auth.user.repository;

import io.github.hyeonseo.auth.common.types.AuthProvider;
import io.github.hyeonseo.auth.user.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByProviderTypeAndProviderId(AuthProvider providerType, String providerId);
}
