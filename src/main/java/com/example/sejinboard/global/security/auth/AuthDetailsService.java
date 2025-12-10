package com.example.sejinboard.global.security.auth;

import com.example.sejinboard.domain.user.application.exception.UserNotFoundException;
import com.example.sejinboard.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .map(AuthDetails::new)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }
}
