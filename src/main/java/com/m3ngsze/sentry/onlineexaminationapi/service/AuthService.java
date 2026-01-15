package com.m3ngsze.sentry.onlineexaminationapi.service;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.AuthDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.*;

public interface AuthService {

    AuthDTO authenticate(AuthRequest authRequest);

    UserDTO registerUser(RegisterRequest request);

    boolean verifyOtp(OtpRequest otp);

    boolean resendOtp(String email);

    boolean forgotPassword(ForgotPasswordRequest request);

    AuthDTO refreshToken(String refreshToken);

    boolean logout(String refreshToken);

}
