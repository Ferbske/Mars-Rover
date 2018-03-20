package com.rv.prog3tentmars;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DictionaryOpenhelper extends SQLiteOpenHelper {
    private final String TAG = this.getClass().getSimpleName();
    private static final String DATABASE_NAME = "rover_db";
    private static final int DATABASE_VERSION = 1;

    public DictionaryOpenhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, "DictionaryOpenhelper Constructor Called");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "DictionaryOpenhelper onCreate Called");
        String query = "CREATE TABLE rover_db (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "camId INTEGER  , " +
                "fullName TEXT, " +
                "URL TEXT" +
                "); ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "DictionaryOpenhelper onUpgrade Called");
        db.execSQL("DROP TABLE IF EXISTS rover_db;");
        onCreate(db);
    }
}