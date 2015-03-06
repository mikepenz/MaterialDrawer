package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;
import com.mikepenz.materialdrawer.util.UIUtils;

/**
 * Created by mikepenz on 03.02.15.
 */
public class ProfileDrawerItem implements IDrawerItem, IProfile<ProfileDrawerItem>, Tagable<ProfileDrawerItem> {

    private int identifier = -1;

    private boolean selectable = true;
    private boolean nameShown = false;

    private Drawable icon;

    private String name;
    private String email;

    private boolean enabled = true;
    private Object tag;

    private int selectedColor = -1;
    private int selectedColorRes = -1;

    private int textColor = -1;
    private int textColorRes = -1;

    public ProfileDrawerItem withIdentifier(int identifier) {
        this.identifier = identifier;
        return this;
    }

    public ProfileDrawerItem withIcon(Drawable icon) {
        this.icon = icon;
        return this;
    }

    public ProfileDrawerItem withName(String name) {
        this.name = name;
        return this;
    }

    public ProfileDrawerItem withEmail(String email) {
        this.email = email;
        return this;
    }

    public ProfileDrawerItem withTag(Object object) {
        this.tag = object;
        return this;
    }

    public ProfileDrawerItem setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public ProfileDrawerItem withNameShown(boolean nameShown) {
        this.nameShown = nameShown;
        return this;
    }

    public ProfileDrawerItem withSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
        return this;
    }

    public ProfileDrawerItem withSelectedColorRes(int selectedColorRes) {
        this.selectedColorRes = selectedColorRes;
        return this;
    }

    public ProfileDrawerItem withTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public ProfileDrawerItem withTextColorRes(int textColorRes) {
        this.textColorRes = textColorRes;
        return this;
    }

    @Override
    public ProfileDrawerItem withSelectable(boolean selectable) {
        this.selectable = selectable;
        return this;
    }

    public boolean isNameShown() {
        return nameShown;
    }

    public void setNameShown(boolean nameShown) {
        this.nameShown = nameShown;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public int getSelectedColorRes() {
        return selectedColorRes;
    }

    public void setSelectedColorRes(int selectedColorRes) {
        this.selectedColorRes = selectedColorRes;
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
    public Object getTag() {
        return tag;
    }

    @Override
    public void setTag(Object tag) {
        this.tag = tag;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public boolean isSelectable() {
        return selectable;
    }

    @Override
    public ProfileDrawerItem setSelectable(boolean selectable) {
        this.selectable = selectable;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getType() {
        return "PROFILE_ITEM";
    }

    @Override
    public int getLayoutRes() {
        return R.layout.material_drawer_item_profile;
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

        int selected_color = selectedColor;
        if (selected_color == -1 && selectedColorRes != -1) {
            selected_color = ctx.getResources().getColor(selectedColorRes);
        } else if (selected_color == -1) {
            selected_color = UIUtils.getThemeColor(ctx, R.attr.material_drawer_selected);
        }
        UIUtils.setBackground(viewHolder.view, UIUtils.getDrawerItemBackground(ctx, selected_color));

        if (nameShown) {
            viewHolder.name.setVisibility(View.VISIBLE);
            viewHolder.name.setText(this.getName());
        } else {
            viewHolder.name.setVisibility(View.GONE);
        }
        viewHolder.email.setText(this.getEmail());

        int color = textColor;
        if (color == -1 && textColorRes != -1) {
            color = ctx.getResources().getColor(textColorRes);
        } else if (color == -1) {
            color = UIUtils.getThemeColor(ctx, R.attr.material_drawer_primary_text);
        }
        if (nameShown) {
            viewHolder.name.setTextColor(color);
        }
        viewHolder.email.setTextColor(color);

        if (this.getIcon() != null) {
            viewHolder.profileIcon.setImageDrawable(this.getIcon());
            viewHolder.profileIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.profileIcon.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private static class ViewHolder {
        private View view;
        private ImageView profileIcon;
        private TextView name;
        private TextView email;

        private ViewHolder(View view) {
            this.view = view;
            this.profileIcon = (ImageView) view.findViewById(R.id.profileIcon);
            this.name = (TextView) view.findViewById(R.id.name);
            this.email = (TextView) view.findViewById(R.id.email);
        }
    }
}
