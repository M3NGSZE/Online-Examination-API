package com.m3ngsze.sentry.onlineexaminationapi.service.impl;

import com.m3ngsze.sentry.onlineexaminationapi.exception.NotFoundException;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.UserInfo;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserInfoRepository;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        UUID userId = UUID.fromString((String) auth.getCredentials());
        if (auth == null) {
            throw new NotFoundException("Authentication not found");
        }

        User user = (User) auth.getPrincipal();

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        UserInfo userInfo = userInfoRepository.findById(user.getUserInfo().getInfoId())
                .orElseThrow(() -> new NotFoundException("User info not found"));

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        modelMapper.map(userInfo, userDTO);

        return userDTO;
    }
}
