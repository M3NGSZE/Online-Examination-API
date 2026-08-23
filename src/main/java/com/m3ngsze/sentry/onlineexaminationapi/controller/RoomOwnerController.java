package com.m3ngsze.sentry.onlineexaminationapi.controller;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserProfileDTO;
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

import java.util.UUID;

@RestController
@RequestMapping( "api/v1/room-owner" )
@SecurityRequirement( name = "bearerAuth" )
@Tag( name = "Room Owner Controller", description = "Handle creating new assignment for enrollment and manage enrollments" )
@RequiredArgsConstructor
public class RoomOwnerController {

    private final RoomOwnerService roomOwnerService;

    @GetMapping( "/{room-id}" )
    @PreAuthorize( "hasRole('ROLE_USER')" )
    @Operation(
            summary = "Retrieve all users by room id",
            description = "Room Owner use for retrieve all users join room (room-owner & enrollment)"
    )
    public ResponseEntity< ApiResponse< ListResponse< UserProfileDTO > > > retrieveStudentsByRoomId(
            @PathVariable( "room-id" ) UUID roomId ,
            @RequestParam( defaultValue = "1" ) @Positive @Min( value = 1, message = "must greater than 0" ) Integer page,
            @RequestParam( defaultValue = "20" ) @Positive @Min( value = 1, message = "must greater than 0" ) Integer size,
            @RequestParam( required = false ) String search,
            @RequestParam( required = false ) Sort.Direction sort,
            @RequestParam( required = false ) RoomType roomType
    ) {
        return ResponseEntity.ok( ApiResponse.< ListResponse< UserProfileDTO > >builder()
                .message( "User enrollment successfully fetched" )
                .payload( roomOwnerService.retrieveEnrollmentsByRoomId( roomId, page, size, search, sort, roomType ) )
                .status( HttpStatus.OK )
                .build()
        );
    }

}
