package io.github.hyeonseo.auth.signup;

import io.github.hyeonseo.auth.signup.dto.AuthTokenResponse;
import io.github.hyeonseo.auth.signup.dto.CustomerSignupRequest;
import io.github.hyeonseo.auth.signup.dto.OwnerSignupRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signup")
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;

    @PostMapping("/customer")
    public ResponseEntity<AuthTokenResponse> signupCustomer(@Valid @RequestBody CustomerSignupRequest req) {
        return ResponseEntity.ok(signupService.signupCustomer(req));
    }

    @PostMapping("/owner")
    public ResponseEntity<AuthTokenResponse> signupOwner(@Valid @RequestBody OwnerSignupRequest req) {
        return ResponseEntity.ok(signupService.signupOwner(req));
    }
}
