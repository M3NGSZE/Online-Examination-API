package com.m3ngsze.sentry.onlineexaminationapi.service.impl;

import com.m3ngsze.sentry.onlineexaminationapi.jwt.JwtService;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.TokenDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.UserSession;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserSessionRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.TokenService;
import com.m3ngsze.sentry.onlineexaminationapi.utility.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;
    private final UserSessionRepository userSessionRepository;

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

}
