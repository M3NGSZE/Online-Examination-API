package com.m3ngsze.sentry.onlineexaminationapi.mapper;

import com.m3ngsze.sentry.onlineexaminationapi.exception.BadRequestException;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserProfileDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.request.UserInfoRequest;
import com.m3ngsze.sentry.onlineexaminationapi.utility.ConvertUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class UserMapper {

    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getUserInfo().getFirstName());
        userDTO.setLastName(user.getUserInfo().getLastName());
        userDTO.setPhoneNumber(user.getUserInfo().getPhoneNumber());
        userDTO.setDateOfBirth(user.getUserInfo().getDateOfBirth());
        userDTO.setPlaceOfBirth(user.getUserInfo().getPlaceOfBirth());
        userDTO.setProfileUrl(user.getUserInfo().getProfileUrl());

        return userDTO;
    }

    public static UserInfoRequest validateRegisterRequest(UserInfoRequest request )  {

        request.setFirstName( ConvertUtil.toPascalCase( request.getFirstName().trim() ) );
        request.setLastName( ConvertUtil.toPascalCase (request.getLastName().trim() ) );
        request.setPlaceOfBirth( request.getGender() != null ? String.valueOf( request.getGender() ) : null) ;
        request.setPlaceOfBirth( request.getPlaceOfBirth() != null ? request.getPlaceOfBirth().trim() : null );
        request.setPhoneNumber( request.getPhoneNumber() != null ? request.getPhoneNumber().trim() : null );
        request.setProfileUrl( request.getProfileUrl() != null ? request.getProfileUrl().trim() : null );

        if (request.getDateOfBirth() != null) {
            long years = ChronoUnit.YEARS.between(request.getDateOfBirth(), LocalDate.now());
            if (years < 13 || years > 100) {
                throw new BadRequestException("Invalid date of birth");
            }
        }
        return request;
    }

    public static UserProfileDTO toUserProfileDTO( User user ) {
        UserProfileDTO userProfileDTO = new UserProfileDTO();

        userProfileDTO.setUserId( user.getUserId() );
        userProfileDTO.setAccountStatus( user.getAccountStatus() );
        userProfileDTO.setFirstName( user.getUserInfo().getFirstName() );
        userProfileDTO.setLastName( user.getUserInfo().getLastName() );
        userProfileDTO.setGender( user.getUserInfo().getGender() );
        userProfileDTO.setProfileUrl( user.getUserInfo().getProfileUrl() );

        return  userProfileDTO;
    }
}
