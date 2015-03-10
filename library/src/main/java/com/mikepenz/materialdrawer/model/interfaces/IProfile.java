package com.mikepenz.materialdrawer.model.interfaces;

import android.graphics.drawable.Drawable;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface IProfile<T> {
    public T withName(String name);

    public String getName();

    public void setName(String name);

    public T withEmail(String email);

    public String getEmail();

    public void setEmail(String email);

    public T withIcon(Drawable icon);

    public Drawable getIcon();

    public void setIcon(Drawable icon);

    public T withSelectable(boolean selectable);

    public boolean isSelectable();

    public T setSelectable(boolean selectable);

    public int getIdentifier();

}
