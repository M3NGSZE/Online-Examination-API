package com.m3ngsze.sentry.onlineexaminationapi.service;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.ResetPasswordRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public interface UserService {

    UserDTO getUserById(UUID userId);

    UserDTO getUserProfile();

    ListResponse<UserDTO> getAllUsers(Integer page, Integer size, String search, Sort.Direction sort, Boolean enable, Boolean verify);

    boolean resetPassword(ResetPasswordRequest request);

    boolean deactivateAccount(String refreshToken, HttpServletRequest request);

    boolean reactivateAccount(String email);

    boolean deactivateUser(UUID userId);

}
