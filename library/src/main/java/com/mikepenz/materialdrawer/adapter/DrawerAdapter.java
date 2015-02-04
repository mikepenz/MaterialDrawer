package com.mikepenz.materialdrawer.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mikepenz.materialdrawer.model.IDrawerItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DrawerAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<IDrawerItem> mDrawerItems;
    private LayoutInflater mInflater;

    private List<String> mTypeMapper;

    public DrawerAdapter(Activity activity) {
        this.mActivity = activity;
        this.mInflater = (LayoutInflater) mActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        update(null);
    }

    public DrawerAdapter(Activity activity, ArrayList<IDrawerItem> drawerItems) {
        this.mActivity = activity;
        this.mInflater = (LayoutInflater) mActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        update(drawerItems);
    }

    public void update(ArrayList<IDrawerItem> drawerItems) {
        this.mDrawerItems = drawerItems;

        if (this.mDrawerItems == null) {
            mDrawerItems = new ArrayList<IDrawerItem>();
        }

        mapTypes();
    }

    public void add(IDrawerItem... drawerItems) {
        if (this.mDrawerItems == null) {
            mDrawerItems = new ArrayList<IDrawerItem>();
        }

        if (drawerItems != null) {
            Collections.addAll(this.mDrawerItems, drawerItems);
        }

        mapTypes();
    }

    private void mapTypes() {
        if (this.mTypeMapper == null) {
            this.mTypeMapper = new ArrayList<String>();
        }

        if (this.mDrawerItems != null) {
            for (IDrawerItem drawerItem : this.mDrawerItems) {
                mTypeMapper.add(drawerItem.getType());
            }
        }
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
    public int getItemViewType(int position) {
        if (mDrawerItems != null && mDrawerItems.size() > position) {
            return mTypeMapper.indexOf(mDrawerItems.get(position).getType());
        } else {
            return -1;
        }

    }

    @Override
    public int getViewTypeCount() {
        return mTypeMapper.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IDrawerItem item = (IDrawerItem) getItem(position);
        return item.convertView(mActivity, mInflater, convertView, parent);
    }
}
