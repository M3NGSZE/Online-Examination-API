package com.m3ngsze.sentry.onlineexaminationapi.controller;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.InviteCodeDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.JoinRoomRequest;
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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
                .message("Room successfully created")
                .payload(roomService.createRoom(roomRequest))
                .status(HttpStatus.OK)
                .build());
    }

    @PatchMapping("{room-id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "User role",
            description = "Use for update own user room"
    )
    public ResponseEntity<ApiResponse<RoomDTO>> updateRoom(@PathVariable("room-id") UUID roomId, @RequestBody RoomRequest roomRequest) {
        return ResponseEntity.ok(ApiResponse.<RoomDTO>builder()
                .message("Room successfully updated")
                .payload(roomService.updateRoom(roomId, roomRequest))
                .status(HttpStatus.OK)
                .build());
    }

    @GetMapping("{room-id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "User role",
            description = "Use for get own user room detail by id"
    )
    public ResponseEntity<ApiResponse<RoomDTO>> getRoomById(@PathVariable("room-id") UUID roomId) {
        return ResponseEntity.ok(ApiResponse.<RoomDTO>builder()
                .message("Room successfully fetched")
                .payload(roomService.findRoomById(roomId))
                .status(HttpStatus.OK)
                .build());
    }

    @DeleteMapping("{room-id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "User role",
            description = "Use for delete own user room"
    )
    public ResponseEntity<ApiResponse<Boolean>> deleteRoomById(@PathVariable("room-id") UUID roomId) {
        roomService.deleteRoomById(roomId);
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .message("Room successfully deleted")
                .payload(true)
                .status(HttpStatus.OK)
                .build());
    }

    @GetMapping("/admin/{room-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Admin role",
            description = "Admin use to get user room detail by id"
    )
    public ResponseEntity<ApiResponse<RoomDTO>> getRoomByIdAdmin(@PathVariable("room-id") UUID roomId) {
        return ResponseEntity.ok(ApiResponse.<RoomDTO>builder()
                .message("Room successfully fetched")
                .payload(roomService.findRoomByIdAdmin(roomId))
                .status(HttpStatus.OK)
                .build());
    }

    @DeleteMapping("admin/{room-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Admin role",
            description = "Admin use to delete  user room"
    )
    public ResponseEntity<ApiResponse<Boolean>> deleteRoomByIdAdmin(@PathVariable("room-id") UUID roomId) {
        roomService.deleteRoomByIdAdmin(roomId);
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .message("Room successfully deleted")
                .payload(true)
                .status(HttpStatus.OK)
                .build());
    }

    @PostMapping("/invitation-code/{room-id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "User role",
            description = "Use for create room invitation code for other user to join"
    )
    public ResponseEntity<ApiResponse<InviteCodeDTO>> createInvitationCode(@PathVariable("room-id") UUID roomId) {
        return ResponseEntity.ok(ApiResponse.<InviteCodeDTO>builder()
                .message("Room successfully updated")
                .payload(roomService.createInviteCode(roomId))
                .status(HttpStatus.CREATED)
                .build());
    }

    @PostMapping("/join-room")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "User role",
            description = "Use for join room by room code"
    )
    public ResponseEntity<ApiResponse<RoomDTO>> joinRoom(@RequestBody JoinRoomRequest joinRoomRequest) {
        return ResponseEntity.ok(ApiResponse.<RoomDTO>builder()
                .message("Room successfully updated")
                .payload(roomService.joinRoom(joinRoomRequest.getCode()))
                .status(HttpStatus.CREATED)
                .build());
    }

    @DeleteMapping("/leave-room/{room-id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "User role",
            description = "Use for join room by room code"
    )
    public ResponseEntity<ApiResponse<Boolean>> leaveRoom(@PathVariable("room-id") UUID roomId) {
        roomService.leaveRoom(roomId);
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .message("Room successfully updated")
                .payload(true)
                .status(HttpStatus.OK)
                .build());
    }

}
