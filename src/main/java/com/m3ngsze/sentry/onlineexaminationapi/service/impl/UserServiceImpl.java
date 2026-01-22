package com.m3ngsze.sentry.onlineexaminationapi.service.impl;

import com.m3ngsze.sentry.onlineexaminationapi.exception.BadRequestException;
import com.m3ngsze.sentry.onlineexaminationapi.exception.NotFoundException;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.UserInfo;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.ResetPasswordRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.PaginationResponse;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserInfoRepository;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.UserService;
import com.m3ngsze.sentry.onlineexaminationapi.specification.UserSpecification;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }


    @Override
    public UserDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserInfo userInfo = userInfoRepository.findById(user.getUserInfo().getInfoId())
                .orElseThrow(() -> new NotFoundException("User info not found"));

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        modelMapper.map(userInfo, userDTO);

        return userDTO;
    }

    @Override
    public UserDTO getUserProfile() {
        User user = getCurrentUser();

        UserInfo userInfo = userInfoRepository.findById(user.getUserInfo().getInfoId())
                .orElseThrow(() -> new NotFoundException("User info not found"));

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        modelMapper.map(userInfo, userDTO);

        return userDTO;
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
                .map(user -> modelMapper.map(user, UserDTO.class));

        List<UUID> infoId = userPage.map(UserDTO::getUserId).toList();

//        userRepository.findAll

        List<UserInfo> infoPage = userInfoRepository.findAllById(infoId);

        log.info("idinfo" + infoId.toString());
        log.info("info" + infoPage.toString());

        modelMapper.map(infoPage, userPage);

        return ListResponse.<UserDTO>builder()
                .data(userPage.getContent())
                .pagination(PaginationResponse.of(userPage.getTotalElements(), page, size))
                .build();
    }

    @Override
    public boolean resetPassword(ResetPasswordRequest request) {
        User user = getCurrentUser();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
            throw new BadCredentialsException("Incorrect password");

        if (!request.getNewPassword().equals(request.getConfirmPassword()))
            throw new BadRequestException("New passwords and confirm password do not match");

        user.setPassword(passwordEncoder.encode(request.getConfirmPassword()));
        user.setUpdatedAt(LocalDateTime.now());

        return true;
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        UUID userId = UUID.fromString((String) auth.getCredentials());
        if (auth == null) {
            throw new NotFoundException("Authentication not found");
        }

        User user = (User) auth.getPrincipal();

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        return user;
    }

}
