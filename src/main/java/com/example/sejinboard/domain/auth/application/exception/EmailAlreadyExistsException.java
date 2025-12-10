package com.example.sejinboard.domain.auth.application.exception;

import com.example.sejinboard.global.error.ErrorCode;
import com.example.sejinboard.global.error.exception.BusinessBaseException;

public class EmailAlreadyExistsException extends BusinessBaseException {

    public static final EmailAlreadyExistsException EXCEPTION = new EmailAlreadyExistsException();

    private EmailAlreadyExistsException() {
        super(ErrorCode.USER_ALREADY_EXISTS);
    }
}
