package com.mikepenz.materialdrawer.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView

/**
 * This abstract class provides functionality to add custom image loader implementations, based on the image loader used in the app.
 */
abstract class AbstractDrawerImageLoader : DrawerImageLoader.IDrawerImageLoader {
    /**
     * Start loading the image [uri] for the given [imageView] providing the [placeholder], allowing to identify the location it is gonna be used via the [tag]
     */
    override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable, tag: String?) {
        //this won't do anything
        Log.i("MaterialDrawer", "You have not specified a ImageLoader implementation through the DrawerImageLoader.init() method, or you are still overriding the deprecated method set(ImageView iv, Uri u, Drawable d) instead of set(ImageView iv, Uri u, Drawable d, String tag)")
    }

    /**
     * Cancel loading images for the imageView
     */
    override fun cancel(imageView: ImageView) {}

    /**
     * Retrieve the placeholder to display
     */
    override fun placeholder(ctx: Context): Drawable {
        return getPlaceHolder(ctx)
    }

    /**
     * Retrieve the placeholder to display, using the [tag] to identify the location it is gonna be used
     */
    override fun placeholder(ctx: Context, tag: String?): Drawable {
        return placeholder(ctx)
    }
}
