package com.m3ngsze.sentry.onlineexaminationapi.repository;

import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {

    Optional<UserSession> findByRefreshTokenHash(String refreshTokenHash);

    Optional<UserSession> findByRefreshTokenHashAndUser(String refreshTokenHash, User user);

    @Modifying
    @Query(value = """
    DELETE FROM user_sessions us
        WHERE us.user_id = :userId
    """, nativeQuery = true)
    void deleteByUser_UserId( @Param("userId")UUID userId);

}
