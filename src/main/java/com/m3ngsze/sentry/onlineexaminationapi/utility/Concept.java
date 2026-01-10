package com.m3ngsze.sentry.onlineexaminationapi.utility;

public class Concept {

/*  concept not separate cause "Circular Dependency" don't call  authenticationManager.authenticate outside this method

    public AuthDTO authenticate(AuthRequest request) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);

        return AuthDTO.builder()
                .accessToken(accessToken)
                .role(user != null ? user.getRole().getRoleName() : null)
                .build();
    }*/
}
