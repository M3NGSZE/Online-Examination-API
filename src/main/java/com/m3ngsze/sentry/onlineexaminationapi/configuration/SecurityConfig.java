package com.m3ngsze.sentry.onlineexaminationapi.configuration;

import com.m3ngsze.sentry.onlineexaminationapi.jwt.CustomAccessDeniedHandler;
import com.m3ngsze.sentry.onlineexaminationapi.jwt.JwtAuthEntryPoint;
import com.m3ngsze.sentry.onlineexaminationapi.jwt.JwtAuthFilter;
import com.m3ngsze.sentry.onlineexaminationapi.oauth2.CustomOAuth2UserService;
import com.m3ngsze.sentry.onlineexaminationapi.oauth2.OAuth2FailureHandler;
import com.m3ngsze.sentry.onlineexaminationapi.oauth2.OAuth2SuccessHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@AllArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthEntryPoint jwtAuthEntrypoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers(
                                "/api/v1/auths/oauth2/google",
                                "/api/v1/auths/login",
                                "/api/v1/auths/register",
                                "/api/v1/auths/verify-otp",
                                "/api/v1/auths/**",
                                "/api/v1/users/reactivate",
                                "/oauth2/authorization/**",
                                "/login/oauth2/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // Stateless JWT
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // OAuth2 Login
                .oauth2Login(oauth -> {
                    oauth.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService));
                    oauth.successHandler(oAuth2SuccessHandler);
                    oauth.failureHandler(oAuth2FailureHandler);
                })
                // JWT filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthEntrypoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                );

        return http.build();
    }

}
