package com.mikepenz.materialdrawer.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

public class DrawerAdapter extends BaseDrawerAdapter {

    private ArrayList<IDrawerItem> mDrawerItems;
    private LayoutInflater mInflater;

    private LinkedHashSet<String> mTypeMapper;

    public DrawerAdapter(Activity activity) {
        this.mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        update(null);
    }

    public DrawerAdapter(Activity activity, ArrayList<IDrawerItem> drawerItems) {
        this.mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        update(drawerItems);
    }

    public void update(ArrayList<IDrawerItem> drawerItems) {
        this.mDrawerItems = drawerItems;

        if (this.mDrawerItems == null) {
            mDrawerItems = new ArrayList<>();
        }

        mapTypes();
    }

    public void add(IDrawerItem... drawerItems) {
        if (this.mDrawerItems == null) {
            mDrawerItems = new ArrayList<>();
        }

        if (drawerItems != null) {
            Collections.addAll(this.mDrawerItems, drawerItems);
        }

        mapTypes();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        if (mDrawerItems != null && mDrawerItems.size() > position) {
            return mDrawerItems.get(position).isEnabled();
        } else {
            return false;
        }
    }

    @Override
    public int getCount() {
        if (mDrawerItems == null) {
            return 0;
        }
        return mDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        if (mDrawerItems != null && mDrawerItems.size() > position) {
            return mDrawerItems.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public ArrayList<IDrawerItem> getDrawerItems() {
        return mDrawerItems;
    }

    @Override
    public void setDrawerItems(ArrayList<IDrawerItem> drawerItems) {
        this.mDrawerItems = drawerItems;
        mapTypes();
    }

    @Override
    public LinkedHashSet<String> getTypeMapper() {
        return mTypeMapper;
    }

    @Override
    public void setTypeMapper(LinkedHashSet<String> typeMapper) {
        this.mTypeMapper = typeMapper;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IDrawerItem item = (IDrawerItem) getItem(position);
        return item.convertView(mInflater, convertView, parent);
    }
}
