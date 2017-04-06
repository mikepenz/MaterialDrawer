package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable;
import com.mikepenz.materialize.util.UIUtils;

import java.util.List;

/**
 * Created by mikepenz on 03.02.15.
 */
public class SectionDrawerItem extends AbstractDrawerItem<SectionDrawerItem, SectionDrawerItem.ViewHolder> implements Nameable<SectionDrawerItem>, Typefaceable<SectionDrawerItem> {

    private StringHolder name;
    private boolean divider = true;

    private ColorHolder textColor;

    private Typeface typeface = null;

    public SectionDrawerItem withName(StringHolder name) {
        this.name = name;
        return this;
    }

    public SectionDrawerItem withName(String name) {
        this.name = new StringHolder(name);
        return this;
    }

    public SectionDrawerItem withName(@StringRes int nameRes) {
        this.name = new StringHolder(nameRes);
        return this;
    }

    public SectionDrawerItem withDivider(boolean divider) {
        this.divider = divider;
        return this;
    }

    public SectionDrawerItem withTextColor(int textColor) {
        this.textColor = ColorHolder.fromColor(textColor);
        return this;
    }

    public SectionDrawerItem withTextColorRes(int textColorRes) {
        this.textColor = ColorHolder.fromColorRes(textColorRes);
        return this;
    }

    public SectionDrawerItem withTypeface(Typeface typeface) {
        this.typeface = typeface;
        return this;
    }

    public boolean hasDivider() {
        return divider;
    }

    public ColorHolder getTextColor() {
        return textColor;
    }

    public StringHolder getName() {
        return name;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public int getType() {
        return R.id.material_drawer_item_section;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_section;
    }

    @Override
    public Typeface getTypeface() {
        return typeface;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List payloads) {
        super.bindView(viewHolder, payloads);

        Context ctx = viewHolder.itemView.getContext();

        //set the identifier from the drawerItem here. It can be used to run tests
        viewHolder.itemView.setId(hashCode());

        //define this item to be not clickable nor enabled
        viewHolder.view.setClickable(false);
        viewHolder.view.setEnabled(false);

        //define the text color
        viewHolder.name.setTextColor(ColorHolder.color(getTextColor(), ctx, R.attr.material_drawer_secondary_text, R.color.material_drawer_secondary_text));

        //set the text for the name
        StringHolder.applyTo(this.getName(), viewHolder.name);

        //define the typeface for our textViews
        if (getTypeface() != null) {
            viewHolder.name.setTypeface(getTypeface());
        }

        //hide the divider if we do not need one
        if (this.hasDivider()) {
            viewHolder.divider.setVisibility(View.VISIBLE);
        } else {
            viewHolder.divider.setVisibility(View.GONE);
        }

        //set the color for the divider
        viewHolder.divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_divider, R.color.material_drawer_divider));

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, viewHolder.itemView);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private View divider;
        private TextView name;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            this.divider = view.findViewById(R.id.material_drawer_divider);
            this.name = (TextView) view.findViewById(R.id.material_drawer_name);
        }
    }
}
