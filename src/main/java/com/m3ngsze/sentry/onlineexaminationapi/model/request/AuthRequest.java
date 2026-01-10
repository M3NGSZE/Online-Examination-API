package com.m3ngsze.sentry.onlineexaminationapi.model.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
