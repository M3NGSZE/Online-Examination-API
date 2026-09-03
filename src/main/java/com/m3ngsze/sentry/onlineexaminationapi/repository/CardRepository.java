package com.m3ngsze.sentry.onlineexaminationapi.repository;

import com.m3ngsze.sentry.onlineexaminationapi.model.card.Card;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository< Card, String > {

    @Query( value ="""
        SELECT EXISTS(
            SELECT
                1
            FROM CARD C
            WHERE C.CARD_ID = :cardId
            AND C.USER_ID   = :userId
        )
    """, nativeQuery = true )
    Card findByCardIdAndUserId( String cardId, String userId );

    @Query( value = """
        SELECT
              C.CARD_ID         AS "cardId"
            , C.CARD_NUMBER     AS "cardNumber"
            , C.USER_ID         AS "userId"
            , C.CARD_TYPE       AS "cardType"
            , C.REMARK          AS "remark"
            , C.EXPIRED_DATE    AS "expiredDate"
            , C.ISSUED_DATE     AS "issuedDate"
            , C.CREATED_DATE    AS "createdAt"
            , C.UPDATED_DATE    AS "updatedAt"
        FROM CARD C
        WHERE C.CARD_ID = :cardId
    """, nativeQuery = true )
    Tuple retrieveCardInfo( String cardId );
}
