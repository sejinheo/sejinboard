package com.example.sejinboard.domain.auth.application.mapper;

import com.example.sejinboard.domain.auth.presentation.dto.request.RegisterRequest;
import com.example.sejinboard.domain.user.domain.Role;
import com.example.sejinboard.domain.user.domain.User;

public class AuthMapper {

    public static User toUser(RegisterRequest request, String encodedPassword) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(encodedPassword)
                .gender(request.gender())
                .role(Role.USER)
                .build();
    }
}