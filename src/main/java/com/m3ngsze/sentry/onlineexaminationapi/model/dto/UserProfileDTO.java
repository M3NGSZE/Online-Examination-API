package com.m3ngsze.sentry.onlineexaminationapi.model.dto;

import com.m3ngsze.sentry.onlineexaminationapi.model.entity.BaseEntity;
import com.m3ngsze.sentry.onlineexaminationapi.model.enums.AccountStatus;
import com.m3ngsze.sentry.onlineexaminationapi.model.enums.Gender;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO extends BaseEntity {

    private UUID userId;

    private AccountStatus accountStatus;

    private String firstName;

    private String lastName;

    private Gender gender;

    private String profileUrl;

}
