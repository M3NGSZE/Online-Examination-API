package com.m3ngsze.sentry.onlineexaminationapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final long OTP_TTL = 2; // minutes

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
