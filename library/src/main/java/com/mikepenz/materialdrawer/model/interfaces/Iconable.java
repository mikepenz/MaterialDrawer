package com.mikepenz.materialdrawer.model.interfaces;

import android.graphics.drawable.Drawable;

import com.mikepenz.iconics.typeface.IIcon;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface Iconable<T> {
    public T withIcon(Drawable icon);

    public T withIcon(IIcon iicon);

    public Drawable getIcon();

    public IIcon getIIcon();

    public void setIcon(Drawable icon);

    public void setIIcon(IIcon iicon);
}
