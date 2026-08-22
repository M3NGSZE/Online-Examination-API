package com.m3ngsze.sentry.onlineexaminationapi.service.business;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.enums.RoomType;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public interface RoomOwnerService {

    ListResponse<UserDTO> retrieveEnrollmentsByRoomId(UUID roomId, Integer page, Integer size, String search, Sort.Direction sort, RoomType roomType );
}
