package com.m3ngsze.sentry.onlineexaminationapi.service.business.impl;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Room;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import com.m3ngsze.sentry.onlineexaminationapi.service.support.RoomSupport;
import com.m3ngsze.sentry.onlineexaminationapi.service.support.UserSupport;
import com.m3ngsze.sentry.onlineexaminationapi.service.business.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static com.m3ngsze.sentry.onlineexaminationapi.specification.RoomSpecification.*;

@Service
@RequiredArgsConstructor
public class ReserveServiceImpl implements ReserveService {

    private final UserSupport userSupport;
    private final RoomSupport roomSupport;

    @Override
    public ListResponse<RoomDTO> getUserJoinedRooms(Integer page, Integer size, String search, Sort.Direction sort) {
        User user = userSupport.getCurrentUser();

        Specification<Room> spec = Specification
                .where(search(search))
                .and(isDeleted(false))
                .and(enrolledBy(user));

        return roomSupport.getUserRoom(page, size, sort, spec);
    }

    @Override
    public ListResponse<RoomDTO> getOwnUserRooms(Integer page, Integer size, String search, Sort.Direction sort) {
        User user = userSupport.getCurrentUser();

        Specification<Room> spec = Specification
                .where(search(search))
                .and(isDeleted(false))
                .and(ownBy(user));

        return roomSupport.getUserRoom(page, size, sort, spec);
    }
}
