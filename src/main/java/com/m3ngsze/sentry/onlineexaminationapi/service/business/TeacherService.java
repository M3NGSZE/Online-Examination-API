package com.m3ngsze.sentry.onlineexaminationapi.service.business;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface TeacherService {

    List<UserDTO> retrieveStudentsByRoomId( UUID roomId );
}
