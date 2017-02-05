package com.example.krafjufina.rssreader.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.krafjufina.rssreader.RLog;

/**
 * Created by Krafjufina on 05.02.2017.
 */

public class RssProvider extends ContentProvider {
    private static final String TAG = "RssProvider";

    public static final int RSS_SOURCES = 100;
    public static final int RSS_SOURCES_WITH_ID = 101;
    public static final int RSS_POSTS = 102;
    public static final int RSS_POSTS_WITH_ID = 103;

    private RssDbHelper mRssDbHelper;

    private static final UriMatcher sUriMather = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        sUriMather.addURI(DbContract.CONTENT_AUTHORITY,DbContract.TABLE_CHANNELS,
                RSS_SOURCES);
        sUriMather.addURI(DbContract.CONTENT_AUTHORITY,DbContract.TABLE_CHANNELS + "/#",
                RSS_SOURCES_WITH_ID);

        sUriMather.addURI(DbContract.CONTENT_AUTHORITY,DbContract.TABLE_POSTS,
                RSS_POSTS);
        sUriMather.addURI(DbContract.CONTENT_AUTHORITY,DbContract.TABLE_POSTS + "/#",
                RSS_POSTS_WITH_ID);
    }


    @Override
    public boolean onCreate() {
        mRssDbHelper = new RssDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        RLog.d(TAG,"query, " + uri.toString());

        SQLiteDatabase sqLiteDatabase = mRssDbHelper.getWritableDatabase();

        String tableName = null;
        switch (sUriMather.match(uri)){
            case RSS_SOURCES:{
                RLog.d(TAG,"RSS_SOURCES");
                tableName = DbContract.TABLE_CHANNELS;
                break;
            }


            case RSS_SOURCES_WITH_ID:{
                String id = uri.getLastPathSegment();
                RLog.d(TAG,"RSS_SOURCES_WITH_ID :" + id);
                tableName = DbContract.TABLE_CHANNELS;
                selection = getSelectionWithId(selection,DbContract.ChannelColumns._ID,id);
                break;
            }

            case RSS_POSTS:{
                RLog.d(TAG,"RSS_POSTS");
                tableName = DbContract.TABLE_POSTS;
                break;
            }

            case RSS_POSTS_WITH_ID:{
                String id = uri.getLastPathSegment();
                tableName = DbContract.TABLE_POSTS;
                RLog.d(TAG,"RSS_POSTS_WITH_ID :" + id);
                selection = getSelectionWithId(selection,DbContract.PostColumns._ID,id);
                break;
            }

            default:{
                throw new IllegalArgumentException("Illegal delete URI");
            }

        }

        Cursor cursor = sqLiteDatabase.query(tableName,projection,selection,selectionArgs,sortOrder,null,null);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        RLog.d(TAG,"insert, "+uri.toString());

        SQLiteDatabase sqLiteDatabase = mRssDbHelper.getWritableDatabase();
        Uri uriResult = null;
        long rowId = -1;
        String tableName = null;
        Uri baseUri = null;
        if(sUriMather.match(uri) == RSS_SOURCES){
            tableName = DbContract.TABLE_CHANNELS;
            baseUri = DbContract.CONTENT_RSS_SOURCE_URI;

        }else if(sUriMather.match(uri) == RSS_POSTS){
            tableName = DbContract.TABLE_POSTS;
            baseUri = DbContract.CONTENT_RSS_POST_URI;
        }else{
            throw new IllegalArgumentException("Illegal insert URI");
        }

        rowId = sqLiteDatabase.insert(tableName,null,values);
        uriResult = ContentUris.withAppendedId(baseUri,rowId);
        getContext().getContentResolver().notifyChange(uriResult,null);
        return uriResult;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        RLog.d(TAG,"delete, "+uri.toString());

        SQLiteDatabase sqLiteDatabase = mRssDbHelper.getWritableDatabase();
        String tableName = null;
        switch (sUriMather.match(uri)){
            case RSS_SOURCES_WITH_ID:{
                String id = uri.getLastPathSegment();
                selection = getSelectionWithId(selection,DbContract.ChannelColumns._ID,id);
            }
            case RSS_SOURCES:{
                tableName = DbContract.TABLE_CHANNELS;
                break;
            }
            case RSS_POSTS_WITH_ID:{
                String id = uri.getLastPathSegment();
                selection = getSelectionWithId(selection,DbContract.PostColumns._ID,id);
            }
            case RSS_POSTS:{
                tableName = DbContract.TABLE_POSTS;
                break;
            }
            default:{
                throw new IllegalArgumentException("Illegal delete URI");
            }
        }

        int count = sqLiteDatabase.delete(tableName,selection,selectionArgs);
        if(count > 0){
            RLog.d(TAG,"delete, count " + count);
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        RLog.d(TAG,"update, "+uri.toString());
        SQLiteDatabase sqLiteDatabase = mRssDbHelper.getWritableDatabase();

        String tableName = null;
        switch (sUriMather.match(uri)){
            case RSS_SOURCES_WITH_ID:{
                String id = uri.getLastPathSegment();
                selection = getSelectionWithId(selection,DbContract.ChannelColumns._ID,id);
            }
            case RSS_SOURCES:{
                tableName = DbContract.TABLE_CHANNELS;
                break;
            }

            case RSS_POSTS_WITH_ID:{
                String id = uri.getLastPathSegment();
                selection = getSelectionWithId(selection,DbContract.PostColumns._ID,id);

            }
            case RSS_POSTS:{
                tableName = DbContract.TABLE_POSTS;
                break;
            }
            default:{
                throw new IllegalArgumentException("Illegal update URI");
            }
        }

        int count = sqLiteDatabase.update(tableName,values,selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);

        return count;
    }

    private String getSelectionWithId(String selection,String column,String id){
        if(TextUtils.isEmpty(selection)){
            selection = column + " = " + id;
        }else{
            selection = selection + " AND " + column + " = " + id;
        }
        return selection;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        RLog.d(TAG,"bulkInsert, "+uri.toString());

        SQLiteDatabase sqLiteDatabase = mRssDbHelper.getWritableDatabase();
        Uri uriResult = null;
        long rowId = -1;
        String tableName = null;

        if(sUriMather.match(uri) == RSS_SOURCES){
            tableName = DbContract.TABLE_CHANNELS;

        }else if(sUriMather.match(uri) == RSS_POSTS){
            tableName = DbContract.TABLE_POSTS;
        }

        for(ContentValues cv : values) {
            rowId = sqLiteDatabase.insert(tableName, null, cv);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return values.length;
    }
}