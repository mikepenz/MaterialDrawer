package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;
import com.mikepenz.materialdrawer.util.UIUtils;

/**
 * Created by mikepenz on 03.02.15.
 */
public class SectionDrawerItem implements IDrawerItem, Nameable<SectionDrawerItem>, Tagable<SectionDrawerItem> {

    private String name;
    private int nameRes = -1;
    private boolean divider = true;
    private Object tag;

    private int textColor = -1;
    private int textColorRes = -1;


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

    public SectionDrawerItem withTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public SectionDrawerItem withTextColorRes(int textColorRes) {
        this.textColorRes = textColorRes;
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
        return R.layout.material_drawer_item_section;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextColorRes() {
        return textColorRes;
    }

    public void setTextColorRes(int textColorRes) {
        this.textColorRes = textColorRes;
    }


    @Override
    public View convertView(LayoutInflater inflater, View convertView, ViewGroup parent) {
        Context ctx = parent.getContext();

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

        int text_color = textColor;
        if (text_color == -1 && textColorRes != -1) {
            text_color = ctx.getResources().getColor(textColorRes);
        } else if (text_color == -1) {
            text_color = UIUtils.getThemeColor(ctx, R.attr.material_drawer_hint_text);
        }
        viewHolder.name.setTextColor(text_color);

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
