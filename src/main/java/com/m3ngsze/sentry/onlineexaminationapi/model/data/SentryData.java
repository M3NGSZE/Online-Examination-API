package com.m3ngsze.sentry.onlineexaminationapi.model.data;

import java.util.ArrayList;
import java.util.List;

/*
*   list of M3n9sZe object
*/
public class SentryData extends ArrayList<M3n9sZe> {

    /*
    *   add M3n9sZe to SentryData
    */
    public void addM3n9se( M3n9sZe m3n9sZe ) {
        add( m3n9sZe );
    }

    /*
    *   remove M3n9se from SentryData
    */
    public void removeM3n9se( M3n9sZe m3n9sZe ) {
        remove( m3n9sZe );
    }

    /*
    *   convert SentryData to arraylist
    */
    public List< M3n9sZe > toArrayList() {
        return new ArrayList<>(this );
    }

}
