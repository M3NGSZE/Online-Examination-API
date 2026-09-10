package com.m3ngsze.sentry.onlineexaminationapi.model.mapper;

import com.m3ngsze.sentry.onlineexaminationapi.model.data.M3n9sZe;
import com.m3ngsze.sentry.onlineexaminationapi.model.data.SentryData;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;

import java.util.List;

public class M3n9seMapper {

    public static M3n9sZe toM3n9sZe( Tuple tuple ) {

        M3n9sZe data = new M3n9sZe();

        for (TupleElement<?> element : tuple.getElements()) {

            String key = element.getAlias();

            Object value = tuple.get(key);

            data.setObject(key, value);
        }

        return data;
    }

    public static SentryData toSentryData( List<Tuple> tuples ) {

        SentryData data = new SentryData();

        for (Tuple tuple : tuples) {
            data.add(
                    toM3n9sZe(tuple)
            );
        }

        return data;
    }

}
