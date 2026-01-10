package com.m3ngsze.sentry.onlineexaminationapi.service;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.AuthDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.AuthRequest;

public interface AuthService {
    AuthDTO authenticate(AuthRequest authRequest);
}
