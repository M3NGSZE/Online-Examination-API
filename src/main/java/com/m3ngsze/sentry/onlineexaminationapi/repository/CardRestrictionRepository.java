package com.m3ngsze.sentry.onlineexaminationapi.repository;

import com.m3ngsze.sentry.onlineexaminationapi.model.card.CardRestriction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CardRestrictionRepository extends JpaRepository< CardRepository, UUID > {

    @Query( value = """
        SELECT
              CR.RESTRICTION_ID     AS "restrictionId"
            , CR.CARD_ID            AS "cardId"
            , CR.CARD_NUMBER        AS "cardNumber"
            , CR.USER_ID            AS "userId"
            , CR.SCHEME_ID          AS "schemeId"
            , CR.REMARK             AS "remark"
            , CR.CREATED_AT         AS "createdAt"
            , CR.UPDATED_AT         AS "updatedAT"
        FROM CARD_RESTRICTION CR
        WHERE CR.CARD_NUMBER = :cardNumber
        AND CR.SCHEME_ID     = :schemeId
    """, nativeQuery = true)
    CardRestriction findByCardNumberSchemeId( String cardNumber, String schemeId);

}
