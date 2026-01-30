package com.m3ngsze.sentry.onlineexaminationapi.repository;

import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Room;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.RoomInviteCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoomInviteCodeRepository extends JpaRepository<RoomInviteCode, UUID> {

    RoomInviteCode findFirstByRoomOrderByUpdatedAtDesc(Room room);

}
