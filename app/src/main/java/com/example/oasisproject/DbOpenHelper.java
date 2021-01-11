package com.example.oasisproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// 데이터베이스 여는 helper
public class DbOpenHelper extends SQLiteOpenHelper{
    public static final int DB_VERSION = 2 ;
    public static final String DBFILE_CONTACT = "item.db" ;

    public DbOpenHelper(Context context){
        super(context, DBFILE_CONTACT, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ItemDataBases._CREATE_TBL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
