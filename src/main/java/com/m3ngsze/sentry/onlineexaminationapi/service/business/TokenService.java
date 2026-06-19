package com.m3ngsze.sentry.onlineexaminationapi.service.business;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.TokenDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;

public interface TokenService {

    TokenDTO createRefreshToken(User user);

}
