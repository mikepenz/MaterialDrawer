package com.mikepenz.materialdrawer.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.ColorHolder
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.model.interfaces.Tagable
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable
import com.mikepenz.materialdrawer.util.DrawerUtils.setDrawerVerticalPadding
import com.mikepenz.materialdrawer.util.getPrimaryDrawerIconColor
import com.mikepenz.materialdrawer.util.getSelectableBackground

/**
 * Describes a [IProfile] being used with the [com.mikepenz.materialdrawer.widget.AccountHeaderView]
 */
open class ProfileSettingDrawerItem : AbstractDrawerItem<ProfileSettingDrawerItem, ProfileSettingDrawerItem.ViewHolder>(), IProfile, Tagable, Typefaceable {
    override var icon: ImageHolder? = null
    override var name: StringHolder? = null
    override var description: StringHolder? = null

    var isIconTinted = false
    var iconColor: ColorHolder? = null
    var descriptionTextColor: ColorHolder? = null

    override var isSelectable = false

    override val type: Int
        get() = R.id.material_drawer_item_profile_setting

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_profile_setting

    @Deprecated("Please consider to replace with the actual property setter")
    fun withDescription(description: String): ProfileSettingDrawerItem {
        this.description = StringHolder(description)
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withDescription(@StringRes descriptionRes: Int): ProfileSettingDrawerItem {
        this.description = StringHolder(descriptionRes)
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withDescriptionTextColor(@ColorInt descriptionColor: Int): ProfileSettingDrawerItem {
        this.descriptionTextColor = ColorHolder.fromColor(descriptionColor)
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withDescriptionTextColorRes(@ColorRes descriptionColorRes: Int): ProfileSettingDrawerItem {
        this.descriptionTextColor = ColorHolder.fromColorRes(descriptionColorRes)
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withIconColor(@ColorInt iconColor: Int): ProfileSettingDrawerItem {
        this.iconColor = ColorHolder.fromColor(iconColor)
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withIconColorRes(@ColorRes iconColorRes: Int): ProfileSettingDrawerItem {
        this.iconColor = ColorHolder.fromColorRes(iconColorRes)
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withIconTinted(iconTinted: Boolean): ProfileSettingDrawerItem {
        this.isIconTinted = iconTinted
        return this
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
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
        val selectedColor = getSelectedColor(ctx)
        //get the correct color for the text
        val color = getColor(ctx)
        val iconColor = ctx.getPrimaryDrawerIconColor()
        val descriptionColor = getColor(ctx)

        ViewCompat.setBackground(holder.view, ctx.getSelectableBackground(selectedColor, isSelectedBackgroundAnimated))

        StringHolder.applyTo(this.name, holder.name)
        holder.name.setTextColor(color)

        StringHolder.applyToOrHide(this.description, holder.description)
        holder.description.setTextColor(descriptionColor)

        if (typeface != null) {
            holder.name.typeface = typeface
            holder.description.typeface = typeface
        }

        //set the correct icon
        ImageHolder.applyDecidedIconOrSetGone(icon, holder.icon, iconColor, isIconTinted, 2)

        //for android API 17 --> Padding not applied via xml
        setDrawerVerticalPadding(holder.view)

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    open class ViewHolder internal constructor(internal val view: View) : RecyclerView.ViewHolder(view) {
        internal val icon: ImageView = view.findViewById<ImageView>(R.id.material_drawer_icon)
        internal val name: TextView = view.findViewById<TextView>(R.id.material_drawer_name)
        internal val description: TextView = view.findViewById<TextView>(R.id.material_drawer_description)
    }
}
