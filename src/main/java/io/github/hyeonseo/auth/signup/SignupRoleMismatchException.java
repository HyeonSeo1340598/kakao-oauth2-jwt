package io.github.hyeonseo.auth.signup;

import io.github.hyeonseo.auth.UserRole;

public class SignupRoleMismatchException extends RuntimeException {
    public SignupRoleMismatchException(UserRole expected, UserRole actual) {
        super("Role mismatch. expected=" + expected + ", actual=" + actual);
    }
}
