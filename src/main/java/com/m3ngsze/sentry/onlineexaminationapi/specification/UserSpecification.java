package com.m3ngsze.sentry.onlineexaminationapi.specification;

import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import org.springframework.data.jpa.domain.Specification;

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

            return cb.or(
                    cb.like(cb.lower(root.get("email")), like),
                    cb.like(cb.lower(root.get("username")), like)
            );
        };
    }

}
