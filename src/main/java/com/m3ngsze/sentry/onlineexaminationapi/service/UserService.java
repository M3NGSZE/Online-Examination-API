package com.m3ngsze.sentry.onlineexaminationapi.service;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.ResetPasswordRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UserService extends UserDetailsService {

    UserDTO getUserById(UUID userId);

    UserDTO getUserProfile();

    ListResponse<UserDTO> getAllUsers(Integer page, Integer size, String search, Sort.Direction sort, Boolean enable, Boolean verify);

    boolean resetPassword(ResetPasswordRequest request);

    User getCurrentUser();

    boolean deactivateUser(UUID userId);

    boolean deactivateAccount(String refreshToken, String authHeader);

}
