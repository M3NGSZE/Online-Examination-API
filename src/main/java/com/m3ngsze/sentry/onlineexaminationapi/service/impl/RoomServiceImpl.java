package com.m3ngsze.sentry.onlineexaminationapi.service.impl;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Room;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.RoomRequest;
import com.m3ngsze.sentry.onlineexaminationapi.repository.RoomRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public RoomDTO createRoom(RoomRequest roomRequest) {
        validateRoomRequest(roomRequest);
        return null;
    }

    private RoomRequest validateRoomRequest(RoomRequest roomRequest) {

        List<Room> oldRoom = roomRepository.findByRoomNameIgnoreCase(roomRequest.getRoomName());

        oldRoom.forEach(room -> {
            System.out.println(room.getRoomName());
        });

        return roomRequest;
    }
}
