package com.example.sejinboard.global.security.jwt.handler;

import com.example.sejinboard.global.error.ErrorCode;
import com.example.sejinboard.global.security.jwt.util.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final HttpResponse httpResponseUtil;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        log.error("접근 권한 없음: {}", accessDeniedException.getMessage());
        httpResponseUtil.setErrorResponse(response, ErrorCode.ACCESS_DENIED);
    }
}