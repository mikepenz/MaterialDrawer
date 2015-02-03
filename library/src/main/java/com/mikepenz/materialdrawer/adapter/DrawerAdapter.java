package com.mikepenz.materialdrawer.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mikepenz.materialdrawer.model.IDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SpacerDrawerItem;

import java.util.ArrayList;
import java.util.Collections;

public class DrawerAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<IDrawerItem> mDrawerItems;
    private LayoutInflater mInflater;

    public DrawerAdapter(Activity activity) {
        this.mActivity = activity;
        this.mInflater = (LayoutInflater) mActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mDrawerItems = new ArrayList<IDrawerItem>();
    }

    public DrawerAdapter(Activity activity, ArrayList<IDrawerItem> drawerItems) {
        this.mActivity = activity;
        this.mInflater = (LayoutInflater) mActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        this.mDrawerItems = drawerItems;
    }

    public void update(ArrayList<IDrawerItem> drawerItems) {
        this.mDrawerItems = drawerItems;
        notifyDataSetChanged();
    }

    public void add(IDrawerItem... drawerItems) {
        if (drawerItems != null) {
            Collections.addAll(this.mDrawerItems, drawerItems);
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
            return mDrawerItems.get(position).getType().getIdentifier();
        } else {
            return -1;
        }

    }

    @Override
    public int getViewTypeCount() {
        return IDrawerItem.Type.values().length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        IDrawerItem item = (IDrawerItem) getItem(position);

        if (type == IDrawerItem.Type.PRIMARY.getIdentifier()) {
            PrimaryDrawerItem primaryDrawerItem = (PrimaryDrawerItem) item;
            convertView = primaryDrawerItem.convertView(mActivity, mInflater, convertView, parent);
        } else if (type == IDrawerItem.Type.SECONDARY.getIdentifier()) {
            SecondaryDrawerItem secondaryDrawerItem = (SecondaryDrawerItem) item;
            convertView = secondaryDrawerItem.convertView(mActivity, mInflater, convertView, parent);
        } else if (type == IDrawerItem.Type.SPACER.getIdentifier()) {
            SpacerDrawerItem spacerDrawerItem = (SpacerDrawerItem) item;
            convertView = spacerDrawerItem.convertView(mActivity, mInflater, convertView, parent);
        }

        return convertView;
    }
}
