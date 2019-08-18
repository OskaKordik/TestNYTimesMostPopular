package com.olgaz.testnytimesmostpopular.db;


public class DBNewsContract {
    public static final class NewsEntry {
        public static final String TABLE_NAME = "news";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_SECTION = "section";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PUBLISHED_DATA = "published_date";
        public static final String COLUMN_SOURCE = "source";
        public static final String COLUMN_MEDIA_URL = "media_url";

        public static final String COMMAND_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_URL + " TEXT, "
                + COLUMN_SECTION + " TEXT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_PUBLISHED_DATA + " TEXT, "
                + COLUMN_SOURCE + " TEXT, "
                + COLUMN_MEDIA_URL + ");";
        public static final String COMMAND_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
