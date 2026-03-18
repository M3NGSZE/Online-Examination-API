package com.m3ngsze.sentry.onlineexaminationapi.utility;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Room;
import org.modelmapper.ModelMapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class RoomUtil {

    private static final String CHARSET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate(int length) {
        StringBuilder code = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARSET.length());
            code.append(CHARSET.charAt(index));
        }

        return code.toString();
    }

    public static String hash(String code) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(code.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash room code", e);
        }
    }

    public static RoomDTO getRoomDTO(Room room, ModelMapper modelMapper) {
        RoomDTO map = modelMapper.map(room, RoomDTO.class);
        map.setUserId(room.getRoomOwners().getFirst().getUser().getUserId());
        map.setFirstName(room.getRoomOwners().getFirst().getUser().getUserInfo().getFirstName());
        map.setLastname(room.getRoomOwners().getFirst().getUser().getUserInfo().getLastName());
        return map;
    }

}
