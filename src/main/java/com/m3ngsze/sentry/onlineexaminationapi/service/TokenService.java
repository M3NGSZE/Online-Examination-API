package com.m3ngsze.sentry.onlineexaminationapi.service;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.TokenDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;

public interface TokenService {

    TokenDTO createRefreshToken(User user);

    void revokeToken(String token, long expirationSeconds);

    boolean isTokenRevoked(String token);

    String extractAccessToken(String authHeader);

}
