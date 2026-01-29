package com.m3ngsze.sentry.onlineexaminationapi.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "rooms")
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "room_id")
    private UUID roomId;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @Column(nullable = false)
    private String section;

    @Column(nullable = false)
    private String subject;

    @Column(name = "room_limit")
    private Integer limit;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomOwner> roomOwners;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomInviteCode> roomInviteCodes;
}