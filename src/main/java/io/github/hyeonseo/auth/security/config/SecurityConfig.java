package io.github.hyeonseo.auth.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hyeonseo.auth.security.filter.NoStoreAuthResponseFilter;
import io.github.hyeonseo.auth.security.filter.JwtAuthenticationFilter;
import io.github.hyeonseo.auth.token.AccessTokenService;
import io.github.hyeonseo.auth.auth.oauth2.OAuth2JsonSuccessHandler;
import io.github.hyeonseo.auth.token.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            AccessTokenService accessTokenService,
            ObjectMapper objectMapper
    ) {
        return new JwtAuthenticationFilter(accessTokenService, objectMapper);
    }

    @Bean
    public NoStoreAuthResponseFilter noStoreAuthResponseFilter() {
        return new NoStoreAuthResponseFilter();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("https://front.example.com")); // 프론트 도메인
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // 쿠키 포함 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            OAuth2JsonSuccessHandler successHandler,
            JwtAuthenticationFilter jwtFilter,
            NoStoreAuthResponseFilter noStoreFilter
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/test/**", "/api/signup/**").permitAll() // permitAll()은 인증 없이도 접근 허용
                        .requestMatchers("/api/auth/refresh", "/api/auth/logout").permitAll()
                        .requestMatchers("/api/me").authenticated() // authenticated()은 인증 필요

                        // (선택) 역할 기반 인가 테스트
                        // .hasRole() 인증도 필요하고, 권한(Authority)에 ROLE_{}이 있어야 통과
                        .requestMatchers("/api/customer/**").hasRole("CUSTOMER")
                        .requestMatchers("/api/owner/**").hasRole("OWNER")

                        .anyRequest().authenticated() // 위에서 따로 허용해주지 않은 나머지 모든 요청은 “인증 필요”
                )
                .oauth2Login(oauth -> oauth.successHandler(successHandler))
                .addFilterBefore(noStoreFilter, SecurityContextHolderFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
