package com.m3ngsze.sentry.onlineexaminationapi.model.card;

import com.m3ngsze.sentry.onlineexaminationapi.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table( name = "card_restriction" )
public class CardRestriction extends BaseEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "restriction_id" )
    private UUID restrictionId;

    @Column( name = "card_id" )
    private String cardId;

    @Column( name = "card_number" )
    private String cardNumber;

    @Column( name = "user_id" )
    private String userId;

    @Column(name = "scheme_id")
    private String schemeId;

    private String remark;

}
