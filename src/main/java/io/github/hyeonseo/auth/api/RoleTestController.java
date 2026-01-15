package io.github.hyeonseo.auth.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RoleTestController {

    @GetMapping("/api/customer/ping")
    public Map<String, Object> customerPing() {
        return Map.of("ok", true, "only", "CUSTOMER");
    }

    @GetMapping("/api/owner/ping")
    public Map<String, Object> ownerPing() {
        return Map.of("ok", true, "only", "OWNER");
    }
}
