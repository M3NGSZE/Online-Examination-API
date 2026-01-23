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


    //{map two time insert to object into one}
    //        UserInfo userInfo = userInfoRepository.findById(user.getUserInfo().getInfoId())
//                .orElseThrow(() -> new NotFoundException("User info not found"));
//
//        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
//        modelMapper.map(userInfo, userDTO);


//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(req -> req
//                        // ✅ All auth endpoints bypass JWT automatically
//                        .requestMatchers("/api/v1/auths/**").permitAll()
//                        // ✅ Swagger & API docs
//                        .requestMatchers(
//                                "/v3/api-docs/**",
//                                "/swagger-ui/**",
//                                "/swagger-ui.html"
//                        ).permitAll()
//                        // ✅ Everything else needs authentication
//                        .anyRequest().authenticated()
//                )
//                // Stateless session (JWT)
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                // JWT filter before username/password authentication filter
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                // Exception handling
//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint(jwtAuthEntrypoint)
//                        .accessDeniedHandler(customAccessDeniedHandler)
//                );
//
//        return http.build();
//    }
}
