package com.example.krafjufina.rssreader.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Krafjufina on 05.02.2017.
 */

public class DataUntil {
    public static final SimpleDateFormat sDataFormatXml = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
    public static final SimpleDateFormat sDataFormatAdapter = new SimpleDateFormat("HH:mm dd.MMM.yyyy");
    public static long getTimeInMs(String pubDate){
        try {
            return sDataFormatXml.parse(pubDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getDateForAdapter(long timeMs){
        if(timeMs == 0) return "";

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMs);
        return sDataFormatAdapter.format(calendar.getTime());
    }
}
