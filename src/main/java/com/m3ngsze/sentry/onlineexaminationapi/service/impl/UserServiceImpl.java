package com.m3ngsze.sentry.onlineexaminationapi.service.impl;

import com.m3ngsze.sentry.onlineexaminationapi.exception.BadRequestException;
import com.m3ngsze.sentry.onlineexaminationapi.exception.NotFoundException;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.enums.AccountStatus;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.OtpRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.ResetPasswordRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.PaginationResponse;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.*;
import com.m3ngsze.sentry.onlineexaminationapi.specification.UserSpecification;
import com.m3ngsze.sentry.onlineexaminationapi.utility.EmailValidatorUtil;
import com.m3ngsze.sentry.onlineexaminationapi.utility.UtilMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthService authService;
    private final DetailService detailService;
    private final RedisService redisService;

    @Override
    public UserDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.getEnabled())
            throw new BadRequestException("User is not enabled");

        if (user.getAccountStatus().equals(AccountStatus.DELETED))
            throw new NotFoundException("User not found");

        return UtilMapper.toUserDTO(user);
    }

    @Override
    public UserDTO getUserProfile() {
        User user = detailService.getCurrentUser();

        return UtilMapper.toUserDTO(user);
    }

    @Override
    public ListResponse<UserDTO> getAllUsers(Integer page, Integer size, String search, Sort.Direction sort, Boolean enable, Boolean verify) {

        Specification<User> spec  = Specification
                .where(UserSpecification.search(search))
                .and(UserSpecification.isEnabled(enable))
                .and(UserSpecification.isVerified(verify));

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(sort, "createdAt")
        );

        Page<UserDTO> userPage = (Page<UserDTO>) userRepository.findAll(spec, pageable)
                .filter(u -> !u.getAccountStatus().equals(AccountStatus.DELETED))
                .map(UtilMapper::toUserDTO);

        return ListResponse.<UserDTO>builder()
                .data(userPage.getContent())
                .pagination(PaginationResponse.of(userPage.getTotalElements(), page, size))
                .build();
    }


    @Override
    public boolean resetPassword(ResetPasswordRequest request) {
        User user = detailService.getCurrentUser();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
            throw new BadCredentialsException("Incorrect password");

        if (!request.getNewPassword().equals(request.getConfirmPassword()))
            throw new BadRequestException("New passwords and confirm password do not match");

        user.setPassword(passwordEncoder.encode(request.getConfirmPassword()));
        user.setUpdatedAt(LocalDateTime.now());

        return true;
    }

    @Override
    @Transactional
    public boolean deactivateAccount(String refreshToken, HttpServletRequest request) {
        User user = detailService.getCurrentUser();

        user.setEnabled(false);
        user.setAccountStatus(AccountStatus.DEACTIVATED);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        authService.logout(refreshToken, request);

        return true;
    }

    @Override
    @Transactional
    public boolean reactivateAccount(OtpRequest request) {
        if (!EmailValidatorUtil.isValid(request.getEmail())) {
            throw new BadRequestException("Invalid email format");
        }

        boolean isValid = redisService.verifyOtp(request.getEmail(), request.getOtp());

        if (!isValid)
            throw new BadRequestException("Invalid email format");

        UserDetails userDetails = detailService.loadUserByUsername(request.getEmail());
        User user = (User) userDetails;

        user.setEnabled(true);
        user.setAccountStatus(AccountStatus.ACTIVATED);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return true;
    }

    @Override
    public boolean deactivateUser(UUID userId) {
        return false;
    }

}
