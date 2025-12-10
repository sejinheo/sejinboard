package com.example.sejinboard.global.security.jwt.handler;

import com.example.sejinboard.global.error.ErrorCode;
import com.example.sejinboard.global.security.jwt.util.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HttpResponse httpResponseUtil;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.error("인증되지 않은 요청: {}", authException.getMessage());
        httpResponseUtil.setErrorResponse(response, ErrorCode.NOT_VALID_TOKEN);
    }
}