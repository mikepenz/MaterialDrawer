package com.mikepenz.materialdrawer.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mikepenz on 27.03.15.
 */
public class DrawerItemViewHelper {

    private Context mContext;

    public DrawerItemViewHelper(Context context) {
        this.mContext = context;
    }

    private ArrayList<IDrawerItem> mDrawerItems = new ArrayList<>();

    public DrawerItemViewHelper withDrawerItems(ArrayList<IDrawerItem> drawerItems) {
        this.mDrawerItems = drawerItems;
        return this;
    }

    public DrawerItemViewHelper withDrawerItems(IDrawerItem... drawerItems) {
        Collections.addAll(this.mDrawerItems, drawerItems);
        return this;
    }

    private boolean mDivider = true;

    public DrawerItemViewHelper withDivider(boolean divider) {
        this.mDivider = divider;
        return this;
    }

    private OnDrawerItemClickListener mOnDrawerItemClickListener = null;

    public DrawerItemViewHelper withOnDrawerItemClickListener(OnDrawerItemClickListener onDrawerItemClickListener) {
        mOnDrawerItemClickListener = onDrawerItemClickListener;
        return this;
    }

    public View build() {
        //create the container view
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        //create the divider
        if (mDivider) {
            LinearLayout divider = new LinearLayout(mContext);
            divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            divider.setMinimumHeight((int) UIUtils.convertDpToPixel(1, mContext));
            divider.setOrientation(LinearLayout.VERTICAL);
            divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(mContext, R.attr.material_drawer_divider, R.color.material_drawer_divider));
            linearLayout.addView(divider);
        }

        //get the inflater
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        //add all drawer items
        for (IDrawerItem drawerItem : mDrawerItems) {
            View view = drawerItem.convertView(layoutInflater, null, linearLayout);
            view.setTag(drawerItem);

            if (drawerItem.isEnabled()) {
                view.setBackgroundResource(UIUtils.getSelectableBackground(mContext));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnDrawerItemClickListener != null) {
                            mOnDrawerItemClickListener.onItemClick(v, (IDrawerItem) v.getTag());
                        }
                    }
                });
            }

            linearLayout.addView(view);
        }

        return linearLayout;
    }


    public interface OnDrawerItemClickListener {
        public void onItemClick(View view, IDrawerItem drawerItem);
    }
}
