package com.m3ngsze.sentry.onlineexaminationapi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotNull(message = "Old password cannot be null")
    @NotBlank(message = "Old password cannot be blank")
    private String oldPassword;

    @NotNull(message = "New password cannot be null")
    @NotBlank(message = "New password cannot be blank")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
            message = "Password must be at least 8 characters long and include 1 uppercase letter, 1 number, and 1 special character"
    )
    private String newPassword;

    @NotNull(message = "Confirm password cannot be null")
    @NotBlank(message = "Confirm password cannot be blank")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
            message = "Password must be at least 8 characters long and include 1 uppercase letter, 1 number, and 1 special character"
    )
    private String confirmPassword;
}
