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
//                        //  All auth endpoints bypass JWT automatically
//                        .requestMatchers("/api/v1/auths/**").permitAll()
//                        //  Swagger & API docs
//                        .requestMatchers(
//                                "/v3/api-docs/**",
//                                "/swagger-ui/**",
//                                "/swagger-ui.html"
//                        ).permitAll()
//                        //  Everything else needs authentication
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

    //        long years = ChronoUnit.YEARS.between(request.getDateOfBirth(), LocalDate.now());
//        if (years < 13 || years > 100) {
//            throw new BadRequestException("Invalid date of birth");
//        }

//    if (roomType == null) {
//        Join<Room, Enrollment> enrollment = root.join("enrollments", JoinType.LEFT);
//        Join<Room, RoomOwner> owner = root.join("roomOwners", JoinType.LEFT);
//
//        return cb.or(
//                cb.equal(enrollment.get("user"), user),
//                cb.equal(owner.get("user"), user)
//        );
//    }

//    M3n9sZe checkByParam = paramCheck( inputData, cardStatus , sKey );
//
//            if ( checkByParam.getString( "isCheckYN" ).equalsIgnoreCase( "Y" ) ) {
//        M3n9sZe mapFieldYN = mapField( cardStatus );
//        outputData = inquiryCheck( inputData, mapFieldYN, sKey);
//    } else {
//        checkByParam.remove( "isCheckYN" );
//        outputData = checkByParam;

//    private M3n9sZe paramCheck(M3n9sZe inputData, M3n9sZe inputParam, String... sKey ) {
//        M3n9sZe outputData = new M3n9sZe();
//        int X = 0;
//        int Y = 0;
//        String isCheckYN = "N";
//
//        for ( String key: sKey ) {
//            if ( inputParam.getString( "isVirtual" ).equalsIgnoreCase( "N" ) && inputData.containsKey( key )
//                    && inputData.getString( key ).equalsIgnoreCase( "Y" ) )
//                Y += 1;
//            else if ( inputParam.getString( "isVirtual" ).equalsIgnoreCase( "Y" ) && inputData.containsKey( key )
//                    && inputData.getString( key ).equalsIgnoreCase( "Y" ) )
//                X += 1;
//        }
//
//        if ( X >= 2 || Y >= 4 ) {
//            isCheckYN = "Y";
//            outputData.setString( "isFreezeYN", "Y" );
//            outputData.setString( "isCheckYN", isCheckYN );
//        } else
//            outputData.setString( "isCheckYN", isCheckYN );
//
//        return outputData;
//    }
//
//    private M3n9sZe inquiryCheck( M3n9sZe inputData, M3n9sZe inputParam, String... sKey ) {
//        M3n9sZe outputData = new M3n9sZe();
//        int X = 0;
//
//        for ( String key: sKey ) {
//            if ( inputParam.getString( "isVirtualYN" ).matches("[YN]" ) &&
//                    inputData.containsKey( key ) && inputData.getString( key).equalsIgnoreCase( "Y" ) )
//                X += 1;
//
//            if ( inputParam.getString( key ).equalsIgnoreCase( "N" ) )
//                X += 1;
//        }
//
//        if ( X >= 4 )
//            outputData.setString( "isFreezeYN", "Y" );
//
//        return outputData;
//    }
}
