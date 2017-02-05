package com.example.krafjufina.rssreader.database;


import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DbContract {

    public static final String CONTENT_AUTHORITY = "com.example.kraftu.rssreader";

    public static final String TABLE_CHANNELS = "rssChannels";
    public static final String TABLE_POSTS= "rssPosts";

    public static final class ChannelColumns implements BaseColumns {
        public static final String NAME = "name";
        public static final String URL_RSS = "urlRss";
        public static final String TITLE = "title";
        public static final String URL_IMAGE = "urlImage";
        public static final String LINK = "link";
        public static final String DESCRIPTION = "description";
        public static final String PUB_DATE = "pubDate";


    }

    public static final class PostColumns implements BaseColumns{

        public static final String CHANNEL_ID = "channelId";
        public static final String TITLE = "title";
        public static final String LINK = "link";
        public static final String AUTHOR = "author";
        public static final String DESCRIPTION = "description";
        public static final String PUB_DATE = "pubDate";

    }

    public static final String DEFAULT_SORT_CHANNEL = String.format("%s ASC",
            ChannelColumns.NAME);

    public static final String DEFAULT_SORT_POST = String.format("%s DESC",
            ChannelColumns.PUB_DATE);

    public static final Uri CONTENT_CHANNEL_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_CHANNELS).build();

    public static final Uri CONTENT_POST_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_POSTS).build();

    /* Helpers to retrieve column values */
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }
    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt( cursor.getColumnIndex(columnName) );
    }
    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong( cursor.getColumnIndex(columnName) );
    }

    public static String getSelection(String name,String value){
        return String.format("%s = %s",name,value);
    }
    public static String getSelection(String name,long value){
        return getSelection(name,String.valueOf(value));
    }
}