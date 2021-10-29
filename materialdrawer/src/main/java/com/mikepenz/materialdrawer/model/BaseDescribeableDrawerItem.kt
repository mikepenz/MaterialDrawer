package com.mikepenz.materialdrawer.model

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.google.android.material.shape.ShapeAppearanceModel
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.Describable
import com.mikepenz.materialdrawer.model.interfaces.DescribableColor
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.mikepenz.materialdrawer.util.getSecondaryDrawerTextColor
import com.mikepenz.materialdrawer.util.setDrawerVerticalPadding
import com.mikepenz.materialdrawer.util.themeDrawerItem

/**
 * An abstract [IDrawerItem] implementation describing a drawerItem with support for a description
 */
abstract class BaseDescribeableDrawerItem<T, VH : BaseViewHolder> : BaseDrawerItem<T, VH>(), Describable, DescribableColor {
    override var description: StringHolder? = null
    override var descriptionTextColor: ColorStateList? = null

    /**
     * a helper method to have the logic for all secondaryDrawerItems only once
     *
     * @param viewHolder
     */
    protected fun bindViewHelper(viewHolder: BaseViewHolder) {
        val ctx = viewHolder.itemView.context

        //set the identifier from the drawerItem here. It can be used to run tests
        viewHolder.itemView.id = hashCode()

        //get the correct color for the background
        val selectedColor = this.selectedColor?.color(ctx) ?: getSelectedColor(ctx)
        //get the correct color for the text

        val textColor = this.textColor ?: getColor(ctx)
        val textColorSecondary = this.descriptionTextColor ?: ctx.getSecondaryDrawerTextColor()
        //get the correct color for the icon
        val iconColor = this.iconColor ?: getIconColor(ctx)
        val shapeAppearanceModel = getShapeAppearanceModel(ctx)

        //set the background for the item
        applyDrawerItemTheme(ctx, viewHolder.view, selectedColor, isSelectedBackgroundAnimated, shapeAppearanceModel)
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

        // check if we should load from a url, false if normal icon
        val loaded = icon?.uri?.let {
            DrawerImageLoader.instance.setImage(viewHolder.icon, it, DrawerImageLoader.Tags.PRIMARY_ITEM.name)
        } ?: false

        if (!loaded) {
            // get the drawables for our icon and set it
            val icon = ImageHolder.decideIcon(icon, ctx, iconColor, isIconTinted, 1)
            val selectedIcon = ImageHolder.decideIcon(selectedIcon, ctx, iconColor, isIconTinted, 1)
            ImageHolder.applyMultiIconTo(icon, selectedIcon, iconColor, isIconTinted, viewHolder.icon)
        }

        if (viewHolder.icon.isVisible) {
            viewHolder.name.updatePadding(left = 0)
            viewHolder.description.updatePadding(left = 0)
        } else {
            viewHolder.name.updatePadding(left = ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_item_primary_icon_padding_left))
            viewHolder.description.updatePadding(left = ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_item_primary_icon_padding_left))
        }

        //for android API 17 --> Padding not applied via xml
        viewHolder.view.setDrawerVerticalPadding(level)

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
    }

    override fun unbindView(holder: VH) {
        super.unbindView(holder)

        // reset image loading for the item
        DrawerImageLoader.instance.cancelImage(holder.icon)
        holder.icon.setImageBitmap(null)
    }

    /**
     * will apply and theme the drawer item using the standard logic, overwrite this in your custom item to redefine the algorithm to do so
     */
    protected open fun applyDrawerItemTheme(ctx: Context, view: View, selected_color: Int, animate: Boolean, shapeAppearanceModel: ShapeAppearanceModel) {
        themeDrawerItem(ctx, view, selected_color, animate, shapeAppearanceModel, isSelected = isSelected)
    }
}
