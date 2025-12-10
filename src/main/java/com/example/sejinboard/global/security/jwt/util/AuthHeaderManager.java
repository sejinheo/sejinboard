package com.example.sejinboard.global.security.jwt.util;

import com.example.sejinboard.global.config.properties.JwtProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthHeaderManager {

    private final JwtProperties jwtProperties;

    public void setAuthorizationHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(jwtProperties.header(), jwtProperties.prefix() + " " + accessToken);
    }
}