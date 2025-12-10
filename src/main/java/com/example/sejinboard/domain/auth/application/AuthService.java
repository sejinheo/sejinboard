package com.example.sejinboard.domain.auth.application;

import com.example.sejinboard.domain.auth.application.exception.EmailAlreadyExistsException;
import com.example.sejinboard.domain.auth.application.exception.InvalidCredentialsException;
import com.example.sejinboard.domain.auth.application.exception.InvalidRefreshTokenException;
import com.example.sejinboard.domain.auth.application.mapper.AuthMapper;
import com.example.sejinboard.domain.auth.domain.RefreshToken;
import com.example.sejinboard.domain.auth.presentation.dto.request.LoginRequest;
import com.example.sejinboard.domain.auth.presentation.dto.request.RegisterRequest;
import com.example.sejinboard.domain.auth.presentation.dto.response.LoginResponse;
import com.example.sejinboard.domain.auth.presentation.dto.response.RefreshResponse;
import com.example.sejinboard.domain.auth.repository.RefreshTokenRepository;
import com.example.sejinboard.domain.user.domain.User;
import com.example.sejinboard.domain.user.repository.UserRepository;
import com.example.sejinboard.global.security.jwt.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterRequest request) {
        userRepository.findByEmail(request.email())
                .ifPresent(user -> {
                    throw EmailAlreadyExistsException.EXCEPTION;
                });

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = AuthMapper.toUser(request, encodedPassword);
        userRepository.save(user);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> InvalidCredentialsException.EXCEPTION);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw InvalidCredentialsException.EXCEPTION;
        }

        String accessToken = tokenProvider.createAccessToken(user.getEmail(), user.getRole().name());
        String refreshToken = tokenProvider.createRefreshToken(user.getEmail());

        saveOrUpdateRefreshToken(user.getEmail(), refreshToken);

        return LoginResponse.of(accessToken);
    }

    @Transactional
    public RefreshResponse refresh(String refreshToken) {
        if (refreshToken == null || !tokenProvider.validateRefreshToken(refreshToken)) {
            throw InvalidRefreshTokenException.EXCEPTION;
        }

        String email = tokenProvider.getEmailFromToken(refreshToken);

        RefreshToken storedToken = refreshTokenRepository.findById(email)
                .orElseThrow(() -> InvalidRefreshTokenException.EXCEPTION);

        if (!storedToken.getRefreshToken().equals(refreshToken)) {
            throw InvalidRefreshTokenException.EXCEPTION;
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> InvalidRefreshTokenException.EXCEPTION);

        String newAccessToken = tokenProvider.createAccessToken(user.getEmail(), user.getRole().name());

        return RefreshResponse.of(newAccessToken);
    }

    @Transactional
    public void logout(String email) {
        refreshTokenRepository.deleteById(email);
    }

    public String getRefreshTokenFromUser(String email) {
        RefreshToken refreshToken = refreshTokenRepository.findById(email)
                .orElseThrow(() -> InvalidRefreshTokenException.EXCEPTION);
        return refreshToken.getRefreshToken();
    }

    private void saveOrUpdateRefreshToken(String email, String token) {
        refreshTokenRepository.findById(email)
                .ifPresentOrElse(
                        refreshToken -> {
                            refreshToken.updateToken(token);
                            refreshTokenRepository.save(refreshToken);
                        },
                        () -> refreshTokenRepository.save(new RefreshToken(email, token))
                );
    }
}