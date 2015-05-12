package com.mikepenz.materialdrawer.model;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.model.interfaces.Checkable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Iconable;
import com.mikepenz.materialdrawer.model.interfaces.Identifyable;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable;

/**
 * Created by mikepenz on 03.02.15.
 */
public abstract class BaseDrawerItem<T> implements IDrawerItem, Nameable<T>, Iconable<T>, Checkable<T>, Tagable<T>, Identifyable<T>, Typefaceable<T> {

    private int identifier = -1;
    private Drawable icon;
    private int iconRes = -1;
    private IIcon iicon;
    private Drawable selectedIcon;
    private int selectedIconRes = -1;
    private String name;
    private int nameRes = -1;
    private boolean enabled = true;
    private boolean checkable = true;
    private Object tag;

    private boolean iconTinted = false;

    private int selectedColor = 0;
    private int selectedColorRes = -1;

    private int textColor = 0;
    private int textColorRes = -1;
    private int selectedTextColor = 0;
    private int selectedTextColorRes = -1;
    private int disabledTextColor = 0;
    private int disabledTextColorRes = -1;

    private int iconColor = 0;
    private int iconColorRes = -1;
    private int selectedIconColor = 0;
    private int selectedIconColorRes = -1;
    private int disabledIconColor = 0;
    private int disabledIconColorRes = -1;

    private Typeface typeface = null;

    public T withIdentifier(int identifier) {
        this.identifier = identifier;
        return (T) this;
    }

    public T withIcon(Drawable icon) {
        this.icon = icon;
        return (T) this;
    }

    public T withIcon(int iconRes) {
        this.iconRes = iconRes;
        return (T) this;
    }

    public T withIcon(IIcon iicon) {
        this.iicon = iicon;
        return (T) this;
    }

    public T withSelectedIcon(Drawable selectedIcon) {
        this.selectedIcon = selectedIcon;
        return (T) this;
    }

    public T withSelectedIcon(int selectedIconRes) {
        this.selectedIconRes = selectedIconRes;
        return (T) this;
    }

    public T withName(String name) {
        this.name = name;
        this.nameRes = -1;
        return (T) this;
    }

    public T withName(int nameRes) {
        this.nameRes = nameRes;
        this.name = null;
        return (T) this;
    }

    public T withTag(Object object) {
        this.tag = object;
        return (T) this;
    }

    public T withCheckable(boolean checkable) {
        this.checkable = checkable;
        return (T) this;
    }

    public T withEnabled(boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }

    public T setEnabled(boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }

    public T withSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
        return (T) this;
    }

    public T withSelectedColorRes(int selectedColorRes) {
        this.selectedColorRes = selectedColorRes;
        return (T) this;
    }

    public T withTextColor(int textColor) {
        this.textColor = textColor;
        return (T) this;
    }

    public T withTextColorRes(int textColorRes) {
        this.textColorRes = textColorRes;
        return (T) this;
    }

    public T withSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
        return (T) this;
    }

    public T withSelectedTextColorRes(int selectedColorRes) {
        this.selectedTextColorRes = selectedColorRes;
        return (T) this;
    }

    public T withDisabledTextColor(int disabledTextColor) {
        this.disabledTextColor = disabledTextColor;
        return (T) this;
    }

    public T withDisabledTextColorRes(int disabledTextColorRes) {
        this.disabledTextColorRes = disabledTextColorRes;
        return (T) this;
    }

    public T withIconColor(int iconColor) {
        this.iconColor = iconColor;
        return (T) this;
    }

    public T withIconColorRes(int iconColorRes) {
        this.iconColorRes = iconColorRes;
        return (T) this;
    }

    public T withSelectedIconColor(int selectedIconColor) {
        this.selectedIconColor = selectedIconColor;
        return (T) this;
    }

    public T withSelectedIconColorRes(int selectedColorRes) {
        this.selectedIconColorRes = selectedColorRes;
        return (T) this;
    }

    public T withDisabledIconColor(int disabledIconColor) {
        this.disabledIconColor = disabledIconColor;
        return (T) this;
    }

    public T withDisabledIconColorRes(int disabledIconColorRes) {
        this.disabledIconColorRes = disabledIconColorRes;
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

    public int getSelectedTextColor() {
        return selectedTextColor;
    }

    public void setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
    }

    public int getSelectedTextColorRes() {
        return selectedTextColorRes;
    }

    public void setSelectedTextColorRes(int selectedTextColorRes) {
        this.selectedTextColorRes = selectedTextColorRes;
    }

    public int getDisabledTextColor() {
        return disabledTextColor;
    }

    public void setDisabledTextColor(int disabledTextColor) {
        this.disabledTextColor = disabledTextColor;
    }

    public int getDisabledTextColorRes() {
        return disabledTextColorRes;
    }

    public void setDisabledTextColorRes(int disabledTextColorRes) {
        this.disabledTextColorRes = disabledTextColorRes;
    }

    public boolean isIconTinted() {
        return iconTinted;
    }

    public void setIconTinted(boolean iconTinted) {
        this.iconTinted = iconTinted;
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

    @Override
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public int getSelectedIconRes() {
        return selectedIconRes;
    }

    public void setSelectedIconRes(int selectedIconRes) {
        this.selectedIconRes = selectedIconRes;
    }

    public IIcon getIIcon() {
        return iicon;
    }

    @Override
    public void setIIcon(IIcon iicon) {
        this.iicon = iicon;
    }

    public Drawable getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(Drawable selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        this.nameRes = -1;
    }

    public int getNameRes() {
        return nameRes;
    }

    @Override
    public void setNameRes(int nameRes) {
        this.nameRes = nameRes;
        this.name = null;
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
    public boolean isCheckable() {
        return checkable;
    }

    @Override
    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    public int getDisabledIconColorRes() {
        return disabledIconColorRes;
    }

    public void setDisabledIconColorRes(int disabledIconColorRes) {
        this.disabledIconColorRes = disabledIconColorRes;
    }

    public int getDisabledIconColor() {
        return disabledIconColor;
    }

    public void setDisabledIconColor(int disabledIconColor) {
        this.disabledIconColor = disabledIconColor;
    }

    public int getSelectedIconColorRes() {
        return selectedIconColorRes;
    }

    public void setSelectedIconColorRes(int selectedIconColorRes) {
        this.selectedIconColorRes = selectedIconColorRes;
    }

    public int getSelectedIconColor() {
        return selectedIconColor;
    }

    public void setSelectedIconColor(int selectedIconColor) {
        this.selectedIconColor = selectedIconColor;
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

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }
}
