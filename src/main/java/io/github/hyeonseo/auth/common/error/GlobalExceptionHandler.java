package io.github.hyeonseo.auth.common.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 프로젝트 표준 커스텀 예외 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorCode code = e.getErrorCode();
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(new ErrorResponse("ERROR", code.name(), e.getMessage()));
    }

    // @RequestBody validation 실패 -> 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        ErrorCode code = ErrorCode.VALIDATION_FAILED;

        String msg = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .orElse(ErrorCode.VALIDATION_FAILED.getMessage());

        return ResponseEntity
                .status(ErrorCode.VALIDATION_FAILED.getHttpStatus())
                .body(new ErrorResponse("ERROR", code.name(), msg));
    }

    // DB 무결성(Unique 등) -> 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DataIntegrityViolationException e) {
        ErrorCode code = ErrorCode.DUPLICATE_VALUE;
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(new ErrorResponse("ERROR", code.name(), code.getMessage()));
    }

    // 최종 fallback -> 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleEtc(Exception e) {
        ErrorCode code = ErrorCode.INTERNAL_ERROR;
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(new ErrorResponse("ERROR", code.name(), code.getMessage()));
    }
}
