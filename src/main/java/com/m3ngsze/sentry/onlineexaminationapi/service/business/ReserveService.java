package com.m3ngsze.sentry.onlineexaminationapi.service.business;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import org.springframework.data.domain.Sort;

public interface ReserveService {

    ListResponse<RoomDTO> getUserJoinedRooms(Integer page, Integer size, String search, Sort.Direction sort);

    ListResponse<RoomDTO> getOwnUserRooms(Integer page, Integer size, String search, Sort.Direction sort);

}
