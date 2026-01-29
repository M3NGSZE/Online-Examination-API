package com.m3ngsze.sentry.onlineexaminationapi.service;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.RoomRequest;

import java.util.UUID;

public interface RoomService {

    RoomDTO createRoom(RoomRequest roomRequest);

    RoomDTO updateRoom(UUID roomId, RoomRequest roomRequest);

}
