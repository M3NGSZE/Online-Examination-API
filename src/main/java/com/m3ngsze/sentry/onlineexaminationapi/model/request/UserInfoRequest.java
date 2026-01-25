package com.m3ngsze.sentry.onlineexaminationapi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserInfoRequest {

    @NotNull(message = "first name cannot be null")
    @NotBlank(message = "first name cannot be blank")
    private String firstName;

    @NotNull(message = "last name cannot be null")
    @NotBlank(message = "last name cannot be blank")
    private String lastName;

    private LocalDate dateOfBirth;

    private String placeOfBirth;

    private String phoneNumber;

    private String profileUrl;

}
