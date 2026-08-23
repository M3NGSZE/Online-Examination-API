package com.m3ngsze.sentry.onlineexaminationapi.service.business.impl;

import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserProfileDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Room;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.enums.RoomType;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.PaginationResponse;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.business.RoomOwnerService;
import com.m3ngsze.sentry.onlineexaminationapi.service.support.RoomSupport;
import com.m3ngsze.sentry.onlineexaminationapi.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.m3ngsze.sentry.onlineexaminationapi.specification.UserSpecification.*;

@Service
@RequiredArgsConstructor
public class RoomOwnerServiceImpl implements RoomOwnerService {

    private final UserRepository userRepository;

    private final RoomSupport roomSupport;

    @Override
    public ListResponse< UserProfileDTO > retrieveEnrollmentsByRoomId( UUID roomId, Integer page, Integer size, String search, Sort.Direction sort, RoomType roomType  ) {

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(
                        new Sort.Order(sort, "userInfo.firstName"),
                        new Sort.Order(sort, "userInfo.lastName")
                )
        );

        Room room = roomSupport.retrieveRoomById( roomId );

        Specification<User> spec = Specification
                .where( enrolledInRoom ( roomId ) )
                .and( search( search ) )
                .and( type( room,  roomType) );

        Page<UserProfileDTO> mapUsers1 = userRepository
                .findAll( spec, pageable )
                .map( UserMapper::toUserProfileDTO );

        return ListResponse.< UserProfileDTO >builder()
                .data( mapUsers1.getContent() )
                .pagination(PaginationResponse.of (mapUsers1.getTotalElements(), page, size ) )
                .build();
    }
}
