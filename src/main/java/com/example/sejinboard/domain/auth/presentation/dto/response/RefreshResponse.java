package com.example.sejinboard.domain.auth.presentation.dto.response;

public record RefreshResponse(
        String accessToken
) {
    public static RefreshResponse of(String accessToken) {
        return new RefreshResponse(accessToken);
    }
}