package com.m3ngsze.sentry.onlineexaminationapi.service.business.impl;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.business.TeacherService;
import com.m3ngsze.sentry.onlineexaminationapi.utility.UtilMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final UserRepository userRepository;

    @Override
    public List<UserDTO> retrieveStudentsByRoomId( UUID roomId ) {

        return userRepository.findAllByEnrollments_Room_RoomId(roomId)
                .stream()
                .map(UtilMapper::toUserDTO)
                .toList();
    }
}
