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
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "S4", "유효하지 않은 입력값입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
