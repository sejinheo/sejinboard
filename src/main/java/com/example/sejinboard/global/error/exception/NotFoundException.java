package com.example.sejinboard.global.error.exception;

import com.example.sejinboard.global.error.ErrorCode;

public class NotFoundException extends BusinessBaseException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException() {
        super(ErrorCode.NOT_FOUND_EXCEPTION);
    }
}
