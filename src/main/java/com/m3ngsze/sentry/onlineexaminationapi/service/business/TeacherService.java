package com.m3ngsze.sentry.onlineexaminationapi.service.business;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;

import java.util.List;

public interface TeacherService {

    List<UserDTO> retrieveStudentsByRoomId();
}
