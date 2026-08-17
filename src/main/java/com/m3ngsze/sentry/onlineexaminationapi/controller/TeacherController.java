package com.m3ngsze.sentry.onlineexaminationapi.controller;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ApiResponse;
import com.m3ngsze.sentry.onlineexaminationapi.service.business.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/teacher")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Teacher Controller", description = "Handle creating new assignment for student and manage students")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/students/{room-id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "Retrieve all student by room id",
            description = "Teacher use for retrieve all students include both join room and own room"
    )
    public ResponseEntity<ApiResponse<UserDTO>> retrieveStudentsByRoomId() {

        return ResponseEntity.ok(ApiResponse.<UserDTO>builder()
                .message("User successfully deleted")
                .payload(null)
                .status(HttpStatus.OK)
                .build());
    }
}
