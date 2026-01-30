package com.m3ngsze.sentry.onlineexaminationapi.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JoinRoomRequest {

    @NotNull(message = "code cannot be null")
    @NotBlank(message = "code cannot be blank")
    @Schema(example = "xxx-xxx")
    private String code;

}
