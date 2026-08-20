package com.m3ngsze.sentry.onlineexaminationapi.specification;

import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Enrollment;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Room;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.UserInfo;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class UserSpecification {

    public static Specification<User> isEnabled(Boolean enable) {
        return (root, query, cb) ->
                enable == null ? null : cb.equal(root.get("enabled"), enable);
    }

    public static Specification<User> isVerified(Boolean verify) {
        return (root, query, cb) ->
                verify == null ? null : cb.equal(root.get("verified"), verify);
    }

    public static Specification<User> search(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            String like = "%" + keyword.toLowerCase() + "%";

            // JOIN User -> UserInfo
            Join<User, UserInfo> userInfoJoin = root.join("userInfo", JoinType.LEFT);

            return cb.or(
//                    cb.like(cb.lower(root.get("email")), like),
                    cb.like(cb.lower(userInfoJoin.get("firstName")), like),
                    cb.like(cb.lower(userInfoJoin.get("lastName")), like)
            );
        };
    }

    public static Specification<User> enrolledInRoom(UUID roomId) {
        return (root, query, cb) -> {

            Join<User, Enrollment> enrollment =
                    root.join("enrollments");

            Join<Enrollment, Room> room =
                    enrollment.join("room");

            return cb.equal(
                    room.get("roomId"),
                    roomId
            );
        };
    }
}
