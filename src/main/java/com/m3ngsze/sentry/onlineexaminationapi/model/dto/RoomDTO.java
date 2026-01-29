package com.m3ngsze.sentry.onlineexaminationapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {

    private UUID roomId;

    private UUID userId;

    private String roomName;

    private String section;

    private String subject;

    private Integer limit;

    private Boolean isDeleted;

}
