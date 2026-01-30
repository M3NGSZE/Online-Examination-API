package com.m3ngsze.sentry.onlineexaminationapi.service.impl;

import com.m3ngsze.sentry.onlineexaminationapi.exception.BadRequestException;
import com.m3ngsze.sentry.onlineexaminationapi.exception.NotFoundException;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.InviteCodeDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.*;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.RoomRequest;
import com.m3ngsze.sentry.onlineexaminationapi.repository.*;
import com.m3ngsze.sentry.onlineexaminationapi.service.DetailService;
import com.m3ngsze.sentry.onlineexaminationapi.service.RoomService;
import com.m3ngsze.sentry.onlineexaminationapi.utility.ConvertUtil;
import com.m3ngsze.sentry.onlineexaminationapi.utility.RoomCodeUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomOwnerRepository roomOwnerRepository;
    private final UserRepository userRepository;
    private final RoomInviteCodeRepository roomInviteCodeRepository;
    private final EnrollmentRepository enrollmentRepository;

    private final DetailService detailService;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public RoomDTO createRoom(RoomRequest request) {

        User user = detailService.getCurrentUser();

        RoomRequest trim =  RoomRequestTrim(request);

        validateRoomRequest(trim, user);

        Room room = modelMapper.map(trim, Room.class);

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

    @Override
    @Transactional
    public RoomDTO updateRoom(UUID roomId, RoomRequest request) {
        User user = detailService.getCurrentUser();

        Room room = roomRepository.findByRoomIdAndIsDeletedFalseAndRoomOwners_User(roomId, user)
                .orElseThrow(() -> new NotFoundException("Room not found"));

        RoomRequest trim =  RoomRequestTrim(request);

        validateRoomRequest(trim, user);

        room.setRoomName(trim.getRoomName());
        room.setRoomName(trim.getRoomName());
        room.setSection(trim.getSection());
        room.setSubject(trim.getSubject());
        room.setLimit(trim.getLimit());

        Room save = roomRepository.save(room);

        RoomDTO map = modelMapper.map(save, RoomDTO.class);
        map.setUserId(user.getUserId());

        return map;
    }

    @Override
    public RoomDTO findRoomById(UUID roomId) {
        User user = detailService.getCurrentUser();

        Room room = roomRepository.findByRoomIdAndIsDeletedFalseAndRoomOwners_User(roomId, user)
                .orElseThrow(() -> new NotFoundException("Room not found"));

        RoomDTO map = modelMapper.map(room, RoomDTO.class);
        map.setUserId(user.getUserId());
        map.setRoomName(user.getUserInfo().getFirstName() + " " + user.getUserInfo().getLastName());

        return map;
    }

    @Override
    @Transactional
    public void deleteRoomById(UUID roomId) {
        User user = detailService.getCurrentUser();

        Room room = roomRepository.findByRoomIdAndIsDeletedFalseAndRoomOwners_User(roomId, user)
                .orElseThrow(() -> new NotFoundException("Room not found"));

        room.setIsDeleted(true);
        room.setDeletedAt(LocalDateTime.now());
        room.setUpdatedAt(LocalDateTime.now());

        roomRepository.save(room);
    }

    @Override
    public RoomDTO findRoomByIdAdmin(UUID roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Room not found"));

        userRepository.findById(room.getRoomOwners().getFirst().getUser().getUserId());

        RoomDTO map = modelMapper.map(room, RoomDTO.class);
        map.setUserId(room.getRoomOwners().getFirst().getUser().getUserId());
        map.setRoomName(room.getRoomOwners().getFirst().getUser().getUserInfo().getFirstName() + " " + room.getRoomOwners().getFirst().getUser().getUserInfo().getLastName());

        return map;
    }

    @Override
    public void deleteRoomByIdAdmin(UUID roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Room not found"));

        room.setIsDeleted(true);
        room.setDeletedAt(LocalDateTime.now());
        room.setUpdatedAt(LocalDateTime.now());

        roomRepository.save(room);
    }

    @Override
    @Transactional
    public InviteCodeDTO createInviteCode(UUID roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Room not found"));

        RoomInviteCode oldInviteCode = roomInviteCodeRepository.findFirstByRoomOrderByUpdatedAtDesc(room);

        RoomInviteCode inviteCode;

        if (oldInviteCode == null){
            RoomInviteCode newInviteCode = new RoomInviteCode();
            newInviteCode.setRoom(room);
            inviteCode = roomInviteCodeRepository.save(newInviteCode);
        }else {
            oldInviteCode.setIsActivate(false);
            oldInviteCode.setRevokedAt(LocalDateTime.now());

            if(oldInviteCode.getExpiresAt().isBefore(LocalDateTime.now()))
                oldInviteCode.setUpdatedAt(LocalDateTime.now());

            roomInviteCodeRepository.save(oldInviteCode);

            String plainCode = RoomCodeUtil.generate(6);
            String hashedCode = RoomCodeUtil.hash(plainCode);

            RoomInviteCode newInviteCode = new RoomInviteCode();
            newInviteCode.setRoom(room);
            newInviteCode.setCodeHash(hashedCode);
            newInviteCode.setExpiresAt(LocalDateTime.now().plusHours(24));

            inviteCode = roomInviteCodeRepository.save(newInviteCode);

        }

        return modelMapper.map(inviteCode, InviteCodeDTO.class);
    }

    @Override
    public RoomDTO joinRoom(String code) {
        User user = detailService.getCurrentUser();

        String hashCode = RoomCodeUtil.hash(code);

        Room room = roomRepository.findRoomByRoomInviteCodes_CodeHash(hashCode)
                .orElseThrow(() -> new NotFoundException("Room invite code not found"));

        Enrollment enrollment = new Enrollment();
        enrollment.setRoom(room);
        enrollment.setUser(user);

        enrollmentRepository.save(enrollment);

        RoomDTO map = modelMapper.map(room, RoomDTO.class);
        map.setUserId(user.getUserId());

        return map;
    }

    @Override
    public void leaveRoom(UUID roomId) {

    }
}
