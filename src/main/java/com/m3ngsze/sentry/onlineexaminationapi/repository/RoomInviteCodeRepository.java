package com.m3ngsze.sentry.onlineexaminationapi.repository;

import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Room;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.RoomInviteCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomInviteCodeRepository extends CrudRepository<RoomInviteCode, UUID> {

    RoomInviteCode findFirstByRoomOrderByUpdatedAtDesc(Room room);
}
