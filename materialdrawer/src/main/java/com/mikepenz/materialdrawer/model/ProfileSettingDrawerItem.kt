package com.mikepenz.materialdrawer.model

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.*
import com.mikepenz.materialdrawer.util.getPrimaryDrawerIconColor
import com.mikepenz.materialdrawer.util.setDrawerVerticalPadding
import com.mikepenz.materialdrawer.util.themeDrawerItem

/**
 * Describes a [IProfile] being used with the [com.mikepenz.materialdrawer.widget.AccountHeaderView]
 */
open class ProfileSettingDrawerItem : AbstractDrawerItem<ProfileSettingDrawerItem, ProfileSettingDrawerItem.ViewHolder>(), IProfile, Tagable, Typefaceable, ColorfulBadgeable, NameableColor, DescribableColor, SelectIconable {
    override var icon: ImageHolder? = null
    override var iconColor: ColorStateList? = null
    override var selectedIcon: ImageHolder? = null
    override var name: StringHolder? = null
    override var textColor: ColorStateList? = null
    override var description: StringHolder? = null
    override var descriptionTextColor: ColorStateList? = null
    override var isIconTinted = false

    override var badge: StringHolder? = null
    override var badgeStyle: BadgeStyle? = BadgeStyle()

    override var isSelectable = false

    override val type: Int
        get() = R.id.material_drawer_item_profile_setting

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_profile_setting

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        //get the context
        val ctx = holder.itemView.context

        //set the identifier from the drawerItem here. It can be used to run tests
        holder.itemView.id = hashCode()

        //set the item enabled if it is
        holder.itemView.isEnabled = isEnabled
        holder.name.isEnabled = isEnabled
        holder.description.isEnabled = isEnabled
        holder.icon.isEnabled = isEnabled

        //set the item selected if it is
        holder.itemView.isSelected = isSelected
        holder.name.isSelected = isSelected
        holder.description.isSelected = isSelected
        holder.icon.isSelected = isSelected

        //get the correct color for the background
        val selectedColor = this.selectedColor?.color(ctx) ?: getSelectedColor(ctx)
        //get the correct color for the text
        val color = this.textColor ?: getColor(ctx)
        val iconColor = this.iconColor ?: ctx.getPrimaryDrawerIconColor()
        val descriptionColor = this.descriptionTextColor ?: getColor(ctx)
        val shapeAppearanceModel = getShapeAppearanceModel(ctx)

        //set the background for the item
        themeDrawerItem(ctx, holder.view, selectedColor, isSelectedBackgroundAnimated, shapeAppearanceModel, isSelected = isSelected)

        StringHolder.applyTo(this.name, holder.name)
        holder.name.setTextColor(color)

        StringHolder.applyToOrHide(this.description, holder.description)
        holder.description.setTextColor(descriptionColor)

        if (typeface != null) {
            holder.name.typeface = typeface
            holder.description.typeface = typeface
        }

        //set the text for the badge or hide
        val badgeVisible = StringHolder.applyToOrHide(badge, holder.badge)
        //style the badge if it is visible
        if (badgeVisible) {
            badgeStyle?.style(holder.badge, getColor(ctx))
            holder.badge.visibility = View.VISIBLE
        } else {
            holder.badge.visibility = View.GONE
        }

        //define the typeface for our textViews
        if (typeface != null) {
            holder.badge.typeface = typeface
        }

        //set the correct icon
        val icon = ImageHolder.decideIcon(icon, ctx, iconColor, isIconTinted, 2)
        val selectedIcon = ImageHolder.decideIcon(selectedIcon, ctx, iconColor, isIconTinted, 2)
        ImageHolder.applyMultiIconTo(icon, selectedIcon, iconColor, isIconTinted, holder.icon)

        //for android API 17 --> Padding not applied via xml
        setDrawerVerticalPadding(holder.view)

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    open class ViewHolder internal constructor(internal val view: View) : RecyclerView.ViewHolder(view) {
        internal val icon: ImageView = view.findViewById(R.id.material_drawer_icon)
        internal val name: TextView = view.findViewById(R.id.material_drawer_name)
        internal val description: TextView = view.findViewById(R.id.material_drawer_description)
        internal val badge: TextView = view.findViewById(R.id.material_drawer_badge)
    }
}
