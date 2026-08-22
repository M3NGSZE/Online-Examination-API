package com.m3ngsze.sentry.onlineexaminationapi.service.support.impl;

import com.m3ngsze.sentry.onlineexaminationapi.exception.NotFoundException;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Room;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.PaginationResponse;
import com.m3ngsze.sentry.onlineexaminationapi.repository.RoomRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.support.RoomSupport;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.m3ngsze.sentry.onlineexaminationapi.utility.RoomUtil.getRoomDTO;

@Service
@RequiredArgsConstructor
public class RoomSupportImpl implements RoomSupport {

    private final RoomRepository roomRepository;

    private final ModelMapper modelMapper;

    @Override
    public ListResponse<RoomDTO> getUserRoom(Integer page, Integer size, Sort.Direction sort, Specification<Room> spec) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(sort, "updatedAt")
        );

        Page<RoomDTO> roompage = roomRepository.findAll(spec, pageable)
                .map(room -> getRoomDTO(room, modelMapper));

        return ListResponse.<RoomDTO>builder()
                .data(roompage.getContent())
                .pagination(PaginationResponse.of(roompage.getTotalElements(), page, size))
                .build();
    }

    @Override
    public Room retrieveRoomById( UUID roomId ) {
        return roomRepository.findById( roomId )
                .orElseThrow(() -> new NotFoundException( "Room not found" ) );
    }

}
