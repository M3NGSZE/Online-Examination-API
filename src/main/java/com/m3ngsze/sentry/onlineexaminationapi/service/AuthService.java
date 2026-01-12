package com.m3ngsze.sentry.onlineexaminationapi.service;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.AuthDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.AuthRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.OtpRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.RegisterRequest;

public interface AuthService {

    AuthDTO authenticate(AuthRequest authRequest);

    UserDTO registerUser(RegisterRequest request);

    boolean verifyOtp(OtpRequest otp);
}
