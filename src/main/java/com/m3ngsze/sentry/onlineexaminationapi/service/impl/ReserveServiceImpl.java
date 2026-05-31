package com.m3ngsze.sentry.onlineexaminationapi.service.impl;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Room;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import com.m3ngsze.sentry.onlineexaminationapi.service.DetailService;
import com.m3ngsze.sentry.onlineexaminationapi.service.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static com.m3ngsze.sentry.onlineexaminationapi.specification.RoomSpecification.*;

@Service
@RequiredArgsConstructor
public class ReserveServiceImpl implements ReserveService {


    private final DetailService detailService;

    @Override
    public ListResponse<RoomDTO> getUserJoinedRooms(Integer page, Integer size, String search, Sort.Direction sort) {
        User user = detailService.getCurrentUser();

        Specification<Room> spec = Specification
                .where(search(search))
                .and(isDeleted(false))
                .and(enrolledBy(user));

        return detailService.getUserRoom(page, size, sort, spec);
    }

    @Override
    public ListResponse<RoomDTO> getOwnUserRooms(Integer page, Integer size, String search, Sort.Direction sort) {
        User user = detailService.getCurrentUser();

        Specification<Room> spec = Specification
                .where(search(search))
                .and(isDeleted(false))
                .and(ownBy(user));

        return detailService.getUserRoom(page, size, sort, spec);
    }
}
