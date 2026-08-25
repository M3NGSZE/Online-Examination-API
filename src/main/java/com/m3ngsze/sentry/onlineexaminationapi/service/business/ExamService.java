package com.m3ngsze.sentry.onlineexaminationapi.service.business;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.ExamDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.ExamRequest;

public interface ExamService {

    ExamDTO createExam(ExamRequest examRequest );
}
