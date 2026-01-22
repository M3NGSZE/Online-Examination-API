package com.m3ngsze.sentry.onlineexaminationapi.utility;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;

public class UtilMapper {

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

}
