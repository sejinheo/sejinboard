package com.example.sejinboard.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secretKey,
        Long accessExpiration,
        Long refreshExpiration,
        String header,
        String prefix
) {
}