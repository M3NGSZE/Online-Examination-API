package com.m3ngsze.sentry.onlineexaminationapi.controller;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.enums.RoomType;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ApiResponse;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import com.m3ngsze.sentry.onlineexaminationapi.service.business.RoomOwnerService;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/room-owner")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Room Owner Controller", description = "Handle creating new assignment for enrollment and manage enrollments")
@RequiredArgsConstructor
public class RoomOwnerController {

    private final RoomOwnerService roomOwnerService;

    @GetMapping("/enrollment/{room-id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "Retrieve all enrollments by room id",
            description = "Room Owner use for retrieve all enrollments join room"
    )
    public ResponseEntity<ApiResponse<ListResponse<UserDTO>>> retrieveStudentsByRoomId(
            @PathVariable("room-id") UUID roomId ,
            @RequestParam(defaultValue = "1") @Positive @Min(value = 1, message = "must greater than 0") Integer page,
            @RequestParam(defaultValue = "20") @Positive @Min(value = 1, message = "must greater than 0") Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "DESC") Sort.Direction sort
    ) {
        return ResponseEntity.ok( ApiResponse.<ListResponse<UserDTO>>builder()
                .message("User successfully deleted")
                .payload(roomOwnerService.retrieveEnrollmentsByRoomId(roomId, page, size, search, sort))
                .status(HttpStatus.OK)
                .build()
        );
    }

    @GetMapping("/{room-id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "Retrieve all room owners by room id",
            description = "Room Owner use for retrieve all room owner join room"
    )
    public ResponseEntity<ApiResponse<List<UserDTO>>> retrieveRoomOwnersByRoomId(@PathVariable("room-id") UUID roomId ) {
        return ResponseEntity.ok(ApiResponse.<List<UserDTO>>builder()
                .message("User successfully deleted")
                .payload( null )
                .status(HttpStatus.OK)
                .build());
    }
}
