package com.tundem.holokitkatdrawer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.tundem.holokitkatdrawer.R;
import com.tundem.holokitkatdrawer.model.NavDrawerItem;

import java.util.ArrayList;

public class NavDrawerListAdapter extends BaseAdapter {

    private Context c;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter(Context c, ArrayList<NavDrawerItem> navDrawerItems) {
        this.c = c;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)
                c.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (navDrawerItems.get(position).isPrimary()) {
            convertView = mInflater.inflate(R.layout.drawer_list_item_primary, null);

            int color = c.getResources().getColor(R.color.list_item_title);

            TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
            txtTitle.setText(navDrawerItems.get(position).getTitle());
            txtTitle.setTextColor(color);

            ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            if (navDrawerItems.get(position).getIcon() != null) {
                imgIcon.setImageDrawable(new IconDrawable(c, navDrawerItems.get(position).getIcon()).color(color).actionBarSize());
            } else {
                imgIcon.setVisibility(View.GONE);
            }
        } else {
            convertView = mInflater.inflate(R.layout.drawer_list_item_secondary, null);

            int color = c.getResources().getColor(R.color.list_item_title_secondary);

            TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
            txtTitle.setText(navDrawerItems.get(position).getTitle());
            txtTitle.setTextColor(color);

            ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            if (navDrawerItems.get(position).getIcon() != null) {
                imgIcon.setImageDrawable(new IconDrawable(c, navDrawerItems.get(position).getIcon()).color(color).actionBarSize());
            } else {
                imgIcon.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

}
