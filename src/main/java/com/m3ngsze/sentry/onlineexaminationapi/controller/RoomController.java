package com.m3ngsze.sentry.onlineexaminationapi.controller;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.InviteCodeDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.JoinRoomRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.RoomRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ApiResponse;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import com.m3ngsze.sentry.onlineexaminationapi.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("api/v1/rooms")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Room Controller", description = "Handle creating new class room for use and user join or participate in classroom")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "Create new room",
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
            summary = "Update one room",
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
            summary = "Get one room",
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
            summary = "Delete one room",
            description = "User use for delete own user room"
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
            summary = "Create room code",
            description = "User use for create room invitation code for other user to join"
    )
    public ResponseEntity<ApiResponse<InviteCodeDTO>> createInvitationCode(@PathVariable("room-id") UUID roomId) {
        return ResponseEntity.ok(ApiResponse.<InviteCodeDTO>builder()
                .message("Room code successfully created")
                .payload(roomService.createInviteCode(roomId))
                .status(HttpStatus.CREATED)
                .build());
    }

    @PostMapping("/join-room")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "Join new room",
            description = "User use for join room by room code"
    )
    public ResponseEntity<ApiResponse<RoomDTO>> joinRoom(@RequestBody JoinRoomRequest joinRoomRequest) {
        return ResponseEntity.ok(ApiResponse.<RoomDTO>builder()
                .message("User successfully join")
                .payload(roomService.joinRoom(joinRoomRequest.getCode()))
                .status(HttpStatus.CREATED)
                .build());
    }

    @DeleteMapping("/leave-room/{room-id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "left from room",
            description = "User use for leave examination room"
    )
    public ResponseEntity<ApiResponse<Boolean>> leaveRoom(@PathVariable("room-id") UUID roomId) {
        roomService.leaveRoom(roomId);
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .message("User successfully left the room")
                .payload(true)
                .status(HttpStatus.OK)
                .build());
    }

    @GetMapping("/join-room")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "Get all user rooms",
            description = "User use for get all own user join rooms"
    )
    public ResponseEntity<ApiResponse<ListResponse<RoomDTO>>> getAllUserJoinRoom(
            @RequestParam(defaultValue = "1") @Positive @Min(value = 1, message = "must greater than 0") Integer page,
            @RequestParam(defaultValue = "3") @Positive @Min(value = 1, message = "must greater than 0") Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "ASC") Sort.Direction sort
    ) {
        return ResponseEntity.ok(ApiResponse.<ListResponse<RoomDTO>>builder()
                .message("User room successfully fetched")
                .payload(roomService.getUserJoinedRooms(page, size, search, sort))
                .status(HttpStatus.OK)
                .build());
    }

    @GetMapping("/owner-room")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "Get all user own rooms",
            description = "User use for get all own user rooms that have been created"
    )
    public ResponseEntity<ApiResponse<ListResponse<RoomDTO>>> getAllOwnUserRoom(
            @RequestParam(defaultValue = "1") @Positive @Min(value = 1, message = "must greater than 0") Integer page,
            @RequestParam(defaultValue = "3") @Positive @Min(value = 1, message = "must greater than 0") Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "ASC") Sort.Direction sort
    ) {
        return ResponseEntity.ok(ApiResponse.<ListResponse<RoomDTO>>builder()
                .message("Own user room successfully fetched")
                .payload(roomService.getOwnUserRooms(page, size, search, sort))
                .status(HttpStatus.OK)
                .build());
    }

    @GetMapping("/admin/all-rooms")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Admin role",
            description = "Admin use for get all rooms"
    )
    public ResponseEntity<ApiResponse<ListResponse<RoomDTO>>> getAllRooms(
            @RequestParam(defaultValue = "1") @Positive @Min(value = 1, message = "must greater than 0") Integer page,
            @RequestParam(defaultValue = "3") @Positive @Min(value = 1, message = "must greater than 0") Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "ASC") Sort.Direction sort
    ) {
        return ResponseEntity.ok(ApiResponse.<ListResponse<RoomDTO>>builder()
                .message("All rooms successfully fetched")
                .payload(roomService.getAllRooms(page, size, search, sort))
                .status(HttpStatus.OK)
                .build());
    }

}
