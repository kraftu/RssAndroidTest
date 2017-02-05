package com.example.krafjufina.rssreader.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.krafjufina.rssreader.R;
import com.example.krafjufina.rssreader.model.Channel;

/**
 * Created by Krafjufina on 05.02.2017.
 */

public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.ChannelViewHolder>{

    private Cursor mCursor;
    private OnItemClickListener mOnItemClickListener;
    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_channel,parent,false);
        return new ChannelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        Channel channel = getItem(position);
        holder.setChannel(channel);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    @Override
    public int getItemCount() {
        return mCursor!=null?mCursor.getCount():0;
    }

    public Channel getItem(int postion){
        if(!mCursor.moveToPosition(postion)){
            throw new IllegalStateException("Invalid item position requested");
        }
        return new Channel(mCursor);
    }

    public class ChannelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mName;
        private TextView mUri;

        public ChannelViewHolder(View itemView) {
            super(itemView);
            mName = (TextView)itemView.findViewById(R.id.tvName);
            mUri = (TextView)itemView.findViewById(R.id.tvRssUrl);
        }
        public void setChannel(Channel channel){
            mName.setText(channel.name);
            mUri.setText(channel.urlRss);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClick(this);
        }
    }

    public void swap(Cursor cursor){
        if(mCursor!=null){
            mCursor.close();
        }
        mCursor = cursor;
        notifyDataSetChanged();
    }

    private void onItemClick(ChannelViewHolder channelViewHolder){
        if(mOnItemClickListener!=null) mOnItemClickListener.onItemClick(this,channelViewHolder.itemView,channelViewHolder.getLayoutPosition());
    }
    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(ChannelsAdapter channelsAdapter, View v, int position);
    }
}
