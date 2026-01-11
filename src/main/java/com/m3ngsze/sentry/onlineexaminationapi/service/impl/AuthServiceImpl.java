package com.m3ngsze.sentry.onlineexaminationapi.service.impl;

import com.m3ngsze.sentry.onlineexaminationapi.exception.BadRequestException;
import com.m3ngsze.sentry.onlineexaminationapi.exception.NotFoundException;
import com.m3ngsze.sentry.onlineexaminationapi.jwt.JwtService;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.AuthDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Role;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.UserInfo;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.UserSession;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.AuthRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.RegisterRequest;
import com.m3ngsze.sentry.onlineexaminationapi.repository.RoleRepository;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserInfoRepository;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserRepository;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserSessionRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.AuthService;
import com.m3ngsze.sentry.onlineexaminationapi.service.UserService;
import com.m3ngsze.sentry.onlineexaminationapi.utility.ConvertUtil;
import com.m3ngsze.sentry.onlineexaminationapi.utility.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private final UserSessionRepository userSessionRepository;
    private final UserRepository UserRepository;
    private final ModelMapper modelMapper;
    private final UserInfoRepository userInfoRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;


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

    @Override
    @Transactional
    public UserDTO registerUser(RegisterRequest request) {

        RegisterRequest trimRequest = validateRegisterRequest(request);

        UserInfo userInfo = modelMapper.map(trimRequest, UserInfo.class);

        Role userRole = roleRepository.findRoleByRoleName("USER")
                .orElseThrow(() -> new NotFoundException("This role does not exist."));

        User user = modelMapper.map(trimRequest, User.class);

        UserInfo savedInfo = userInfoRepository.save(userInfo);
        user.setUserInfo(savedInfo );
        user.setRole(userRole);
        User saveUser = userRepository.save(user);

        UserDTO userDTO = modelMapper.map(saveUser, UserDTO.class);
        modelMapper.map(savedInfo, userDTO);

        return userDTO;
    }

    private RegisterRequest validateRegisterRequest(RegisterRequest request) {
        request.setEmail(request.getEmail().trim().toLowerCase());

        if (UserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("This email already been used");
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setFirstName(ConvertUtil.toPascalCase(request.getFirstName().trim()));
        request.setLastName(ConvertUtil.toPascalCase(request.getLastName().trim()));
        request.setPlaceOfBirth(request.getPlaceOfBirth().trim());
        request.setPhoneNumber(request.getPhoneNumber().trim());
        request.setProfileUrl(request.getProfileUrl().trim());

        long years = ChronoUnit.YEARS.between(request.getDateOfBirth(), LocalDate.now());
        if (years < 13 || years > 100) {
            throw new BadRequestException("Invalid date of birth");
        }
        return request;
    }

}
