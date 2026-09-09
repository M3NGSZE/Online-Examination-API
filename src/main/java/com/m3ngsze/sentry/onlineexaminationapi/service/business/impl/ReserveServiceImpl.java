package com.m3ngsze.sentry.onlineexaminationapi.service.business.impl;

import com.m3ngsze.sentry.onlineexaminationapi.exception.BadRequestException;
import com.m3ngsze.sentry.onlineexaminationapi.exception.NotFoundException;
import com.m3ngsze.sentry.onlineexaminationapi.mapper.CardMapper;
import com.m3ngsze.sentry.onlineexaminationapi.mapper.M3n9seMapper;
import com.m3ngsze.sentry.onlineexaminationapi.model.card.CardType;
import com.m3ngsze.sentry.onlineexaminationapi.model.data.M3n9sZe;
import com.m3ngsze.sentry.onlineexaminationapi.model.data.SentryData;
import com.m3ngsze.sentry.onlineexaminationapi.model.dto.RoomDTO;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.Room;
import com.m3ngsze.sentry.onlineexaminationapi.model.entity.User;
import com.m3ngsze.sentry.onlineexaminationapi.model.response.ListResponse;
import com.m3ngsze.sentry.onlineexaminationapi.service.common.RoomCommon;
import com.m3ngsze.sentry.onlineexaminationapi.service.common.UserCommon;
import com.m3ngsze.sentry.onlineexaminationapi.service.business.ReserveService;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.m3ngsze.sentry.onlineexaminationapi.specification.RoomSpecification.*;

@Service
@RequiredArgsConstructor
public class ReserveServiceImpl implements ReserveService {

    private final UserCommon userCommon;
    private final RoomCommon roomCommon;

    private final CardMapper cardMapper;

    @Override
    public ListResponse<RoomDTO> getUserJoinedRooms(Integer page, Integer size, String search, Sort.Direction sort) {
        User user = userCommon.getCurrentUser();

        Specification<Room> spec = Specification
                .where(search(search))
                .and(isDeleted(false))
                .and(enrolledBy(user));

        return roomCommon.getUserRoom(page, size, sort, spec);
    }

    @Override
    public ListResponse<RoomDTO> getOwnUserRooms(Integer page, Integer size, String search, Sort.Direction sort) {
        User user = userCommon.getCurrentUser();

        Specification<Room> spec = Specification
                .where(search(search))
                .and(isDeleted(false))
                .and(ownBy(user));

        return roomCommon.getUserRoom(page, size, sort, spec);
    }

    @Override
    public M3n9sZe M3n9seAndSentryData(M3n9sZe requestBody) {

        M3n9sZe outputData = new M3n9sZe();

        SentryData zList = new SentryData();

        M3n9sZe user = new M3n9sZe();
        user.setString( "userId", "9f0882eb-e278-4b14-96ca-5f2931407fd9" );
        user.setString( "username", "Sentry Void" );
        user.setString( "gender", "Male" );

        zList.addM3n9se( user );

        M3n9sZe user1 = new M3n9sZe();
        user1.setString( "userId", requestBody.getString("userId" ) );
        user1.setString( "username", requestBody.getString("username" ) );
        user1.setString( "gender", requestBody.getString("gender" ) );

        zList.addM3n9se( user1 );

        outputData.addM3n9sZe( "user", user );
        outputData.setSentryData( "listUsers", zList);
        outputData.setSentryData( "newCardList", requestBody.getSentryData( "cardList" ) );
        outputData.addM3n9sZe( "newHeader", requestBody.getM3n9sZe( "header" ) );

        List<M3n9sZe> list = zList.toArrayList();
        for ( M3n9sZe m: list) {
            System.out.println( m );
        }

        zList.removeM3n9se( user1 );

        return outputData;
    }

    @Override
    public M3n9sZe restrictionAndFreeze( M3n9sZe requestBody ) {
        M3n9sZe outputData = new M3n9sZe();
        SentryData cardList = new SentryData();
        /*M3n9sZe acquireCards = separateCard( requestBody );

        for ( M3n9sZe card: acquireCards.getSentryData( "cardList" ).toArrayList() ) {
            if ( card.getString( "isRestrict" ).equalsIgnoreCase( "Y" ) ) {
                M3n9sZe tCard = processRestrictCard( card );
                if ( card.getString( "FreezeYN" ).equalsIgnoreCase( "Y" ) ) {
                    cardList.add( tCard );
                }
            }

            M3n9sZe tCard = new M3n9sZe();
            if ( card.containsKey( "isFreezeYN" ) && card.getString( "isFreezeYN" ).equalsIgnoreCase( "Y" ) ) {
                tCard = processFreezeCard( card );
            } else if ( card.containsKey( "isFreezeYN" ) && card.getString( "isFreezeYN" ).equalsIgnoreCase( "N" ) ) {
                tCard = processUnFreezeCard( card );
            }
            cardList.add( tCard );
        }

        outputData.setSentryData( "cardList" , cardList );*/

        for ( M3n9sZe card: requestBody.getSentryData( "cardList").toArrayList() ) {
            cardList.add( retrieveCardStatus( card ) );
        }

        outputData.setSentryData( "cardList" , cardList );

        return outputData;
    }

