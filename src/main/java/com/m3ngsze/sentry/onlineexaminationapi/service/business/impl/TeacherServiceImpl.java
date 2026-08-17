package com.m3ngsze.sentry.onlineexaminationapi.service.business.impl;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
//import com.m3ngsze.sentry.onlineexaminationapi.repository.TeacherRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.business.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    //private final TeacherRepository teacherRepository;

    @Override
    public List<UserDTO> retrieveStudentsByRoomId() {
        return List.of();
    }
}
