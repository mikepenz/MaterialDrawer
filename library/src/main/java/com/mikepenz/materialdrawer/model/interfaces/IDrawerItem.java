package com.mikepenz.materialdrawer.model.interfaces;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface IDrawerItem {
    int getIdentifier();

    Object getTag();

    boolean isEnabled();

    String getType();

    int getLayoutRes();

    View convertView(LayoutInflater inflater, View convertView, ViewGroup parent);
}
