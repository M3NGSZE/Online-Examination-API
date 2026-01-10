package com.m3ngsze.sentry.onlineexaminationapi.service.impl;

import com.m3ngsze.sentry.onlineexaminationapi.jwt.JwtService;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.AuthDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.UserSession;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.AuthRequest;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserSessionRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.AuthService;
import com.m3ngsze.sentry.onlineexaminationapi.service.UserService;
import com.m3ngsze.sentry.onlineexaminationapi.utility.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.m3ngsze.sentry.onlineexaminationapi.utility.TokenUtil.generateRefreshToken;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserSessionRepository userSessionRepository;

    // do not separate authenticationManager.authenticate it can cause error "Circular Dependency"

    @Override
    public AuthDTO authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (DisabledException e) {
            throw new RuntimeException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("INVALID_CREDENTIALS", e);
        }

        UserDetails userDetails = userService.loadUserByUsername(request.getEmail());

        return AuthDTO.builder()
                .accessToken(jwtService.generateToken(userDetails))
                .refreshToken(CreateRefreshToken((User) userDetails))
                .role(userDetails.getAuthorities().iterator().next().getAuthority())
                .build();
    }

    public String CreateRefreshToken(User user) {
        String plainToken = TokenUtil.generateRefreshToken();
        String hashedToken = TokenUtil.hashToken(plainToken);

        UserSession userSession = new UserSession();
        userSession.setUser(user);
        userSession.setRefreshTokenHash(hashedToken);
        userSession.setExpiresAt(LocalDateTime.from(Instant.now().plus(30, ChronoUnit.DAYS)));

        userSessionRepository.save(userSession);

        return plainToken;
    }
}
