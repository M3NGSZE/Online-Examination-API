package com.m3ngsze.sentry.onlineexaminationapi.specification;

import com.m3ngsze.sentry.onlineexaminationapi.model.entity.*;
import com.m3ngsze.sentry.onlineexaminationapi.model.enums.RoomType;
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
            Join<User, UserInfo> userInfoJoin = root.join("userInfo", JoinType.LEFT );

            return cb.or(
//                    cb.like(cb.lower(root.get("email")), like),
                    cb.like(cb.lower(userInfoJoin.get("firstName")), like),
                    cb.like(cb.lower(userInfoJoin.get("lastName")), like)
            );
        };
    }

    public static Specification<User> enrolledInRoom( Room room, RoomType roomType ) {
        return (root, query, cb ) -> {
            if ( room == null ) return null;

            if ( roomType == null ) {
                Join< User, Enrollment > enrollment = root.join( "enrollments", JoinType.LEFT );
                Join< User, RoomOwner> owner = root.join( "roomOwners", JoinType.LEFT );
                return cb.or(
                        cb.equal(enrollment.get( "room" ), room ),
                        cb.equal(owner.get( "room" ), room )
                );
            }

            if ( roomType == RoomType.ENROLL ) {
                Join< User, Enrollment > enroll = root.join( "enrollments" );
                return cb.equal( enroll.get( "room" ), room );
            }

            if ( roomType == RoomType.OWN ) {
                Join< User, RoomOwner> owner = root.join( "roomOwners" );
                return cb.equal( owner.get( "room" ), room );
            }

            return null;
        };
    }
}
