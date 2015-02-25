package com.mikepenz.materialdrawer.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;

/**
 * Created by mikepenz on 03.02.15.
 */
public class SectionDrawerItem implements IDrawerItem, Nameable<SectionDrawerItem>, Tagable<SectionDrawerItem> {

    private String name;
    private int nameRes = -1;
    private boolean divider = true;
    private Object tag;

    public SectionDrawerItem withName(String name) {
        this.name = name;
        return this;
    }

    public SectionDrawerItem withName(int nameRes) {
        this.nameRes = nameRes;
        return this;
    }

    public SectionDrawerItem withTag(Object object) {
        this.tag = object;
        return this;
    }

    public SectionDrawerItem setDivider(boolean divider) {
        this.divider = divider;
        return this;
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public void setTag(Object tag) {
        this.tag = tag;
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
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setNameRes(int nameRes) {
        this.nameRes = nameRes;
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
    public View convertView(LayoutInflater inflater, View convertView, ViewGroup parent) {
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

    private static class ViewHolder {
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
