package com.m3ngsze.sentry.onlineexaminationapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InviteCodeDTO extends BaseDTO {

    private UUID codeId;

    private UUID roomId;

    private String roomNumber;

    private String codeHash;

    private Boolean isActivate;

    private LocalDateTime expiresAt;

    private LocalDateTime revokedAt;

}
