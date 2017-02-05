package com.example.krafjufina.rssreader;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.krafjufina.rssreader.model.Channel;
import com.example.krafjufina.rssreader.network.TaskService;

import java.net.URI;
import java.net.URISyntaxException;

public class AddChannelActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout mTilName;
    private EditText mEtName;
    private TextInputLayout mTilUrl;
    private EditText mEtUrl;
    private Button mBtnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_channel_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle(getString(R.string.title_activity_channel_add));

        mTilName = (TextInputLayout) findViewById(R.id.tilName);
        mEtName = (EditText) findViewById(R.id.etName);
        mTilUrl = (TextInputLayout) findViewById(R.id.tilUrl);
        mEtUrl = (EditText) findViewById(R.id.etUrl);
        mBtnAdd = (Button) findViewById(R.id.btnAdd);
        mBtnAdd.setOnClickListener(this);

        mEtName.addTextChangedListener(new ErrorReset(mTilName));
        mEtUrl.addTextChangedListener(new ErrorReset(mTilUrl));
    }

    @Override
    public void onClick(View view) {

        Channel channel = new Channel(mEtName.getText().toString().trim(),
                mEtUrl.getText().toString().trim());
        if(TextUtils.isEmpty(channel.name)){
            mTilName.setError(getString(R.string.add_channel_error_name));
            mTilName.setErrorEnabled(true);
            return;
        }
        if(!isValidUrl(channel.urlRss)){
            mTilUrl.setError(getString(R.string.add_channel_error_url));
            mTilUrl.setErrorEnabled(true);
            return;
        }
        TaskService.insertNewChannel(this,Channel.getContentValues(channel));
        finish();
    }

    private boolean isValidUrl(String urlSting){
        try {
            URI uri = new URI(urlSting);
            return true;
        }
        catch (URISyntaxException e) {
            return false;
        }
    }

    public static void start(Context context){
        Intent intent = new Intent(context,AddChannelActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public class ErrorReset implements TextWatcher{
        private TextInputLayout mLayout;

        public ErrorReset(TextInputLayout mLayout) {
            this.mLayout = mLayout;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(mLayout.isErrorEnabled()) mLayout.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
