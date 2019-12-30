package com.mikepenz.materialdrawer.app.drawerItems

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.materialdrawer.app.R
import com.mikepenz.materialdrawer.holder.ColorHolder
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.model.AbstractDrawerItem
import com.mikepenz.materialdrawer.util.createDrawerItemColorStateList

/**
 * Created by mikepenz on 03.02.15.
 */
class IconDrawerItem : AbstractDrawerItem<IconDrawerItem, IconDrawerItem.ViewHolder>() {
    var icon: ImageHolder? = null
    var selectedIcon: ImageHolder? = null
    var isIconTinted = false
    var iconColor: ColorHolder? = null
    var selectedIconColor: ColorHolder? = null
    var disabledIconColor: ColorHolder? = null

    override val type: Int
        get() = R.id.material_drawer_item_icon_only

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_icon_only

    fun withIcon(icon: ImageHolder): IconDrawerItem {
        this.icon = icon
        return this
    }

    fun withIcon(icon: Drawable): IconDrawerItem {
        this.icon = ImageHolder(icon)
        return this
    }

    fun withIcon(@DrawableRes iconRes: Int): IconDrawerItem {
        this.icon = ImageHolder(iconRes)
        return this
    }

    fun withSelectedIcon(selectedIcon: Drawable): IconDrawerItem {
        this.selectedIcon = ImageHolder(selectedIcon)
        return this
    }

    fun withSelectedIcon(@DrawableRes selectedIconRes: Int): IconDrawerItem {
        this.selectedIcon = ImageHolder(selectedIconRes)
        return this
    }

    fun withIcon(iicon: IIcon): IconDrawerItem {
        /*
        TODO
        this.icon = ImageHolder(iicon)
        //if we are on api 21 or higher we use the IconicsDrawable for selection too and color it with the correct color
        //else we use just the one drawable and enable tinting on press
        if (Build.VERSION.SDK_INT >= 21) {
            this.selectedIcon = ImageHolder(iicon)
        } else {
            this.withIconTintingEnabled(true)
        }

         */

        return this
    }

    fun withIconColor(@ColorInt iconColor: Int): IconDrawerItem {
        this.iconColor = ColorHolder.fromColor(iconColor)
        return this
    }

    fun withIconColorRes(@ColorRes iconColorRes: Int): IconDrawerItem {
        this.iconColor = ColorHolder.fromColorRes(iconColorRes)
        return this
    }

    fun withSelectedIconColor(@ColorInt selectedIconColor: Int): IconDrawerItem {
        this.selectedIconColor = ColorHolder.fromColor(selectedIconColor)
        return this
    }

    fun withSelectedIconColorRes(@ColorRes selectedColorRes: Int): IconDrawerItem {
        this.selectedIconColor = ColorHolder.fromColorRes(selectedColorRes)
        return this
    }

    fun withDisabledIconColor(@ColorInt disabledIconColor: Int): IconDrawerItem {
        this.disabledIconColor = ColorHolder.fromColor(disabledIconColor)
        return this
    }

    fun withDisabledIconColorRes(@ColorRes disabledIconColorRes: Int): IconDrawerItem {
        this.disabledIconColor = ColorHolder.fromColorRes(disabledIconColorRes)
        return this
    }

    /**
     * will tint the icon with the default (or set) colors
     * (default and selected state)
     *
     * @param iconTintingEnabled
     * @return
     */
    fun withIconTintingEnabled(iconTintingEnabled: Boolean): IconDrawerItem {
        this.isIconTinted = iconTintingEnabled
        return this
    }

    @Deprecated("")
    fun withIconTinted(iconTinted: Boolean): IconDrawerItem {
        this.isIconTinted = iconTinted
        return this
    }

    /**
     * for backwards compatibility - withIconTinted..
     *
     * @param iconTinted
     * @return
     */
    @Deprecated("")
    fun withTintSelectedIcon(iconTinted: Boolean): IconDrawerItem {
        return withIconTintingEnabled(iconTinted)
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        val ctx = holder.itemView.context

        //set the identifier from the drawerItem here. It can be used to run tests
        holder.itemView.id = hashCode()

        //get the correct color for the icon
        val iconColor = ctx.getPrimaryDrawerIconColor()

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