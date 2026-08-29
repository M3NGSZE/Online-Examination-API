package com.m3ngsze.sentry.onlineexaminationapi.model.data;

import java.util.LinkedHashMap;

/*
*   custom class use for request and response body
*/
public class M3n9sZe extends LinkedHashMap< String, Object > {

    /*
    *   set method use for setting data: new field, key and value
    */

    public M3n9sZe setString( String key, String value ) {
        put( key, value);
        return this;
    }

    public M3n9sZe addM3n9sZe( String key, M3n9sZe value ) {
        put( key, value );
        return this;
    }

    public M3n9sZe setSentryData( String key, SentryData value ) {
        put( key, value);
        return this;
    }

    /*
    *   get method use for getting data: field, key, and value
    */

    public String getString( String key ) {
        return containsKey( key ) ? ( String ) get( key ) : null;
    }

    public M3n9sZe getM3n9sZe( String key ) {
        return containsKey( key ) ? ( M3n9sZe ) get( key ) : null;
    }



}

