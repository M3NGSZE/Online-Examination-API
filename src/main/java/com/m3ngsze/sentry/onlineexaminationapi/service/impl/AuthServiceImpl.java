package com.m3ngsze.sentry.onlineexaminationapi.service.impl;

import com.m3ngsze.sentry.onlineexaminationapi.exception.BadRequestException;
import com.m3ngsze.sentry.onlineexaminationapi.exception.NotFoundException;
import com.m3ngsze.sentry.onlineexaminationapi.exception.OtpException;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.AuthDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.TokenDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Role;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.UserInfo;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.UserSession;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.*;
import com.m3ngsze.sentry.onlineexaminationapi.repository.RoleRepository;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserInfoRepository;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserRepository;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserSessionRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.*;
import com.m3ngsze.sentry.onlineexaminationapi.utility.ConvertUtil;
import com.m3ngsze.sentry.onlineexaminationapi.utility.OtpGenerator;
import com.m3ngsze.sentry.onlineexaminationapi.utility.TokenUtil;
import lombok.RequiredArgsConstructor;
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
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;



@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final OtpGenerator otpGenerator;

    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final TokenService tokenService;

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
        User user = (User) userDetails;

        if(!user.getVerified())
            throw new BadCredentialsException("Account is not verified");

        return AuthDTO.builder()
                .accessToken(tokenService.createRefreshToken(user).getAccessToken())
                .refreshToken(tokenService.createRefreshToken(user).getRefreshToken())
                .role(userDetails.getAuthorities().iterator().next().getAuthority())
                .build();
    }

    @Override
    @Transactional
    public UserDTO registerUser(RegisterRequest request) {

        // validate
        RegisterRequest trimRequest = validateRegisterRequest(request);

        // 1. Save user as NOT VERIFIED
        UserDTO userDTO = insertUser(trimRequest);

        // 2. Generate OTP
        String otp = otpGenerator.generateOtp();

        // 3. Save OTP in Redis
        otpService.saveOtp(userDTO.getEmail(), otp);

        // 4. Send OTP
        emailService.sendOtp(userDTO.getEmail(), otp);

        return userDTO;
    }

    private UserDTO insertUser(RegisterRequest request) {
        UserInfo userInfo = modelMapper.map(request, UserInfo.class);

        Role userRole = roleRepository.findRoleByRoleName("USER")
                .orElseThrow(() -> new NotFoundException("This role does not exist."));

        User user = modelMapper.map(request, User.class);

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

    @Override
    public boolean verifyOtp(OtpRequest otp) {
        boolean isValid = otpService.verifyOtp(otp.getEmail(), otp.getOtp());
        if (!isValid) {
            throw new OtpException("Invalid or expired OTP");
        }

        verifyUser(otp.getEmail());

        return true;
    }

    private void verifyUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("This email does not exist"));

        if (user.getVerified())
            return;

        user.setVerified(true);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }


    @Override
    public boolean resendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("This email does not exist"));

        String otp = otpGenerator.generateOtp();

        otpService.saveOtp(user.getEmail(), otp);

        emailService.sendOtp(user.getEmail(), otp);

        return true;
    }

    @Override
    public boolean forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("This email does not exist"));

        if (!request.getConfirmPassword().equals(request.getPassword()))
            throw new BadRequestException("Passwords and confirm password do not match");

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return true;
    }

    @Override
    public AuthDTO refreshToken(String refreshToken) {

        String hashToken = TokenUtil.hashToken(refreshToken);

        UserSession userSession = userSessionRepository.findByRefreshTokenHash(hashToken)
                .orElseThrow(() -> new BadRequestException("Refresh token does not exist"));

        if (userSession.getExpiresAt().isBefore(ChronoLocalDateTime.from(Instant.now()))){
            userSessionRepository.delete(userSession);
            throw new BadRequestException("Refresh token expired");
        }

        User user = userSession.getUser();

        // rotate refresh token
        userSessionRepository.delete(userSession);

        TokenDTO tokenDTO = tokenService.createRefreshToken(user);

        return AuthDTO.builder()
                .accessToken(tokenDTO.getAccessToken())
                .refreshToken(tokenDTO.getRefreshToken())
                .role(user.getRole().getRoleName())
                .build();
    }

}
