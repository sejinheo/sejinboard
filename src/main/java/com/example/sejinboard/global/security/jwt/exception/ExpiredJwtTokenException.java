package com.example.sejinboard.global.security.jwt.exception;

import com.example.sejinboard.global.error.ErrorCode;
import com.example.sejinboard.global.error.exception.BusinessBaseException;

public class ExpiredJwtTokenException extends BusinessBaseException {

    public static final BusinessBaseException EXCEPTION = new ExpiredJwtTokenException();

    private ExpiredJwtTokenException() {
        super(ErrorCode.TOKEN_EXPIRED);
    }
}