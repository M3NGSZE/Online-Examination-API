package com.m3ngsze.sentry.onlineexaminationapi.model.card;

import com.m3ngsze.sentry.onlineexaminationapi.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table( name = "card_freeze" )
public class CardFreeze extends BaseEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "freeze_id" )
    private UUID freezeId;

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
