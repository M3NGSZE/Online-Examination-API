package com.m3ngsze.sentry.onlineexaminationapi.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RoomDTO {

    private UUID roomId;

    private UUID userId;

    private String roomName;

    private String section;

    private String subject;

    private String secretCodeHash;

    private Integer limit;

}
