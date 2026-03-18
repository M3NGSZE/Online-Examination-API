package com.m3ngsze.sentry.onlineexaminationapi.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollCount {

    private UUID roomId;

    private Long enrollmentCount;

}
