package com.example.krafjufina.rssreader.model;


import android.content.ContentValues;
import android.database.Cursor;

import com.example.krafjufina.rssreader.database.DbContract;

import static com.example.krafjufina.rssreader.database.DbContract.getColumnLong;
import static com.example.krafjufina.rssreader.database.DbContract.getColumnString;

public class Post {

    public static final long NO_ID = -1;

    public long id;
    public long channelId;
    public String title;
    public String link;
    public String author;
    public String description;
    public long pubDate;

    public Post(){
        id = NO_ID;
    }

    public Post(Cursor cursor){
        this.id = getColumnLong(cursor, DbContract.PostColumns._ID);
        this.channelId = getColumnLong(cursor,DbContract.PostColumns.CHANNEL_ID);
        this.title = getColumnString(cursor, DbContract.PostColumns.TITLE);
        this.link = getColumnString(cursor, DbContract.PostColumns.LINK);
        this.author = getColumnString(cursor, DbContract.PostColumns.AUTHOR);
        this.description = getColumnString(cursor, DbContract.PostColumns.DESCRIPTION);
        this.pubDate = getColumnLong(cursor, DbContract.PostColumns.PUB_DATE);
    }

    public static ContentValues getContentValues(Post post){
        ContentValues cValues = new ContentValues();

        cValues.put(DbContract.PostColumns.CHANNEL_ID,post.channelId);
        cValues.put(DbContract.PostColumns.TITLE,post.title);
        cValues.put(DbContract.PostColumns.LINK,post.link);
        cValues.put(DbContract.PostColumns.AUTHOR,post.author);
        cValues.put(DbContract.PostColumns.DESCRIPTION,post.description);
        cValues.put(DbContract.PostColumns.PUB_DATE,post.pubDate);

        return cValues;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", channelId=" + channelId +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", pubDate=" + pubDate +
                '}';
    }
}
