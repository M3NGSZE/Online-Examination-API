package com.m3ngsze.sentry.onlineexaminationapi.controller;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.AuthDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.AuthRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.OtpRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.RegisterRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ApiResponse;
import com.m3ngsze.sentry.onlineexaminationapi.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
                .status(HttpStatus.OK)
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

}
