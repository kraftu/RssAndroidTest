package com.example.krafjufina.rssreader.tools;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

public class Http {


    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(TextView textView, String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY,new URLImageParser(textView),null);
        } else {
            result = Html.fromHtml(html,new URLImageParser(textView),null);
        }
        return result;
    }

    public static class URLDrawable extends BitmapDrawable {
        protected Drawable drawable;

        @Override
        public void draw(Canvas canvas) {
            if(drawable != null) {
                drawable.draw(canvas);
            }
        }
        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }
    }

    public static class URLImageParser implements Html.ImageGetter {
        View container;
        public URLImageParser(View view) {
            this.container = view;
        }
        public Drawable getDrawable(String source) {
            final URLDrawable urlDrawable = new URLDrawable();
            urlDrawable.setBounds(0, 0, 0,0);
            return urlDrawable;
        }
    }


}
