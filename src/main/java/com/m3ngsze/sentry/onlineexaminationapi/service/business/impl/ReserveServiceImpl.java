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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

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

            card.setString( "mRetrieve", "Y" );
            M3n9sZe cardStatus = retrieveCardStatus( card );
            cardStatus.remove( "isVirtual" );
            cardStatus.remove( "vExistYN" );
            cardStatus.setString( "cardId" , card.getString( "cardId" ) );
            cardList.add( cardStatus );
        }

        outputData.setSentryData( "cardList" , cardList );

        return outputData;
    }

    private M3n9sZe processRestrictCard( M3n9sZe inputData ) {
        M3n9sZe outputData = new M3n9sZe();
        outputData.setString( "carId" , inputData.getString( "carId" ) );

        if ( inputData.containsKey( "restrictOnlineCambodia" ) && inputData.getString( "restrictOnlineCambodia" ).matches("[YN]" ) ) {
            M3n9sZe rOCS = new M3n9sZe();
            String label;

            if ( inputData.getString("restrictOnlineCambodia").equalsIgnoreCase("Y") ) {
                outputData.setString( "applyRestrictOnlineCambodia", "Y" );
                label = "add";
            } else {
                outputData.setString( "removeRestrictOnlineCambodia", "Y" );
                label = "remove";
            }

            rOCS.setString( "schemeId", "16" );
            rOCS.setString( "remark", label + " restriction online purchase cambodia" );
            insertAndUpdateRestriction( rOCS );
        }

        if ( inputData.containsKey( "restrictOnlineOversea" ) && inputData.getString( "restrictOnlineOversea" ).matches("[YN]" ) ) {
            M3n9sZe rOOS = new M3n9sZe();
            String label;

            if ( inputData.getString("restrictOnlineOversea").equalsIgnoreCase("Y") ) {
                outputData.setString( "applyRestrictOnlineOversea", "Y" );
                label = "add";
            } else {
                outputData.setString( "removeRestrictOnlineOversea", "Y" );
                label = "remove";
            }

            rOOS.setString( "schemeId", "17" );
            rOOS.setString( "remark", label + " restriction online purchase oversea" );
            insertAndUpdateRestriction( rOOS );
        }

        if ( inputData.containsKey( "restrictInStoreCambodia" ) && inputData.getString( "restrictInStoreCambodia" ).matches("[YN]" ) ) {
            M3n9sZe rICS = new M3n9sZe();
            String label;

            if ( inputData.getString("restrictInStoreCambodia").equalsIgnoreCase("Y") ) {
                outputData.setString( "applyRestrictInStoreCambodia", "Y" );
                label = "add";
            } else {
                outputData.setString( "removeRestrictInStoreCambodia", "Y" );
                label = "remove";
            }

            rICS.setString( "schemeId", "18" );
            rICS.setString( "remark", label + " restriction in-store purchase cambodia" );
            insertAndUpdateRestriction( rICS );
        }

        if ( inputData.containsKey( "restrictInStoreOversea" ) && inputData.getString( "restrictInStoreOversea" ).matches("[YN]" ) ) {
            M3n9sZe rIOS = new M3n9sZe();
            String label;

            if ( inputData.getString("restrictInStoreOversea").equalsIgnoreCase("Y") ) {
                outputData.setString( "applyRestrictInStoreOversea", "Y" );
                label = "add";
            } else {
                outputData.setString( "removeRestrictInStoreOversea", "Y" );
                label = "remove";
            }

            rIOS.setString( "schemeId", "19" );
            rIOS.setString( "remark", label + " restriction in-store purchase oversea" );
            insertAndUpdateRestriction( rIOS );
        }

        return outputData;
    }

    private void insertAndUpdateRestriction ( M3n9sZe inputData ) {

        M3n9sZe cardInfo = cardMapper.retrieveCardInfoByCardId( inputData );
        boolean isUpdate = true;

        M3n9sZe cardRestriction = cardRestrictionMapper.retrieveCardRestrictionByCardNumberAndSchemeId( inputData );

        if ( cardRestriction == null ) {
            isUpdate = false;
            M3n9sZe insert = new M3n9sZe();
            insert.setUUID( "restrictionId", UUID.randomUUID() );
            insert.setString( "cardId", insert.getString( "cardId" ) );
            insert.setString( "cardNumber", cardInfo.getString( "cardNumber" ) );
            insert.setString( "userId", cardInfo.getString( "userId" ) );
            insert.setString( "schemeId", inputData.getString( "schemeId" ) );
            insert.setString( "remark", inputData.getString( "remark" ) );
            insert.setString( "fromDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) );
            insert.setString( "toDate", cardInfo.getString( "expiredDate" ) );
            insert.setLocalDateTime( "createdAt", LocalDateTime.now() );
            insert.setLocalDateTime( "updatedAt", LocalDateTime.now() );

            cardRestrictionMapper.insertRestrictionCard( insert );
        }

        if ( isUpdate ) {
            if ((inputData.containsKey("restrictOnlineCambodia") && inputData.getString("restrictOnlineCambodia").equalsIgnoreCase("N")) ||
                    (inputData.containsKey("restrictOnlineOversea") && inputData.getString("restrictOnlineOversea").equalsIgnoreCase("N")) ||
                    (inputData.containsKey("restrictInStoreCambodia") && inputData.getString("restrictInStoreCambodia").equalsIgnoreCase("N")) ||
                    (inputData.containsKey("restrictInStoreOversea") && inputData.getString("restrictInStoreOversea").equalsIgnoreCase("N"))) {

                M3n9sZe removeRestriction = new M3n9sZe();
                removeRestriction.setString("remark", inputData.getString("remark"));
                removeRestriction.setString("fromDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                removeRestriction.setString("fromDate", LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                removeRestriction.setLocalDateTime("updatedAt", LocalDateTime.now());

                cardRestrictionMapper.updateRemoveRestrictionCard(removeRestriction);

            } else if ((inputData.containsKey("restrictOnlineCambodia") && inputData.getString("restrictOnlineCambodia").equalsIgnoreCase("Y")) ||
                    (inputData.containsKey("restrictOnlineOversea") && inputData.getString("restrictOnlineOversea").equalsIgnoreCase("Y")) ||
                    (inputData.containsKey("restrictInStoreCambodia") && inputData.getString("restrictInStoreCambodia").equalsIgnoreCase("Y")) ||
                    (inputData.containsKey("restrictInStoreOversea") && inputData.getString("restrictInStoreOversea").equalsIgnoreCase("Y"))) {

                M3n9sZe reapplyRestriction = new M3n9sZe();
                reapplyRestriction.setString("remark", inputData.getString("remark"));
                reapplyRestriction.setString("toDate", cardInfo.getString("expiredDate"));
                reapplyRestriction.setLocalDateTime("updatedAt", LocalDateTime.now());

                cardRestrictionMapper.updateReapplyRestrictionCard(reapplyRestriction);
            }
        }
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
            M3n9sZe mapFieldYN = mapField( cardStatus );
            outputData = manipulateField( inputData, mapFieldYN, sKey );
        } else {
            outputData.setString( "isFreezeCardYN", "Y" );
        }

        outputData.setString( "cardId", inputData.getString( "cardId" ) );
        outputData.setString( "isVirtual", cardStatus.getString( "isVirtual" ) );

        return outputData;
    }

    private M3n9sZe manipulateField( M3n9sZe inputData, M3n9sZe inputParam, String... sKey ) {
        M3n9sZe outputData = new M3n9sZe( inputData );
        int x = 0;

        for ( String key: sKey ) {
            if ( inputParam.getString("isVirtualYN" ).matches( "[YN]" ) &&
                    inputData.containsKey( key ) && inputData.getString( key).equalsIgnoreCase( "Y" ) )
                x += 1;
            else if ( inputParam.getString( key ).equalsIgnoreCase( "N" ) )
                x += 1;
        }

        if ( x >= 4 )
            outputData.setString( "isFreezeYN", "Y" );

        if ( inputParam.containsKey( "vExistYN" ) && inputParam.getString( "vExistYN" ).equalsIgnoreCase( "N" ) ) {
            outputData.put( "restrictInStoreCambodia", "Y");
            outputData.put( "restrictInStoreOversea", "Y");
        }

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

        outputData.setString( "isVirtualYN", inputData.getString( "isVirtualYN" ) );
        outputData.setString( "vExistYN", inputData.getString( "vExistYN" ) );

        return outputData;
    }

    private M3n9sZe retrieveCardStatus( M3n9sZe inputData ) {
        M3n9sZe outputData = new M3n9sZe();
        String isVirtual = "Y";
        String vExistYN = "N";

        M3n9sZe cardInfo = cardMapper.retrieveCardInfoByCardId( inputData );

        if ( cardInfo == null ) throw new NotFoundException( "Card with id: " + inputData.getString( "cardId" ) + " not found" );

        M3n9sZe rOCS = new M3n9sZe();
        rOCS.setString( "cardNumber" , cardInfo.getString( "cardNumber" ) );
        rOCS.setString( "schemeId" , "16" );
        M3n9sZe rOSR = cardRestrictionMapper.retrieveCardRestrictionByCardNumberAndSchemeId( rOCS );
        outputData.setString( "isActiveOnlineCambodiaYN", checkStatus ( rOSR, cardInfo, "isActiveOnlineCambodiaYN" ).getString( "isActiveOnlineCambodiaYN" ) );

        M3n9sZe rOOS = new M3n9sZe();
        rOOS.setString( "cardNumber" , cardInfo.getString( "cardNumber" ) );
        rOOS.setString( "schemeId" , "17" );
        M3n9sZe rOOR = cardRestrictionMapper.retrieveCardRestrictionByCardNumberAndSchemeId( rOOS );
        outputData.setString( "isActiveOnlineOverseaYN", checkStatus ( rOOR, cardInfo, "isActiveOnlineOverseaYN" ).getString( "isActiveOnlineOverseaYN" ) );

        M3n9sZe rICS = new M3n9sZe();
        rICS.setString( "cardNumber" , cardInfo.getString( "cardNumber" ) );
        rICS.setString( "schemeId" , "18" );
        M3n9sZe rICR = cardRestrictionMapper.retrieveCardRestrictionByCardNumberAndSchemeId( rICS );

        M3n9sZe rIOS = new M3n9sZe();
        rIOS.setString( "cardNumber" , cardInfo.getString( "cardNumber" ) );
        rIOS.setString( "schemeId" , "19" );
        M3n9sZe rIOR = cardRestrictionMapper.retrieveCardRestrictionByCardNumberAndSchemeId( rIOS );
        outputData.setString( "isActiveInStoreOverseaYN", checkStatus ( rIOR , cardInfo, "isActiveInStoreOverseaYN" ).getString( "isActiveInStoreOverseaYN" ) );

        if ( !cardInfo.getString( "cardType" ).equals( CardType.VIRTUAL.toString() ) ) {
            isVirtual = "N";
            outputData.setString( "isActiveInStoreCambodiaYN", checkStatus ( rICR, cardInfo, "isActiveInStoreCambodiaYN" ).getString( "isActiveInStoreCambodiaYN" ) );
            outputData.setString( "isActiveInStoreOverseaYN", checkStatus ( rIOR , cardInfo, "isActiveInStoreOverseaYN" ).getString( "isActiveInStoreOverseaYN" ) );
        } else {
            outputData.setString( "isActiveInStoreCambodiaYN", "N" );
            outputData.setString( "isActiveInStoreOverseaYN", "N" );
            if ( rICR == null && rIOR == null )
                vExistYN = "Y";
        }

        if ( inputData.containsKey( "mRetrieveYN" ) && inputData.getString( "mRetrieveYN" ).equalsIgnoreCase( "Y" ) ) {
            outputData.setString( "isVirtualYN", isVirtual );
            outputData.setString( "vExistYN", vExistYN );
            outputData.addM3n9sZe( "16", null );
            outputData.addM3n9sZe( "17", null );
            outputData.addM3n9sZe( "18", null );
            outputData.addM3n9sZe( "19", null );
        }
        outputData.setString( "isFreezeCardYN", checkFreezeCard( inputData ).getString( "isFreezeCardYN" ) );

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
