package com.mikepenz.materialdrawer.app.drawerItems;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.app.R;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.model.AbstractDrawerItem;

import java.util.List;

/**
 * Created by mikepenz on 03.02.15.
 */
public class IconDrawerItem extends AbstractDrawerItem<IconDrawerItem, IconDrawerItem.ViewHolder> {
    protected ImageHolder icon;
    protected ImageHolder selectedIcon;

    protected boolean iconTinted = false;

    protected ColorHolder iconColor;
    protected ColorHolder selectedIconColor;
    protected ColorHolder disabledIconColor;

    public IconDrawerItem withIcon(ImageHolder icon) {
        this.icon = icon;
        return this;
    }

    public IconDrawerItem withIcon(Drawable icon) {
        this.icon = new ImageHolder(icon);
        return this;
    }

    public IconDrawerItem withIcon(@DrawableRes int iconRes) {
        this.icon = new ImageHolder(iconRes);
        return this;
    }

    public IconDrawerItem withSelectedIcon(Drawable selectedIcon) {
        this.selectedIcon = new ImageHolder(selectedIcon);
        return this;
    }

    public IconDrawerItem withSelectedIcon(@DrawableRes int selectedIconRes) {
        this.selectedIcon = new ImageHolder(selectedIconRes);
        return this;
    }

    public IconDrawerItem withIcon(IIcon iicon) {
        this.icon = new ImageHolder(iicon);
        //if we are on api 21 or higher we use the IconicsDrawable for selection too and color it with the correct color
        //else we use just the one drawable and enable tinting on press
        if (Build.VERSION.SDK_INT >= 21) {
            this.selectedIcon = new ImageHolder(iicon);
        } else {
            this.withIconTintingEnabled(true);
        }

        return this;
    }

    public IconDrawerItem withIconColor(@ColorInt int iconColor) {
        this.iconColor = ColorHolder.fromColor(iconColor);
        return this;
    }

    public IconDrawerItem withIconColorRes(@ColorRes int iconColorRes) {
        this.iconColor = ColorHolder.fromColorRes(iconColorRes);
        return this;
    }

    public IconDrawerItem withSelectedIconColor(@ColorInt int selectedIconColor) {
        this.selectedIconColor = ColorHolder.fromColor(selectedIconColor);
        return this;
    }

    public IconDrawerItem withSelectedIconColorRes(@ColorRes int selectedColorRes) {
        this.selectedIconColor = ColorHolder.fromColorRes(selectedColorRes);
        return this;
    }

    public IconDrawerItem withDisabledIconColor(@ColorInt int disabledIconColor) {
        this.disabledIconColor = ColorHolder.fromColor(disabledIconColor);
        return this;
    }

    public IconDrawerItem withDisabledIconColorRes(@ColorRes int disabledIconColorRes) {
        this.disabledIconColor = ColorHolder.fromColorRes(disabledIconColorRes);
        return this;
    }

    /**
     * will tint the icon with the default (or set) colors
     * (default and selected state)
     *
     * @param iconTintingEnabled
     * @return
     */
    public IconDrawerItem withIconTintingEnabled(boolean iconTintingEnabled) {
        this.iconTinted = iconTintingEnabled;
        return this;
    }

    @Deprecated
    public IconDrawerItem withIconTinted(boolean iconTinted) {
        this.iconTinted = iconTinted;
        return this;
    }

    /**
     * for backwards compatibility - withIconTinted..
     *
     * @param iconTinted
     * @return
     */
    @Deprecated
    public IconDrawerItem withTintSelectedIcon(boolean iconTinted) {
        return withIconTintingEnabled(iconTinted);
    }


    public boolean isIconTinted() {
        return iconTinted;
    }

    public ImageHolder getIcon() {
        return icon;
    }

    public ImageHolder getSelectedIcon() {
        return selectedIcon;
    }


    public ColorHolder getDisabledIconColor() {
        return disabledIconColor;
    }

    public ColorHolder getSelectedIconColor() {
        return selectedIconColor;
    }

    public ColorHolder getIconColor() {
        return iconColor;
    }

    @Override
    public int getType() {
        return R.id.material_drawer_item_icon_only;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_icon_only;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List payloads) {
        super.bindView(viewHolder, payloads);

        Context ctx = viewHolder.itemView.getContext();

        //set the identifier from the drawerItem here. It can be used to run tests
        viewHolder.itemView.setId(hashCode());

        //get the correct color for the icon
        int iconColor;
        if (this.isEnabled()) {
            iconColor = ColorHolder.color(getIconColor(), ctx, R.attr.material_drawer_primary_icon, R.color.material_drawer_primary_icon);
        } else {
            iconColor = ColorHolder.color(getDisabledIconColor(), ctx, R.attr.material_drawer_hint_icon, R.color.material_drawer_hint_icon);
        }
        int selectedIconColor = ColorHolder.color(getSelectedIconColor(), ctx, R.attr.material_drawer_selected_text, R.color.material_drawer_selected_text);

        //get the drawables for our icon and set it
        Drawable icon = ImageHolder.decideIcon(getIcon(), ctx, iconColor, isIconTinted(), 1);
        Drawable selectedIcon = ImageHolder.decideIcon(getSelectedIcon(), ctx, selectedIconColor, isIconTinted(), 1);
        ImageHolder.applyMultiIconTo(icon, iconColor, selectedIcon, selectedIconColor, isIconTinted(), viewHolder.icon);

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, viewHolder.itemView);
    }

    @Override
    public ViewHolderFactory getFactory() {
        return new ItemFactory();
    }

    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        protected ImageView icon;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            this.icon = (ImageView) view.findViewById(R.id.material_drawer_icon);
        }
    }
}
