package com.m3ngsze.sentry.onlineexaminationapi.model.dto;

import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO extends BaseDTO {

    private UUID roomId;

    private String roomName;

    private UUID userId;

    private String roomOwner;

    private String section;

    private String subject;

    private Integer limit;

    private Boolean isDeleted;

}
