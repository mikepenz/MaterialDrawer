package com.mikepenz.materialdrawer.model

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.mikepenz.materialdrawer.holder.ColorHolder
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.util.DrawerUIUtils
import com.mikepenz.materialdrawer.util.DrawerUIUtils.themeDrawerItem

/**
 * Created by mikepenz on 03.02.15.
 */
abstract class BaseDescribeableDrawerItem<T, VH : BaseViewHolder> : BaseDrawerItem<T, VH>() {
    var description: StringHolder? = null
    var descriptionTextColor: ColorHolder? = null

    fun withDescription(description: String): T {
        this.description = StringHolder(description)
        return this as T
    }

    fun withDescription(@StringRes descriptionRes: Int): T {
        this.description = StringHolder(descriptionRes)
        return this as T
    }

    fun withDescriptionTextColor(@ColorInt color: Int): T {
        this.descriptionTextColor = ColorHolder.fromColor(color)
        return this as T
    }

    fun withDescriptionTextColorRes(@ColorRes colorRes: Int): T {
        this.descriptionTextColor = ColorHolder.fromColorRes(colorRes)
        return this as T
    }

    /**
     * a helper method to have the logic for all secondaryDrawerItems only once
     *
     * @param viewHolder
     */
    protected fun bindViewHelper(viewHolder: BaseViewHolder) {
        val ctx = viewHolder.itemView.context

        //set the identifier from the drawerItem here. It can be used to run tests
        viewHolder.itemView.id = hashCode()

        //set the item selected if it is
        viewHolder.itemView.isSelected = isSelected

        //set the item enabled if it is
        viewHolder.itemView.isEnabled = isEnabled

        //get the correct color for the background
        val selectedColor = getSelectedColor(ctx)
        //get the correct color for the text
        val color = getColor(ctx)
        val selectedTextColor = getTextColorStateList(color, getSelectedTextColor(ctx))
        //get the correct color for the icon
        val iconColor = getIconColor(ctx)
        val selectedIconColor = getSelectedIconColor(ctx)
        val shapeAppearanceModel = getShapeAppearanceModel(ctx)

        //set the background for the item
        themeDrawerItem(ctx, viewHolder.view, selectedColor, isSelectedBackgroundAnimated, shapeAppearanceModel)
        //set the text for the name
        StringHolder.applyTo(this.name, viewHolder.name)
        //set the text for the description or hide
        StringHolder.applyToOrHide(this.description, viewHolder.description)

        //set the colors for textViews
        viewHolder.name.setTextColor(selectedTextColor)
        //set the description text color
        descriptionTextColor?.applyToOr(viewHolder.description, selectedTextColor)

        //define the typeface for our textViews
        if (typeface != null) {
            viewHolder.name.typeface = typeface
            viewHolder.description.typeface = typeface
        }

        //get the drawables for our icon and set it
        val icon = ImageHolder.decideIcon(icon, ctx, iconColor, isIconTinted, 1)
        if (icon != null) {
            val selectedIcon = ImageHolder.decideIcon(selectedIcon, ctx, selectedIconColor, isIconTinted, 1)
            ImageHolder.applyMultiIconTo(icon, iconColor, selectedIcon, selectedIconColor, isIconTinted, viewHolder.icon)
        } else {
            ImageHolder.applyDecidedIconOrSetGone(icon, viewHolder.icon, iconColor, isIconTinted, 1)
        }

        //for android API 17 --> Padding not applied via xml
        DrawerUIUtils.setDrawerVerticalPadding(viewHolder.view, level)
    }
}
