package com.m3ngsze.sentry.onlineexaminationapi.specification;

import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Enrollment;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Room;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.RoomOwner;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class RoomSpecification {

    public static Specification<Room> search(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) {
                return null;
            }

            return cb.or(
                    cb.like(cb.lower(root.get("roomName")), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("section")), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("subject")), "%" + search.toLowerCase() + "%")
            );
        };
    }

    public static Specification<Room> isDeleted(Boolean status) {
        return ((root, query, cb) ->
                status == null ? null : cb.equal(root.get("isDeleted"), status));
    }

    public static Specification<Room> enrolledBy(User user) {
        return ((root, query, cb) -> {
            if (user == null) return null;

            Join<Room, Enrollment> enrollment = root.join("enrollments");

            return cb.equal(enrollment.get("user"), user);
        });
    }

    public static Specification<Room> ownBy (User user) {
        return ((root, query, cb) -> {
            if (user == null) return null;

            Join<Room, RoomOwner> enrollment = root.join("roomOwners");

            return cb.equal(enrollment.get("user"), user);
        });
    }

}
