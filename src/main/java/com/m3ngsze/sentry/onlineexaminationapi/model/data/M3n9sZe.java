package com.m3ngsze.sentry.onlineexaminationapi.model.data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
*   custom class use for request and response body
*/
public class M3n9sZe extends LinkedHashMap< String, Object > {

    /*
    *   set method use for setting data: new field, key and value
    */

    public void setString( String key, String value ) {
        put( key, value);
    }

    public void addM3n9sZe( String key, M3n9sZe value ) {
        put( key, value );
    }

    public void setSentryData( String key, SentryData value ) {
        put( key, value);
    }

    /*
    *   get method use for getting data: field, key, and value
    */

    public String getString( String key ) {
        return containsKey( key ) ? ( String ) get( key ) : null;
    }

    public M3n9sZe getM3n9sZe( String key ) {
        return containsKey( key ) ? toM3n9sZe( get( key ) ) : null;
    }

    public SentryData getSentryData( String key ) {
        return containsKey( key) ? toSentryData( get( key ) ) : null;
    }

    /*
    *   convert object into M3n9sZe object
    */
    public M3n9sZe toM3n9sZe(Object object) {
        if (object instanceof M3n9sZe data) {
            return data;
        }

        if (object instanceof Map<?, ?> map) {
            M3n9sZe data = new M3n9sZe();

            for (Map.Entry<?, ?> entry : map.entrySet()) {
                data.put(
                        String.valueOf(entry.getKey()),
                        entry.getValue()
                );
            }

            return data;
        }

        throw new IllegalArgumentException(
                "Cannot convert " +
                        object.getClass().getName() +
                        " to M3n9sZe"
        );
    }

    /*
     *   convert list object into SentryData object
     */
    public SentryData toSentryData ( Object object ) {
        switch (object) {
            case null -> {
                return null;
            }
            case SentryData data -> {
                return data;
            }
            case List<?> list -> {

                SentryData data = new SentryData();

                for (Object item : list) {
                    data.add(toM3n9sZe(item));
                }

                return data;
            }
            default -> {
            }
        }

        throw new IllegalArgumentException(
                "Cannot convert " +
                        object.getClass().getName() +
                        " to SentryData"
        );
    }

}

