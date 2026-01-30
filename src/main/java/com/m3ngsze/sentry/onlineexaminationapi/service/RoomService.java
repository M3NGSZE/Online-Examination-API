package com.m3ngsze.sentry.onlineexaminationapi.service;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.InviteCodeDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.RoomRequest;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public interface RoomService {

    RoomDTO createRoom(RoomRequest roomRequest);

    RoomDTO updateRoom(UUID roomId, RoomRequest roomRequest);

    RoomDTO findRoomById(UUID roomId);

    void deleteRoomById(UUID roomId);

    RoomDTO findRoomByIdAdmin(UUID roomId);

    void deleteRoomByIdAdmin(UUID roomId);

    InviteCodeDTO createInviteCode(UUID roomId);

    RoomDTO joinRoom(String code);

    void leaveRoom(UUID roomId);

    ListResponse<RoomDTO> getUserJoinedRooms(Integer page, Integer size, String search, Sort.Direction sort);

    ListResponse<RoomDTO> getOwnUserRooms(Integer page, Integer size, String search, Sort.Direction sort);

}
