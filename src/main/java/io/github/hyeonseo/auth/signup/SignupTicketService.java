package io.github.hyeonseo.auth.signup;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SignupTicketService {

    private static final String PREFIX = "signup:ticket:";
    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;

    public String create(SignupTicketPayload payload, Duration ttl) {
        String ticket = UUID.randomUUID().toString();
        String key = PREFIX + ticket;

        try {
            String json = objectMapper.writeValueAsString(payload);
            redis.opsForValue().set(key, json, ttl);
            return ticket;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create signup ticket", e);
        }
    }

    public SignupTicketPayload consume(String ticket) {
        String key = PREFIX + ticket;

        String json = redis.opsForValue().get(key);
        if (json == null) return null;

        redis.delete(key); // 1회성
        try {
            return objectMapper.readValue(json, SignupTicketPayload.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse signup ticket", e);
        }
    }

}
