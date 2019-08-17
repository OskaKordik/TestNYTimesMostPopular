package com.olgaz.testnytimesmostpopular.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.olgaz.testnytimesmostpopular.api.ApiClient;
import com.olgaz.testnytimesmostpopular.api.ApiConstants;
import com.olgaz.testnytimesmostpopular.api.ApiService;
import com.olgaz.testnytimesmostpopular.db.DBNewsContract;
import com.olgaz.testnytimesmostpopular.db.DBNewsHelper;
import com.olgaz.testnytimesmostpopular.model.Media;
import com.olgaz.testnytimesmostpopular.model.MediaMetadata;
import com.olgaz.testnytimesmostpopular.model.News;
import com.olgaz.testnytimesmostpopular.model.NewsResults;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

class MostPopularPresenter {
    private MostPopularView view;
    private Disposable disposable;
    private SQLiteDatabase database;
    private SQLiteOpenHelper dbHelper;
    private Cursor cursor;

    public MostPopularPresenter(MostPopularView view, Context context) {
        this.view = view;
        dbHelper = new DBNewsHelper(context);
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            this.view.showInfo("Ошибка БД");
        }
    }

    void loadData(String tabArticle) {
        ApiClient apiClient = ApiClient.getInstance();
        ApiService apiService = apiClient.getApiService();
        disposable = apiService.getResponseNews(tabArticle, ApiConstants.PERIOD, ApiConstants.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NewsResults>() {
                    @Override
                    public void accept(NewsResults newsResults) throws Exception {
                        view.showData(newsResults.getResults());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.showInfo(throwable.getMessage());
                    }
                });
    }

    void loadDataFromDB() {
        List<News> newsFromDB = new ArrayList<>();

        try {
            cursor = database.query(DBNewsContract.NewsEntry.TABLE_NAME, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(DBNewsContract.NewsEntry._ID));
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

                newsFromDB.add(new News((long)id, url, section, title, description, publishedDate, source, mediaList));
            }
            cursor.close();
        } catch (SQLiteException e) {
            this.view.showInfo("Не удалось прочитать данные");
            Log.i("MyInfo", e.getMessage());
        }

        if (newsFromDB.size() > 0) view.showData(newsFromDB);
    }

    void insertNewsToDB(News news) {

        if (news != null) {
            ContentValues newsValues = new ContentValues();
            newsValues.put(DBNewsContract.NewsEntry.COLUMN_URL, news.getUrl());
            newsValues.put(DBNewsContract.NewsEntry.COLUMN_SECTION, news.getSection());
            newsValues.put(DBNewsContract.NewsEntry.COLUMN_TITLE, news.getTitle());
            newsValues.put(DBNewsContract.NewsEntry.COLUMN_DESCRIPTION, news.getDescription());
            newsValues.put(DBNewsContract.NewsEntry.COLUMN_PUBLISHED_DATA, news.getPublishedDate());
            newsValues.put(DBNewsContract.NewsEntry.COLUMN_SOURCE, news.getSource());
            newsValues.put(DBNewsContract.NewsEntry.COLUMN_MEDIA_URL, news.getMedia().get(0).getMediaMetadata().get(1).getUrl());

            try {
                // Insert the new entry into the DB.
                database.insert(DBNewsContract.NewsEntry.TABLE_NAME, null, newsValues);
                view.showInfo("Добавлено в избранное");
            } catch (SQLiteException e) {
                this.view.showInfo("Не удалось добавить данные");
            }

        } else {
            view.showInfo("передан пустой объект!");
        }
    }

    void disposeDisposable() {
        if (disposable != null) disposable.dispose();
    }

    void closeDB() {
        database.close();
    }

}
