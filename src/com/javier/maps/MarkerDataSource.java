/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javier.maps;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import android.util.Log;

/**
 *
 * @author javierAle
 */
public class MarkerDataSource {
    
    private SQLiteDatabase db;
    private MySQLiteHelper dbhelper;
//    String id, String title, String snippet, String position
    private String[] allColumns = {  
        MySQLiteHelper.TITLE, 
        MySQLiteHelper.SNIPPET, 
        MySQLiteHelper.POSISION };
    
    public MarkerDataSource(Context context){
        dbhelper = new MySQLiteHelper(context);
    }
    public void open() throws SQLException{
        db = dbhelper.getWritableDatabase();
    }
    
    public void close(){
        dbhelper.close();
    }
    
    public void addMarker(MyMarker m){
        ContentValues v = new ContentValues();

        v.put(MySQLiteHelper.TITLE, m.getTitle());
        v.put(MySQLiteHelper.SNIPPET, m.getSnippet());       
        v.put(MySQLiteHelper.POSISION, m.getPosition());

        
        long insertId = db.insert(MySQLiteHelper.TABLE_LOCATIONS, null,
        v);       
            Cursor cursor = db.query(MySQLiteHelper.TABLE_LOCATIONS,
        allColumns, MySQLiteHelper.ID_COL + " = " + insertId, null,
        null, null, null);
            
           cursor.moveToFirst();
           MyMarker mm = cursorToMarker(cursor);
           cursor.close();
           

    }
    public List<MyMarker> getMyMarkers(){
        List<MyMarker> markers = new ArrayList<MyMarker>();

        
         Cursor cursor = db.query(MySQLiteHelper.TABLE_LOCATIONS, 
                 allColumns, null, null, null, null, null);

        
        
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {            
            MyMarker mm = cursorToMarker(cursor);
            markers.add(mm);
            cursor.moveToNext();
        }
        cursor.close();
            
        return markers;
    }

    private MyMarker cursorToMarker(Cursor cursor) {
        MyMarker mm = new MyMarker();
//        mm.setId(cursor.getLong(0));
        mm.setTitle(cursor.getString(0));
        mm.setSnippet(cursor.getString(1));
        mm.setPosition(cursor.getString(2));
        return mm;
        
        
    }
    
}
