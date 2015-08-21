package com.mikepenz.materialdrawer.model.interfaces;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface IProfile<T> {
    T withName(String name);

    StringHolder getName();

    T withEmail(String email);

    StringHolder getEmail();

    T withIcon(Drawable icon);

    T withIcon(Bitmap bitmap);

    T withIcon(String url);

    T withIcon(Uri uri);

    ImageHolder getIcon();

    T withSelectable(boolean selectable);

    boolean isSelectable();

    int getIdentifier();

}
