package com.m3ngsze.sentry.onlineexaminationapi.repository;

import com.m3ngsze.sentry.onlineexaminationapi.model.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {

    @Query( value ="""
        SELECT EXISTS(
            SELECT
                1
            FROM CARD C
            WHERE C.CARD_ID = :cardId
            AND C.USER_ID   = :userId
        )
    """, nativeQuery = true)
    Card findByCardIdAndUserId( String cardId, String userId );

}
