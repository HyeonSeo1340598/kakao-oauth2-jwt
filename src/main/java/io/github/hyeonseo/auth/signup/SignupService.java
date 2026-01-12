package io.github.hyeonseo.auth.signup;

import io.github.hyeonseo.auth.UserRole;
import io.github.hyeonseo.auth.jwt.JwtService;
import io.github.hyeonseo.auth.signup.dto.AuthTokenResponse;
import io.github.hyeonseo.auth.signup.dto.CustomerSignupRequest;
import io.github.hyeonseo.auth.signup.dto.OwnerSignupRequest;
import io.github.hyeonseo.auth.user.domain.Customer;
import io.github.hyeonseo.auth.user.domain.Owner;
import io.github.hyeonseo.auth.user.repository.CustomerRepository;
import io.github.hyeonseo.auth.user.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final SignupTicketService signupTicketService;
    private final CustomerRepository customerRepository;
    private final OwnerRepository ownerRepository;
    private final JwtService jwtService;

    @Transactional
    public AuthTokenResponse signupCustomer(CustomerSignupRequest req) {
        SignupTicketPayload ticket = signupTicketService.consume(req.ticket());
        if (ticket == null) throw new SignupTicketInvalidException("Ticket is invalid or expired");

        if (ticket.role() != UserRole.CUSTOMER) {
            throw new SignupRoleMismatchException(UserRole.CUSTOMER, ticket.role());
        }

        // 혹시 이미 가입된 상태면 그대로 토큰 발급(중복 가입 방지)
        var existing = customerRepository.findByProviderTypeAndProviderId(ticket.providerType(), ticket.providerId());
        if (existing.isPresent()) {
            String subject = "CUSTOMER:" + existing.get().getCustomerId();
            String jwt = jwtService.issueAccessToken(subject, UserRole.CUSTOMER);
            return new AuthTokenResponse("SUCCESS", UserRole.CUSTOMER.name(), jwt, "Bearer", jwtService.accessTtlSeconds());
        }

        try {
            Customer customer = Customer.builder()
                    .providerId(ticket.providerId())
                    .providerType(ticket.providerType())
                    .email(ticket.email()) // 카카오에서 받은 이메일(없을 수도 있음)
                    .phoneNumber(req.phoneNumber())
                    .birth(req.birth())
                    .name(req.name())
                    .gender(req.gender())
                    .pin(req.pin())
                    .build();

            Customer saved = customerRepository.save(customer);

            String subject = "CUSTOMER:" + saved.getCustomerId();
            String jwt = jwtService.issueAccessToken(subject, UserRole.CUSTOMER);

            return new AuthTokenResponse("SUCCESS", UserRole.CUSTOMER.name(), jwt, "Bearer", jwtService.accessTtlSeconds());
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Duplicate email/phone/provider", e);
        }
    }

    @Transactional
    public AuthTokenResponse signupOwner(OwnerSignupRequest req) {
        SignupTicketPayload ticket = signupTicketService.consume(req.ticket());
        if (ticket == null) throw new SignupTicketInvalidException("Ticket is invalid or expired");

        if (ticket.role() != UserRole.OWNER) {
            throw new SignupRoleMismatchException(UserRole.OWNER, ticket.role());
        }

        var existing = ownerRepository.findByProviderTypeAndProviderId(ticket.providerType(), ticket.providerId());
        if (existing.isPresent()) {
            String subject = "OWNER:" + existing.get().getOwnerId();
            String jwt = jwtService.issueAccessToken(subject, UserRole.OWNER);
            return new AuthTokenResponse("SUCCESS", UserRole.OWNER.name(), jwt, "Bearer", jwtService.accessTtlSeconds());
        }

        try {
            Owner owner = Owner.builder()
                    .providerId(ticket.providerId())
                    .providerType(ticket.providerType())
                    .email(ticket.email())
                    .phoneNumber(req.phoneNumber())
                    .birth(req.birth())
                    .name(req.name())
                    .gender(req.gender())
                    .build();

            Owner saved = ownerRepository.save(owner);

            String subject = "OWNER:" + saved.getOwnerId();
            String jwt = jwtService.issueAccessToken(subject, UserRole.OWNER);

            return new AuthTokenResponse("SUCCESS", UserRole.OWNER.name(), jwt, "Bearer", jwtService.accessTtlSeconds());
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Duplicate email/phone/provider", e);
        }
    }
}
