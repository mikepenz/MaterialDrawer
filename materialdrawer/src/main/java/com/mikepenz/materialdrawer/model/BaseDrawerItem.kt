package com.mikepenz.materialdrawer.model

import android.content.Context
import android.content.res.ColorStateList
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.*
import com.mikepenz.materialdrawer.util.getPrimaryDrawerIconColor

/**
 * An abstract [IDrawerItem] implementation providing the base properties with their default value
 */
abstract class BaseDrawerItem<T, VH : RecyclerView.ViewHolder> : AbstractDrawerItem<T, VH>(), Nameable, NameableColor, Iconable, SelectIconable, Tagable {
    override var icon: ImageHolder? = null
    override var iconColor: ColorStateList? = null
    override var selectedIcon: ImageHolder? = null
    override var name: StringHolder? = null
    override var textColor: ColorStateList? = null
    override var isIconTinted = false

    /** Allows to set the 'level' of this item */
    var level = 1

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
