package com.m3ngsze.sentry.onlineexaminationapi.service.impl;

import com.m3ngsze.sentry.onlineexaminationapi.exception.BadRequestException;
import com.m3ngsze.sentry.onlineexaminationapi.exception.NotFoundException;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.enums.AccountStatus;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.ResetPasswordRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.PaginationResponse;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.DetailService;
import com.m3ngsze.sentry.onlineexaminationapi.service.UserService;
import com.m3ngsze.sentry.onlineexaminationapi.specification.UserSpecification;
import com.m3ngsze.sentry.onlineexaminationapi.utility.UtilMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
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

    private final AuthServiceImpl authService;
    private final DetailService detailService;

    @Override
    public UserDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.getEnabled())
            throw new BadRequestException("User is not enabled");

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

        Page<UserDTO> userPage = userRepository.findAll(spec, pageable)
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
    public boolean deactivateUser(UUID userId) {
        return false;
    }

    @Override
    @Transactional
    public boolean deactivateAccount(String refreshToken, String authHeader) {
        User user = detailService.getCurrentUser();

        user.setEnabled(false);
        user.setAccountStatus(AccountStatus.DEACTIVATED);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        authService.logout(refreshToken, authHeader);

        return true;
    }

}
