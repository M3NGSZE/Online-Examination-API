package com.m3ngsze.sentry.onlineexaminationapi.model.card;

import com.m3ngsze.sentry.onlineexaminationapi.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "card")
public class Card extends BaseEntity {

    @Id
    @Column( name = "card_id" )
    private String cardId;

    @Column( name = "card_number" )
    private String cardNumber;

    @Column( name = "user_id" )
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type")
    private CardType cardType;

    private String remark;

    @Column( name = "expired_date" )
    private String expiredDate;

}
