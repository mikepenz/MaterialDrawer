package com.mikepenz.materialdrawer.app.DrawerItems;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

public class CustomPrimaryDrawerItem extends PrimaryDrawerItem {

    private int backgroundRes = -1;
    private int backgroundColor = 0;


    public CustomPrimaryDrawerItem withBackgroundRes(int backgroundRes) {
        this.backgroundRes = backgroundRes;
        return this;
    }

    public CustomPrimaryDrawerItem withBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    @Override
    public View convertView(LayoutInflater inflater, View convertView, ViewGroup parent) {
        //use the logic of our PrimaryDrawerItem
        convertView = super.convertView(inflater, convertView, parent);

        if (backgroundColor != 0) {
            convertView.setBackgroundColor(backgroundColor);
        } else if (backgroundRes != -1) {
            convertView.setBackgroundResource(backgroundRes);
        }

        return convertView;
    }
}
