package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Pair;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.interfaces.Iconable;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

/**
 * Created by mikepenz on 03.02.15.
 */
public abstract class BaseDrawerItem<T> extends AbstractDrawerItem<T> implements Nameable<T>, Iconable<T>, Tagable<T>, Typefaceable<T> {
    private ImageHolder icon;
    private ImageHolder selectedIcon;
    private StringHolder name;

    private boolean iconTinted = false;

    private ColorHolder selectedColor;
    private ColorHolder textColor;
    private ColorHolder selectedTextColor;
    private ColorHolder disabledTextColor;

    private ColorHolder iconColor;
    private ColorHolder selectedIconColor;
    private ColorHolder disabledIconColor;

    private Typeface typeface = null;

    private Pair<Integer, ColorStateList> colorStateList;

    public T withIcon(Drawable icon) {
        this.icon = new ImageHolder(icon);
        return (T) this;
    }

    public T withIcon(int iconRes) {
        this.icon = new ImageHolder(iconRes);
        return (T) this;
    }

    public T withSelectedIcon(Drawable selectedIcon) {
        this.selectedIcon = new ImageHolder(selectedIcon);
        return (T) this;
    }

    public T withSelectedIcon(int selectedIconRes) {
        this.selectedIcon = new ImageHolder(selectedIconRes);
        return (T) this;
    }

    public T withIcon(IIcon iicon) {
        this.icon = new ImageHolder(iicon);
        this.selectedIcon = new ImageHolder(iicon);
        return (T) this;
    }

    public T withName(String name) {
        this.name = new StringHolder(name);
        return (T) this;
    }

    public T withName(int nameRes) {
        this.name = new StringHolder(nameRes);
        return (T) this;
    }

    public T withSelectedColor(int selectedColor) {
        this.selectedColor = ColorHolder.fromColor(selectedColor);
        return (T) this;
    }

    public T withSelectedColorRes(int selectedColorRes) {
        this.selectedColor = ColorHolder.fromColorRes(selectedColorRes);
        return (T) this;
    }

    public T withTextColor(int textColor) {
        this.textColor = ColorHolder.fromColor(textColor);
        return (T) this;
    }

    public T withTextColorRes(int textColorRes) {
        this.textColor = ColorHolder.fromColorRes(textColorRes);
        return (T) this;
    }

