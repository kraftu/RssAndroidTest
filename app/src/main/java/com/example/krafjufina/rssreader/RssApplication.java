package com.example.krafjufina.rssreader;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.krafjufina.rssreader.model.Channel;

/**
 * Created by Krafjufina on 06.02.2017.
 */

public class RssApplication extends Application {
    public static final String SHARED_CONFIG = "SHARED_CONFIG";
    public static final String SHARED_FIRST_LOAD = "SHARED_CONFIG";

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
        /*String[] names = new String[]{
                "1)Habr за сутки",
                "2)Habr за неделю",
                "3)Habr за месяц",
                "2)Habr за за время"};
        String[] rssUrl = new String[]{"https://habrahabr.ru/rss/interesting/top/",
                "https://habrahabr.ru/rss/interesting/top/",
                "https://habrahabr.ru/rss/interesting/top/",
                "https://habrahabr.ru/rss/interesting/top/",
        };*/

    }
}
