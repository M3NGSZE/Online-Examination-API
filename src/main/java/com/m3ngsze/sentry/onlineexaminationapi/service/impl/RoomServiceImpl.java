package com.m3ngsze.sentry.onlineexaminationapi.service.impl;

import com.m3ngsze.sentry.onlineexaminationapi.exception.BadRequestException;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Room;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.RoomOwner;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.RoomRequest;
import com.m3ngsze.sentry.onlineexaminationapi.repository.RoomOwnerRepository;
import com.m3ngsze.sentry.onlineexaminationapi.repository.RoomRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.DetailService;
import com.m3ngsze.sentry.onlineexaminationapi.service.RoomService;
import com.m3ngsze.sentry.onlineexaminationapi.utility.ConvertUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomOwnerRepository roomOwnerRepository;

    private final DetailService detailService;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public RoomDTO createRoom(RoomRequest request) {

        User user = detailService.getCurrentUser();

        RoomRequest trim =  RoomRequestTrim(request);

        validateRoomRequest(trim, user);

        Room room = modelMapper.map(request, Room.class);

        Room newRoom = roomRepository.save(room);

        RoomOwner owner = new RoomOwner();
        owner.setRoom(newRoom);
        owner.setUser(user);

        RoomOwner save = roomOwnerRepository.save(owner);

//        newRoom.setRoomOwners(List.of(owner));

        RoomDTO dto = modelMapper.map(newRoom, RoomDTO.class);
        dto.setUserId(save.getUser().getUserId());

        return dto;
    }

    private void validateRoomRequest(RoomRequest request, User user) {

        List<Room> oldRoom = roomRepository.findByRoomNameIgnoreCaseAndRoomOwners_User(request.getRoomName(), user);

        oldRoom.forEach(room -> System.out.println(room.getRoomName()));

        if (!oldRoom.isEmpty())
            throw new BadRequestException("Room already exists");
    }

    private RoomRequest RoomRequestTrim(RoomRequest request){
        request.setRoomName(request.getRoomName().trim());
        request.setRoomName(request.getRoomName().trim());
        request.setSection(request.getSection().trim());
        request.setSubject(request.getSubject().trim());

        ConvertUtil.parseRoomLimit(request.getLimit().toString());

        if (request.getLimit() <= 0 || request.getLimit() > 100)
            throw new BadRequestException("Limit must be greater than 0 and less than 100");

        return request;
    }
}
