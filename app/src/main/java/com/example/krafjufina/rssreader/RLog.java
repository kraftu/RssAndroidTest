package com.example.krafjufina.rssreader;

import android.util.Log;

import com.example.krafjufina.rssreader.BuildConfig;

/**
 * Created by Krafjufina on 05.02.2017.
 */

public class RLog {
    public static boolean DEBUG = BuildConfig.DEBUG;

    public static final void d(String tag,String message){
        if(DEBUG) Log.d(tag,message);
    }
    public static final void e(String tag,String message){
        if(DEBUG) Log.e(tag,message);
    }
}
