package com.m3ngsze.sentry.onlineexaminationapi.service.component;

import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserCbc extends UserDetailsService {

    User getCurrentUser();

}
