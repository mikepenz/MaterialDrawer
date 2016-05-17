package com.mikepenz.materialdrawer.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

/**
 * Created by mikepenz on 24.03.15.
 */
public class DrawerImageLoader {
    public enum Tags {
        PROFILE,
        PROFILE_DRAWER_ITEM,
        ACCOUNT_HEADER
    }

    private static DrawerImageLoader SINGLETON = null;

    private IDrawerImageLoader imageLoader;
    private boolean mHandleAllUris = false;

    private DrawerImageLoader(IDrawerImageLoader loaderImpl) {
        imageLoader = loaderImpl;
    }

    public static DrawerImageLoader init(IDrawerImageLoader loaderImpl) {
        SINGLETON = new DrawerImageLoader(loaderImpl);
        return SINGLETON;
    }

    public static DrawerImageLoader getInstance() {
        if (SINGLETON == null) {
            SINGLETON = new DrawerImageLoader(new AbstractDrawerImageLoader() {
            });
        }
        return SINGLETON;
    }

    public DrawerImageLoader withHandleAllUris(boolean handleAllUris) {
        this.mHandleAllUris = handleAllUris;
        return this;
    }

    /**
     * @param imageView
     * @param uri
     * @param tag
     * @return false if not consumed
     */
    public boolean setImage(ImageView imageView, Uri uri, String tag) {
        //if we do not handle all uris and are not http or https we keep the original behavior
        if (mHandleAllUris || "http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) {
            if (imageLoader != null) {
                Drawable placeHolder = imageLoader.placeholder(imageView.getContext(), tag);
                imageLoader.set(imageView, uri, placeHolder);
            }
            return true;
        }
        return false;
    }

    public void cancelImage(ImageView imageView) {
        if (imageLoader != null) {
            imageLoader.cancel(imageView);
        }
    }

    public IDrawerImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(IDrawerImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public interface IDrawerImageLoader {
        void set(ImageView imageView, Uri uri, Drawable placeholder);

        void cancel(ImageView imageView);

        Drawable placeholder(Context ctx);

        /**
         * @param ctx
         * @param tag current possible tags: "profile", "profileDrawerItem", "accountHeader"
         * @return
         */
        Drawable placeholder(Context ctx, String tag);
    }
}
