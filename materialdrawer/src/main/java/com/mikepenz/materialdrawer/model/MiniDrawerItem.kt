package com.mikepenz.materialdrawer.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.holder.DimenHolder
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.mikepenz.materialdrawer.util.themeDrawerItem

/**
 * Describes a [IDrawerItem] being used for the [com.mikepenz.materialdrawer.widget.MiniDrawerSliderView]
 */
open class MiniDrawerItem : BaseDrawerItem<MiniDrawerItem, MiniDrawerItem.ViewHolder> {
    var badge: StringHolder? = null
    var badgeStyle: BadgeStyle? = BadgeStyle()
    var enableSelectedBackground = false
    var customHeight: DimenHolder? = null

    override val type: Int
        get() = R.id.material_drawer_item_mini

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_mini

    constructor(primaryDrawerItem: PrimaryDrawerItem) {
        this.identifier = primaryDrawerItem.identifier
        this.tag = primaryDrawerItem.tag

        this.badge = primaryDrawerItem.badge
        this.badgeStyle = primaryDrawerItem.badgeStyle

        this.isEnabled = primaryDrawerItem.isEnabled
        this.isSelectable = primaryDrawerItem.isSelectable
        this.isSelected = primaryDrawerItem.isSelected

        this.icon = primaryDrawerItem.icon
        this.selectedIcon = primaryDrawerItem.selectedIcon

        this.isIconTinted = primaryDrawerItem.isIconTinted
        this.selectedColor = primaryDrawerItem.selectedColor

        this.iconColor = primaryDrawerItem.iconColor
    }

    constructor(secondaryDrawerItem: SecondaryDrawerItem) {
        this.identifier = secondaryDrawerItem.identifier
        this.tag = secondaryDrawerItem.tag

        this.badge = secondaryDrawerItem.badge
        this.badgeStyle = secondaryDrawerItem.badgeStyle

        this.isEnabled = secondaryDrawerItem.isEnabled
        this.isSelectable = secondaryDrawerItem.isSelectable
        this.isSelected = secondaryDrawerItem.isSelected

        this.icon = secondaryDrawerItem.icon
        this.selectedIcon = secondaryDrawerItem.selectedIcon

        this.isIconTinted = secondaryDrawerItem.isIconTinted
        this.selectedColor = secondaryDrawerItem.selectedColor

        this.iconColor = secondaryDrawerItem.iconColor
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withCustomHeightRes(@DimenRes customHeightRes: Int): MiniDrawerItem {
        this.customHeight = DimenHolder.fromResource(customHeightRes)
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withCustomHeightDp(customHeightDp: Int): MiniDrawerItem {
        this.customHeight = DimenHolder.fromDp(customHeightDp)
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withCustomHeightPx(customHeightPx: Int): MiniDrawerItem {
        this.customHeight = DimenHolder.fromPixel(customHeightPx)
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withCustomHeight(customHeight: DimenHolder): MiniDrawerItem {
        this.customHeight = customHeight
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withEnableSelectedBackground(enableSelectedBackground: Boolean): MiniDrawerItem {
        this.enableSelectedBackground = enableSelectedBackground
        return this
    }

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        val ctx = holder.itemView.context

        //set a different height for this item
        customHeight?.let {
            val lp = holder.itemView.layoutParams as RecyclerView.LayoutParams
            lp.height = it.asPixel(ctx)
            holder.itemView.layoutParams = lp
        }

        //set the identifier from the drawerItem here. It can be used to run tests
        holder.itemView.id = hashCode()

        //set the item enabled if it is
        holder.itemView.isEnabled = isEnabled
        holder.icon.isEnabled = isEnabled

        //set the item selected if it is
        holder.itemView.isSelected = isSelected
        holder.icon.isSelected = isSelected

        //
        holder.itemView.tag = this

        //get the correct color for the icon
        val iconColor = getIconColor(ctx)
        val shapeAppearanceModel = getShapeAppearanceModel(ctx)

        if (enableSelectedBackground) {
            //get the correct color for the background
            val selectedColor = selectedColor?.color(ctx) ?: getSelectedColor(ctx)
            //set the background for the item
            themeDrawerItem(ctx, holder.view, selectedColor, isSelectedBackgroundAnimated, shapeAppearanceModel, isSelected = isSelected)
        }

        //set the text for the badge or hide
        val badgeVisible = StringHolder.applyToOrHide(badge, holder.badge)
        //style the badge if it is visible
        if (badgeVisible) {
            badgeStyle?.style(holder.badge)
        }

        // check if we should load from a url, false if normal icon
        val loaded = icon?.uri?.let {
            DrawerImageLoader.instance.setImage(holder.icon, it, DrawerImageLoader.Tags.MINI_ITEM.name)
        } ?: false

        if (!loaded) {
            // get the drawables for our icon and set it
            val icon = ImageHolder.decideIcon(icon, ctx, iconColor, isIconTinted, 1)
            val selectedIcon = ImageHolder.decideIcon(selectedIcon, ctx, iconColor, isIconTinted, 1)
            ImageHolder.applyMultiIconTo(icon, selectedIcon, iconColor, isIconTinted, holder.icon)
        }

        //for android API 17 --> Padding not applied via xml
        val verticalPadding = ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_padding)
        val topBottomPadding = ctx.resources.getDimensionPixelSize(R.dimen.material_mini_drawer_item_padding)
        holder.itemView.setPadding(verticalPadding, topBottomPadding, verticalPadding, topBottomPadding)

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)

        // reset image loading for the item
        DrawerImageLoader.instance.cancelImage(holder.icon)
        holder.icon.setImageBitmap(null)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(internal val view: View) : RecyclerView.ViewHolder(view) {
        internal val icon: ImageView = view.findViewById<ImageView>(R.id.material_drawer_icon)
        internal val badge: TextView = view.findViewById<TextView>(R.id.material_drawer_badge)
    }
}
