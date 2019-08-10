package com.mikepenz.materialdrawer.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView

abstract class AbstractDrawerImageLoader : DrawerImageLoader.IDrawerImageLoader {
    override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable) {}

    override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable, tag: String?) {
        //for backwards compatibility call the method without tag too
        set(imageView, uri, placeholder)
        //this won't do anything
        Log.i("MaterialDrawer", "You have not specified a ImageLoader implementation through the DrawerImageLoader.init() method, or you are still overriding the deprecated method set(ImageView iv, Uri u, Drawable d) instead of set(ImageView iv, Uri u, Drawable d, String tag)")
    }

    override fun cancel(imageView: ImageView) {}

    override fun placeholder(ctx: Context): Drawable {
        return DrawerUIUtils.getPlaceHolder(ctx)
    }

    override fun placeholder(ctx: Context, tag: String?): Drawable {
        return placeholder(ctx)
    }
}
