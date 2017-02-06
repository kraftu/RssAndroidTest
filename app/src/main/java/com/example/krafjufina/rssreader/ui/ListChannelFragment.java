package com.example.krafjufina.rssreader.ui;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.krafjufina.rssreader.R;
import com.example.krafjufina.rssreader.adapter.ChannelsAdapter;
import com.example.krafjufina.rssreader.database.DbContract;
import com.example.krafjufina.rssreader.model.Channel;

/**
 * Created by Krafjufina on 05.02.2017.
 */

public class ListChannelFragment extends Fragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>,ChannelsAdapter.OnItemClickListener {

    public static final int LOAD_CHANNEL = 1;

    private RecyclerView mRecyclerView;
    private ChannelsAdapter mChannelsAdapter;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_channel_list,container,false);
        mRecyclerView = (RecyclerView)root.findViewById(R.id.list);
        fab = (FloatingActionButton)root.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        mChannelsAdapter = new ChannelsAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mChannelsAdapter);
        getLoaderManager().initLoader(LOAD_CHANNEL,null,this);
        getLoaderManager().getLoader(LOAD_CHANNEL).forceLoad();
        mChannelsAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        AddChannelActivity.start(getActivity());
    }

    public MainActivity getMainActivity(){
        return (MainActivity) getActivity();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == LOAD_CHANNEL){
            return new CursorLoader(getContext(), DbContract.CONTENT_CHANNEL_URI,null,null,null,
                    DbContract.DEFAULT_SORT_CHANNEL);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(mChannelsAdapter !=null && data != null){
            mChannelsAdapter.swap(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(ChannelsAdapter channelsAdapter, View v, int position) {
        Channel channel = channelsAdapter.getItem(position);
        Uri uri = ContentUris.withAppendedId(DbContract.CONTENT_CHANNEL_URI,channel.id);
        ListPostActivity.start(getActivity(),uri,channel.name);
    }
}
