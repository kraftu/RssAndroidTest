package com.example.krafjufina.rssreader;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.krafjufina.rssreader.adapter.PostsAdapter;
import com.example.krafjufina.rssreader.database.DbContract;
import com.example.krafjufina.rssreader.model.Channel;
import com.example.krafjufina.rssreader.model.Post;
import com.example.krafjufina.rssreader.network.RssParserTask;
import com.example.krafjufina.rssreader.network.TaskService;

import java.util.List;

public class ListPostActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener, PostsAdapter.OnItemClickListener {

    public static final int LOAD_CHANNEL = 0;
    public static final int LOAD_POSTS = 1;
    public static final String BUNDLE_TITLE = "BUNDLE_TITLE";
    public static final String BUNDLE_URL_IMAGE = "BUNDLE_IMAGE";

    private Channel mChannel;
    private boolean isInitChannel;

    private PostsAdapter mPostsAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private Uri mUriChannel;
    private LoadChannel mLoadChannel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_post);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.sRefreshLayout);
        mRecyclerView = (RecyclerView)findViewById(R.id.list);
        mUriChannel = getIntent().getData();

        getSupportLoaderManager().initLoader(LOAD_CHANNEL,null,this);

        mPostsAdapter = new PostsAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mPostsAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setEnabled(false);
        isInitChannel = false;
        getSupportLoaderManager().getLoader(LOAD_CHANNEL).forceLoad();
        mPostsAdapter.setOnItemClickListener(this);
    }

    public static void start(Context context, Uri uriChannel){
        Intent intent = new Intent(context, ListPostActivity.class);
        intent.setData(uriChannel);
        context.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLoadChannel!=null && !mLoadChannel.getStatus().equals(AsyncTask.Status.FINISHED)){
            mLoadChannel.cancel(false);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == LOAD_CHANNEL){
            return new CursorLoader(this, mUriChannel, null, null, null, null);
        }else if (id == LOAD_POSTS){
            return new CursorLoader(this, DbContract.CONTENT_POST_URI, null,
                    DbContract.getSelection(DbContract.PostColumns.CHANNEL_ID,mChannel.id)
                    , null, DbContract.DEFAULT_SORT_POST);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == LOAD_CHANNEL){
            if(data.moveToNext()){
                mChannel = new Channel(data);
                setChannel(mChannel);
            }else{
                finish();
                Toast.makeText(this,"FAIL LOAD CHANNEL",Toast.LENGTH_LONG).show();
            }
        }else if(loader.getId() == LOAD_POSTS){
            if(mPostsAdapter != null) mPostsAdapter.swap(data);
            if(mLoadChannel == null && mPostsAdapter.getItemCount() == 0) autoLoad();
        }
    }

    private void setChannel(Channel channel){
        mChannel = channel;
        getSupportActionBar().setTitle(TextUtils.isEmpty(mChannel.title)?mChannel.name:mChannel.title);
        if(!isInitChannel){
            isInitChannel = true;
            getSupportLoaderManager().initLoader(LOAD_POSTS,null,this);
            getSupportLoaderManager().getLoader(LOAD_CHANNEL).forceLoad();
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    public void autoLoad(){
        mSwipeRefreshLayout.setRefreshing(true);
        startLoadChannel(mChannel.id,mChannel.urlRss);
    }

    private void startLoadChannel(long channelId , String urlRss){
        mLoadChannel = new LoadChannel(channelId,urlRss);
        mLoadChannel.execute();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onRefresh() {
        startLoadChannel(mChannel.id,mChannel.urlRss);
    }

    @Override
    public void onItemClick(PostsAdapter postsAdapter, View v, int position) {
        Post post = postsAdapter.getItem(position);
        Uri uri = ContentUris.withAppendedId(DbContract.CONTENT_POST_URI,post.id);
        PostActivity.start(this,uri);
    }

    public class LoadChannel extends RssParserTask {

        public long channelId;
        public LoadChannel(long channelId, String url) {
            super(url);
            this.channelId = channelId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onSeccess(Channel channel, List<Post> posts) {
            TaskService.updateChannel(ListPostActivity.this,
                    mUriChannel,
                    Channel.getContentValues(channel));

            TaskService.deletePostForChannel(ListPostActivity.this,
                    mUriChannel.getLastPathSegment());
            for(Post post:posts){
                post.channelId = channelId;
                TaskService.insertNewPost(ListPostActivity.this,
                        Post.getContentValues(post));
            }
        }

        @Override
        public void onFailure(String error) {
            Toast.makeText(ListPostActivity.this,error,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
