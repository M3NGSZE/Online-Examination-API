package com.m3ngsze.sentry.onlineexaminationapi.controller;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ApiResponse;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import com.m3ngsze.sentry.onlineexaminationapi.service.ReserveService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/reserve")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reserve Controller", description = "Left over endpoints that have already been created and no usage, could be useful some days if needed")
@RequiredArgsConstructor
public class ReserveController {

    private final ReserveService reserveService;

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
                .payload(reserveService.getOwnUserRooms(page, size, search, sort))
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
                .payload(reserveService.getUserJoinedRooms(page, size, search, sort))
                .status(HttpStatus.OK)
                .build());
    }
}
