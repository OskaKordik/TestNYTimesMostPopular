package com.olgaz.testnytimesmostpopular.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.olgaz.testnytimesmostpopular.model.Media;
import com.olgaz.testnytimesmostpopular.model.MediaMetadata;
import com.olgaz.testnytimesmostpopular.model.News;

import java.util.ArrayList;
import java.util.List;

public class DBNewsHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "news.db";
    private static final int DB_VERSION = 1;

    public DBNewsHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBNewsContract.NewsEntry.COMMAND_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBNewsContract.NewsEntry.COMMAND_DROP);
        onCreate(db);
    }

    void insertData(ContentValues newsValues) {
        new InsertDataTask().execute(newsValues);
    }

    void deleteData(String url) {
        new DeleteDataTask().execute(url);
    }

    boolean isHasNewsInFavorites(String url) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor mCursor = db.rawQuery(DBNewsContract.NewsEntry.COMMAND_EXISTS_NEWS, new String[]{url});

            if  (mCursor.moveToFirst()) {
                mCursor.close();
                return true;
            } else {
                return false;
            }

        } catch (SQLiteException e) {
            Log.i("MyInfo", "Error searching in favorites");
            return false;
        }
    }

    List<News> loadNews() {
        List<News> news = new ArrayList<>();
        try {
            SQLiteDatabase db =  DBNewsHelper.this.getWritableDatabase();
            Cursor cursor = db.query(DBNewsContract.NewsEntry.TABLE_NAME, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                String url = cursor.getString(cursor.getColumnIndex(DBNewsContract.NewsEntry.COLUMN_URL));
                String section = cursor.getString(cursor.getColumnIndex(DBNewsContract.NewsEntry.COLUMN_SECTION));
                String title = cursor.getString(cursor.getColumnIndex(DBNewsContract.NewsEntry.COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(DBNewsContract.NewsEntry.COLUMN_DESCRIPTION));
                String publishedDate = cursor.getString(cursor.getColumnIndex(DBNewsContract.NewsEntry.COLUMN_PUBLISHED_DATA));
                String source = cursor.getString(cursor.getColumnIndex(DBNewsContract.NewsEntry.COLUMN_SOURCE));
                String mediaUrl = cursor.getString(cursor.getColumnIndex(DBNewsContract.NewsEntry.COLUMN_MEDIA_URL));

                MediaMetadata mediaMetadata = new MediaMetadata();
                mediaMetadata.setUrl(mediaUrl);

                ArrayList<MediaMetadata> mediaMetadataList = new ArrayList<>();
                mediaMetadataList.add(new MediaMetadata());
                mediaMetadataList.add(mediaMetadata);

                Media media = new Media();
                media.setMediaMetadata(mediaMetadataList);

                List<Media> mediaList = new ArrayList<>();
                mediaList.add(media);

                news.add(new News(url, section, title, description, publishedDate, source, mediaList));
            }
            cursor.close();
        } catch (SQLiteException e) {
            Log.i("MyInfo", e.getMessage());
        }
        return news;
    }

    private class DeleteDataTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String url = strings[0];
            try {
                SQLiteDatabase db =  DBNewsHelper.this.getWritableDatabase();
                db.delete(DBNewsContract.NewsEntry.TABLE_NAME, DBNewsContract.NewsEntry.COLUMN_URL + " =? ", new String[]{url});
                db.close();
            } catch (SQLiteException e) {
                Log.i("MyInfo", "Error delete from favorites");
            }
            return null;
        }
    }

    private class InsertDataTask extends AsyncTask<ContentValues, Void, Void> {
        ContentValues value;

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            value = contentValues[0];
            try {
                SQLiteDatabase db =  DBNewsHelper.this.getWritableDatabase();
                db.insert(DBNewsContract.NewsEntry.TABLE_NAME, null, value);
                db.close();
            } catch (SQLiteException e) {
                Log.i("MyInfo", "Error adding to favorites");
            }
            return null;
        }
    }
}
