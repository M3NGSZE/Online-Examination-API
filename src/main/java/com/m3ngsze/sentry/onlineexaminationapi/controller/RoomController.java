package com.m3ngsze.sentry.onlineexaminationapi.controller;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.RoomRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ApiResponse;
import com.m3ngsze.sentry.onlineexaminationapi.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/rooms")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Room Controller", description = "Handle creating new class room for use and user join or participate in classroom")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "User role",
            description = "Use for create new room"
    )
    public ResponseEntity<ApiResponse<RoomDTO>> createRoom(@RequestBody RoomRequest roomRequest) {
        return ResponseEntity.ok(ApiResponse.<RoomDTO>builder()
                .message("All users successfully fetched")
                .payload(roomService.createRoom(roomRequest))
                .status(HttpStatus.OK)
                .build());
    }

}
