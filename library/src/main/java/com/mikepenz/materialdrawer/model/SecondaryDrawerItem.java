package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.ColorfulBadgeable;
import com.mikepenz.materialdrawer.util.PressedEffectStateListDrawable;
import com.mikepenz.materialdrawer.util.UIUtils;

/**
 * Created by mikepenz on 03.02.15.
 */
public class SecondaryDrawerItem extends BaseDrawerItem<SecondaryDrawerItem> implements ColorfulBadgeable<SecondaryDrawerItem> {

    private String badge;
    private int badgeTextColor = 0;

    public SecondaryDrawerItem withBadge(String badge) {
        this.badge = badge;
        return this;
    }

    public String getBadge() {
        return badge;
    }

    @Override
    public void setBadge(String badge) {
        this.badge = badge;
    }

    @Override
    public SecondaryDrawerItem withBadgeTextColor(int color) {
        this.badgeTextColor = color;
        return this;
    }

    @Override
    public int getBadgeTextColor() {
        return badgeTextColor;
    }

    @Override
    public void setBadgeTextColor(int color) { this.badgeTextColor = color; }

    @Override
    public String getType() {
        return "SECONDARY_ITEM";
    }

    @Override
    public int getLayoutRes() {
        return R.layout.material_drawer_item_secondary;
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

        int selected_color = getSelectedColor();
        if (selected_color == 0 && getSelectedColorRes() != -1) {
            selected_color = ctx.getResources().getColor(getSelectedColorRes());
        } else if (selected_color == 0) {
            selected_color = UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_selected, R.color.material_drawer_selected);
        }
        UIUtils.setBackground(viewHolder.view, UIUtils.getDrawerItemBackground(selected_color));

        if (this.getNameRes() != -1) {
            viewHolder.name.setText(this.getNameRes());
        } else {
            viewHolder.name.setText(this.getName());
        }

        if (getBadge() != null) {
            viewHolder.badge.setText(getBadge());
            viewHolder.badge.setVisibility(View.VISIBLE);
        } else {
            viewHolder.badge.setVisibility(View.GONE);
        }

        //get the correct color for the text
        int color;
        int selected_text = getSelectedTextColor();
        if (selected_text == 0 && getSelectedTextColorRes() != -1) {
            selected_text = ctx.getResources().getColor(getSelectedTextColorRes());
        } else if (selected_text == 0) {
            selected_text = UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_selected_text, R.color.material_drawer_selected_text);
        }
        if (this.isEnabled()) {
            color = getTextColor();
            if (color == 0 && getTextColorRes() != -1) {
                color = ctx.getResources().getColor(getTextColorRes());
            } else if (color == 0) {
                color = UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_secondary_text, R.color.material_drawer_secondary_text);
            }
        } else {
            color = getDisabledTextColor();
            if (color == 0 && getDisabledTextColorRes() != -1) {
                color = ctx.getResources().getColor(getDisabledTextColorRes());
            } else if (color == 0) {
                color = UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_hint_text, R.color.material_drawer_hint_text);
            }
        }

        //get the correct color for the icon
        int iconColor;
        int selected_icon = getSelectedIconColor();
        if (selected_icon == 0 && getSelectedIconColorRes() != -1) {
            selected_icon = ctx.getResources().getColor(getSelectedIconColorRes());
        } else if (selected_icon == 0) {
            selected_icon = UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_selected_text, R.color.material_drawer_selected_text);
        }
        if (this.isEnabled()) {
            iconColor = getIconColor();
            if (iconColor == 0 && getIconColorRes() != -1) {
                iconColor = ctx.getResources().getColor(getIconColorRes());
            } else if (iconColor == 0) {
                iconColor = UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_primary_icon, R.color.material_drawer_primary_icon);
            }
        } else {
            iconColor = getDisabledIconColor();
            if (iconColor == 0 && getDisabledIconColorRes() != -1) {
                iconColor = ctx.getResources().getColor(getDisabledIconColorRes());
            } else if (iconColor == 0) {
                iconColor = UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_hint_text, R.color.material_drawer_hint_text);
            }
        }

        viewHolder.name.setTextColor(UIUtils.getTextColor(color, selected_text));
        if (badgeTextColor != 0) {
            viewHolder.badge.setTextColor(badgeTextColor);
        } else viewHolder.badge.setTextColor(UIUtils.getTextColor(color, selected_text));

        if (getTypeface() != null) {
            viewHolder.name.setTypeface(getTypeface());
            viewHolder.badge.setTypeface(getTypeface());
        }

        Drawable icon = null;
        Drawable selectedIcon = null;
        if (this.getIcon() != null) {
            icon = this.getIcon();

            if (this.getSelectedIcon() != null) {
                selectedIcon = this.getSelectedIcon();
            } else if (this.isSelectedIconTinted()) {
                icon = new PressedEffectStateListDrawable(icon, selected_icon);
            }
        } else if (this.getIIcon() != null) {
            icon = new IconicsDrawable(ctx, this.getIIcon()).color(iconColor).actionBarSize().paddingDp(1);
            selectedIcon = new IconicsDrawable(ctx, this.getIIcon()).color(selected_icon).actionBarSize().paddingDp(1);
        } else if (this.getIconRes() > -1) {
            icon = UIUtils.getCompatDrawable(ctx, getIconRes());

            if (this.getSelectedIconRes() > -1) {
                selectedIcon = UIUtils.getCompatDrawable(ctx, getSelectedIconRes());
            } else if (this.isSelectedIconTinted()) {
                icon = new PressedEffectStateListDrawable(icon, selected_icon);
            }
        }

        if (icon != null) {
            if (selectedIcon != null) {
                viewHolder.icon.setImageDrawable(UIUtils.getIconColor(icon, selectedIcon));
            } else {
                viewHolder.icon.setImageDrawable(icon);
            }

            viewHolder.icon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.icon.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class ViewHolder {
        private View view;
        private ImageView icon;
        private TextView name;
        private TextView badge;

        private ViewHolder(View view) {
            this.view = view;
            this.icon = (ImageView) view.findViewById(R.id.icon);
            this.name = (TextView) view.findViewById(R.id.name);
            this.badge = (TextView) view.findViewById(R.id.badge);
        }
    }
}
