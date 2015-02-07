package com.mikepenz.materialdrawer.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface IDrawerItem {
    public int getIdentifier();

    public boolean isEnabled();

    public String getType();

    public int getLayoutRes();

    public View convertView(Activity activity, LayoutInflater inflater, View convertView, ViewGroup parent);
}
