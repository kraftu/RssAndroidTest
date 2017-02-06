package com.example.krafjufina.rssreader.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krafjufina.rssreader.R;
import com.example.krafjufina.rssreader.model.Post;
import com.example.krafjufina.rssreader.tools.Http;

public class PostActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOAD_POST = 1;
    public static final String BUNLDE_TITLE = "BUNLDE_TITLE";

    private TextView mDescription;
    private Uri mPostUri;
    private Post mPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPostUri = getIntent().getData();
        String title = getIntent().getStringExtra(BUNLDE_TITLE);
        if(!TextUtils.isEmpty(title)) getSupportActionBar().setTitle(title);

        mDescription = (TextView)findViewById(R.id.tvDescription);

        getSupportLoaderManager().initLoader(LOAD_POST,null,this);
        getSupportLoaderManager().getLoader(LOAD_POST).forceLoad();
    }

    public static void start(Context context,Uri uri,String title){
        Intent intent = new Intent(context,PostActivity.class);
        intent.setData(uri);
        intent.putExtra(BUNLDE_TITLE,title);
        context.startActivity(intent);
    }
    private void setData(Post post){
        mPost = post;
        getSupportActionBar().setTitle(mPost.title);
        mDescription.setText(Http.fromHtml(mDescription,post.description));
        mDescription.setMovementMethod(LinkMovementMethod.getInstance());


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == LOAD_POST){
            return new CursorLoader(this,mPostUri,null,null,null,null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.moveToNext()){
            setData(new Post(data));
        }else{
            finish();
            Toast.makeText(this,"Not found is post",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
