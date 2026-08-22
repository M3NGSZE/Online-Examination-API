package com.m3ngsze.sentry.onlineexaminationapi.service.support;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Room;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserSupport extends UserDetailsService {

    User getCurrentUser();

    String extractAccessToken(HttpServletRequest request);

    ListResponse<RoomDTO> getUserRoom(Integer page, Integer size, Sort.Direction sort, Specification<Room> spec);

}
