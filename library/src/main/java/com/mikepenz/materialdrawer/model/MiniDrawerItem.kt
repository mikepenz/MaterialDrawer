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
import com.mikepenz.materialdrawer.util.DrawerUIUtils.themeDrawerItem

/**
 * Created by mikepenz on 03.02.15.
 */
class MiniDrawerItem : BaseDrawerItem<MiniDrawerItem, MiniDrawerItem.ViewHolder> {
    var mBadge: StringHolder? = null
    var mBadgeStyle: BadgeStyle? = BadgeStyle()
    var mEnableSelectedBackground = false
    var mCustomHeight: DimenHolder? = null

    override val type: Int
        get() = R.id.material_drawer_item_mini

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_mini

    constructor(primaryDrawerItem: PrimaryDrawerItem) {
        this.identifier = primaryDrawerItem.identifier
        this.tag = primaryDrawerItem.tag

        this.mBadge = primaryDrawerItem.badge
        this.mBadgeStyle = primaryDrawerItem.badgeStyle

        this.isEnabled = primaryDrawerItem.isEnabled
        this.isSelectable = primaryDrawerItem.isSelectable
        this.isSelected = primaryDrawerItem.isSelected

        this.icon = primaryDrawerItem.icon
        this.selectedIcon = primaryDrawerItem.selectedIcon

        this.isIconTinted = primaryDrawerItem.isIconTinted
        this.selectedColor = primaryDrawerItem.selectedColor

        this.iconColor = primaryDrawerItem.iconColor
        this.selectedIconColor = primaryDrawerItem.selectedIconColor
        this.disabledIconColor = primaryDrawerItem.disabledIconColor
    }

    constructor(secondaryDrawerItem: SecondaryDrawerItem) {
        this.identifier = secondaryDrawerItem.identifier
        this.tag = secondaryDrawerItem.tag

        this.mBadge = secondaryDrawerItem.badge
        this.mBadgeStyle = secondaryDrawerItem.badgeStyle

        this.isEnabled = secondaryDrawerItem.isEnabled
        this.isSelectable = secondaryDrawerItem.isSelectable
        this.isSelected = secondaryDrawerItem.isSelected

        this.icon = secondaryDrawerItem.icon
        this.selectedIcon = secondaryDrawerItem.selectedIcon

        this.isIconTinted = secondaryDrawerItem.isIconTinted
        this.selectedColor = secondaryDrawerItem.selectedColor

        this.iconColor = secondaryDrawerItem.iconColor
        this.selectedIconColor = secondaryDrawerItem.selectedIconColor
        this.disabledIconColor = secondaryDrawerItem.disabledIconColor
    }


    fun withCustomHeightRes(@DimenRes customHeightRes: Int): MiniDrawerItem {
        this.mCustomHeight = DimenHolder.fromResource(customHeightRes)
        return this
    }

    fun withCustomHeightDp(customHeightDp: Int): MiniDrawerItem {
        this.mCustomHeight = DimenHolder.fromDp(customHeightDp)
        return this
    }

    fun withCustomHeightPx(customHeightPx: Int): MiniDrawerItem {
        this.mCustomHeight = DimenHolder.fromPixel(customHeightPx)
        return this
    }

    fun withCustomHeight(customHeight: DimenHolder): MiniDrawerItem {
        this.mCustomHeight = customHeight
        return this
    }

    fun withEnableSelectedBackground(enableSelectedBackground: Boolean): MiniDrawerItem {
        this.mEnableSelectedBackground = enableSelectedBackground
        return this
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        val ctx = holder.itemView.context

        //set a different height for this item
        mCustomHeight?.let {
            val lp = holder.itemView.layoutParams as RecyclerView.LayoutParams
            lp.height = it.asPixel(ctx)
            holder.itemView.layoutParams = lp
        }

        //set the identifier from the drawerItem here. It can be used to run tests
        holder.itemView.id = hashCode()

        //set the item enabled if it is
        holder.itemView.isEnabled = isEnabled

        //set the item selected if it is
        holder.itemView.isSelected = isSelected

        //
        holder.itemView.tag = this

        //get the correct color for the icon
        val iconColor = getIconColor(ctx)
        val selectedIconColor = getSelectedIconColor(ctx)

        if (mEnableSelectedBackground) {
            //get the correct color for the background
            val selectedColor = getSelectedColor(ctx)
            //set the background for the item
            themeDrawerItem(ctx, holder.view, selectedColor, isSelectedBackgroundAnimated)
        }

        //set the text for the badge or hide
        val badgeVisible = StringHolder.applyToOrHide(mBadge, holder.badge)
        //style the badge if it is visible
        if (badgeVisible) {
            mBadgeStyle?.style(holder.badge)
        }

        //get the drawables for our icon and set it
        val icon = ImageHolder.decideIcon(icon, ctx, iconColor, isIconTinted, 1)
        val selectedIcon = ImageHolder.decideIcon(selectedIcon, ctx, selectedIconColor, isIconTinted, 1)
        ImageHolder.applyMultiIconTo(icon, iconColor, selectedIcon, selectedIconColor, isIconTinted, holder.icon)

        //for android API 17 --> Padding not applied via xml
        val verticalPadding = ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_padding)
        val topBottomPadding = ctx.resources.getDimensionPixelSize(R.dimen.material_mini_drawer_item_padding)
        holder.itemView.setPadding(verticalPadding, topBottomPadding, verticalPadding, topBottomPadding)

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(internal val view: View) : RecyclerView.ViewHolder(view) {
        internal val icon: ImageView = view.findViewById<View>(R.id.material_drawer_icon) as ImageView
        internal val badge: TextView = view.findViewById<View>(R.id.material_drawer_badge) as TextView
    }
}
