package com.mikepenz.materialdrawer.model

import android.content.res.ColorStateList
import androidx.annotation.StringRes
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.getSecondaryDrawerTextColor
import com.mikepenz.materialdrawer.util.setDrawerVerticalPadding
import com.mikepenz.materialdrawer.util.themeDrawerItem

/**
 * An abstract [IDrawerItem] implementation describing a drawerItem with support for a description
 */
abstract class BaseDescribeableDrawerItem<T, VH : BaseViewHolder> : BaseDrawerItem<T, VH>() {
    var description: StringHolder? = null
    var descriptionTextColor: ColorStateList? = null

    @Deprecated("Please consider to replace with the actual property setter")
    fun withDescription(description: String): T {
        this.description = StringHolder(description)
        return this as T
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withDescription(@StringRes descriptionRes: Int): T {
        this.description = StringHolder(descriptionRes)
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
        viewHolder.name.isSelected = isSelected
        viewHolder.description.isSelected = isSelected
        viewHolder.icon.isSelected = isSelected

        //set the item enabled if it is
        viewHolder.itemView.isEnabled = isEnabled
        viewHolder.name.isEnabled = isEnabled
        viewHolder.description.isEnabled = isEnabled
        viewHolder.icon.isEnabled = isEnabled

        //get the correct color for the background
        val selectedColor = this.selectedColor?.color(ctx) ?: getSelectedColor(ctx)
        //get the correct color for the text

        val textColor = this.textColor ?: getColor(ctx)
        val textColorSecondary = this.descriptionTextColor ?: ctx.getSecondaryDrawerTextColor()
        //get the correct color for the icon
        val iconColor = this.iconColor ?: getIconColor(ctx)
        val shapeAppearanceModel = getShapeAppearanceModel(ctx)

        //set the background for the item
        themeDrawerItem(ctx, viewHolder.view, selectedColor, isSelectedBackgroundAnimated, shapeAppearanceModel)
        //set the text for the name
        StringHolder.applyTo(this.name, viewHolder.name)
        //set the text for the description or hide
        StringHolder.applyToOrHide(this.description, viewHolder.description)

        //set the colors for textViews
        viewHolder.name.setTextColor(textColor)
        //set the description text color
        viewHolder.description.setTextColor(textColorSecondary)

        //define the typeface for our textViews
        if (typeface != null) {
            viewHolder.name.typeface = typeface
            viewHolder.description.typeface = typeface
        }

        //get the drawables for our icon and set it)
        val icon = ImageHolder.decideIcon(icon, ctx, iconColor, isIconTinted, 1)
        val selectedIcon = ImageHolder.decideIcon(selectedIcon, ctx, iconColor, isIconTinted, 1)
        ImageHolder.applyMultiIconTo(icon, selectedIcon, iconColor, isIconTinted, viewHolder.icon)

        //for android API 17 --> Padding not applied via xml
        viewHolder.view.setDrawerVerticalPadding(level)
    }
}
