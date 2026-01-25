package com.m3ngsze.sentry.onlineexaminationapi.service;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.OtpRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.ResetPasswordRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.UserInfoRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public interface UserService {

    UserDTO getUserById(UUID userId);

    UserDTO getUserProfile();

    ListResponse<UserDTO> getAllUsers(Integer page, Integer size, String search, Sort.Direction sort, Boolean enable, Boolean verify);

    boolean resetPassword(ResetPasswordRequest request);

    boolean deactivateAccount();

    boolean reactivateAccount(OtpRequest request);

    boolean adminDeactivateUser(UUID userId);

    boolean adminReactivateUser(UUID userId);

    void deleteCurrentUser();

    void adminDeleteUser(UUID userId);

    UserDTO updateUser(UserInfoRequest request);

}
