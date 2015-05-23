package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Identifyable;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable;
import com.mikepenz.materialdrawer.util.UIUtils;

/**
 * Created by mikepenz on 03.02.15.
 */
public class ProfileSettingDrawerItem implements IDrawerItem, IProfile<ProfileSettingDrawerItem>, Tagable<ProfileSettingDrawerItem>, Identifyable<ProfileSettingDrawerItem>, Typefaceable<ProfileSettingDrawerItem> {

    private int identifier = -1;

    private boolean selectable = false;

    private Drawable icon;
    private Bitmap iconBitmap;
    private IIcon iicon;
    private Uri iconUri;

    private String name;
    private String email;

    private boolean enabled = true;
    private Object tag;

    private boolean iconTinted = false;

    private int selectedColor = 0;
    private int selectedColorRes = -1;

    private int textColor = 0;
    private int textColorRes = -1;

    private int iconColor = 0;
    private int iconColorRes = -1;

    private Typeface typeface = null;

    public ProfileSettingDrawerItem withIdentifier(int identifier) {
        this.identifier = identifier;
        return this;
    }

    public ProfileSettingDrawerItem withIcon(Drawable icon) {
        this.icon = icon;
        return this;
    }

    public ProfileSettingDrawerItem withIcon(Bitmap icon) {
        this.iconBitmap = icon;
        return this;
    }

    public ProfileSettingDrawerItem withIcon(IIcon iicon) {
        this.iicon = iicon;
        return this;
    }

    @Override
    public ProfileSettingDrawerItem withIcon(String url) {
        this.iconUri = Uri.parse(url);
        return this;
    }

    @Override
    public ProfileSettingDrawerItem withIcon(Uri uri) {
        this.iconUri = uri;
        return this;
    }

    public ProfileSettingDrawerItem withName(String name) {
        this.name = name;
        return this;
    }

    public ProfileSettingDrawerItem withDescription(String description) {
        this.email = description;
        return this;
    }

    //NOTE we reuse the IProfile here to allow custom items within the AccountSwitcher. There is an alias method withDescription for this
    public ProfileSettingDrawerItem withEmail(String email) {
        this.email = email;
        return this;
    }

    public ProfileSettingDrawerItem withTag(Object object) {
        this.tag = object;
        return this;
    }

    public ProfileSettingDrawerItem setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public ProfileSettingDrawerItem withEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public ProfileSettingDrawerItem withSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
        return this;
    }

    public ProfileSettingDrawerItem withSelectedColorRes(int selectedColorRes) {
        this.selectedColorRes = selectedColorRes;
        return this;
    }

    public ProfileSettingDrawerItem withTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public ProfileSettingDrawerItem withTextColorRes(int textColorRes) {
        this.textColorRes = textColorRes;
        return this;
    }

    public ProfileSettingDrawerItem withIconColor(int iconColor) {
        this.iconColor = iconColor;
        return this;
    }

    public ProfileSettingDrawerItem withIconColorRes(int iconColorRes) {
        this.iconColorRes = iconColorRes;
        return this;
    }

    @Override
    public ProfileSettingDrawerItem withSelectable(boolean selectable) {
        this.selectable = selectable;
        return this;
    }

    public ProfileSettingDrawerItem withTypeface(Typeface typeface) {
        this.typeface = typeface;
        return this;
    }

    public ProfileSettingDrawerItem withIconTinted(boolean iconTinted) {
        this.iconTinted = iconTinted;
        return this;
    }

    @Override
    public Bitmap getIconBitmap() {
        return iconBitmap;
    }

    @Override
    public void setIconBitmap(Bitmap iconBitmap) {
        this.iconBitmap = iconBitmap;
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

    public int getIconColorRes() {
        return iconColorRes;
    }

    public void setIconColorRes(int iconColorRes) {
        this.iconColorRes = iconColorRes;
    }

    public int getIconColor() {
        return iconColor;
    }

    public void setIconColor(int iconColor) {
        this.iconColor = iconColor;
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

    public IIcon getIIcon() {
        return iicon;
    }

    public void setIIcon(IIcon iicon) {
        this.iicon = iicon;
    }

    public void setIcon(Uri uri) {
        this.iconUri = uri;
    }

    public void setIcon(String url) {
        this.iconUri = Uri.parse(url);
    }

    @Override
    public Uri getIconUri() {
        return iconUri;
    }

    @Override
    public boolean isSelectable() {
        return selectable;
    }

    @Override
    public ProfileSettingDrawerItem setSelectable(boolean selectable) {
        this.selectable = selectable;
        return this;
    }

    public boolean isIconTinted() {
        return iconTinted;
    }

    public void setIconTinted(boolean iconTinted) {
        this.iconTinted = iconTinted;
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

    public String getDescription() {
        return email;
    }

    public void setDescription(String description) {
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
        return "PROFILE_SETTING_ITEM";
    }

    @Override
    public int getLayoutRes() {
        return R.layout.material_drawer_item_profile_setting;
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

        //get the correct color for the background
        int selectedColor = UIUtils.decideColor(ctx, getSelectedColor(), getSelectedColorRes(), R.attr.material_drawer_selected, R.color.material_drawer_selected);
        //get the correct color for the text
        int color = UIUtils.decideColor(ctx, getTextColor(), getTextColorRes(), R.attr.material_drawer_primary_text, R.color.material_drawer_primary_text);
        int iconColor = UIUtils.decideColor(ctx, getIconColor(), getIconColorRes(), R.attr.material_drawer_primary_icon, R.color.material_drawer_primary_icon);

        UIUtils.setBackground(viewHolder.view, UIUtils.getDrawerItemBackground(selectedColor));

        viewHolder.name.setText(this.getName());
        viewHolder.name.setTextColor(color);

        if (getTypeface() != null) {
            viewHolder.name.setTypeface(getTypeface());
        }

        //get the correct icon
        if (this.getIcon() != null) {
            if (icon != null && isIconTinted()) {
                icon.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
            }
            viewHolder.icon.setImageDrawable(icon);
            viewHolder.icon.setVisibility(View.VISIBLE);
        } else if (this.getIconBitmap() != null) {
            viewHolder.icon.setImageBitmap(iconBitmap);
            viewHolder.icon.setVisibility(View.VISIBLE);
        } else if (this.getIIcon() != null) {
            viewHolder.icon.setImageDrawable(new IconicsDrawable(ctx, this.getIIcon()).color(iconColor).actionBarSize().paddingDp(2));
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

        private ViewHolder(View view) {
            this.view = view;
            this.icon = (ImageView) view.findViewById(R.id.icon);
            this.name = (TextView) view.findViewById(R.id.name);
        }
    }
}
