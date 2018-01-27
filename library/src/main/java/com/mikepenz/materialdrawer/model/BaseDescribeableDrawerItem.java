package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;

import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

/**
 * Created by mikepenz on 03.02.15.
 */
public abstract class BaseDescribeableDrawerItem<T, VH extends BaseViewHolder> extends BaseDrawerItem<T, VH> {
    private StringHolder description;
    private ColorHolder descriptionTextColor;

    public T withDescription(String description) {
        this.description = new StringHolder(description);
        return (T) this;
    }

    public T withDescription(@StringRes int descriptionRes) {
        this.description = new StringHolder(descriptionRes);
        return (T) this;
    }

    public T withDescriptionTextColor(@ColorInt int color) {
        this.descriptionTextColor = ColorHolder.fromColor(color);
        return (T) this;
    }

    public T withDescriptionTextColorRes(@ColorRes int colorRes) {
        this.descriptionTextColor = ColorHolder.fromColorRes(colorRes);
        return (T) this;
    }

    public StringHolder getDescription() {
        return description;
    }

    public ColorHolder getDescriptionTextColor() {
        return descriptionTextColor;
    }

    /**
     * a helper method to have the logic for all secondaryDrawerItems only once
     *
     * @param viewHolder
     */
    protected void bindViewHelper(BaseViewHolder viewHolder) {
        Context ctx = viewHolder.itemView.getContext();

        //set the identifier from the drawerItem here. It can be used to run tests
        viewHolder.itemView.setId(hashCode());

        //set the item selected if it is
        viewHolder.itemView.setSelected(isSelected());

        //set the item enabled if it is
        viewHolder.itemView.setEnabled(isEnabled());

        //get the correct color for the background
        int selectedColor = getSelectedColor(ctx);
        //get the correct color for the text
        int color = getColor(ctx);
        ColorStateList selectedTextColor = getTextColorStateList(color, getSelectedTextColor(ctx));
        //get the correct color for the icon
        int iconColor = getIconColor(ctx);
        int selectedIconColor = getSelectedIconColor(ctx);

        //set the background for the item
        ViewCompat.setBackground(viewHolder.view, UIUtils.getSelectableBackground(ctx, selectedColor, isSelectedBackgroundAnimated()));
        //set the text for the name
        StringHolder.applyTo(this.getName(), viewHolder.name);
        //set the text for the description or hide
        StringHolder.applyToOrHide(this.getDescription(), viewHolder.description);

        //set the colors for textViews
        viewHolder.name.setTextColor(selectedTextColor);
        //set the description text color
        ColorHolder.applyToOr(getDescriptionTextColor(), viewHolder.description, selectedTextColor);

        //define the typeface for our textViews
        if (getTypeface() != null) {
            viewHolder.name.setTypeface(getTypeface());
            viewHolder.description.setTypeface(getTypeface());
        }

        //get the drawables for our icon and set it
        Drawable icon = ImageHolder.decideIcon(getIcon(), ctx, iconColor, isIconTinted(), 1);
        if (icon != null) {
            Drawable selectedIcon = ImageHolder.decideIcon(getSelectedIcon(), ctx, selectedIconColor, isIconTinted(), 1);
            ImageHolder.applyMultiIconTo(icon, iconColor, selectedIcon, selectedIconColor, isIconTinted(), viewHolder.icon);
        } else {
            ImageHolder.applyDecidedIconOrSetGone(getIcon(), viewHolder.icon, iconColor, isIconTinted(), 1);
        }

        //for android API 17 --> Padding not applied via xml
        DrawerUIUtils.setDrawerVerticalPadding(viewHolder.view, level);
    }
}
