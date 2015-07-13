package com.mikepenz.materialdrawer.model.interfaces;

import android.graphics.drawable.Drawable;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.holder.ImageHolder;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface Iconable<T> {
    public T withIcon(Drawable icon);

    public T withIcon(IIcon iicon);

    public ImageHolder getIcon();
}
