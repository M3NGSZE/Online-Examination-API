package com.m3ngsze.sentry.onlineexaminationapi.utility;

import com.m3ngsze.sentry.onlineexaminationapi.exception.BadRequestException;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.UserInfoRequest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RequestMapUtil {

        public static UserInfoRequest validateRegisterRequest(UserInfoRequest request) {

        request.setFirstName(ConvertUtil.toPascalCase(request.getFirstName().trim()));
        request.setLastName(ConvertUtil.toPascalCase(request.getLastName().trim()));
        request.setPlaceOfBirth(request.getPlaceOfBirth() != null ? request.getPlaceOfBirth().trim() : null);
        request.setPhoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber().trim() : null);
        request.setProfileUrl(request.getProfileUrl() != null ? request.getProfileUrl().trim() : null);

        if (request.getDateOfBirth() != null) {
                long years = ChronoUnit.YEARS.between(request.getDateOfBirth(), LocalDate.now());
                if (years < 13 || years > 100) {
                        throw new BadRequestException("Invalid date of birth");
                }
        }
        return request;
    }

}
