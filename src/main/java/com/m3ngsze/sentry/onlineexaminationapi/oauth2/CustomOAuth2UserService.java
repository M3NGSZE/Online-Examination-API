package com.m3ngsze.sentry.onlineexaminationapi.oauth2;

import com.m3ngsze.sentry.onlineexaminationapi.exception.NotFoundException;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Role;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.UserInfo;
import com.m3ngsze.sentry.onlineexaminationapi.model.enums.AuthProvider;
import com.m3ngsze.sentry.onlineexaminationapi.repository.RoleRepository;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserInfoRepository;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            // Delegate to default Spring user service
            OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

            // Extract user info from Google
            String email = oauth2User.getAttribute("email");
            String providerId = oauth2User.getAttribute("sub"); // unique Google ID
            String firstName = oauth2User.getAttribute("given_name");
            String lastName = oauth2User.getAttribute("family_name");
            String profile = oauth2User.getAttribute("picture");

            if (email == null || providerId == null) {
                throw new OAuth2AuthenticationException("Missing required OAuth2 attributes (email or sub)");
            }

        Role userRole = roleRepository.findRoleByRoleName("USER")
                .orElseThrow(() -> new NotFoundException("This role does not exist."));

        // Check if user exists in DB
        User user = userRepository.findByEmail(email)
                .map(existingUser -> {
                    // User exists - handle different scenarios
                    if (existingUser.getProvider() == AuthProvider.LOCAL) {
                        // User registered with email/password, now trying OAuth2
                        // Link OAuth2 account to existing user
                        existingUser.setProvider(AuthProvider.GOOGLE);
                        existingUser.setProviderId(providerId);
                        existingUser.setVerified(true); // OAuth2 users are auto-verified
                        // Keep existing password for LOCAL users who link OAuth2
                        
                        // Update user info if available
                        if (existingUser.getUserInfo() != null) {
                            UserInfo userInfo = existingUser.getUserInfo();
                            if (firstName != null && userInfo.getFirstName() == null) {
                                userInfo.setFirstName(firstName);
                            }
                            if (lastName != null && userInfo.getLastName() == null) {
                                userInfo.setLastName(lastName);
                            }
                            if (profile != null && userInfo.getProfileUrl() == null) {
                                userInfo.setProfileUrl(profile);
                            }
                            userInfoRepository.save(userInfo);
                        } else {
                            // Create user info if it doesn't exist
                            UserInfo userInfo = new UserInfo();
                            userInfo.setFirstName(firstName);
                            userInfo.setLastName(lastName);
                            userInfo.setProfileUrl(profile);
                            userInfoRepository.save(userInfo);
                            existingUser.setUserInfo(userInfo);
                        }
                        
                        return userRepository.save(existingUser);
                    } else if (existingUser.getProvider() == AuthProvider.GOOGLE) {
                        // User already registered with Google OAuth2
                        if (!providerId.equals(existingUser.getProviderId())) {
                            // Different Google account trying to use same email
                            throw new OAuth2AuthenticationException(
                                    "This email is already associated with a different Google account"
                            );
                        }
                        
                        // Update user info if needed
                        if (existingUser.getUserInfo() != null) {
                            UserInfo userInfo = existingUser.getUserInfo();
                            boolean updated = false;
                            
                            if (firstName != null && !firstName.equals(userInfo.getFirstName())) {
                                userInfo.setFirstName(firstName);
                                updated = true;
                            }
                            if (lastName != null && !lastName.equals(userInfo.getLastName())) {
                                userInfo.setLastName(lastName);
                                updated = true;
                            }
                            if (profile != null && !profile.equals(userInfo.getProfileUrl())) {
                                userInfo.setProfileUrl(profile);
                                updated = true;
                            }
                            
                            if (updated) {
                                userInfoRepository.save(userInfo);
                            }
                        }
                        
                        return existingUser;
                    }
                    return existingUser;
                })
                .orElseGet(() -> {
                    // New user - create account
                    log.info("Creating new OAuth2 user: {}", email);
                    UserInfo userInfo = new UserInfo();
                    userInfo.setFirstName(firstName);
                    userInfo.setLastName(lastName);
                    userInfo.setProfileUrl(profile);

                    userInfoRepository.save(userInfo);

                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setPassword("OAUTH2_USER"); // Placeholder password (not used for OAuth2 authentication)
                    newUser.setProvider(AuthProvider.GOOGLE);
                    newUser.setProviderId(providerId);
                    newUser.setUserInfo(userInfo);
                    newUser.setRole(userRole);
                    newUser.setEnabled(true);
                    newUser.setVerified(true);

                    return userRepository.save(newUser);
                });


            // GUARANTEE userId exists
            if (user.getUserId() == null) {
                throw new IllegalStateException("User ID is null after save");
            }

            // Attach userId to OAuth2 attributes
            Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
            attributes.put("userId", user.getUserId().toString());

            return new DefaultOAuth2User(
                    Collections.singleton(
                            new SimpleGrantedAuthority(user.getRole().getRoleName())
                    ),
                    attributes,
                    "sub"
            );
        } catch (OAuth2AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new OAuth2AuthenticationException("Failed to process OAuth2 user: " + e.getMessage());
        }
    }

}
