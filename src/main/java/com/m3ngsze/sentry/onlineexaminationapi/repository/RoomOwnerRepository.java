package com.m3ngsze.sentry.onlineexaminationapi.repository;

import com.m3ngsze.sentry.onlineexaminationapi.model.entity.RoomOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomOwnerRepository extends JpaRepository<RoomOwner, UUID> {
}
