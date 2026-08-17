package com.m3ngsze.sentry.onlineexaminationapi.service.business;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface RoomOwnerService {

    List<UserDTO> retrieveEnrollmentsByRoomId( UUID roomId );
}
