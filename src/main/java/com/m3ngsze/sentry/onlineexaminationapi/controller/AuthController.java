package com.m3ngsze.sentry.onlineexaminationapi.controller;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.AuthDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.*;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ApiResponse;
import com.m3ngsze.sentry.onlineexaminationapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/auths")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "Handle user authentication, registration, refresh token, otp, and logout.")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login with email and password", description = "Login will provide access token to any endpoint and authorize user base on role")
    public ResponseEntity<ApiResponse<AuthDTO>> authentication(@RequestBody @Valid AuthRequest authRequest) {
        return ResponseEntity.ok(ApiResponse.<AuthDTO>builder()
                .message("Authentication successfully completed")
                .payload(authService.authenticate(authRequest))
                .status(HttpStatus.OK)
                .build());
    }

    @PostMapping("/register")
    @Operation(summary = "Register account", description = "User must provide credential for register in requirement below")
    public ResponseEntity<ApiResponse<UserDTO>> register(@RequestBody @Valid RegisterRequest registerRequest) {
        UserDTO userDTO = authService.registerUser(registerRequest);
        return ResponseEntity.ok(ApiResponse.<UserDTO>builder()
                .message("New user successfully created. OTP sent to your email")
                .payload(userDTO)
                .status(HttpStatus.CREATED)
                .build());
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify account with otp", description = "After complete information user must verify account with otp")
    public ResponseEntity<ApiResponse<Boolean>> verifyOtp(@RequestBody @Valid OtpRequest request) {
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .message("Account verified successfully")
                .payload(authService.verifyOtp(request))
                .status(HttpStatus.OK)
                .build());
    }

    @PostMapping("/resend-otp")
    @Operation(summary = "Resent otp to user account", description = "User can resent otp again in case otp is invalid or expired")
    public ResponseEntity<ApiResponse<Boolean>> resendOtp(@RequestParam String email) {
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .message("OTP resend successfully")
                .payload(authService.resendOtp(email))
                .status(HttpStatus.OK)
                .build());
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Reset new password if user forgot password", description = "User must verify account with otp then create new password")
    public ResponseEntity<ApiResponse<Boolean>> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .message("User password successfully reset")
                .payload(authService.forgotPassword(request))
                .status(HttpStatus.OK)
                .build());
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Renew fresh token", description = "When token expired client side can new token and refresh token by refresh token")
    public ResponseEntity<ApiResponse<AuthDTO>> refreshToken(@RequestParam String refreshToken) {
        return ResponseEntity.ok(ApiResponse.<AuthDTO>builder()
                .message("Refresh token successfully reset")
                .payload(authService.refreshToken(refreshToken))
                .status(HttpStatus.OK)
                .build());
    }

    @GetMapping("/oauth2/google")
    @Operation(summary = "Login with Google", description = "Redirects to Google login page")
    public void googleLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

    @PostMapping("/logout")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Logout of account", description = "Logging out of session")
    public ResponseEntity<ApiResponse<Boolean>> logout(@RequestParam String refreshToken) {
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .message("Logout successfully")
                .payload(authService.logout(refreshToken))
                .status(HttpStatus.OK)
                .build());
    }

}