    private M3n9sZe processRestrictCard( M3n9sZe inputData ) {
        return null;
    }

    private M3n9sZe processFreezeCard( M3n9sZe inputData ) {
        return null;
    }
    private M3n9sZe processUnFreezeCard( M3n9sZe inputData ) {
        return null;
    }

    private M3n9sZe separateCard( M3n9sZe inputData ) {
        M3n9sZe outputData = new M3n9sZe();
        SentryData cardList = new SentryData();

        for ( M3n9sZe card: inputData.getSentryData( "cardList" ).toArrayList() ) {
            if ( card.containsKey( "restrictOnlineCambodia" ) || card.containsKey( "restrictOnlineOversea" ) ||
                card.containsKey( "restrictInStoreCambodia" ) || card.containsKey( "restrictInStoreOversea" ) ) {

                validateField( card, "restrictOnlineCambodia", "restrictOnlineOversea", "restrictInStoreCambodia", "restrictInStoreOversea" );

                M3n9sZe tCard = restrictionToFreeze( card, "restrictOnlineCambodia", "restrictOnlineOversea", "restrictInStoreCambodia", "restrictInStoreOversea" );

                cardList.add( card );
            }

            if ( card.containsKey( "freezeYN" ) ) {
                validateField( card, "freezeYN" );
                card.setString( "isFreezeYN" , card.getString( "freezeYN" ) );

                cardList.add( card );
            }
        }

        outputData.setSentryData( "cardList", cardList );

        return outputData;
    }

    private void validateField( M3n9sZe param, String... sKey ) {
        for ( String key : sKey ) {
            if ( param.containsKey( key ) && param.getString( key ).isEmpty() ) {
                throw new BadRequestException( key + " field is empty");
            }
        }
    }

    private M3n9sZe restrictionToFreeze( M3n9sZe inputData, String... sKey ) {

        return null;
    }

    private M3n9sZe retrieveCardStatus( M3n9sZe inputData ) {
        M3n9sZe outputData = new M3n9sZe();

        M3n9sZe cardInfo = cardMapper.retrieveCardInfoByCardId( inputData );

        if ( cardInfo == null ) throw new NotFoundException( "Card with id: " + inputData.getString( "cardId" ) + " not found" );

        M3n9sZe rOCS = new M3n9sZe();
        rOCS.setString( "cardNumber" , cardInfo.getString( "cardNumber" ));
        rOCS.setString( "schemeId" , "16" );
        outputData.setString( "isActiveOnlineCambodia", checkStatus ( cardMapper.retrieveCardRestrictionByCardNumberAndSchemeId( rOCS ), "isActiveOnlineCambodia" ).getString( "isActiveOnlineCambodia" ) );

        M3n9sZe rIOS = new M3n9sZe();
        rIOS.setString( "cardNumber" , cardInfo.getString( "cardNumber" ));
        rIOS.setString( "schemeId" , "17" );
        outputData.setString( "isActiveOnlineOversea", checkStatus ( cardMapper.retrieveCardRestrictionByCardNumberAndSchemeId( rIOS ), "isActiveOnlineOversea" ).getString( "isActiveOnlineOversea" ) );

        if ( !cardInfo.getString( "cardType" ).equals( CardType.VIRTUAL.toString() ) ) {
            M3n9sZe rICS = new M3n9sZe();
            rICS.setString( "cardNumber" , cardInfo.getString( "cardNumber" ));
            rICS.setString( "schemeId" , "18" );
            outputData.setString( "isActiveInStoreCambodia", checkStatus ( cardMapper.retrieveCardRestrictionByCardNumberAndSchemeId( rICS ), "isActiveInStoreCambodia" ).getString( "isActiveInStoreCambodia" ) );

            M3n9sZe rIOsS = new M3n9sZe();
            rIOsS.setString( "cardNumber" , cardInfo.getString( "cardNumber" ));
            rIOsS.setString( "schemeId" , "19" );
            outputData.setString( "isActiveInStoreOversea", checkStatus ( cardMapper.retrieveCardRestrictionByCardNumberAndSchemeId( rIOsS ), "isActiveInStoreOversea" ).getString( "isActiveInStoreOversea" ) );
        }

        return outputData;
    }

    private M3n9sZe checkStatus ( M3n9sZe inputData, String sKey ) {
        M3n9sZe outputData = new M3n9sZe();

        if ( inputData == null )
            outputData.setString( sKey, "Y");

        return  outputData;
    }

    private M3n9sZe retrieveCardFreezeStatus( M3n9sZe inputData ) {
        return null;
    }
}
