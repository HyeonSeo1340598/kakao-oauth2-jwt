package io.github.hyeonseo.auth.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hyeonseo.auth.AuthProvider;
import io.github.hyeonseo.auth.UserRole;
import io.github.hyeonseo.auth.jwt.JwtService;
import io.github.hyeonseo.auth.signup.SignupTicketPayload;
import io.github.hyeonseo.auth.signup.SignupTicketPayloadFactory;
import io.github.hyeonseo.auth.signup.SignupTicketService;
import io.github.hyeonseo.auth.user.repository.CustomerRepository;
import io.github.hyeonseo.auth.user.repository.OwnerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2JsonSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final CustomerRepository customerRepository;
    private final OwnerRepository ownerRepository;
    private final SignupTicketService signupTicketService;
    private final JwtService jwtService;

//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        try {
//            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
//            String registrationId = token.getAuthorizedClientRegistrationId(); // kakao-customer / kakao-owner
//
//            // role 결정
//            UserRole role = switch (registrationId) {
//                case "kakao-customer" -> UserRole.CUSTOMER;
//                case "kakao-owner" -> UserRole.OWNER;
//                default -> throw new IllegalStateException("Unknown registrationId: " + registrationId);
//            };
//
//            OAuth2User user = token.getPrincipal();
//            String providerId = user.getName(); // 카카오 id (user-name-attribute=id 설정)
//            String email = extractKakaoEmail(user.getAttributes());
//
//            AuthProvider providerType = AuthProvider.KAKAO;
//
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType("application/json");
//
//            if (role == UserRole.CUSTOMER) {
//                // 회원가입된 사용자가 있는지 조회
//                var existing = customerRepository.findByProviderTypeAndProviderId(providerType, providerId);
//                if (existing.isPresent()) { // 있으면 → JWT 발급해서 SUCCESS 응답
//                    String subject = "CUSTOMER:" + existing.get().getCustomerId();
//                    String jwt = jwtService.issueAccessToken(subject, role);
//                    writeJson(response, 200, Map.of(
//                            "status", "SUCCESS",
//                            "role", role.name(),
//                            "accessToken", jwt,
//                            "tokenType", "Bearer",
//                            "expiresIn", jwtService.accessTtlSeconds()
//                    ));
//                    return;
//                }
//            } else {
//                var existing = ownerRepository.findByProviderTypeAndProviderId(providerType, providerId);
//                if (existing.isPresent()) {
//                    String subject = "OWNER:" + existing.get().getOwnerId();
//                    String jwt = jwtService.issueAccessToken(subject, role);
//                    writeJson(response, 200, Map.of(
//                            "status", "SUCCESS",
//                            "role", role.name(),
//                            "accessToken", jwt,
//                            "tokenType", "Bearer",
//                            "expiresIn", jwtService.accessTtlSeconds()
//                    ));
//                    return;
//                }
//            }
//
//            // 미가입 → ticket 발급
//            SignupTicketPayload payload =
//                    SignupTicketPayloadFactory.payload(role, AuthProvider.KAKAO, providerId, email);
//            String ticket = signupTicketService.create(payload, Duration.ofMinutes(10));
//
//            writeJson(response, 200, Map.of(
//                    "status", "SIGNUP_REQUIRED",
//                    "role", role.name(),
//                    "ticket", ticket,
//                    "expiresIn", 600
//            ));
//        } catch (Exception e) {
//            try {
//                writeJson(response, 500, Map.of("status", "ERROR", "message", e.getMessage()));
//            } catch (Exception ignored) {}
//        }
//    }

    // test를 위해서 브라우저인 경우에는 페이지로 이동하도록 수정하는 코드입니다. test 종류 후에는 위의 코드를 주석 해제하고 사용해주세요.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            boolean wantsHtml = isBrowser(request);

            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            String registrationId = token.getAuthorizedClientRegistrationId(); // kakao-customer / kakao-owner

            UserRole role = switch (registrationId) {
                case "kakao-customer" -> UserRole.CUSTOMER;
                case "kakao-owner" -> UserRole.OWNER;
                default -> throw new IllegalStateException("Unknown registrationId: " + registrationId);
            };

            OAuth2User user = token.getPrincipal();
            String providerId = user.getName(); // kakao id
            String email = extractKakaoEmail(user.getAttributes()); // null 가능

            AuthProvider providerType = AuthProvider.KAKAO;

            // 가입 여부 확인
            if (role == UserRole.CUSTOMER) {
                var existing = customerRepository.findByProviderTypeAndProviderId(providerType, providerId);
                if (existing.isPresent()) {
                    String subject = "CUSTOMER:" + existing.get().getCustomerId();
                    String jwt = jwtService.issueAccessToken(subject, role);

                    if (wantsHtml) {
                        redirectToSuccessPage(response, jwt, role);
                        return;
                    }

                    writeJson(response, 200, Map.of(
                            "status", "SUCCESS",
                            "role", role.name(),
                            "accessToken", jwt,
                            "tokenType", "Bearer",
                            "expiresIn", jwtService.accessTtlSeconds()
                    ));
                    return;
                }
            } else {
                var existing = ownerRepository.findByProviderTypeAndProviderId(providerType, providerId);
                if (existing.isPresent()) {
                    String subject = "OWNER:" + existing.get().getOwnerId();
                    String jwt = jwtService.issueAccessToken(subject, role);

                    if (wantsHtml) {
                        redirectToSuccessPage(response, jwt, role);
                        return;
                    }

                    writeJson(response, 200, Map.of(
                            "status", "SUCCESS",
                            "role", role.name(),
                            "accessToken", jwt,
                            "tokenType", "Bearer",
                            "expiresIn", jwtService.accessTtlSeconds()
                    ));
                    return;
                }
            }

            // 미가입 → ticket 발급
            SignupTicketPayload payload =
                    SignupTicketPayloadFactory.payload(role, providerType, providerId, email);
            String ticket = signupTicketService.create(payload, Duration.ofMinutes(10));

            if (wantsHtml) {
                response.sendRedirect("/test/signup?ticket=" + url(ticket) + "&role=" + url(role.name()));
                return;
            }

            writeJson(response, 200, Map.of(
                    "status", "SIGNUP_REQUIRED",
                    "role", role.name(),
                    "ticket", ticket,
                    "expiresIn", 600
            ));
        } catch (Exception e) {
            try {
                writeJson(response, 500, Map.of("status", "ERROR", "message", e.getMessage()));
            } catch (Exception ignored) {}
        }
    }

    private boolean isBrowser(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept != null && accept.contains("text/html");
    }

    private void redirectToSuccessPage(HttpServletResponse response, String jwt, UserRole role) throws Exception {
        // 토큰을 URL fragment(#)에 넣으면 서버 로그/쿼리로 안 넘어가서 테스트에 비교적 안전함
        String fragment = "#accessToken=" + url(jwt) + "&role=" + url(role.name());
        response.sendRedirect("/test/success" + fragment);
    }

    private String url(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    // 여기까지 test를 위한 코드입니다!!!!!

    private void writeJson(HttpServletResponse response, int status, Object body) throws Exception {
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    private String extractKakaoEmail(Map<String, Object> attrs) {
        Object kakaoAccount = attrs.get("kakao_account");
        if (kakaoAccount instanceof Map<?, ?> account) {
            Object email = account.get("email");
            if (email != null) return String.valueOf(email);
        }
        return null;
    }
}
