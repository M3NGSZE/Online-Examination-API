package com.m3ngsze.sentry.onlineexaminationapi.service.component.impl;

import com.m3ngsze.sentry.onlineexaminationapi.exception.BadRequestException;
import com.m3ngsze.sentry.onlineexaminationapi.exception.NotFoundException;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Room;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.PaginationResponse;
import com.m3ngsze.sentry.onlineexaminationapi.repository.RoomRepository;
import com.m3ngsze.sentry.onlineexaminationapi.repository.UserRepository;
import com.m3ngsze.sentry.onlineexaminationapi.service.component.UserCbc;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.m3ngsze.sentry.onlineexaminationapi.utility.RoomUtil.getRoomDTO;

@Service
@RequiredArgsConstructor
public class UserCbcImpl implements UserCbc {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    private final ModelMapper modelMapper;

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        UUID userId = UUID.fromString((String) auth.getCredentials());
        if (auth == null) {
            throw new NotFoundException("Authentication not found");
        }

        User user = (User) auth.getPrincipal();

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        return user;
    }

    @Override
    public String extractAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BadRequestException("Invalid Authorization header");
        }

        return authHeader.substring(7);
    }

    @Override
    public ListResponse<RoomDTO> getUserRoom(Integer page, Integer size, Sort.Direction sort, Specification<Room> spec) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(sort, "updatedAt")
        );

        Page<RoomDTO> roompage = roomRepository.findAll(spec, pageable)
                .map(room -> getRoomDTO(room, modelMapper));

        return ListResponse.<RoomDTO>builder()
                .data(roompage.getContent())
                .pagination(PaginationResponse.of(roompage.getTotalElements(), page, size))
                .build();
    }



}
