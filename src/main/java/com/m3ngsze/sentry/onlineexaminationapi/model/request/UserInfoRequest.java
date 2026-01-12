package com.m3ngsze.sentry.onlineexaminationapi.model.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserInfoRequest {

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String placeOfBirth;

    private String phoneNumber;

    private String profileUrl;

}
