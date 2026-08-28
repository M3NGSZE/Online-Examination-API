package com.m3ngsze.sentry.onlineexaminationapi.controller;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserProfileDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.enums.RoomType;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.RoomInvitationRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ApiResponse;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import com.m3ngsze.sentry.onlineexaminationapi.service.business.RoomOwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping( "api/v1/room-owner" )
@SecurityRequirement( name = "bearerAuth" )
@Tag( name = "Room Owner Controller", description = "Handle creating for enrollment and manage enrollments" )
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
                .message( "All enrollments successfully fetched" )
                .payload( roomOwnerService.retrieveEnrollmentsByRoomId( roomId, page, size, search, sort, roomType ) )
                .status( HttpStatus.OK )
                .build()
        );
    }

    @GetMapping( "invitation/{room-id}" )
    @PreAuthorize( "hasRole('ROLE_USER')" )
    @Operation(
            summary = "Invite users to room",
            description = "Room Owner use for invite any of user (Co-Owner & Enrollment)"
    )
    public ResponseEntity< ApiResponse < UserProfileDTO > > roomInvitation(
            @PathVariable( "room-id" ) UUID roomId ,
            @RequestBody @Valid List< RoomInvitationRequest > roomInvitationRequest
            ) {
        return ResponseEntity.ok( ApiResponse.< UserProfileDTO >builder()
                .message( "Users invitation to room is successfully" )
                .payload( null )
                .status( HttpStatus.OK)
                .build()
        );
    }

}
