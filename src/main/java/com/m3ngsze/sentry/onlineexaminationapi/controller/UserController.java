package com.m3ngsze.sentry.onlineexaminationapi.controller;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ApiResponse;
import com.m3ngsze.sentry.onlineexaminationapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Controller", description = "Handle user information, use by admin and user role")
public class UserController {
    private final UserService userService;

    @GetMapping("/{user-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Admin role",
            description = "Use to get user by user id"
    )
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(
            @PathVariable ("user-id")
            UUID userId
    ) {
        return ResponseEntity.ok(ApiResponse.<UserDTO>builder()
                .message("A user successfully fetched")
                .payload(userService.getUserById(userId))
                .status(HttpStatus.OK)
                .build());
    }

    @GetMapping("/user-profile")
    @Operation(
            summary = "User role",
            description = "Use for get own user profile"
    )
    public ResponseEntity<ApiResponse<UserDTO>> getUserProfile() {
        return ResponseEntity.ok(ApiResponse.<UserDTO>builder()
                .message("User profile successfully fetched")
                .payload(userService.getUserProfile())
                .status(HttpStatus.OK)
                .build());
    }
}
