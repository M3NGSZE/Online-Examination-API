package com.m3ngsze.sentry.onlineexaminationapi.service;

import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface DetailService extends UserDetailsService {

    User getCurrentUser();

}
