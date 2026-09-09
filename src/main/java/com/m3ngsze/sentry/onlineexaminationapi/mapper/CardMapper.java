package com.m3ngsze.sentry.onlineexaminationapi.mapper;

import com.m3ngsze.sentry.onlineexaminationapi.model.data.M3n9sZe;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CardMapper {

    M3n9sZe retrieveCardInfoByCardId ( M3n9sZe param );
}
