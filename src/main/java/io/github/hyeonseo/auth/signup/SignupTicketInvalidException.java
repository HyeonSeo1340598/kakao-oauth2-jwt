package io.github.hyeonseo.auth.signup;

public class SignupTicketInvalidException extends RuntimeException {
    public SignupTicketInvalidException(String message) {
        super(message);
    }
}
