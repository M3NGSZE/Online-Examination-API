package com.m3ngsze.sentry.onlineexaminationapi.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenDTO {

    private String accessToken;

    private String refreshToken;

}
