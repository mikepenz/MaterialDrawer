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
        this(activity, null);
    }

    public DrawerAdapter(Activity activity, ArrayList<IDrawerItem> drawerItems) {
        mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mDrawerItems = new ArrayList<>();

        update(drawerItems);
    }

    public void update(ArrayList<IDrawerItem> drawerItems) {
        mDrawerItems = drawerItems;

        mapTypes();
    }

    public void add(IDrawerItem... drawerItems) {
        if (drawerItems != null) {
            Collections.addAll(mDrawerItems, drawerItems);
        }
        mapTypes();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return position < getCount() && mDrawerItems.get(position).isEnabled();
    }

    @Override
    public int getCount() {
        return mDrawerItems == null ? 0 : mDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return position < getCount() ? mDrawerItems.get(position) : null;
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
        mDrawerItems = drawerItems;
        mapTypes();
    }

    @Override
    public LinkedHashSet<String> getTypeMapper() {
        return mTypeMapper;
    }

    @Override
    public void setTypeMapper(LinkedHashSet<String> typeMapper) {
        mTypeMapper = typeMapper;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IDrawerItem item = (IDrawerItem) getItem(position);
        return item.convertView(mInflater, convertView, parent);
    }
}
