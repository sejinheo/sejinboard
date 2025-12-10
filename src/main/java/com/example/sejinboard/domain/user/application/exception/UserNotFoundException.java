package com.example.sejinboard.domain.user.application.exception;

import com.example.sejinboard.global.error.ErrorCode;
import com.example.sejinboard.global.error.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public static final UserNotFoundException EXCEPTION = new UserNotFoundException();

    private UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}