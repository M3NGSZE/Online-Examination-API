package com.m3ngsze.sentry.onlineexaminationapi.mapper;

import com.m3ngsze.sentry.onlineexaminationapi.model.data.M3n9sZe;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CardRestrictionMapper {

    M3n9sZe retrieveCardRestrictionByCardNumberAndSchemeId (M3n9sZe param );

    void insertRestrictionCard ( M3n9sZe param );

    void updateRemoveRestrictionCard ( M3n9sZe param );

    void updateReapplyRestrictionCard ( M3n9sZe param );

}
