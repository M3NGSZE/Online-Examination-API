package com.m3ngsze.sentry.onlineexaminationapi.model.request;


import com.m3ngsze.sentry.onlineexaminationapi.model.enums.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
public class RoomInvitationRequest {

    @Getter
    @NotNull( message = "Email cannot be null" )
    @NotBlank( message = "Email cannot be blank" )
    @Email( message = "Invalid email format. Please provide a valid email address." )
    @Schema( example = "example@gmail.com" )
    private String email;

    @Enumerated( EnumType.STRING )
    @Column(name = "room_status")
    private RoomType roomType;

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim().toLowerCase();
    }

}
