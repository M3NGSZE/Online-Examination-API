package com.m3ngsze.sentry.onlineexaminationapi.service.component;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Room;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public interface RoomCbc {

    ListResponse<RoomDTO> getUserRoom(Integer page, Integer size, Sort.Direction sort, Specification<Room> spec);

}
