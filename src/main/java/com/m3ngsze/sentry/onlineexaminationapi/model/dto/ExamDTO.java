package com.m3ngsze.sentry.onlineexaminationapi.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class ExamDTO extends BaseDTO{

    private UUID examId;


}
