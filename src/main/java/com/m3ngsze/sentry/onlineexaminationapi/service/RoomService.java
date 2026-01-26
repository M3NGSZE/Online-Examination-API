package com.m3ngsze.sentry.onlineexaminationapi.service;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.RoomRequest;

public interface RoomService {

    RoomDTO createRoom(RoomRequest roomRequest);

}