    public T withSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = ColorHolder.fromColor(selectedTextColor);
        return (T) this;
    }

    public T withSelectedTextColorRes(int selectedColorRes) {
        this.selectedTextColor = ColorHolder.fromColorRes(selectedColorRes);
        return (T) this;
    }

    public T withDisabledTextColor(int disabledTextColor) {
        this.disabledTextColor = ColorHolder.fromColor(disabledTextColor);
        return (T) this;
    }

    public T withDisabledTextColorRes(int disabledTextColorRes) {
        this.disabledTextColor = ColorHolder.fromColorRes(disabledTextColorRes);
        return (T) this;
    }

    public T withIconColor(int iconColor) {
        this.iconColor = ColorHolder.fromColor(iconColor);
        return (T) this;
    }

    public T withIconColorRes(int iconColorRes) {
        this.iconColor = ColorHolder.fromColorRes(iconColorRes);
        return (T) this;
    }

    public T withSelectedIconColor(int selectedIconColor) {
        this.selectedIconColor = ColorHolder.fromColor(selectedIconColor);
        return (T) this;
    }

    public T withSelectedIconColorRes(int selectedColorRes) {
        this.selectedIconColor = ColorHolder.fromColorRes(selectedColorRes);
        return (T) this;
    }

    public T withDisabledIconColor(int disabledIconColor) {
        this.disabledIconColor = ColorHolder.fromColor(disabledIconColor);
        return (T) this;
    }

    public T withDisabledIconColorRes(int disabledIconColorRes) {
        this.disabledIconColor = ColorHolder.fromColorRes(disabledIconColorRes);
        return (T) this;
    }

    /**
     * will tint the icon with the default (or set) colors
     * (default and selected state)
     *
     * @param iconTintingEnabled
     * @return
     */
    public T withIconTintingEnabled(boolean iconTintingEnabled) {
        this.iconTinted = iconTintingEnabled;
        return (T) this;
    }

    @Deprecated
    public T withIconTinted(boolean iconTinted) {
        this.iconTinted = iconTinted;
        return (T) this;
    }

    /**
     * for backwards compatibility - withIconTinted..
     *
     * @param iconTinted
     * @return
     */
    @Deprecated
    public T withTintSelectedIcon(boolean iconTinted) {
        return withIconTintingEnabled(iconTinted);
    }

    public T withTypeface(Typeface typeface) {
        this.typeface = typeface;
        return (T) this;
    }

    public ColorHolder getSelectedColor() {
        return selectedColor;
    }

    public ColorHolder getTextColor() {
        return textColor;
    }

    public ColorHolder getSelectedTextColor() {
        return selectedTextColor;
    }

    public ColorHolder getDisabledTextColor() {
        return disabledTextColor;
    }

    public boolean isIconTinted() {
        return iconTinted;
    }

    public void setIconTinted(boolean iconTinted) {
        this.iconTinted = iconTinted;
    }

    public ImageHolder getIcon() {
        return icon;
    }

    public ImageHolder getSelectedIcon() {
        return selectedIcon;
    }

    public StringHolder getName() {
        return name;
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

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }


    /**
     * helper method to decide for the correct color
     *
     * @param ctx
     * @return
     */
    protected int getSelectedColor(Context ctx) {
        return ColorHolder.color(getSelectedColor(), ctx, R.attr.material_drawer_selected, R.color.material_drawer_selected);
    }

    /**
     * helper method to decide for the correct color
     *
     * @param ctx
     * @return
     */
    protected int getColor(Context ctx) {
        int color;
        if (this.isEnabled()) {
            color = ColorHolder.color(getTextColor(), ctx, R.attr.material_drawer_primary_text, R.color.material_drawer_primary_text);
        } else {
            color = ColorHolder.color(getDisabledTextColor(), ctx, R.attr.material_drawer_hint_text, R.color.material_drawer_hint_text);
        }
        return color;
    }

    /**
     * helper method to decide for the correct color
     *
     * @param ctx
     * @return
     */
    protected int getSelectedTextColor(Context ctx) {
        return ColorHolder.color(getSelectedTextColor(), ctx, R.attr.material_drawer_selected_text, R.color.material_drawer_selected_text);
    }

    /**
     * helper method to decide for the correct color
     *
     * @param ctx
     * @return
     */
    public int getIconColor(Context ctx) {
        int iconColor;
        if (this.isEnabled()) {
            iconColor = ColorHolder.color(getIconColor(), ctx, R.attr.material_drawer_primary_icon, R.color.material_drawer_primary_icon);
        } else {
            iconColor = ColorHolder.color(getDisabledIconColor(), ctx, R.attr.material_drawer_hint_text, R.color.material_drawer_hint_text);
        }
        return iconColor;
    }

    /**
     * helper method to decide for the correct color
     *
     * @param ctx
     * @return
     */
    protected int getSelectedIconColor(Context ctx) {
        return ColorHolder.color(getSelectedIconColor(), ctx, R.attr.material_drawer_selected_text, R.color.material_drawer_selected_text);
    }

    /**
     * helper to get the ColorStateList for the text and remembering it so we do not have to recreate it all the time
     *
     * @param color
     * @param selectedTextColor
     * @return
     */
    protected ColorStateList getTextColorStateList(int color, int selectedTextColor) {
        if (colorStateList == null || color + selectedTextColor != colorStateList.first) {
            colorStateList = new Pair<>(color + selectedTextColor, DrawerUIUtils.getTextColorStateList(color, selectedTextColor));
        }

        return colorStateList.second;
    }
}
