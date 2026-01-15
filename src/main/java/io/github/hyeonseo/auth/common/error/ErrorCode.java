package io.github.hyeonseo.auth.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ---- Signup ----
    SIGNUP_TICKET_INVALID(HttpStatus.BAD_REQUEST, "회원가입 티켓이 유효하지 않거나 만료되었습니다."),
    SIGNUP_ROLE_MISMATCH(HttpStatus.BAD_REQUEST, "요청한 회원 유형과 티켓의 회원 유형이 일치하지 않습니다."),

    // ---- Auth / Token ----
    REFRESH_TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 없습니다."),
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않거나 만료되었습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // ---- Common ----
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "요청 값 검증에 실패했습니다."),
    DUPLICATE_VALUE(HttpStatus.CONFLICT, "중복된 값이 존재합니다. (이메일/전화번호/제공자 정보)"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
