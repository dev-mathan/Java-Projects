package com.mathan.nimbusvault.service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.security.SecureRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mathan.nimbusvault.entity.RefreshToken;
import com.mathan.nimbusvault.entity.User;
import com.mathan.nimbusvault.exception.InvalidCredentialsException;
import com.mathan.nimbusvault.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public RefreshToken createRefreshToken(User user) {

        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        String tokenValue = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(tokenValue);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));

        return refreshTokenRepository.save(refreshToken);
    }
    @Transactional(readOnly = true)
    public RefreshToken verifyRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
        		.orElseThrow(() -> new InvalidCredentialsException("Refresh token not found"));

        if (refreshToken.isRevoked()) {
        	throw new RuntimeException("Refresh token has been revoked");
        }

        if (!refreshToken.getExpiryDate().isAfter(LocalDateTime.now())) {
        	throw new InvalidCredentialsException("Refresh token has expired");
        }

        return refreshToken;
    }
    @Transactional
    public void revokeRefreshToken(RefreshToken refreshToken) {

        refreshToken.setRevoked(true);

        refreshTokenRepository.save(refreshToken);
    }
}