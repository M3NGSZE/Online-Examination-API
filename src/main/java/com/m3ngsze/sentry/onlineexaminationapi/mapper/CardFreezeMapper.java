package com.m3ngsze.sentry.onlineexaminationapi.mapper;

import com.m3ngsze.sentry.onlineexaminationapi.model.data.M3n9sZe;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CardFreezeMapper {

    M3n9sZe retrieveCardFreezeStatusByCardId (M3n9sZe param );

}
