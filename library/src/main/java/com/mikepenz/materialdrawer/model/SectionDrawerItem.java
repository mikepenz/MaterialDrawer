package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

/**
 * Created by mikepenz on 03.02.15.
 */
public class SectionDrawerItem implements IDrawerItem, Nameable<SectionDrawerItem>, Tagable<SectionDrawerItem>, Typefaceable<SectionDrawerItem> {

    private int identifier = -1;

    private StringHolder name;
    private boolean divider = true;
    private Object tag;

    private int textColor = 0;
    private int textColorRes = -1;

    private Typeface typeface = null;

    public SectionDrawerItem withIdentifier(int identifier) {
        this.identifier = identifier;
        return this;
    }

    public SectionDrawerItem withName(String name) {
        this.name = new StringHolder(name);
        return this;
    }

    public SectionDrawerItem withName(int nameRes) {
        this.name = new StringHolder(nameRes);
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

    public SectionDrawerItem withTypeface(Typeface typeface) {
        this.typeface = typeface;
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

    public StringHolder getName() {
        return name;
    }

    @Override
    public int getIdentifier() {
        return identifier;
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
    public Typeface getTypeface() {
        return typeface;
    }

    @Override
    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
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

        //set the identifier from the drawerItem here. It can be used to run tests
        convertView.setId(getIdentifier());

        viewHolder.view.setClickable(false);
        viewHolder.view.setEnabled(false);

        textColor = DrawerUIUtils.decideColor(ctx, getTextColor(), getTextColorRes(), R.attr.material_drawer_secondary_text, R.color.material_drawer_secondary_text);
        viewHolder.name.setTextColor(textColor);

        //set the text for the name
        StringHolder.applyTo(this.getName(), viewHolder.name);

        if (this.hasDivider()) {
            viewHolder.divider.setVisibility(View.VISIBLE);
        } else {
            viewHolder.divider.setVisibility(View.GONE);
        }
        //set the color for the divider
        viewHolder.divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(parent.getContext(), R.attr.material_drawer_divider, R.color.material_drawer_divider));

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
