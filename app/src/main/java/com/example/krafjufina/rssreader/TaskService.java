package com.example.krafjufina.rssreader;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Krafjufina on 05.02.2017.
 */

public class TaskService extends IntentService {

    private static final String TAG = TaskService.class.getSimpleName();
    //Intent actions
    public static final String ACTION_INSERT_CHANNEL = TAG + ".INSERT_CHANNEL";
    public static final String ACTION_UPDATE_CHANNEL = TAG + ".UPDATE_CHANNEL";
    public static final String ACTION_DELETE_CHANNEL  = TAG + ".DELETE_CHANNEL";
    public static final String EXTRA_VALUES_CHANNEL = TAG + ".ContentValues_CHANNEL";

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


    @Override
    protected void onHandleIntent(Intent intent) {
        if (ACTION_INSERT_CHANNEL.equals(intent.getAction())) {

        } else if (ACTION_UPDATE_CHANNEL.equals(intent.getAction())) {

        } else if (ACTION_DELETE_CHANNEL.equals(intent.getAction())) {

        }
    }
}
