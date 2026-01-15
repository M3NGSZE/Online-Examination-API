package com.m3ngsze.sentry.onlineexaminationapi.oauth2;

import com.m3ngsze.sentry.onlineexaminationapi.exception.BadRequestException;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.AuthDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.TokenDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Authentication authentication
    ) throws IOException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        if (oauthUser == null) {
            throw new RuntimeException("OAuth2User principal is null");
        }

        // Get email from OAuth2 attributes (standard attribute that's always present)
        String email = oauthUser.getAttribute("email");
        if (email == null) {
            throw new BadRequestException("OAuth2 email missing");
        }

        // Look up user by email (more reliable than custom userId attribute)
        User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new BadRequestException("User not found. Please ensure the user was created during OAuth2 processing."));

        log.info("Found user: {} (ID: {})", email, user.getUserId());

        TokenDTO refreshToken = tokenService.createRefreshToken(user);

        AuthDTO authDTO = AuthDTO.builder()
                .accessToken(refreshToken.getAccessToken())
                .refreshToken(refreshToken.getRefreshToken())
                .expiresIn(300L)
                .role(user.getRole().getRoleName())
                .build();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), authDTO);
    }
}
