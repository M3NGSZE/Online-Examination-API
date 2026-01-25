package com.m3ngsze.sentry.onlineexaminationapi.service.impl;

import com.m3ngsze.sentry.onlineexaminationapi.exception.BadRequestException;
import com.m3ngsze.sentry.onlineexaminationapi.exception.NotFoundException;
import com.m3ngsze.sentry.onlineexaminationapi.exception.OtpException;
import com.m3ngsze.sentry.onlineexaminationapi.jwt.JwtService;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.AuthDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.TokenDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Role;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.UserInfo;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.UserSession;
import com.m3ngsze.sentry.onlineexaminationapi.model.enums.AccountStatus;
import com.m3ngsze.sentry.onlineexaminationapi.model.enums.AuthProvider;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.*;
import com.m3ngsze.sentry.onlineexaminationapi.repository.RoleRepository;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserRepository;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserSessionRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.*;
import com.m3ngsze.sentry.onlineexaminationapi.utility.*;
import jakarta.servlet.http.HttpServletRequest;
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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final OtpGenerator otpGenerator;

    private final DetailService detailService;
    private final RedisService redisService;
    private final EmailService emailService;
    private final TokenService tokenService;

    private final UserSessionRepository userSessionRepository;
    private final UserRepository UserRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    // do not separate authenticationManager.authenticate it can cause error "Circular Dependency"

    @Override
    public AuthDTO authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail().trim(),
                            request.getPassword()
                    )
            );
        } catch (DisabledException e) {
            throw new RuntimeException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("INVALID_CREDENTIALS", e);
        }

        UserDetails userDetails = detailService.loadUserByUsername(request.getEmail());
        User user = getUser((User) userDetails);

        TokenDTO refreshToken = tokenService.createRefreshToken(user);

        return AuthDTO.builder()
                .accessToken(refreshToken.getAccessToken())
                .refreshToken(refreshToken.getRefreshToken())
                .expiresIn(300L)
                .role(userDetails.getAuthorities().iterator().next().getAuthority())
                .build();
    }

    private static User getUser(User userDetails) {
        if(!userDetails.getVerified())
            throw new BadCredentialsException("Account is not verified");

        if (!userDetails.getEnabled()) {
            if (userDetails.getAccountStatus() == AccountStatus.DELETED) {
                throw new BadCredentialsException("INVALID_CREDENTIALS");
            } else if (userDetails.getAccountStatus() == AccountStatus.DEACTIVATED) {
                throw new BadCredentialsException("Account is deactivated. Please reactivate.");
            } else {
                throw new BadCredentialsException("Account disabled");
            }
        }
        return userDetails;
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
        redisService.saveOtp(userDTO.getEmail(), otp);

        // 4. Send OTP
        emailService.sendOtp(userDTO.getEmail(), otp);

        return userDTO;
    }

    private UserDTO insertUser(RegisterRequest request) {

        Role userRole = roleRepository.findRoleByRoleName("USER")
                .orElseThrow(() -> new NotFoundException("This role does not exist."));

        User user = modelMapper.map(request, User.class);
        user.setRole(userRole);
        user.setProvider(AuthProvider.LOCAL);
        user.setAccountStatus(AccountStatus.ACTIVATED);

        UserInfo userInfo = modelMapper.map(request, UserInfo.class);
        userInfo.setUser(user);
        user.setUserInfo(userInfo);

        User saveUser = userRepository.save(user);

        return modelMapper.map(saveUser, UserDTO.class);
    }

    private RegisterRequest validateRegisterRequest(RegisterRequest request) {
        request.setEmail(request.getEmail().trim().toLowerCase());

        if (UserRepository.findByEmailAndDeletedAtIsNull(request.getEmail(), null).isPresent()) {
            throw new BadRequestException("This email already been used");
        }

        UserInfoRequest toUserInfoRequest = modelMapper.map(request, UserInfoRequest.class);

        UserInfoRequest userInfoRequest = RequestMapUtil.validateRegisterRequest(toUserInfoRequest);

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setFirstName(userInfoRequest.getFirstName());
        request.setLastName(userInfoRequest.getLastName());
        request.setPlaceOfBirth(userInfoRequest.getPlaceOfBirth());
        request.setPhoneNumber(userInfoRequest.getPhoneNumber());
        request.setProfileUrl(userInfoRequest.getProfileUrl());

        return request;
    }

    @Override
    public boolean verifyOtp(OtpRequest otp) {
        boolean isValid = redisService.verifyOtp(otp.getEmail(), otp.getOtp());
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

        if (!EmailValidatorUtil.isValid(email)) {
            throw new BadRequestException("Invalid email format");
        }

        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new NotFoundException("This email does not exist"));

        String otp = otpGenerator.generateOtp();

        redisService.saveOtp(user.getEmail(), otp);

        emailService.sendOtp(user.getEmail(), otp);

        return true;
    }

    @Override
    public boolean forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim())
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
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadRequestException("Refresh token cannot be null or blank");
        }

        String hashToken = TokenUtil.hashToken(refreshToken.trim());

        UserSession userSession = userSessionRepository.findByRefreshTokenHash(hashToken)
                .orElseThrow(() -> new BadRequestException("Refresh token does not exist"));

        if (userSession.getExpiresAt().isBefore(LocalDateTime.now())){
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
                .expiresIn(300L)
                .role(user.getRole().getRoleName())
                .build();
    }

    @Override
    public boolean logout(String refreshToken, HttpServletRequest request) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadRequestException("Refresh token cannot be null or blank");
        }

        String hashToken = TokenUtil.hashToken(refreshToken.trim());

        User user = detailService.getCurrentUser();

        UserSession userSession = userSessionRepository.findByRefreshTokenHashAndUser(hashToken, user)
                .orElseThrow(() -> new NotFoundException("Refresh token not found for current user"));

        String token =detailService.extractAccessToken(request);
        long jwtTokenExpiry = JwtService.JWT_TOKEN_EXPIRY;// seconds until token expires
        redisService.revokeToken(token, jwtTokenExpiry);

        userSessionRepository.delete(userSession);

        return true;
    }

}
