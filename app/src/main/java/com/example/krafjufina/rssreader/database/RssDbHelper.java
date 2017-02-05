package com.example.krafjufina.rssreader.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.krafjufina.rssreader.tools.RLog;

public class RssDbHelper extends SQLiteOpenHelper {
    public static final String TAG = "RssDbHelper";

    private static final String DATABASE_NAME = "rssReader.db";
    private static final int DATABASE_VERSION = 4;

    private static final String SQL_CREATE_TABLE_CHANNEL = String.format("create table %s "
                    + "(%s integer primary key autoincrement, " + //ID
                    "%s text, " + //NAME
                    "%s text, " + //RSS_URL
                    "%s text, " + //TITLE
                    "%s text, " + //URI_IMAGE
                    "%s text, " + //LINK
                    "%s text, " + //DESCRIPTION
                    "%s integer)", //PUB_DATE
            DbContract.TABLE_CHANNELS,
            DbContract.ChannelColumns._ID,
            DbContract.ChannelColumns.NAME,
            DbContract.ChannelColumns.URL_RSS,
            DbContract.ChannelColumns.TITLE,
            DbContract.ChannelColumns.URL_IMAGE,
            DbContract.ChannelColumns.LINK,
            DbContract.ChannelColumns.DESCRIPTION,
            DbContract.ChannelColumns.PUB_DATE
    );

    private static final String SQL_CREATE_TABLE_POSTS = String.format("create table %s "
                    + "(%s integer primary key autoincrement, " + //ID
                    "%s integer, " + //PUB_DATE
                    "%s text, " + //TITLE
                    "%s text, " + //LINK
                    "%s text, " + //AUTHOR
                    "%s text, " + //DESCRIPTION
                    "%s integer, " + //CHANNEL_ID
                    "FOREIGN KEY(%s) REFERENCES %s(%s)) ",
            DbContract.TABLE_POSTS,
            DbContract.PostColumns._ID,
            DbContract.PostColumns.PUB_DATE,
            DbContract.PostColumns.TITLE,
            DbContract.PostColumns.LINK,
            DbContract.PostColumns.AUTHOR,
            DbContract.PostColumns.DESCRIPTION,
            DbContract.PostColumns.CHANNEL_ID,
            DbContract.PostColumns.CHANNEL_ID,
            DbContract.TABLE_CHANNELS,
            DbContract.ChannelColumns._ID
    );

    public RssDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        RLog.d(TAG,"-create database:");

        RLog.d(TAG, SQL_CREATE_TABLE_CHANNEL);
        db.execSQL(SQL_CREATE_TABLE_CHANNEL);

        RLog.d(TAG, SQL_CREATE_TABLE_POSTS);
        db.execSQL(SQL_CREATE_TABLE_POSTS);

        RLog.d(TAG,"------------------");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        RLog.d(TAG,"onUpgrade");
        db.execSQL("drop table if exists " + DbContract.TABLE_CHANNELS);
        db.execSQL("drop table if exists " + DbContract.TABLE_POSTS);

        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }
}
