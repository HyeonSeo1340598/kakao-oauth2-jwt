# Kakao OAuth2 + JWT 인증 시스템

Spring Boot 기반의 카카오 소셜 로그인과 JWT 토큰 인증 시스템입니다.

## 기술 스택

- Java 21
- Spring Boot 3.5.5
- Spring Security 6 (OAuth2 Client)
- Spring Data JPA
- MySQL
- Redis (리프레시 토큰 저장)
- JJWT 0.12.5

## 주요 기능

- 카카오 OAuth2 소셜 로그인
- JWT 액세스 토큰 + 리프레시 토큰 인증
- 역할 기반 권한 관리 (Customer / Owner)
- 단일 세션 관리 (사용자당 하나의 활성 세션)
- 토큰 로테이션

## 시작하기

### 1. 환경 설정

`application-local.yml.example`을 `application-local.yml`로 복사 후 설정:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/kakao_auth_lab
    username: root
    password: root

  security:
    oauth2:
      client:
        registration:
          kakao:
            client-id: "카카오_REST_API_KEY"
            client-secret: "카카오_CLIENT_SECRET"

app:
  auth:
    jwt:
      secret: "32바이트_이상의_시크릿_키"
```

### 2. 데이터베이스 생성

```sql
CREATE DATABASE kakao_auth_lab;
```

### 3. Redis 실행

```bash
redis-server
```

### 4. 애플리케이션 실행

```bash
./gradlew bootRun
```

## API 엔드포인트

### 인증

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/oauth2/authorization/kakao` | 카카오 로그인 시작 |
| POST | `/api/auth/refresh` | 토큰 갱신 |
| POST | `/api/auth/logout` | 로그아웃 |

### 회원가입

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/signup/customer` | 고객 회원가입 |
| POST | `/api/signup/owner` | 점주 회원가입 |

### 테스트

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/me` | 현재 사용자 정보 (인증 필요) |
| GET | `/api/customer/test` | Customer 역할 테스트 |
| GET | `/api/owner/test` | Owner 역할 테스트 |

## 인증 흐름

```
1. 카카오 로그인 (/oauth2/authorization/kakao)
        ↓
2. OAuth2 성공 핸들러
   - 신규 사용자: signup_ticket 발급 → 회원가입 진행
   - 기존 사용자: access_token + refresh_token 발급
        ↓
3. API 요청 시 Authorization: Bearer {access_token} 헤더 사용
        ↓
4. 토큰 만료 시 /api/auth/refresh로 갱신
```

## 토큰 정책

| 토큰 | 저장 위치 | 만료 시간 |
|------|----------|----------|
| Access Token | 클라이언트 | 15분 |
| Refresh Token | Redis (HttpOnly 쿠키) | 14일 |
