package com.example.sejinboard.domain.user.application.dto.response;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String email,
        String username,
        String role
) {
}