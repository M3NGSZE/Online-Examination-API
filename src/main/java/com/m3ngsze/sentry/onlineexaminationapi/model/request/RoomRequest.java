package com.m3ngsze.sentry.onlineexaminationapi.model.request;

import lombok.Data;

@Data
public class RoomRequest {

    private String roomName;

    private String section;

    private String subject;

    private String secretCodeHash;

    private Integer limit;

}
