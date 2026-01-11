package com.m3ngsze.sentry.onlineexaminationapi.model.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public class UserDTO {
    private UUID userId;

    private String email;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String placeOfBirth;

    private String phoneNumber;

    private String profileUrl;
}
