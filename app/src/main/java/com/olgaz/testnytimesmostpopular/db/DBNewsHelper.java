package com.olgaz.testnytimesmostpopular.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBNewsHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "news.db";
    private static final int DB_VERSION = 1;

    public DBNewsHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.i("MyInfo", "помошник создан");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBNewsContract.NewsEntry.COMMAND_CREATE);
        Log.i("MyInfo", "таблица создана");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBNewsContract.NewsEntry.COMMAND_DROP);
        onCreate(db);
    }
}
