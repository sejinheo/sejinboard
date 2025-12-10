package com.example.sejinboard.global.security.jwt.exception;

import com.example.sejinboard.global.error.ErrorCode;
import com.example.sejinboard.global.error.exception.BusinessBaseException;

public class InvalidJwtTokenException extends BusinessBaseException {

    public static final BusinessBaseException EXCEPTION = new InvalidJwtTokenException();

    private InvalidJwtTokenException() {
        super(ErrorCode.NOT_VALID_TOKEN);
    }
}