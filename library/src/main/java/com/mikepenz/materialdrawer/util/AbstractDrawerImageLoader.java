package com.mikepenz.materialdrawer.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

public abstract class AbstractDrawerImageLoader implements DrawerImageLoader.IDrawerImageLoader {
    @Override
    public void set(ImageView imageView, Uri uri, Drawable placeholder) {
    }

    @Override
    public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {
        //for backwards compatibility call the method without tag too
        set(imageView, uri, placeholder);
        //this won't do anything
        Log.i("MaterialDrawer", "you have not specified a ImageLoader implementation through the DrawerImageLoader.init(IDrawerImageLoader) method");
    }

    @Override
    public void cancel(ImageView imageView) {
    }

    @Override
    public Drawable placeholder(Context ctx) {
        return DrawerUIUtils.getPlaceHolder(ctx);
    }

    @Override
    public Drawable placeholder(Context ctx, String tag) {
        return placeholder(ctx);
    }
}