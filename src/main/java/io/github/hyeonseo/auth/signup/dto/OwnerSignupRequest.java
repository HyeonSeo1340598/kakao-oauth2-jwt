package io.github.hyeonseo.auth.signup.dto;

import io.github.hyeonseo.auth.common.types.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record OwnerSignupRequest(
        @NotBlank String ticket,
        @NotBlank String name,
        @NotNull LocalDate birth,
        @NotNull Gender gender,
        @NotBlank String phoneNumber
) {}