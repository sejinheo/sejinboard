package com.example.sejinboard.domain.auth.application.exception;

import com.example.sejinboard.global.error.ErrorCode;
import com.example.sejinboard.global.error.exception.BusinessBaseException;

public class InvalidCredentialsException extends BusinessBaseException {

    public static final InvalidCredentialsException EXCEPTION = new InvalidCredentialsException();

    private InvalidCredentialsException() {
        super(ErrorCode.INVALID_CREDENTIALS);
    }
}