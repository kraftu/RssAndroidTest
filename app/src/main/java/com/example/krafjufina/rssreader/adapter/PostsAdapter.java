package com.example.krafjufina.rssreader.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.krafjufina.rssreader.tools.DataUntil;
import com.example.krafjufina.rssreader.R;
import com.example.krafjufina.rssreader.model.Post;

/**
 * Created by Krafjufina on 05.02.2017.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder>{

    private Cursor mCursor;
    private OnItemClickListener mOnItemClickListener;

    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_post,parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = getItem(position);
        holder.setPost(post);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    @Override
    public int getItemCount() {
        return mCursor!=null?mCursor.getCount():0;
    }

    public Post getItem(int postion){
        if(!mCursor.moveToPosition(postion)){
            throw new IllegalStateException("Invalid item position requested");
        }
        return new Post(mCursor);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitle;
        private TextView mInfo;
        private TextView mDescription;

        public PostViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            mInfo = (TextView)itemView.findViewById(R.id.tvInfo);
            mDescription = (TextView)itemView.findViewById(R.id.tvDescription);
        }
        public void setPost(Post post){
            mTitle.setText(post.title);
            mInfo.setText(String.format("%s / %s",
                    post.author,
                    DataUntil.getDateForAdapter(post.pubDate)));
            mDescription.setText(fromHtml(post.description));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClick(this);
        }
    }

    public void swap(Cursor cursor){
        if(mCursor != null){
            mCursor.close();
        }
        mCursor = cursor;
        notifyDataSetChanged();
    }

    private void onItemClick(PostViewHolder channelViewHolder){
        if(mOnItemClickListener!=null) mOnItemClickListener.onItemClick(this,channelViewHolder.itemView,channelViewHolder.getLayoutPosition());
    }
    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(PostsAdapter channelAdapter, View v, int position);
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}
