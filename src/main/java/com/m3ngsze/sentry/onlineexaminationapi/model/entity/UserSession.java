package com.m3ngsze.sentry.onlineexaminationapi.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_sessions")
@Data
public class UserSession extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "session_id", updatable = false, nullable = false)
    private UUID sessionId;

    @Column(name = "refresh_token_hash", nullable = false)
    private String refreshToken;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
