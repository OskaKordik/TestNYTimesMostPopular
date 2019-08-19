package com.olgaz.testnytimesmostpopular.db;

import android.content.ContentValues;
import android.content.Context;

import com.olgaz.testnytimesmostpopular.model.News;

import java.util.List;

public class DBNewsHandler {
    private DBNewsHelper dbNewsHelper;

    public DBNewsHandler(Context context) {
        dbNewsHelper = new DBNewsHelper(context);
    }

    public void saveNewsData(News news){
        ContentValues newsValues = new ContentValues();
        newsValues.put(DBNewsContract.NewsEntry.COLUMN_URL, news.getUrl());
        newsValues.put(DBNewsContract.NewsEntry.COLUMN_SECTION, news.getSection());
        newsValues.put(DBNewsContract.NewsEntry.COLUMN_TITLE, news.getTitle());
        newsValues.put(DBNewsContract.NewsEntry.COLUMN_DESCRIPTION, news.getDescription());
        newsValues.put(DBNewsContract.NewsEntry.COLUMN_PUBLISHED_DATA, news.getPublishedDate());
        newsValues.put(DBNewsContract.NewsEntry.COLUMN_SOURCE, news.getSource());
        newsValues.put(DBNewsContract.NewsEntry.COLUMN_MEDIA_URL, news.getMedia().get(0).getMediaMetadata().get(1).getUrl());
        dbNewsHelper.insertData(newsValues);
    }

    public void deleteNewsData(String url){
        dbNewsHelper.deleteData(url);
    }

    public boolean isHasNews(String url) {
        return dbNewsHelper.isHasNewsInFavorites(url);
    }

    public List<News> loadNewsData(){
        return dbNewsHelper.loadNews();
    }
}
