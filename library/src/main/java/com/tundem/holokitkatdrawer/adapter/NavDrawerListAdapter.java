package com.tundem.holokitkatdrawer.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.tundem.holokitkatdrawer.R;
import com.tundem.holokitkatdrawer.model.NavDrawerItem;
import com.tundem.holokitkatdrawer.util.UIUtils;

import java.util.ArrayList;

public class NavDrawerListAdapter extends BaseAdapter {

    private Activity act;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter(Activity act, ArrayList<NavDrawerItem> navDrawerItems) {
        this.act = act;
        this.navDrawerItems = navDrawerItems;
    }

    public void updateData(ArrayList<NavDrawerItem> navDrawerItems) {
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return navDrawerItems.get(position).isEnabled();
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
                act.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        int color;
        if (navDrawerItems.get(position).isPrimary()) {
            convertView = mInflater.inflate(R.layout.drawer_list_item_primary, null);
            UIUtils.setBackground(convertView, UIUtils.getInstance().getDrawerListItem(act));

            color = act.getResources().getColor(R.color.material_text_primary);
        } else {
            convertView = mInflater.inflate(R.layout.drawer_list_item_secondary, null);
            UIUtils.setBackground(convertView, UIUtils.getInstance().getDrawerListSecondaryItem(act));

            color = act.getResources().getColor(R.color.material_text_secondary);
        }

        if (!navDrawerItems.get(position).isEnabled()) {
            color = act.getResources().getColor(R.color.material_text_hint);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        txtTitle.setText(navDrawerItems.get(position).getTitle());
        txtTitle.setTextColor(color);

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        if (navDrawerItems.get(position).getIcon() != null) {
            imgIcon.setImageDrawable(new IconDrawable(act, navDrawerItems.get(position).getIcon()).color(color).actionBarSize());
        } else {
            imgIcon.setVisibility(View.GONE);
        }
        return convertView;
    }

}
