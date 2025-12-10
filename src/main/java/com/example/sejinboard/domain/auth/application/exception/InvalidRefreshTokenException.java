package com.example.sejinboard.domain.auth.application.exception;

import com.example.sejinboard.global.error.ErrorCode;
import com.example.sejinboard.global.error.exception.BusinessBaseException;

public class InvalidRefreshTokenException extends BusinessBaseException {

    public static final InvalidRefreshTokenException EXCEPTION = new InvalidRefreshTokenException();

    private InvalidRefreshTokenException() {
        super(ErrorCode.INVALID_REFRESH_TOKEN);
    }
}