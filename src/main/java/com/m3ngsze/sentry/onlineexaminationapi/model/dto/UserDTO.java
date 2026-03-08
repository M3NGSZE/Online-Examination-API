package com.m3ngsze.sentry.onlineexaminationapi.model.dto;

import com.m3ngsze.sentry.onlineexaminationapi.model.enums.AccountStatus;
import com.m3ngsze.sentry.onlineexaminationapi.model.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends BaseDTO {

    private UUID userId;

    private String email;

    private AccountStatus accountStatus;

    private String firstName;

    private String lastName;

    private Gender gender;

    private Integer age;

    private LocalDate dateOfBirth;

    private String placeOfBirth;

    private String phoneNumber;

    private String profileUrl;

}
