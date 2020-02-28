package com.mikepenz.materialdrawer.model

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Iconable
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import com.mikepenz.materialdrawer.model.interfaces.Tagable
import com.mikepenz.materialdrawer.util.getPrimaryDrawerIconColor

/**
 * An abstract [IDrawerItem] implementation providing the base properties with their default value
 */
abstract class BaseDrawerItem<T, VH : RecyclerView.ViewHolder> : AbstractDrawerItem<T, VH>(), Nameable, Iconable, Tagable {
    override var icon: ImageHolder? = null
    var selectedIcon: ImageHolder? = null
    override var name: StringHolder? = null
    var isIconTinted = false

    var level = 1
        protected set

    @Deprecated("Please consider to replace with the actual property setter")
    fun withSelectedIcon(selectedIcon: Drawable): T {
        this.selectedIcon = ImageHolder(selectedIcon)
        return this as T
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withSelectedIcon(@DrawableRes selectedIconRes: Int): T {
        this.selectedIcon = ImageHolder(selectedIconRes)
        return this as T
    }

    /**
     * will tint the icon with the default (or set) colors
     * (default and selected state)
     *
     * @param iconTintingEnabled
     * @return
     */
    @Deprecated("Please consider to replace with the actual property setter")
    fun withIconTintingEnabled(iconTintingEnabled: Boolean): T {
        this.isIconTinted = iconTintingEnabled
        return this as T
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withLevel(level: Int): T {
        this.level = level
        return this as T
    }

    /**
     * helper method to decide for the correct color
     *
     * @param ctx
     * @return
     */
    open fun getIconColor(ctx: Context): ColorStateList {
        return ctx.getPrimaryDrawerIconColor()
    }
}
