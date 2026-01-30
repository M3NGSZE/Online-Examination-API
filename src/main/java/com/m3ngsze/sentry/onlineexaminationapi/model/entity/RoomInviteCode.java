package com.m3ngsze.sentry.onlineexaminationapi.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "room_invite_code")
public class RoomInviteCode extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "code_id")
    private UUID codeId;

    @Column(name = "code_hash", nullable = false)
    private String codeHash;

    @Column(name = "is_active", nullable = false)
    private Boolean isActivate = true;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

}
