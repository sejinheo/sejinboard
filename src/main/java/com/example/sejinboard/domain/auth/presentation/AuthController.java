package com.example.sejinboard.domain.auth.presentation;

import com.example.sejinboard.domain.auth.application.AuthService;
import com.example.sejinboard.domain.auth.presentation.dto.request.LoginRequest;
import com.example.sejinboard.domain.auth.presentation.dto.request.RegisterRequest;
import com.example.sejinboard.domain.auth.presentation.dto.response.LoginResponse;
import com.example.sejinboard.domain.auth.presentation.dto.response.RefreshResponse;
import com.example.sejinboard.global.security.auth.AuthDetails;
import com.example.sejinboard.global.security.jwt.util.AuthHeaderManager;
import com.example.sejinboard.global.security.jwt.util.CookieManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthHeaderManager authHeaderManager;
    private final CookieManager cookieManager;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest request, HttpServletResponse response) {

        LoginResponse loginResponse = authService.login(request);
        authHeaderManager.setAuthorizationHeader(response, loginResponse.accessToken());

        String refreshToken = authService.getRefreshTokenFromUser(request.email());
        cookieManager.setRefreshTokenCookie(response, refreshToken);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(
            HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = cookieManager.getRefreshTokenFromCookie(request);
        RefreshResponse refreshResponse = authService.refresh(refreshToken);

        authHeaderManager.setAuthorizationHeader(response, refreshResponse.accessToken());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal AuthDetails authDetails, HttpServletResponse response) {

        authService.logout(authDetails.getUser().getEmail());
        cookieManager.deleteRefreshTokenCookie(response);

        return ResponseEntity.noContent().build();
    }
}