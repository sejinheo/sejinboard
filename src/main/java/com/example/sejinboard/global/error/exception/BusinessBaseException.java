package com.example.sejinboard.global.error.exception;

import com.example.sejinboard.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessBaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessBaseException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessBaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
