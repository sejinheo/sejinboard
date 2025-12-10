package com.example.sejinboard.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S1", "서버 에러가 발생했습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "S2", "잘못된 HTTP 메서드를 호출했습니다."),
    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "S3", "존재하지 않는 엔티티입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "S4", "유효하지 않은 입력값입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "S5", "접근 권한이 없습니다."),
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "S6", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "S7", "토큰이 만료되었습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U1", "존재하지 않는 유저입니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "U2", "이미 존재하는 유저입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "U3", "이메일 또는 비밀번호가 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "U4", "리프레시 토큰이 유효하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
