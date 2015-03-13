package com.mikepenz.materialdrawer.model;

import android.graphics.drawable.Drawable;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.Checkable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Iconable;
import com.mikepenz.materialdrawer.model.interfaces.Identifyable;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;

/**
 * Created by mikepenz on 03.02.15.
 */
public abstract class BaseDrawerItem implements IDrawerItem, Nameable<BaseDrawerItem>, Iconable<BaseDrawerItem>, Badgeable<BaseDrawerItem>, Checkable<BaseDrawerItem>, Tagable<BaseDrawerItem>, Identifyable<BaseDrawerItem> {

    private int identifier = -1;
    private Drawable icon;
    private int iconRes = -1;
    private IIcon iicon;
    private Drawable selectedIcon;
    private int selectedIconRes = -1;
    private String name;
    private int nameRes = -1;
    private String badge;
    private boolean enabled = true;
    private boolean checkable = true;
    private Object tag;

    private int selectedColor = 0;
    private int selectedColorRes = -1;
    private boolean selectedIconTinted = false;

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


    public BaseDrawerItem withIdentifier(int identifier) {
        this.identifier = identifier;
        return this;
    }

    public BaseDrawerItem withIcon(Drawable icon) {
        this.icon = icon;
        return this;
    }

    public BaseDrawerItem withIcon(int iconRes) {
        this.iconRes = iconRes;
        return this;
    }

    public BaseDrawerItem withIcon(IIcon iicon) {
        this.iicon = iicon;
        return this;
    }

    public BaseDrawerItem withSelectedIcon(Drawable selectedIcon) {
        this.selectedIcon = selectedIcon;
        return this;
    }

    public BaseDrawerItem withSelectedIcon(int selectedIconRes) {
        this.selectedIconRes = selectedIconRes;
        return this;
    }

    public BaseDrawerItem withName(String name) {
        this.name = name;
        return this;
    }

    public BaseDrawerItem withName(int nameRes) {
        this.nameRes = nameRes;
        return this;
    }

    public BaseDrawerItem withBadge(String badge) {
        this.badge = badge;
        return this;
    }

    public BaseDrawerItem withTag(Object object) {
        this.tag = object;
        return this;
    }

    public BaseDrawerItem withCheckable(boolean checkable) {
        this.checkable = checkable;
        return this;
    }

    public BaseDrawerItem setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public BaseDrawerItem withSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
        return this;
    }

    public BaseDrawerItem withSelectedColorRes(int selectedColorRes) {
        this.selectedColorRes = selectedColorRes;
        return this;
    }

    public BaseDrawerItem withTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public BaseDrawerItem withTextColorRes(int textColorRes) {
        this.textColorRes = textColorRes;
        return this;
    }

    public BaseDrawerItem withSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
        return this;
    }

    public BaseDrawerItem withSelectedTextColorRes(int selectedColorRes) {
        this.selectedTextColorRes = selectedColorRes;
        return this;
    }

    public BaseDrawerItem withDisabledTextColor(int disabledTextColor) {
        this.disabledTextColor = disabledTextColor;
        return this;
    }

    public BaseDrawerItem withDisabledTextColorRes(int disabledTextColorRes) {
        this.disabledTextColorRes = disabledTextColorRes;
        return this;
    }

    public BaseDrawerItem withIconColor(int iconColor) {
        this.iconColor = iconColor;
        return this;
    }

    public BaseDrawerItem withIconColorRes(int iconColorRes) {
        this.iconColorRes = iconColorRes;
        return this;
    }

    public BaseDrawerItem withSelectedIconColor(int selectedIconColor) {
        this.selectedIconColor = selectedIconColor;
        return this;
    }

    public BaseDrawerItem withSelectedIconColorRes(int selectedColorRes) {
        this.selectedIconColorRes = selectedColorRes;
        return this;
    }

    public BaseDrawerItem withDisabledIconColor(int disabledIconColor) {
        this.disabledIconColor = disabledIconColor;
        return this;
    }

    public BaseDrawerItem withDisabledIconColorRes(int disabledIconColorRes) {
        this.disabledIconColorRes = disabledIconColorRes;
        return this;
    }

    public BaseDrawerItem withTintSelectedIcon(boolean tintSelectedIcon) {
        this.selectedIconTinted = tintSelectedIcon;
        return this;
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

    public boolean isSelectedIconTinted() {
        return selectedIconTinted;
    }

    public void setSelectedIconTinted(boolean selectedIconTinted) {
        this.selectedIconTinted = selectedIconTinted;
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
    }

    public int getNameRes() {
        return nameRes;
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
}
