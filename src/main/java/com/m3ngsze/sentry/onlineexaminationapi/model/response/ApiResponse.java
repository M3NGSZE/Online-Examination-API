package com.m3ngsze.sentry.onlineexaminationapi.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T> {

    private String message;

    private HttpStatus status;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEEE, dd MMMM yyyy HH:mm:ss")
    private LocalDateTime requestedTime = LocalDateTime.now();

    private T payload;

}
