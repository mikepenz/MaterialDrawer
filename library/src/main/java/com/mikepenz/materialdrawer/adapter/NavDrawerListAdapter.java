package com.mikepenz.materialdrawer.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.DrawerItem;
import com.mikepenz.materialdrawer.util.UIUtils;

import java.util.ArrayList;

public class NavDrawerListAdapter extends BaseAdapter {

    private Activity act;
    private ArrayList<DrawerItem> drawerItems;

    public NavDrawerListAdapter(Activity act, ArrayList<DrawerItem> drawerItems) {
        this.act = act;
        this.drawerItems = drawerItems;
    }

    public void updateData(ArrayList<DrawerItem> drawerItems) {
        this.drawerItems = drawerItems;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return drawerItems.get(position).isEnabled();
    }

    @Override
    public int getCount() {
        return drawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return drawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) act.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        int color;
        if (drawerItems.get(position).getType() == DrawerItem.PRIMARY) {
            convertView = mInflater.inflate(R.layout.drawer_list_item_primary, null);
            UIUtils.setBackground(convertView, UIUtils.getDrawerListItem(act));

            color = act.getResources().getColor(R.color.material_drawer_primary_text);
        } else if (drawerItems.get(position).getType() == DrawerItem.SECONDARY) {
            convertView = mInflater.inflate(R.layout.drawer_list_item_secondary, null);
            UIUtils.setBackground(convertView, UIUtils.getDrawerListSecondaryItem(act));

            color = act.getResources().getColor(R.color.material_drawer_secondary_text);
        } else {
            convertView = mInflater.inflate(R.layout.drawer_list_item_spacer, null);
            convertView.setMinimumHeight(1);
            color = -1;
        }

        if (drawerItems.get(position).getType() != DrawerItem.SPACER) {
            if (!drawerItems.get(position).isEnabled()) {
                color = act.getResources().getColor(R.color.material_drawer_hint_text);
            }

            TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
            txtTitle.setText(drawerItems.get(position).getTitle());
            txtTitle.setTextColor(color);

            ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            if (drawerItems.get(position).getIcon() != null) {
                imgIcon.setImageDrawable(new IconicsDrawable(act, drawerItems.get(position).getIcon()).color(color).actionBarSize());
            } else {
                imgIcon.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

}
