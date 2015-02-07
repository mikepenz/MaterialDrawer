package com.mikepenz.materialdrawer.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikepenz.materialdrawer.R;

/**
 * Created by mikepenz on 03.02.15.
 */
public class SectionDrawerItem implements IDrawerItem {

    private String name;
    private int nameRes = -1;
    private boolean divider = true;

    public SectionDrawerItem withName(String name) {
        this.name = name;
        return this;
    }

    public SectionDrawerItem withName(int nameRes) {
        this.nameRes = nameRes;
        return this;
    }

    public SectionDrawerItem setDivider(boolean divider) {
        this.divider = divider;
        return this;
    }

    public boolean hasDivider() {
        return divider;
    }

    public String getName() {
        return name;
    }

    public int getNameRes() {
        return nameRes;
    }

    @Override
    public int getIdentifier() {
        return -1;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getType() {
        return "SECTION_ITEM";
    }

    @Override
    public int getLayoutRes() {
        return R.layout.drawer_item_section;
    }


    @Override
    public View convertView(Activity activity, LayoutInflater inflater, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(getLayoutRes(), parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.view.setClickable(false);
        viewHolder.view.setEnabled(false);

        if (this.getNameRes() != -1) {
            viewHolder.name.setText(this.getNameRes());
        } else {
            viewHolder.name.setText(this.getName());
        }

        if (this.hasDivider()) {
            viewHolder.divider.setVisibility(View.VISIBLE);
        } else {
            viewHolder.divider.setVisibility(View.GONE);
        }

        //viewHolder.name.setTextColor(UIUtils.getTextColor(activity, activity.getResources().getColor(R.color.material_drawer_secondary_text)));

        return convertView;
    }

    private class ViewHolder {
        private View view;
        private View divider;
        private TextView name;

        private ViewHolder(View view) {
            this.view = view;
            this.divider = view.findViewById(R.id.divider);
            this.name = (TextView) view.findViewById(R.id.name);
        }
    }
}
