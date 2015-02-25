package com.mikepenz.materialdrawer.model.interfaces;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface IDrawerItem {
    public int getIdentifier();

    public Object getTag();

    public boolean isEnabled();

    public String getType();

    public int getLayoutRes();

    public View convertView(LayoutInflater inflater, View convertView, ViewGroup parent);
}
