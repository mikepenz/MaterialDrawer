package com.mikepenz.materialdrawer.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

public class DrawerAdapter extends BaseDrawerAdapter {

    private ArrayList<IDrawerItem> mDrawerItems;

    private boolean mAnimateDrawerItems = false;
    private ArrayList<Boolean> mDrawerAnimatedItems;

    private LayoutInflater mInflater;

    private LinkedHashSet<String> mTypeMapper;

    public DrawerAdapter(Activity activity) {
        this(activity, false);
    }

    public DrawerAdapter(Activity activity, boolean animateDrawerItems) {
        this(activity, null, animateDrawerItems);
    }

    public DrawerAdapter(Activity activity, ArrayList<IDrawerItem> drawerItems) {
        this(activity, drawerItems, false);
    }

    public DrawerAdapter(Activity activity, ArrayList<IDrawerItem> drawerItems, boolean animateDrawerItems) {
        mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        mDrawerItems = new ArrayList<>();
        mDrawerAnimatedItems = new ArrayList<>();
        mAnimateDrawerItems = animateDrawerItems;

        setDrawerItems(drawerItems);
    }


    public void add(IDrawerItem... drawerItems) {
        if (drawerItems != null) {
            Collections.addAll(mDrawerItems, drawerItems);
        }

        if (drawerItems != null) {
            for (int i = 0; i < drawerItems.length; i++) {
                mDrawerAnimatedItems.add(false);
            }
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

    public Boolean getAnimatedItem(int position) {
        if (mDrawerAnimatedItems != null && position < mDrawerAnimatedItems.size()) {
            return mDrawerAnimatedItems.get(position);
        } else {
            return null;
        }
    }

    public void setAnimatedItem(int position, Boolean animated) {
        if (mDrawerAnimatedItems != null && position < mDrawerAnimatedItems.size()) {
            mDrawerAnimatedItems.set(position, animated);
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
        mDrawerItems = drawerItems;

        if (drawerItems != null) {
            mDrawerAnimatedItems.clear();
            for (int i = 0; i < drawerItems.size(); i++) {
                mDrawerAnimatedItems.add(false);
            }
        }

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
    public void resetAnimation() {
        for (int i = 0; i < mDrawerAnimatedItems.size(); i++) {
            mDrawerAnimatedItems.set(i, false);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IDrawerItem item = (IDrawerItem) getItem(position);

        View view = item.convertView(mInflater, convertView, parent);

        if (mAnimateDrawerItems) {
            if (getAnimatedItem(position) == null || !getAnimatedItem(position)) {
                AnimationSet animationSet = new AnimationSet(false);
                animationSet.setDuration(100);

                Animation scaleAnimation = new ScaleAnimation(1, 1, 0, 1);
                Animation alphaAnimation = new AlphaAnimation(0, 1);

                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(alphaAnimation);

                view.startAnimation(animationSet);
                setAnimatedItem(position, true);
            }
        }

        return view;
    }
}
