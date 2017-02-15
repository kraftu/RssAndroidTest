package com.example.krafjufina.rssreader.network;

import android.content.Context;
import android.os.AsyncTask;


import com.example.krafjufina.rssreader.tools.DataUntil;
import com.example.krafjufina.rssreader.model.Channel;
import com.example.krafjufina.rssreader.model.Post;
import com.example.krafjufina.rssreader.tools.Http;
import com.example.krafjufina.rssreader.tools.RLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

abstract public class RssParserTask extends AsyncTask<String, Integer, String> {

    public static final String TAG = "RssParserTask";
    private Channel mChannel;
    private List<Post> mPosts = new ArrayList<>();
    private String mUrl;
    private String mError;

    public RssParserTask(String url) {
        this.mUrl = url;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            RLog.d(TAG,"StartLoad:"+mUrl);
            if(isCancelled()) throw new IOException("CancelTask");
            Document rssDocument = Jsoup.connect(mUrl).ignoreContentType(true).parser(Parser.xmlParser()).get();

            if(!isRssUrl(rssDocument)){
              throw new IOException("We could not find a material Rss link:"+mUrl);
            }

            Elements mChannels = rssDocument.select("channel");
            Elements mItems = rssDocument.select("item");

            if(isCancelled()) throw new IOException("CancelTask");
            for(Element mEChanel : mChannels){
                mChannel = getChannel(mEChanel);
            }
            for(Element mItem : mItems){
                if(isCancelled()) throw new IOException("CancelTask");
                mPosts.add(getPost(mItem));
            }
        } catch (Exception e) {
            if(isCancelled()){
                RLog.d(TAG,e.getMessage());
            }else{
                RLog.e(TAG,e.getMessage());
            }

            mError = e.getMessage();
            return "failure";
        }
        return "success";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s.equals("success")) {
            onSeccess(mChannel, mPosts);
        } else if (s.equals("failure") && !isCancelled()) {
            onFailure(mError);
        }
    }

    public abstract void onSeccess(Channel channel,List<Post> posts);
    public abstract void onFailure(String error);


    private Post getPost(Element element) throws Exception{
        Post rssPost = new Post();
        rssPost.title = element.select("title").first().text();
        rssPost.description = element.select("description").first().text();
        rssPost.link = element.select("link").first().text();
        rssPost.author = element.select("dc|creator").first().text();
        String pubDate = "";
        if (!element.select("pubDate").isEmpty()) {
            pubDate = element.select("pubDate").first().text();
        }
        rssPost.pubDate = DataUntil.getTimeInMs(pubDate);
        return rssPost;
    }

    private Channel getChannel(Element element) throws Exception{

        Channel channel = new Channel();
        channel.urlRss = mUrl;

        channel.title = element.select("title").first().text();
        channel.description = element.select("description").first().text();

        channel.link = element.select("link").first().text();

        String imageUrl;
        if (!element.select("image").isEmpty()) {
            imageUrl = element.select("image").select("url").first().text();
        } else {
            imageUrl = null;
        }

        channel.urlImage = imageUrl;
        String pubDate = "";
        if (!element.select("pubDate").isEmpty()) {
            pubDate = element.select("pubDate").first().text();
        }
        channel.pubDate = DataUntil.getTimeInMs(pubDate);
        return channel;
    }

    public boolean isRssUrl(Document rssDocument){
        return rssDocument!=null && rssDocument.select("rss").size() != 0;
    }

}