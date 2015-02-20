package com.mikepenz.materialdrawer.model;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Iconable;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.UIUtils;

/**
 * Created by mikepenz on 03.02.15.
 */
public class SecondaryDrawerItem implements IDrawerItem, Nameable<SecondaryDrawerItem>, Iconable<SecondaryDrawerItem>, Badgeable<SecondaryDrawerItem> {

    private int identifier = -1;
    private Drawable icon;
    private IIcon iicon;
    private Drawable selectedIcon;
    private String name;
    private int nameRes = -1;
    private String badge;
    private boolean enabled = true;

    public SecondaryDrawerItem withIdentifier(int identifier) {
        this.identifier = identifier;
        return this;
    }

    public SecondaryDrawerItem withIcon(Drawable icon) {
        this.icon = icon;
        return this;
    }

    public SecondaryDrawerItem withIcon(IIcon iicon) {
        this.iicon = iicon;
        return this;
    }

    public SecondaryDrawerItem withSelectedIcon(Drawable selectedIcon) {
        this.selectedIcon = selectedIcon;
        return this;
    }

    public SecondaryDrawerItem withName(String name) {
        this.name = name;
        return this;
    }

    public SecondaryDrawerItem withName(int nameRes) {
        this.nameRes = nameRes;
        return this;
    }

    public SecondaryDrawerItem withBadge(String badge) {
        this.badge = badge;
        return this;
    }

    public SecondaryDrawerItem setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Drawable getIcon() {
        return icon;
    }

    public IIcon getIIcon() {
        return iicon;
    }

    @Override
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public void setIIcon(IIcon iicon) {
        this.iicon = iicon;
    }

    public Drawable getSelectedIcon() {
        return selectedIcon;
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

    public String getBadge() {
        return badge;
    }

    @Override
    public void setBadge(String badge) {
        this.badge = badge;
    }

    @Override
    public int getIdentifier() {
        return identifier;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getType() {
        return "SECONDARY_ITEM";
    }

    @Override
    public int getLayoutRes() {
        return R.layout.drawer_item_secondary;
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

        UIUtils.setBackground(viewHolder.view, UIUtils.getDrawerItemBackground(activity));

        if (this.getNameRes() != -1) {
            viewHolder.name.setText(this.getNameRes());
        } else {
            viewHolder.name.setText(this.getName());
        }

        if (badge != null) {
            viewHolder.badge.setText(badge);
            viewHolder.badge.setVisibility(View.VISIBLE);
        } else {
            viewHolder.badge.setVisibility(View.GONE);
        }

        int color;
        int selectedColor = activity.getResources().getColor(R.color.material_drawer_selected_text);
        if (this.isEnabled()) {
            color = activity.getResources().getColor(R.color.material_drawer_secondary_text);
            viewHolder.name.setTextColor(UIUtils.getTextColor(color, selectedColor));
            viewHolder.badge.setTextColor(UIUtils.getTextColor(color, selectedColor));
        } else {
            color = activity.getResources().getColor(R.color.material_drawer_hint_text);
            viewHolder.name.setTextColor(color);
            viewHolder.badge.setTextColor(color);
        }

        Drawable icon = null;
        Drawable selectedIcon = null;
        if (this.getIcon() != null) {
            icon = this.getIcon();

            if (this.getSelectedIcon() != null) {
                selectedIcon = this.getSelectedIcon();
            }
        } else if (this.getIIcon() != null) {
            icon = new IconicsDrawable(activity, this.getIIcon()).color(color).actionBarSize().paddingDp(1);
            selectedIcon = new IconicsDrawable(activity, this.getIIcon()).color(selectedColor).actionBarSize().paddingDp(1);
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

    private class ViewHolder {
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
