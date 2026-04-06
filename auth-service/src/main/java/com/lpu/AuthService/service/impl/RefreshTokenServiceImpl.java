package com.lpu.AuthService.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lpu.AuthService.custom.exceptions.TokenExpiredException;
import com.lpu.AuthService.custom.exceptions.TokenNotFoundException;
import com.lpu.AuthService.custom.exceptions.TokenReuseException;
import com.lpu.AuthService.custom.exceptions.UserNotFoundException;
import com.lpu.AuthService.entity.RefreshToken;
import com.lpu.AuthService.entity.User;
import com.lpu.AuthService.repository.AuthUserRepository;
import com.lpu.AuthService.repository.RefreshTokenRepository;
import com.lpu.AuthService.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.refresh.expiration}")
    private Long refreshDuration;

    private final RefreshTokenRepository repository;
    private final AuthUserRepository userRepository;

    @Override
    public RefreshToken createToken(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(LocalDateTime.now().plusSeconds(refreshDuration));
        token.setUsed(false);

        return repository.save(token);
    }

    @Override
    public RefreshToken rotateToken(String requestToken) {
        RefreshToken oldToken = repository.findByToken(requestToken)
                .orElseThrow(() -> new TokenNotFoundException("Token not found"));

        if (oldToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token expired");
        }

        if (oldToken.isUsed()) {
            repository.deleteByUser(oldToken.getUser());
            throw new TokenReuseException("Token reuse detected. Logged out.");
        }

        oldToken.setUsed(true);
        repository.save(oldToken);

        return createToken(oldToken.getUser().getEmail());
    }
}
