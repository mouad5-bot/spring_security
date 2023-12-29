package com.youcode.taskmanager.core.service.impl;

import com.youcode.taskmanager.common.security.provider.jwt.service.token.JwtRefreshService;
import com.youcode.taskmanager.core.database.model.entity.RefreshToken;
import com.youcode.taskmanager.core.database.model.entity.User;
import com.youcode.taskmanager.core.database.repository.RefreshTokenRepository;
import com.youcode.taskmanager.core.service.RefreshTokenService;
import com.youcode.taskmanager.core.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken findByToken(String refreshToken) {
        return findByTokenOrThrow(refreshToken);
    }

    @Override
    public RefreshToken findByUser(User user) {
        return findByUserOrThrow(user);
    }

    @Override
    public RefreshToken update(RefreshToken refreshToken) {

        RefreshToken refreshTokenFound = findByUser(refreshToken.getUser());
        refreshTokenFound.setToken(refreshToken.getToken());
        refreshTokenFound.setIpAddress(refreshToken.getIpAddress());
        refreshTokenFound.setUserAgent(refreshToken.getUserAgent());

        return refreshTokenRepository.save(refreshTokenFound);

    }

    private RefreshToken findByUserOrThrow(User user) {
        return refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new NoSuchElementException("Could not find refresh token"));

    }

    private RefreshToken findByTokenOrThrow(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new NoSuchElementException("Could not find refresh token"));
    }

}