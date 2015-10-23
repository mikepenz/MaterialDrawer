package com.mikepenz.materialdrawer.app.drawerItems;

import android.support.v7.widget.RecyclerView;

import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

public class CustomPrimaryDrawerItem extends PrimaryDrawerItem {

    private ColorHolder background;

    public CustomPrimaryDrawerItem withBackgroundColor(int backgroundColor) {
        this.background = ColorHolder.fromColor(backgroundColor);
        return this;
    }

    public CustomPrimaryDrawerItem withBackgroundRes(int backgroundRes) {
        this.background = ColorHolder.fromColorRes(backgroundRes);
        return this;
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder) {
        super.bindView(holder);

        if (background != null) {
            background.applyToBackground(holder.itemView);
        }
    }
}
