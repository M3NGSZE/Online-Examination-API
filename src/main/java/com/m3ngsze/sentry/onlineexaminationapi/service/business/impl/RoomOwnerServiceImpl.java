package com.m3ngsze.sentry.onlineexaminationapi.service.business.impl;

import com.m3ngsze.sentry.onlineexaminationapi.exception.BadRequestException;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.UserDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import com.m3ngsze.sentry.onlineexaminationapi.repository.EnrollmentRepository;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.business.RoomOwnerService;
import com.m3ngsze.sentry.onlineexaminationapi.service.component.UserCbc;
import com.m3ngsze.sentry.onlineexaminationapi.utility.UtilMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.m3ngsze.sentry.onlineexaminationapi.specification.UserSpecification.search;

@Service
@RequiredArgsConstructor
public class RoomOwnerServiceImpl implements RoomOwnerService {

    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    private final UserCbc userCbc;

    @Override
    public ListResponse<UserDTO> retrieveEnrollmentsByRoomId( UUID roomId, Integer page, Integer size, String search, Sort.Direction sort  ) {
        List<UserDTO> emptyEnrollment = new ArrayList<>();

        User currentUser = userCbc.getCurrentUser();

        if ( !enrollmentRepository.existsByRoom_RoomIdAndUser_UserId( roomId, currentUser.getUserId() ) )
            return null;

         userRepository.findAllByEnrollments_Room_RoomId(roomId)
                .stream()
                .map(UtilMapper::toUserDTO)
                .toList();

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(sort, "userInfo.username")
        );


        Specification<User> spec = Specification
                .where(search(search));


        return null;
    }
}
