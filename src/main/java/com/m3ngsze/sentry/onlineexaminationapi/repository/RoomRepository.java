package com.m3ngsze.sentry.onlineexaminationapi.repository;

import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Room;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

    List<Room> findByRoomNameIgnoreCaseAndRoomOwners_User(String roomName, User roomOwnersUser);

}
