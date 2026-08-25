package com.m3ngsze.sentry.onlineexaminationapi.service.business.impl;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.ExamDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.ExamRequest;
import com.m3ngsze.sentry.onlineexaminationapi.service.business.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {



    @Override
    public ExamDTO createExam( ExamRequest examRequest ) {
        return null;
    }
}
