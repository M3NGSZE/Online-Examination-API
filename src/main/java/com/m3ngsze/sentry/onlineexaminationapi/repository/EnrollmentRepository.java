package com.m3ngsze.sentry.onlineexaminationapi.repository;

import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Enrollment;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Room;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.query.EnrollCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    void deleteByRoomAndUser(Room room, User user);

    boolean existsByRoomAndUser(Room room, User user);

    @Query( value = """
    SELECT en.room_id as roomId,
           count(en.user_id) as enrollmentCount
        FROM enrollments en
        INNER JOIN room_owners ro ON ro.room_id = en.room_id
        WHERE ro.user_id = :userId
        GROUP BY en.room_id;
    """, nativeQuery = true)
    List<EnrollCount> countUserEnrollRoomOwner(@Param("userId") UUID userId);

}
