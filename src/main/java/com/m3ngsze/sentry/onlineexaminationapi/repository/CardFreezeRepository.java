package com.m3ngsze.sentry.onlineexaminationapi.repository;

import com.m3ngsze.sentry.onlineexaminationapi.model.card.CardFreeze;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CardFreezeRepository extends JpaRepository< CardFreeze, String > {

    @Query( value = """
        SELECT
               CF.FREEZE_ID     AS "freezeId"
             , CF.CARD_ID       AS "cardId"
             , CF.CARD_NUMBER   AS "cardNumber"
             , CF.USER_ID       AS "userId"
             , CF.SCHEME_ID     AS "schemeId"
             , CF.REMARK        AS "remark"
             , CF.CREATED_AT    AS "createdAt"
             , CF.UPDATED_AT    AS "updatedAt"
        FROM CARD_FREEZE CF
        WHERE CR.CARD_ID = :cardId
    """, nativeQuery = true)
    Tuple retrieveCardFreezeStatus( String cardId );
}
