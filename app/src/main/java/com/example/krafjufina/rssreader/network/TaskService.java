package com.example.krafjufina.rssreader.network;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.krafjufina.rssreader.tools.RLog;
import com.example.krafjufina.rssreader.database.DbContract;

public class TaskService extends IntentService {

    private static final String TAG = TaskService.class.getSimpleName();
    //Intent actions
    public static final String ACTION_INSERT_CHANNEL = TAG + ".INSERT_CHANNEL";
    public static final String ACTION_UPDATE_CHANNEL = TAG + ".UPDATE_CHANNEL";
    public static final String ACTION_DELETE_CHANNEL  = TAG + ".DELETE_CHANNEL";
    public static final String EXTRA_VALUES_CHANNEL = TAG + ".ContentValues_CHANNEL";


    public static final String ACTION_INSERT_POST = TAG + ".ACTION_INSERT_POST";
    public static final String ACTION_UPDATE_POST = TAG + ".ACTION_UPDATE_POST";
    public static final String ACTION_DELETE_POST  = TAG + ".ACTION_DELETE_POST";
    public static final String EXTRA_VALUES_POST = TAG + ".ContentValues_POST";
    public static final String EXTRA_VALUES_CHANNEL_ID = TAG + ".ContentValues_POST_CHANNEL_ID";


    public TaskService() {
        super(TAG);
    }

    public static void insertNewChannel(Context context, ContentValues values) {
        Intent intent = new Intent(context, TaskService.class);
        intent.setAction(ACTION_INSERT_CHANNEL);
        intent.putExtra(EXTRA_VALUES_CHANNEL, values);
        context.startService(intent);
    }
    public static void updateChannel(Context context, Uri uri, ContentValues values) {
        Intent intent = new Intent(context, TaskService.class);
        intent.setAction(ACTION_UPDATE_CHANNEL);
        intent.setData(uri);
        intent.putExtra(EXTRA_VALUES_CHANNEL, values);
        context.startService(intent);
    }
    public static void deleteChannel(Context context, Uri uri) {
        Intent intent = new Intent(context, TaskService.class);
        intent.setAction(ACTION_DELETE_CHANNEL);
        intent.setData(uri);
        context.startService(intent);
    }


    public static void insertNewPost(Context context, ContentValues values) {
        Intent intent = new Intent(context, TaskService.class);
        intent.setAction(ACTION_INSERT_POST);
        intent.putExtra(EXTRA_VALUES_POST, values);
        context.startService(intent);
    }
    public static void updatePost(Context context, Uri uri, ContentValues values) {
        Intent intent = new Intent(context, TaskService.class);
        intent.setAction(ACTION_UPDATE_POST);
        intent.setData(uri);
        intent.putExtra(EXTRA_VALUES_POST, values);
        context.startService(intent);
    }
    public static void deletePostForChannel(Context context,String channelId) {
        Intent intent = new Intent(context, TaskService.class);
        intent.putExtra(EXTRA_VALUES_CHANNEL_ID,channelId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ACTION_INSERT_CHANNEL.equals(intent.getAction())) {
            ContentValues values = intent.getParcelableExtra(EXTRA_VALUES_CHANNEL);
            performInsertChannel(values);
        } else if (ACTION_UPDATE_CHANNEL.equals(intent.getAction())) {
            ContentValues values = intent.getParcelableExtra(EXTRA_VALUES_CHANNEL);
            performUpdateChannel(intent.getData(),values);
        } else if (ACTION_DELETE_CHANNEL.equals(intent.getAction())) {

        }else if (ACTION_INSERT_POST.equals(intent.getAction())) {
            ContentValues values = intent.getParcelableExtra(EXTRA_VALUES_POST);
            performInsertPost(values);
        } else if (ACTION_UPDATE_POST.equals(intent.getAction())) {
            ContentValues values = intent.getParcelableExtra(EXTRA_VALUES_POST);

        } else if (ACTION_DELETE_POST.equals(intent.getAction())) {
            String channelId = intent.getStringExtra(EXTRA_VALUES_CHANNEL_ID);
            deletePostForId(channelId);
        }
    }

    private Uri performInsertChannel(ContentValues values) {
        Uri uri = getContentResolver().insert(DbContract.CONTENT_CHANNEL_URI, values);
        if (uri!= null) {
            RLog.d(TAG, "Inserted new channel");
        } else {
            RLog.e(TAG, "Error inserting new channel");
        }
        return uri;
    }

    private void performUpdateChannel(Uri uri,ContentValues values) {
        int count = getContentResolver().update(uri, values,null,null);
        if (count > 0) {
            RLog.d(TAG, "Update new channel");
        } else {
            RLog.e(TAG, "Error update new channel");
        }
    }

    private Uri performInsertPost(ContentValues values) {
        Uri uri = getContentResolver().insert(DbContract.CONTENT_POST_URI, values);
        if (uri!= null) {
            RLog.d(TAG, "Inserted new post");
        } else {
            RLog.e(TAG, "Error inserting new post");
        }
        return uri;
    }

    private void deletePostForId(String channelId) {
        int count = getContentResolver().delete(DbContract.CONTENT_CHANNEL_URI
                ,DbContract.getSelection(DbContract.PostColumns.CHANNEL_ID,channelId)
                ,null);
        if (count > 0) {
            RLog.d(TAG, "deletePostForId");
        } else {
            RLog.e(TAG, "Error deletePostForId");
        }
    }


}
