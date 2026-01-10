package com.m3ngsze.sentry.onlineexaminationapi.model.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
public class ApiResponse <T> {
    private String message;
    private HttpStatus status;
    @Builder.Default
    private LocalDateTime requestedTime = LocalDateTime.now();
    private T payload;
}
