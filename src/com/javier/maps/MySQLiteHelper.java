/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javier.maps;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *
*   .title(title.getText().toString())
    .snippet(snippet.getText().toString())
    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
    .position(latlng)
 */
public class MySQLiteHelper extends SQLiteOpenHelper{
    public static final String TABLE_LOCATIONS = "locations";
    public static final String ID_COL = "loc_id";
    public static final String TITLE = "loc_title";
    public static final String SNIPPET = "loc_snippet";
    public static final String POSISION = "loc_position";

    
    private static final int DB_VERSION = 1;                            // version
    private static final String DB_NAME = "mylocations.db";               // name of the DB
    private static final String DATABASE_CREATE = "create table "       // Database creation sql statement
      + TABLE_LOCATIONS + "(" + ID_COL
      + " integer primary key autoincrement, "
      + " text, "+ TITLE
      + " text, " + SNIPPET
      + " text, " + POSISION
      + " text);";
    
    public MySQLiteHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
        
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_LOCATIONS);
        onCreate(db);
    }
    
}
