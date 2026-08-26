package com.m3ngsze.sentry.onlineexaminationapi.model.entity;

import com.m3ngsze.sentry.onlineexaminationapi.model.enums.RoomOwnerRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "room_owners")
public class RoomOwner extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "room_owner_id", updatable = false, nullable = false)
    private UUID roomOwnerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_owner_role")
    private RoomOwnerRole roomOwnerRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false, referencedColumnName = "room_id")
    private Room room;
}
