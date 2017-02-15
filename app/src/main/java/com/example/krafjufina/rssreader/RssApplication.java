package com.example.krafjufina.rssreader;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.krafjufina.rssreader.model.Channel;
import com.example.krafjufina.rssreader.network.TaskService;
import com.example.krafjufina.rssreader.tools.RLog;

/**
 * Created by Krafjufina on 06.02.2017.
 */

public class RssApplication extends Application {
    public static final String SHARED_CONFIG = "SHARED_CONFIG";
    public static final String SHARED_FIRST_LOAD = "SHARED_FIRST_LOAD";
    public static final String TAG = "RssApplication";


    public static final String[] names = new String[]{
            "Habr top за сутки",
            "Habr top за неделю",
            "Habr top за месяц",
            "Habr top за за время"};

    public static final String[] rssUrl = new String[]{"https://habrahabr.ru/rss/best/",
            "https://habrahabr.ru/rss/best/weekly/",
            "https://habrahabr.ru/rss/best/monthly/",
            "https://habrahabr.ru/rss/best/alltime/",
    };


    @Override
    public void onCreate() {
        super.onCreate();
        if(isFirstLoad()) addDefaultData();
    }

    private boolean isFirstLoad(){
        SharedPreferences sPreferences = getSharedPreferences(SHARED_CONFIG,MODE_PRIVATE);
        boolean isFirstLoad =  sPreferences.getBoolean(SHARED_FIRST_LOAD,true);
        sPreferences.edit().putBoolean(SHARED_FIRST_LOAD,false).commit();
        return isFirstLoad;
    }

    private void addDefaultData(){
        RLog.d(TAG,"addDefaultData");
        if(names.length == rssUrl.length){
            for(int i=0 ; i < names.length ; i++){
                Channel channel = new Channel(names[i],rssUrl[i]);
                TaskService.insertNewChannel(this,Channel.getContentValues(channel));
            }
        }

    }
}
