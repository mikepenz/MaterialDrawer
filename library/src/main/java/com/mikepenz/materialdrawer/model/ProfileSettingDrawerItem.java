package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.*;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

import java.util.List;

/**
 * Created by mikepenz on 03.02.15.
 */
public class ProfileSettingDrawerItem extends AbstractDrawerItem<ProfileSettingDrawerItem, ProfileSettingDrawerItem.ViewHolder> implements IProfile<ProfileSettingDrawerItem>, Tagable<ProfileSettingDrawerItem>, Typefaceable<ProfileSettingDrawerItem> {
    private ImageHolder icon;

    private StringHolder name;
    private StringHolder description;

    private boolean iconTinted = false;

    private ColorHolder selectedColor;
    private ColorHolder textColor;
    private ColorHolder iconColor;
    private ColorHolder descriptionTextColor;

    private Typeface typeface = null;

    private boolean selectable = false;

    @Override
    public ProfileSettingDrawerItem withIcon(Drawable icon) {
        this.icon = new ImageHolder(icon);
        return this;
    }

    @Override
    public ProfileSettingDrawerItem withIcon(@DrawableRes int iconRes) {
        this.icon = new ImageHolder(iconRes);
        return this;
    }

    @Override
    public ProfileSettingDrawerItem withIcon(Bitmap icon) {
        this.icon = new ImageHolder(icon);
        return this;
    }

    @Override
    public ProfileSettingDrawerItem withIcon(IIcon iicon) {
        this.icon = new ImageHolder(iicon);
        return this;
    }

    @Override
    public ProfileSettingDrawerItem withIcon(String url) {
        this.icon = new ImageHolder(url);
        return this;
    }

    @Override
    public ProfileSettingDrawerItem withIcon(Uri uri) {
        this.icon = new ImageHolder(uri);
        return this;
    }

    public ProfileSettingDrawerItem withName(String name) {
        this.name = new StringHolder(name);
        return this;
    }

    public ProfileSettingDrawerItem withName(@StringRes int nameRes) {
        this.name = new StringHolder(nameRes);
        return this;
    }

    public ProfileSettingDrawerItem withDescription(String description) {
        this.description = new StringHolder(description);
        return this;
    }

    public ProfileSettingDrawerItem withDescription(@StringRes int descriptionRes) {
        this.description = new StringHolder(descriptionRes);
        return this;
    }

    //NOTE we reuse the IProfile here to allow custom items within the AccountSwitcher. There is an alias method withDescription for this
    public ProfileSettingDrawerItem withEmail(String email) {
        this.description = new StringHolder(email);
        return this;
    }

    public ProfileSettingDrawerItem withSelectedColor(@ColorInt int selectedColor) {
        this.selectedColor = ColorHolder.fromColor(selectedColor);
        return this;
    }

    public ProfileSettingDrawerItem withSelectedColorRes(@ColorRes int selectedColorRes) {
        this.selectedColor = ColorHolder.fromColorRes(selectedColorRes);
        return this;
    }

    public ProfileSettingDrawerItem withTextColor(@ColorInt int textColor) {
        this.textColor = ColorHolder.fromColor(textColor);
        return this;
    }

    public ProfileSettingDrawerItem withTextColorRes(@ColorRes int textColorRes) {
        this.textColor = ColorHolder.fromColorRes(textColorRes);
        return this;
    }

    public ProfileSettingDrawerItem withDescriptionTextColor(@ColorInt int descriptionColor) {
        this.descriptionTextColor = ColorHolder.fromColor(descriptionColor);
        return this;
    }

    public ProfileSettingDrawerItem withDescriptionTextColorRes(@ColorRes int descriptionColorRes) {
        this.descriptionTextColor = ColorHolder.fromColorRes(descriptionColorRes);
        return this;
    }

    public ProfileSettingDrawerItem withIconColor(@ColorInt int iconColor) {
        this.iconColor = ColorHolder.fromColor(iconColor);
        return this;
    }

    public ProfileSettingDrawerItem withIconColorRes(@ColorRes int iconColorRes) {
        this.iconColor = ColorHolder.fromColorRes(iconColorRes);
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

    public ColorHolder getSelectedColor() {
        return selectedColor;
    }

    public ColorHolder getTextColor() {
        return textColor;
    }

    public ColorHolder getDescriptionTextColor() {
        return descriptionTextColor;
    }

    public ColorHolder getIconColor() {
        return iconColor;
    }


    public ImageHolder getIcon() {
        return icon;
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
    public StringHolder getName() {
        return name;
    }

    public StringHolder getEmail() {
        return description;
    }

    public StringHolder getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = new StringHolder(description);
    }

    @Override
    public boolean isSelectable() {
        return selectable;
    }

    public ProfileSettingDrawerItem withSelectable(boolean selectable) {
        this.selectable = selectable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.material_drawer_item_profile_setting;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_profile_setting;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List payloads) {
        super.bindView(viewHolder, payloads);

        //get the context
        Context ctx = viewHolder.itemView.getContext();

        //set the identifier from the drawerItem here. It can be used to run tests
        viewHolder.itemView.setId(hashCode());

        //set the item enabled if it is
        viewHolder.itemView.setEnabled(isEnabled());

        //set the item selected if it is
        viewHolder.itemView.setSelected(isSelected());

        //get the correct color for the background
        int selectedColor = ColorHolder.color(getSelectedColor(), ctx, R.attr.material_drawer_selected, R.color.material_drawer_selected);
        //get the correct color for the text
        int color = ColorHolder.color(getTextColor(), ctx, R.attr.material_drawer_primary_text, R.color.material_drawer_primary_text);
        int iconColor = ColorHolder.color(getIconColor(), ctx, R.attr.material_drawer_primary_icon, R.color.material_drawer_primary_icon);
        int descriptionColor = ColorHolder.color(getDescriptionTextColor(), ctx, R.attr.material_drawer_primary_text, R.color.material_drawer_primary_text);

        UIUtils.setBackground(viewHolder.view, UIUtils.getSelectableBackground(ctx, selectedColor, isSelectedBackgroundAnimated()));

        StringHolder.applyTo(this.getName(), viewHolder.name);
        viewHolder.name.setTextColor(color);

        StringHolder.applyToOrHide(this.getDescription(), viewHolder.description);
        viewHolder.description.setTextColor(descriptionColor);

        if (getTypeface() != null) {
            viewHolder.name.setTypeface(getTypeface());
        }

        //set the correct icon
        ImageHolder.applyDecidedIconOrSetGone(icon, viewHolder.icon, iconColor, isIconTinted(), 2);

        //for android API 17 --> Padding not applied via xml
        DrawerUIUtils.setDrawerVerticalPadding(viewHolder.view);

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, viewHolder.itemView);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private ImageView icon;
        private TextView name;
        private TextView description;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            this.icon = (ImageView) view.findViewById(R.id.material_drawer_icon);
            this.name = (TextView) view.findViewById(R.id.material_drawer_name);
            this.description = (TextView) view.findViewById(R.id.material_drawer_description);
        }
    }
}