package com.m3ngsze.sentry.onlineexaminationapi.service.impl;

import com.m3ngsze.sentry.onlineexaminationapi.jwt.JwtService;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.TokenDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.UserSession;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserSessionRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.TokenService;
import com.m3ngsze.sentry.onlineexaminationapi.utility.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;
    private final RedisTemplate<String, Object> redisTemplate;

    private final UserSessionRepository userSessionRepository;

    @Override
    public TokenDTO createRefreshToken(User user) {
        String plainToken = TokenUtil.generateRefreshToken();
        String hashedToken = TokenUtil.hashToken(plainToken);

        UserSession userSession = new UserSession();
        userSession.setUser(user);
        userSession.setRefreshTokenHash(hashedToken);
        userSession.setExpiresAt(LocalDateTime.now().plusDays(7));

        String accessToken = jwtService.generateToken(user);

        userSessionRepository.save(userSession);

        return TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(plainToken)
                .build();
    }

    // Save token in Redis blacklist
    @Override
    public void revokeToken(String token, long expirationSeconds) {
        String key = "revokedToken:" + token;
        redisTemplate.opsForValue().set(key, "true", expirationSeconds, TimeUnit.SECONDS);
    }

    // Check if token is revoked
    @Override
    public boolean isTokenRevoked(String token) {
        String key = "revokedToken:" + token;
        return redisTemplate.hasKey(key);
    }

    @Override
    public String extractAccessToken(String authHeader) {
        return authHeader.substring(7);
    }

}
