package com.m3ngsze.sentry.onlineexaminationapi.service.business.impl;

import com.m3ngsze.sentry.onlineexaminationapi.exception.BadRequestException;
import com.m3ngsze.sentry.onlineexaminationapi.exception.NotFoundException;
import com.m3ngsze.sentry.onlineexaminationapi.mapper.CardFreezeMapper;
import com.m3ngsze.sentry.onlineexaminationapi.mapper.CardMapper;
import com.m3ngsze.sentry.onlineexaminationapi.mapper.CardRestrictionMapper;
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
    private final CardRestrictionMapper cardRestrictionMapper;
    private final CardFreezeMapper cardFreezeMapper;

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

        M3n9sZe acquireCards = separateCard( requestBody );

        for ( M3n9sZe card: acquireCards.getSentryData( "cardList" ).toArrayList() ) {
            if ( card.containsKey( "isRestrict" ) && card.getString( "isRestrict" ).equalsIgnoreCase( "Y" ) ) {
                M3n9sZe tCard = processRestrictCard( card );
                if ( card.containsKey( "isInclude" ) && card.getString( "isInclude" ).equalsIgnoreCase( "Y" ) ) {
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

        outputData.setSentryData( "cardList" , cardList );

        return outputData;
    }

    @Override
    public M3n9sZe inquiryCardRestrictionStatus(M3n9sZe requestBody) {
        M3n9sZe outputData = new M3n9sZe();
        SentryData cardList = new SentryData();

        for ( M3n9sZe card: requestBody.getSentryData( "cardList" ).toArrayList() ) {

            validate( card, "cardId");

            M3n9sZe cardStatus = retrieveCardStatus( card );
            cardStatus.remove( "isVirtual" );
            cardStatus.setString( "cardId" , card.getString( "cardId" ) );
            cardList.add( cardStatus );
        }

        outputData.setSentryData( "cardList" , cardList );

        return outputData;
    }

    private M3n9sZe processRestrictCard( M3n9sZe inputData ) {
        M3n9sZe outputData = new M3n9sZe();

        if ( inputData.containsKey( "restrictOnlineCambodia" ) && inputData.getString( "restrictOnlineCambodia" ).matches("[YN]" ) ) {
            M3n9sZe rOCS = new M3n9sZe();
            String label = inputData.getString( "restrictOnlineCambodia" ).equalsIgnoreCase( "Y" ) ? "Add" : "remove";

            rOCS.setString( "schemeId", "16" );
            rOCS.setString( "remark", label + " restriction online purchase cambodia" );
            M3n9sZe rOC = insertAndUpdateRestriction( rOCS );
        }

        if ( inputData.containsKey( "restrictOnlineOversea" ) && inputData.getString( "restrictOnlineOversea" ).matches("[YN]" ) ) {
            M3n9sZe rOOS = new M3n9sZe();
            String label = inputData.getString( "restrictOnlineOversea" ).equalsIgnoreCase( "Y" ) ? "Add" : "remove";

            rOOS.setString( "schemeId", "17" );
            rOOS.setString( "remark", label + " restriction online purchase oversea" );
            M3n9sZe rOC = insertAndUpdateRestriction( rOOS );
        }

        if ( inputData.containsKey( "restrictInStoreCambodia" ) && inputData.getString( "restrictInStoreCambodia" ).matches("[YN]" ) ) {
            M3n9sZe rICS = new M3n9sZe();
            String label = inputData.getString( "restrictInStoreCambodia" ).equalsIgnoreCase( "Y" ) ? "Add" : "remove";

            rICS.setString( "schemeId", "18" );
            rICS.setString( "remark", label + " restriction in-store purchase cambodia" );
            M3n9sZe rOC = insertAndUpdateRestriction( rICS );
        }

        if ( inputData.containsKey( "restrictInStoreOversea" ) && inputData.getString( "restrictInStoreOversea" ).matches("[YN]" ) ) {
            M3n9sZe rIOS = new M3n9sZe();
            String label = inputData.getString( "restrictInStoreOversea" ).equalsIgnoreCase( "Y" ) ? "Add" : "remove";

            rIOS.setString( "schemeId", "19" );
            rIOS.setString( "remark", label + " restriction in-store purchase oversea" );
            M3n9sZe rOC = insertAndUpdateRestriction( rIOS );
        }

        return null;
    }

    private M3n9sZe insertAndUpdateRestriction ( M3n9sZe inputData ) {
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

                validate( card, "cardId", "expiredDate" );

                validateField( card, "restrictOnlineCambodia", "restrictOnlineOversea", "restrictInStoreCambodia", "restrictInStoreOversea" );

                M3n9sZe tCard = restrictionToFreeze( card, "restrictOnlineCambodia", "restrictOnlineOversea", "restrictInStoreCambodia", "restrictInStoreOversea" );

                if ( tCard.containsKey( "isFreezeYN" ) && tCard.getString( "isFreezeYN" ).equalsIgnoreCase( "Y" ) ) {
                    modifyField( tCard, "restrictOnlineCambodia", "restrictOnlineOversea", "restrictInStoreCambodia", "restrictInStoreOversea" );
                    card.setString( "isRestrict", "Y");
                    cardList.add( tCard );
                } else if ( !tCard.containsKey( "isFreezeYN" ) ) {
                    card.setString( "isRestrict", "Y");
                    card.setString( "isInclude", "Y" );
                    cardList.add( card );
                }
            }

            if ( card.containsKey( "freezeYN" ) ) {
                validateField( card, "freezeYN" );

                if ( checkFreezeCard ( card ).getString( "isFreezeCardYN" ).equalsIgnoreCase( "N" ) ) {
                    card.setString( "isFreezeYN" , card.getString( "freezeYN" ) );
                    cardList.add( card );
                }
            }
        }

        outputData.setSentryData( "cardList", cardList );

        return outputData;
    }

    private void modifyField ( M3n9sZe inputData, String... sKey ) {
        for ( String key: sKey ) {
            inputData.setString( key, "N" );
        }
    }

    private void validateField( M3n9sZe param, String... sKey ) {
        for ( String key : sKey ) {
            if ( param.containsKey( key ) && param.getString( key ).isEmpty() ) {
                throw new BadRequestException( key + " field is empty");
            }
        }
    }
    private void validate( M3n9sZe param, String... sKey ) {
        for (String key: sKey ) {
            if ( !param.containsKey( key ) )
                throw new BadRequestException( "Missing field " + key );
            else if ( param.getString( key ).isEmpty() )
                throw new BadRequestException( key + " field is empty" );
        }
    }

    private M3n9sZe restrictionToFreeze( M3n9sZe inputData, String... sKey ) {
        M3n9sZe outputData = new M3n9sZe();

        M3n9sZe cardStatus = retrieveCardStatus( inputData );

        if ( cardStatus.getString("isFreezeCardYN" ).equalsIgnoreCase( "N" ) ) {
            M3n9sZe checkByParam = paramCheck( inputData, cardStatus , sKey );

            if ( checkByParam.getString( "isCheckYN" ).equalsIgnoreCase( "Y" ) ) {
                M3n9sZe mapFieldYN = mapField( cardStatus );
                outputData = inquiryCheck( inputData, mapFieldYN, sKey);
            } else {
                checkByParam.remove( "isCheckYN" );
                outputData = checkByParam;
            }

        } else {
            outputData.setString( "isFreezeCardYN", "Y" );
        }

        outputData.setString( "cardId", inputData.getString( "cardId" ) );
        outputData.setString( "isVirtual", cardStatus.getString( "isVirtual" ) );

        return outputData;
    }

    private M3n9sZe paramCheck( M3n9sZe inputData, M3n9sZe inputParam, String... sKey ) {
        M3n9sZe outputData = new M3n9sZe();
        int X = 0;
        int Y = 0;
        String isCheckYN = "N";

        for ( String key: sKey ) {
            if ( inputParam.getString( "isVirtual" ).equalsIgnoreCase( "N" ) && inputData.containsKey( key )
                    && inputData.getString( key ).equalsIgnoreCase( "Y" ) )
                Y += 1;
            else if ( inputParam.getString( "isVirtual" ).equalsIgnoreCase( "Y" ) && inputData.containsKey( key )
                    && inputData.getString( key ).equalsIgnoreCase( "Y" ) )
                X += 1;
        }

        if ( X >= 2 || Y >= 4 ) {
            isCheckYN = "Y";
            outputData.setString( "isFreezeYN", "Y" );
            outputData.setString( "isCheckYN", isCheckYN );
        } else
            outputData.setString( "isCheckYN", isCheckYN );

        return outputData;
    }

    private M3n9sZe inquiryCheck( M3n9sZe inputData, M3n9sZe inputParam, String... sKey ) {
        M3n9sZe outputData = new M3n9sZe();
        int X = 0;

        for ( String key: sKey ) {
            if ( inputParam.getString( "isVirtualYN" ).matches("[YN]" ) &&
                    inputData.containsKey( key ) && inputData.getString( key).equalsIgnoreCase( "Y" ) )
                X += 1;

            if ( inputParam.getString( key ).equalsIgnoreCase( "N" ) )
                X += 1;
        }

        if ( X >= 4 )
            outputData.setString( "isFreezeYN", "Y" );

        return outputData;
    }

    private M3n9sZe mapField( M3n9sZe inputData ) {
        M3n9sZe outputData = new M3n9sZe();

        if ( inputData.containsKey( "isActiveOnlineCambodiaYN" ) )
            outputData.setString( "restrictOnlineCambodia", inputData.getString( "isActiveOnlineCambodiaYN" ) );

        if ( inputData.containsKey( "isActiveOnlineOverseaYN" ) )
            outputData.setString( "restrictOnlineOversea", inputData.getString( "isActiveOnlineOverseaYN" ) );

        if ( inputData.containsKey( "isActiveInStoreCambodiaYN" ) )
            outputData.setString( "restrictInStoreCambodia", inputData.getString( "isActiveInStoreCambodiaYN" ) );

        if ( inputData.containsKey( "isActiveInStoreOverseaYN" ) )
            outputData.setString( "restrictInStoreOversea", inputData.getString( "isActiveInStoreOverseaYN" ) );

        outputData.setString( "isVirtual", inputData.getString( "isVirtual" ) );

        return outputData;
    }

    private M3n9sZe retrieveCardStatus( M3n9sZe inputData ) {
        M3n9sZe outputData = new M3n9sZe();
        String isVirtual = "Y";

        M3n9sZe cardInfo = cardMapper.retrieveCardInfoByCardId( inputData );

        if ( cardInfo == null ) throw new NotFoundException( "Card with id: " + inputData.getString( "cardId" ) + " not found" );

        M3n9sZe rOCS = new M3n9sZe();
        rOCS.setString( "cardNumber" , cardInfo.getString( "cardNumber" ) );
        rOCS.setString( "schemeId" , "16" );
        outputData.setString( "isActiveOnlineCambodia", checkStatus ( cardRestrictionMapper.retrieveCardRestrictionByCardNumberAndSchemeId( rOCS ), cardInfo, "isActiveOnlineCambodiaYN" ).getString( "isActiveOnlineCambodiaYN" ) );

        M3n9sZe rIOS = new M3n9sZe();
        rIOS.setString( "cardNumber" , cardInfo.getString( "cardNumber" ) );
        rIOS.setString( "schemeId" , "17" );
        outputData.setString( "isActiveOnlineOverseaYN", checkStatus ( cardRestrictionMapper.retrieveCardRestrictionByCardNumberAndSchemeId( rIOS ), cardInfo, "isActiveOnlineOverseaYN" ).getString( "isActiveOnlineOverseaYN" ) );

        if ( !cardInfo.getString( "cardType" ).equals( CardType.VIRTUAL.toString() ) ) {
            isVirtual = "N";
            M3n9sZe rICS = new M3n9sZe();
            rICS.setString( "cardNumber" , cardInfo.getString( "cardNumber" ) );
            rICS.setString( "schemeId" , "18" );
            outputData.setString( "isActiveInStoreCambodiaYN", checkStatus ( cardRestrictionMapper.retrieveCardRestrictionByCardNumberAndSchemeId( rICS ), cardInfo, "isActiveInStoreCambodiaYN" ).getString( "isActiveInStoreCambodiaYN" ) );

            M3n9sZe rIOsS = new M3n9sZe();
            rIOsS.setString( "cardNumber" , cardInfo.getString( "cardNumber" ) );
            rIOsS.setString( "schemeId" , "19" );
            outputData.setString( "isActiveInStoreOverseaYN", checkStatus ( cardRestrictionMapper.retrieveCardRestrictionByCardNumberAndSchemeId( rIOsS ), cardInfo, "isActiveInStoreOverseaYN" ).getString( "isActiveInStoreOverseaYN" ) );
        } else {
            outputData.setString( "isActiveInStoreCambodia", "N" );
            outputData.setString( "isActiveInStoreOversea", "N" );
        }

        outputData.setString( "isFreezeCardYN", checkFreezeCard( inputData ).getString( "isFreezeCardYN" ) );
        outputData.setString( "isVirtual", isVirtual );

        return outputData;
    }

    private M3n9sZe checkStatus ( M3n9sZe inputData, M3n9sZe inputParam, String sKey ) {
        M3n9sZe outputData = new M3n9sZe();

        if ( inputData != null ) {
            int toDate = Integer.parseInt( inputData.getString("toDate" ) );
            int expireDate = Integer.parseInt( inputParam.getString("expireDate" ) );

            if ( toDate >= expireDate )
                outputData.setString( sKey, "N" );
            else
                outputData.setString( sKey, "Y" );
        }
        else
            outputData.setString( sKey, "Y");

        return  outputData;
    }

    private M3n9sZe checkFreezeCard( M3n9sZe inputData ) {
        M3n9sZe outputData = new M3n9sZe();

        M3n9sZe cardFreeze = cardFreezeMapper.retrieveCardFreezeStatusByCardId( inputData );
        if ( cardFreeze == null)
            throw new NotFoundException( "Card with id: " + inputData.getString( "cardId" ) + " not found" );

        if ( cardFreeze.getString( "schemeId" ).equals( "0" ) )
            outputData.setString( "isFreezeCardYN" , "N" );
        else
            outputData.setString( "isFreezeCardYN", "Y" );

        return outputData;
    }
}
