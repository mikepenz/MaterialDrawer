package com.mikepenz.materialdrawer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.DrawerItem;
import com.mikepenz.materialdrawer.util.UIUtils;

import java.util.ArrayList;

/**
 * Created by mikepenz on 01.02.15.
 */
public class DrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<DrawerItem> drawerItems = null;

    private Context mContext = null;

    public DrawerAdapter(Context context) {
        this.mContext = context;
    }

    public ArrayList<DrawerItem> getDrawerItems() {
        return drawerItems;
    }

    public void setDrawerItems(ArrayList<DrawerItem> drawerItems) {
        this.drawerItems = drawerItems;
    }

    public void addDrawerItems(ArrayList<DrawerItem> drawerItems) {
        if (this.drawerItems == null) {
            this.drawerItems = new ArrayList<DrawerItem>();
        }

        if (drawerItems != null) {
            this.drawerItems.addAll(drawerItems);
        }
    }

    @Override
    public int getItemCount() {
        if (drawerItems != null) {
            return drawerItems.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (drawerItems != null) {
            return drawerItems.get(position).getType();
        }

        return DrawerItem.PRIMARY;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == DrawerItem.PRIMARY) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.drawer_list_item_primary, viewGroup, false);
            return new ViewHolder(v);
        } else if (viewType == DrawerItem.SECONDARY) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.drawer_list_item_secondary, viewGroup, false);
            return new ViewHolder(v);
        } else if (viewType == DrawerItem.SPACER) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.drawer_list_item_spacer, viewGroup, false);
            return new ViewHolderSpacer(v);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        DrawerItem item = drawerItems.get(position);

        if (viewHolder instanceof ViewHolder) {
            UIUtils.setBackground(((ViewHolder) viewHolder).itemView, UIUtils.getDrawerListSecondaryItem(mContext));

            int color = -1;
            if (!item.isEnabled()) {
                color = mContext.getResources().getColor(R.color.material_drawer_hint_text);
                ((ViewHolder) viewHolder).iconView.setEnabled(false);
            }

            ((ViewHolder) viewHolder).titleView.setText(item.getTitle());
            ((ViewHolder) viewHolder).titleView.setTextColor(color);

            if (item.getIcon() != null) {
                UIUtils.setBackground(((ViewHolder) viewHolder).iconView, new IconicsDrawable(mContext, drawerItems.get(position).getIcon()).color(color).actionBarSize());
            } else {
                ((ViewHolder) viewHolder).iconView.setVisibility(View.GONE);
            }
        } else if (viewHolder instanceof ViewHolderSpacer) {
            ((ViewHolderSpacer) viewHolder).itemView.setMinimumHeight(1);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView titleView;
        public ImageView iconView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.titleView = (TextView) itemView.findViewById(R.id.title);
            this.iconView = (ImageView) itemView.findViewById(R.id.icon);
        }
    }

    public static class ViewHolderSpacer extends RecyclerView.ViewHolder {
        public View itemView;

        public ViewHolderSpacer(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
