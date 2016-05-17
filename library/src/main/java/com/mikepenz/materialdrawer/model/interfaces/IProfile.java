package com.mikepenz.materialdrawer.model.interfaces;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;

import com.mikepenz.fastadapter.IIdentifyable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface IProfile<T> extends IIdentifyable<T> {
    T withName(String name);

    StringHolder getName();

    T withEmail(String email);

    StringHolder getEmail();

    T withIcon(Drawable icon);

    T withIcon(Bitmap bitmap);

    T withIcon(@DrawableRes int iconRes);

    T withIcon(String url);

    T withIcon(Uri uri);

    T withIcon(IIcon icon);

    ImageHolder getIcon();

    T withSelectable(boolean selectable);

    boolean isSelectable();
}
