package com.mikepenz.materialdrawer.app.drawerItems

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.app.R
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.model.AbstractDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Iconable
import com.mikepenz.materialdrawer.model.interfaces.SelectIconable
import com.mikepenz.materialdrawer.util.createDrawerItemColorStateList

/**
 * Created by mikepenz on 03.02.15.
 */
class IconDrawerItem : AbstractDrawerItem<IconDrawerItem, IconDrawerItem.ViewHolder>(), Iconable, SelectIconable {
    override var icon: ImageHolder? = null
    override var iconColor: ColorStateList? = null
    override var selectedIcon: ImageHolder? = null
    override var isIconTinted = false

    override val type: Int
        get() = R.id.material_drawer_item_icon_only

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_icon_only

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        val ctx = holder.itemView.context

        //set the identifier from the drawerItem here. It can be used to run tests
        holder.itemView.id = hashCode()

        //get the correct color for the icon
        val iconColor = this.iconColor ?: ctx.getPrimaryDrawerIconColor()

        //get the drawables for our icon and set it)
        val icon = ImageHolder.decideIcon(icon, ctx, iconColor, isIconTinted, 1)
        val selectedIcon = ImageHolder.decideIcon(selectedIcon, ctx, iconColor, isIconTinted, 1)
        ImageHolder.applyMultiIconTo(icon, selectedIcon, iconColor, isIconTinted, holder.icon)

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder internal constructor(private val view: View) : RecyclerView.ViewHolder(view) {
        var icon: ImageView = view.findViewById<ImageView>(R.id.material_drawer_icon)
    }
}

internal fun Context.getPrimaryDrawerIconColor(): ColorStateList {
    return createDrawerItemColorStateList(this, com.mikepenz.materialdrawer.R.styleable.MaterialDrawerSliderView_materialDrawerPrimaryIcon)!!
}