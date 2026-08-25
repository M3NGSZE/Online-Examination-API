package com.m3ngsze.sentry.onlineexaminationapi.controller;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.ExamDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.ExamRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ApiResponse;
import com.m3ngsze.sentry.onlineexaminationapi.service.business.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "api/v1/exam")
@SecurityRequirement( name = "bearerAuth" )
@Tag( name = "Exam Controller", description = "Handle creating assignment for enrollment and manage enrollments" )
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping
    @PreAuthorize( "hasRole('ROLE_USER')" )
    @Operation(
            summary = "Create exam or assignment",
            description = "Room Owner use for create new exam or assignment for enrollments"
    )
    public ResponseEntity< ApiResponse <ExamDTO> > createExam( ExamRequest examRequest ) {

        return ResponseEntity.ok(ApiResponse.<ExamDTO>builder()
                .message("New user successfully created. OTP sent to your email")
                .payload( null )
                .status(HttpStatus.CREATED)
                .build());
    }
}
