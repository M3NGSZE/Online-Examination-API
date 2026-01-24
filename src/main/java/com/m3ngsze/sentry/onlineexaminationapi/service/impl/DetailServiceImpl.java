package com.m3ngsze.sentry.onlineexaminationapi.service.impl;

import com.m3ngsze.sentry.onlineexaminationapi.exception.NotFoundException;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.DetailService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetailServiceImpl implements DetailService {

    private final UserRepository userRepository;

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        UUID userId = UUID.fromString((String) auth.getCredentials());
        if (auth == null) {
            throw new NotFoundException("Authentication not found");
        }

        User user = (User) auth.getPrincipal();

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        return user;
    }
}
