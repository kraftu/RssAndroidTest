package com.example.krafjufina.rssreader.model;


import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.example.krafjufina.rssreader.database.DbContract;

import static com.example.krafjufina.rssreader.database.DbContract.getColumnLong;
import static com.example.krafjufina.rssreader.database.DbContract.getColumnString;

public class Channel {

    public static final long NO_ID = -1;

    public long id;
    public String name;
    public String urlRss;
    public String title;
    public String urlImage;
    public String link;
    public String description;
    public long pubDate;

    public Channel() {
        this.id = NO_ID;
    }
    public Channel(String name, String urlRss) {
        this.id = NO_ID;
        this.name = name;
        this.urlRss = urlRss;
    }

    public Channel(Cursor cursor){
        this.id = getColumnLong(cursor, DbContract.ChannelColumns._ID);
        this.name = getColumnString(cursor,DbContract.ChannelColumns.NAME);
        this.urlRss = getColumnString(cursor, DbContract.ChannelColumns.URL_RSS);
        this.title = getColumnString(cursor, DbContract.ChannelColumns.TITLE);
        this.urlImage = getColumnString(cursor, DbContract.ChannelColumns.URL_IMAGE);
        this.link = getColumnString(cursor, DbContract.ChannelColumns.LINK);
        this.description = getColumnString(cursor, DbContract.ChannelColumns.DESCRIPTION);
        this.pubDate= getColumnLong(cursor, DbContract.ChannelColumns.PUB_DATE);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", urlRss='" + urlRss + '\'' +
                ", title='" + title + '\'' +
                ", urlImage='" + urlImage + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", pubDate=" + pubDate +
                '}';
    }

    public static ContentValues getContentValues(Channel channel){
        ContentValues cValues = new ContentValues();
        if(!TextUtils.isEmpty(channel.name))
            cValues.put(DbContract.ChannelColumns.NAME,channel.name);
        if(!TextUtils.isEmpty(channel.urlRss))
            cValues.put(DbContract.ChannelColumns.URL_RSS,channel.urlRss);
        if(!TextUtils.isEmpty(channel.title))
            cValues.put(DbContract.ChannelColumns.TITLE,channel.title);
        if(!TextUtils.isEmpty(channel.urlImage))
            cValues.put(DbContract.ChannelColumns.URL_IMAGE,channel.urlImage);
        if(!TextUtils.isEmpty(channel.link))
            cValues.put(DbContract.ChannelColumns.LINK,channel.link);
        if(!TextUtils.isEmpty(channel.description))
            cValues.put(DbContract.ChannelColumns.DESCRIPTION,channel.description);
        cValues.put(DbContract.ChannelColumns.PUB_DATE,channel.pubDate);
        return cValues;
    }

}
