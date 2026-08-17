package com.m3ngsze.sentry.onlineexaminationapi.service.business.impl;

import com.m3ngsze.sentry.onlineexaminationapi.repository.TeacherRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.business.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
}
