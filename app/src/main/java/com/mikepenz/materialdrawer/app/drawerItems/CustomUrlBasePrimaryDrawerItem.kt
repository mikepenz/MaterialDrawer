package com.mikepenz.materialdrawer.app.drawerItems

import android.net.Uri
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.holder.ColorHolder
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.BaseDrawerItem
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.mikepenz.materialdrawer.util.setDrawerVerticalPadding
import com.mikepenz.materialdrawer.util.themeDrawerItem

/**
 * Created by mikepenz on 03.02.15.
 */
abstract class CustomUrlBasePrimaryDrawerItem<T, VH : RecyclerView.ViewHolder> : BaseDrawerItem<T, VH>() {

    var description: StringHolder? = null
    var descriptionTextColor: ColorHolder? = null

    fun withIcon(url: String): T {
        this.icon = ImageHolder(url)
        return this as T
    }

    fun withIcon(uri: Uri): T {
        this.icon = ImageHolder(uri)
        return this as T
    }

    fun withDescription(description: String): T {
        this.description = StringHolder(description)
        return this as T
    }

    fun withDescription(@StringRes descriptionRes: Int): T {
        this.description = StringHolder(descriptionRes)
        return this as T
    }

    /**
     * a helper method to have the logic for all secondaryDrawerItems only once
     *
     * @param viewHolder
     */
    protected fun bindViewHelper(viewHolder: CustomBaseViewHolder) {
        val ctx = viewHolder.itemView.context

        //set the identifier from the drawerItem here. It can be used to run tests
        viewHolder.itemView.id = hashCode()

        //get the correct color for the background
        val selectedColor = getSelectedColor(ctx)
        //get the correct color for the text
        val color = getColor(ctx)
        val shapeAppearanceModel = getShapeAppearanceModel(ctx)

        //set the background for the item
        themeDrawerItem(ctx, viewHolder.view, selectedColor, isSelectedBackgroundAnimated, shapeAppearanceModel, isSelected = isSelected)
        //set the text for the name
        StringHolder.applyTo(this.name, viewHolder.name)
        //set the text for the description or hide
        StringHolder.applyToOrHide(this.description, viewHolder.description)

        //set the colors for textViews
        viewHolder.name.setTextColor(color)
        //set the description text color
        descriptionTextColor?.applyToOr(viewHolder.description, color)

        //define the typeface for our textViews
        if (typeface != null) {
            viewHolder.name.typeface = typeface
            viewHolder.description.typeface = typeface
        }

        //we make sure we reset the image first before setting the new one in case there is an empty one
        DrawerImageLoader.instance.cancelImage(viewHolder.icon)
        viewHolder.icon.setImageBitmap(null)
        //get the drawables for our icon and set it
        icon?.applyTo(viewHolder.icon, "customUrlItem")

        //for android API 17 --> Padding not applied via xml
        setDrawerVerticalPadding(viewHolder.view)

        //set the item selected if it is
        viewHolder.itemView.isSelected = isSelected
    }
}
