package com.m3ngsze.sentry.onlineexaminationapi.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private UUID roleId;

    @Column(name = "role_name")
    private String roleName;
}
