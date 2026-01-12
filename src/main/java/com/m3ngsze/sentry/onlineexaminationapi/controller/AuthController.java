package com.m3ngsze.sentry.onlineexaminationapi.controller;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.AuthDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.*;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ApiResponse;
import com.m3ngsze.sentry.onlineexaminationapi.service.AuthService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auths")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "Handle user authentication, registration, refresh token, otp, and logout.")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthDTO>> authentication(@RequestBody @Valid AuthRequest authRequest) {
        return ResponseEntity.ok(ApiResponse.<AuthDTO>builder()
                .message("Authentication successfully completed")
                .payload(authService.authenticate(authRequest))
                .status(HttpStatus.OK)
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(@RequestBody @Valid RegisterRequest registerRequest) {
        UserDTO userDTO = authService.registerUser(registerRequest);
        return ResponseEntity.ok(ApiResponse.<UserDTO>builder()
                .message("New user successfully created. OTP sent to your email")
                .payload(userDTO)
                .status(HttpStatus.CREATED)
                .build());
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Boolean>> verifyOtp(@RequestBody @Valid OtpRequest request) {
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .message("Account verified successfully")
                .payload(authService.verifyOtp(request))
                .status(HttpStatus.OK)
                .build());
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<Boolean>> resendOtp(
            @RequestBody
            @NotNull(message = "Email cannot be null")
            @NotBlank(message = "Email cannot be blank")
            @Email(message = "Invalid email format. Please provide a valid email address.")
            @Schema(example = "example@gmail.com")
            String email
    ) {
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .message("OTP resend successfully")
                .payload(authService.resendOtp(email))
                .status(HttpStatus.OK)
                .build());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Boolean>> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .message("User password successfully reset")
                .payload(authService.forgotPassword(request))
                .status(HttpStatus.OK)
                .build());
    }

}
