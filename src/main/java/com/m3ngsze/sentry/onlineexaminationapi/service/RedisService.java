package com.m3ngsze.sentry.onlineexaminationapi.service;

public interface RedisService {

    void revokeToken(String token, long expirationSeconds);

    boolean isTokenRevoked(String token);

    void saveOtp(String email, String otp);

    boolean verifyOtp(String email, String otp);

}
