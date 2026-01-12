package com.m3ngsze.sentry.onlineexaminationapi.service;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface UserService extends UserDetailsService {

    UserDTO getUserById(UUID userId);

    UserDTO getUserProfile();

    List<UserDTO> getAllUsers(Integer page, Integer size, String search);

}
