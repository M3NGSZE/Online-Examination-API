package com.m3ngsze.sentry.onlineexaminationapi.controller;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.ResetPasswordRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ApiResponse;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import com.m3ngsze.sentry.onlineexaminationapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Admin role",
            description = "Use for fetch all user for admin role"
    )
    public ResponseEntity<ApiResponse<ListResponse<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "1") @Positive @Min(value = 1, message = "must greater than 0") Integer page,
            @RequestParam(defaultValue = "3") @Positive @Min(value = 1, message = "must greater than 0") Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "ASC") Sort.Direction sort,
            @RequestParam(defaultValue = "true") Boolean enable,
            @RequestParam(defaultValue = "true") Boolean verify
            ) {
        return ResponseEntity.ok(ApiResponse.<ListResponse<UserDTO>>builder()
                .message("All users successfully fetched")
                .payload(userService.getAllUsers(page, size, search, sort, enable, verify))
                .status(HttpStatus.OK)
                .build());
    }

    @GetMapping("/{user-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Admin role",
            description = "Use for fetch user by user id"
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
            summary = "view own profile info"
    )
    public ResponseEntity<ApiResponse<UserDTO>> getUserProfile() {
        return ResponseEntity.ok(ApiResponse.<UserDTO>builder()
                .message("User profile successfully fetched")
                .payload(userService.getUserProfile())
                .status(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/reset-password")
    @Operation(summary = "Reset new password", description = "User must require to input old password and new password")
    public ResponseEntity<ApiResponse<Boolean>> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .message("User password successfully reset")
                .payload(userService.resetPassword(request))
                .status(HttpStatus.OK)
                .build());
    }

    @PatchMapping("/deactivate-account")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "User role",
            description = "Use for deactivate user account for temporary"
    )
    public ResponseEntity<ApiResponse<Boolean>> deactivateAccount(@RequestParam String refreshToken, @RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .message("User successfully deactivated")
                .payload(userService.deactivateAccount(refreshToken, authHeader))
                .status(HttpStatus.OK)
                .build());
    }

}
