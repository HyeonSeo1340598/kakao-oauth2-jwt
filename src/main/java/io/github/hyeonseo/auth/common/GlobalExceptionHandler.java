package io.github.hyeonseo.auth.common;

import io.github.hyeonseo.auth.signup.SignupRoleMismatchException;
import io.github.hyeonseo.auth.signup.SignupTicketInvalidException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SignupTicketInvalidException.class)
    public ResponseEntity<ErrorResponse> handleTicketInvalid(SignupTicketInvalidException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse("ERROR", e.getMessage()));
    }

    @ExceptionHandler(SignupRoleMismatchException.class)
    public ResponseEntity<ErrorResponse> handleRoleMismatch(SignupRoleMismatchException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse("ERROR", e.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DataIntegrityViolationException e) {
        // phone/email unique 등
        return ResponseEntity.status(409).body(new ErrorResponse("ERROR", "Duplicate value (email/phone/provider)"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse("ERROR", "Validation failed"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleEtc(Exception e) {
        return ResponseEntity.status(500).body(new ErrorResponse("ERROR", e.getMessage()));
    }
}
