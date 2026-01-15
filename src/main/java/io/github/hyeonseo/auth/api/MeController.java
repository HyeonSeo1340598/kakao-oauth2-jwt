package io.github.hyeonseo.auth.api;

import io.github.hyeonseo.auth.security.principal.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MeController {

    @GetMapping("/api/me")
    public Map<String, Object> me(@AuthenticationPrincipal UserPrincipal principal) {
        return Map.of(
                "id", principal.id(),
                "role", principal.role()
        );
    }
}
