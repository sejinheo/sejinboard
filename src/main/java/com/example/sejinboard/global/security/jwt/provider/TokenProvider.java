package com.example.sejinboard.global.security.jwt.provider;

import com.example.sejinboard.global.config.properties.JwtProperties;
import com.example.sejinboard.global.security.auth.AuthDetailsService;
import com.example.sejinboard.global.security.jwt.exception.ExpiredJwtTokenException;
import com.example.sejinboard.global.security.jwt.exception.InvalidJwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private final AuthDetailsService authDetailsService;

    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";
    private static final String TOKEN_TYPE_CLAIM = "type";
    private static final String TOKEN_HEADER_TYPE = "typ";
    private static final String TOKEN_ISSUER = "reink";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = 7;

    private SecretKey secretKey;

    @PostConstruct
    private void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secretKey());
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String email, String role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.accessExpiration());

        return Jwts.builder()
                .header()
                .add(TOKEN_HEADER_TYPE, "JWT")
                .and()
                .subject(email)
                .claim("role", role)
                .claim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE)
                .issuer(TOKEN_ISSUER)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(String email) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.refreshExpiration());

        return Jwts.builder()
                .header()
                .add(TOKEN_HEADER_TYPE, "JWT")
                .and()
                .subject(email)
                .claim(TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE)
                .issuer(TOKEN_ISSUER)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtProperties.header());

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX_LENGTH);
        }

        return null;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        validateTokenType(claims);

        String email = claims.getSubject();
        UserDetails userDetails = authDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw ExpiredJwtTokenException.EXCEPTION;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("유효하지 않은 JWT 토큰입니다.");
            throw InvalidJwtTokenException.EXCEPTION;
        }
    }

    private void validateTokenType(Claims claims) {
        String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
        if (!ACCESS_TOKEN_TYPE.equals(tokenType)) {
            log.info("토큰 타입이 일치하지 않습니다. type: {}", tokenType);
            throw InvalidJwtTokenException.EXCEPTION;
        }
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            validateTokenType(claims);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = getClaims(token);
            String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
            if (!REFRESH_TOKEN_TYPE.equals(tokenType)) {
                log.info("Refresh 토큰 타입이 일치하지 않습니다. type: {}", tokenType);
                return false;
            }
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }
}