package com.m3ngsze.sentry.onlineexaminationapi.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OtpRequest {

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format. Please provide a valid email address.")
    @Schema(example = "example@gmail.com")
    private String email;

    @NotNull(message = "OTP cannot be null")
    @NotBlank(message = "OTP cannot be blank")
    @Size(min = 6, max = 6, message = "OTP must be 6 digits")
    @Schema(example = "xxx-xxx")
    private String otp;

}
