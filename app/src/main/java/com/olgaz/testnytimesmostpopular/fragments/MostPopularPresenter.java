package com.olgaz.testnytimesmostpopular.fragments;

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
import com.olgaz.testnytimesmostpopular.db.DBNewsHandler;
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

public class MostPopularPresenter {
    private MostPopularView view;
    private Disposable disposable;
    private SQLiteDatabase database;
    private SQLiteOpenHelper dbHelper;
    private DBNewsHandler dbNewsHandler;
    private Cursor cursor;
    private static List<News> newsFromNetwork = new ArrayList<>();

    public MostPopularPresenter(MostPopularView view, Context context) {
        this.view = view;
        dbHelper = new DBNewsHelper(context);
        dbNewsHandler = new DBNewsHandler(context);
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            this.view.showInfo("Database unavailable");
        }
    }

    public void loadData(String tabArticle) {
        ApiClient apiClient = ApiClient.getInstance();
        ApiService apiService = apiClient.getApiService();
        disposable = apiService.getResponseNews(tabArticle, ApiConstants.PERIOD, ApiConstants.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NewsResults>() {
                    @Override
                    public void accept(NewsResults newsResults) throws Exception {
                        MostPopularPresenter.newsFromNetwork.addAll(newsResults.getResults());
                        view.showData(newsFromNetwork);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.showInfo(throwable.getMessage());
                    }
                });
    }

    public void loadDataFromDB() {
        List<News> newsFromDB = new ArrayList<>();

        try {
            cursor = database.query(DBNewsContract.NewsEntry.TABLE_NAME, null, null, null, null, null, null);
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

                newsFromDB.add(new News(url, section, title, description, publishedDate, source, mediaList));
            }
            cursor.close();
        } catch (SQLiteException e) {
            this.view.showInfo("Failed to read from DB");
            Log.i("MyInfo", e.getMessage());
        }

        if (newsFromDB.size() > 0) view.showData(newsFromDB);
    }

    boolean isHasNewsInDB(String url) {
        return dbNewsHandler.isHasNews(url);
    }

    public void insertToFavorites(String url) {
        if (!newsFromNetwork.isEmpty() && !isHasNewsInDB(url)) {
            News newNews = null;
            for (News news: newsFromNetwork) {
                if (news.getUrl().equals(url)) newNews = news;
            }
            insertNewsToDB(newNews);
        } else {
            Log.i("MyInfo", "newsFromNetwork isEmpty OR news isHasNews!");
        }
    }

    public void deleteNewsFromFavorites(String url) {
        if (isHasNewsInDB(url)) {
            dbNewsHandler.deleteNewsData(url);
            view.showInfo("Delete from favorites");
        }
    }

    private void insertNewsToDB(News news) {
        if (news != null) {
            dbNewsHandler.saveNewsData(news);
            view.showInfo("Added to favorites");
        } else {
            Log.i("MyInfo", "Empty object passed!");
        }
    }

    public void disposeDisposable() {
        if (disposable != null) disposable.dispose();
    }

    public void closeDB() {
        database.close();
    }

}
