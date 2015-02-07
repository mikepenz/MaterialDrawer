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
import com.mikepenz.materialdrawer.util.UIUtils;

/**
 * Created by mikepenz on 03.02.15.
 */
public class PrimaryDrawerItem implements IDrawerItem {

    private int identifier = -1;
    private Drawable icon;
    private IIcon iicon;
    private String name;
    private int nameRes = -1;
    private boolean enabled = true;

    public PrimaryDrawerItem withIdentifier(int identifier) {
        this.identifier = identifier;
        return this;
    }

    public PrimaryDrawerItem withIcon(Drawable icon) {
        this.icon = icon;
        return this;
    }

    public PrimaryDrawerItem withIcon(IIcon iicon) {
        this.iicon = iicon;
        return this;
    }

    public PrimaryDrawerItem withName(String name) {
        this.name = name;
        return this;
    }

    public PrimaryDrawerItem withName(int nameRes) {
        this.nameRes = nameRes;
        return this;
    }

    public PrimaryDrawerItem setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Drawable getIcon() {
        return icon;
    }

    public IIcon getIIcon() {
        return iicon;
    }

    public String getName() {
        return name;
    }

    public int getNameRes() {
        return nameRes;
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
        return "PRIMARY_ITEM";
    }

    @Override
    public int getLayoutRes() {
        return R.layout.drawer_item_primary;
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

        UIUtils.setBackground(viewHolder.view, UIUtils.getDrawerListItem(activity));
        if (this.getNameRes() != -1) {
            viewHolder.name.setText(this.getNameRes());
        } else {
            viewHolder.name.setText(this.getName());
        }

        int color;
        if (this.isEnabled()) {
            color = activity.getResources().getColor(R.color.material_drawer_primary_text);
            viewHolder.name.setTextColor(UIUtils.getTextColor(activity, color));
        } else {
            color = activity.getResources().getColor(R.color.material_drawer_hint_text);
            viewHolder.name.setTextColor(color);
        }

        viewHolder.icon.setVisibility(View.VISIBLE);
        if (this.getIIcon() != null) {
            viewHolder.icon.setImageDrawable(new IconicsDrawable(activity, this.getIIcon()).color(color).actionBarSize());
        } else if (this.getIcon() != null) {
            viewHolder.icon.setImageDrawable(this.getIcon());
        } else {
            viewHolder.icon.setVisibility(View.GONE);
        }

        return convertView;
    }


    private class ViewHolder {
        private View view;
        private ImageView icon;
        private TextView name;

        private ViewHolder(View view) {
            this.view = view;
            this.icon = (ImageView) view.findViewById(R.id.icon);
            this.name = (TextView) view.findViewById(R.id.name);
        }
    }
}
