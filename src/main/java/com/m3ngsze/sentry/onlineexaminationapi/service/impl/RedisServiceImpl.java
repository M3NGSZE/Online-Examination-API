package com.m3ngsze.sentry.onlineexaminationapi.service.impl;

import com.m3ngsze.sentry.onlineexaminationapi.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final long OTP_TTL = 2;

    // Save token in Redis blacklist
    @Override
    public void revokeToken(String token, long expirationSeconds) {
        if (redisTemplate == null) return; // skip if Redis not configured
        String key = "revokedToken:" + token;
        redisTemplate.opsForValue().set(key, "true", expirationSeconds, TimeUnit.SECONDS);
    }

    // Check if token is revoked
    @Override
    public boolean isTokenRevoked(String token) {
        if (redisTemplate == null) return false; // skip if Redis not configured
        String key = "revokedToken:" + token;
        return redisTemplate.hasKey(key);
    }

    public void saveOtp(String email, String otp) {
        String key = "OTP:" + email;
        redisTemplate.opsForValue()
                .set(key, otp, OTP_TTL, TimeUnit.MINUTES);
    }

    public boolean verifyOtp(String email, String otp) {
        String key = "OTP:" + email;
        String savedOtp = redisTemplate.opsForValue().get(key);

        if (savedOtp == null) return false;

        boolean isValid = savedOtp.equals(otp);
        if (isValid) {
            redisTemplate.delete(key);
        }
        return isValid;
    }

}
