package com.mikepenz.materialdrawer.model

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.Iconable
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import com.mikepenz.materialdrawer.model.interfaces.Tagable
import com.mikepenz.materialdrawer.util.ImageHolder
import com.mikepenz.materialdrawer.util.getPrimaryDrawerIconColor

/**
 * Created by mikepenz on 03.02.15.
 */
abstract class BaseDrawerItem<T, VH : RecyclerView.ViewHolder> : AbstractDrawerItem<T, VH>(), Nameable<T>, Iconable<T>, Tagable<T> {
    override var icon: ImageHolder? = null
    var selectedIcon: ImageHolder? = null
    override var name: StringHolder? = null
    var isIconTinted = false
    var iconColor: ColorStateList? = null


    var level = 1
        protected set

    override fun withIcon(icon: ImageHolder?): T {
        this.icon = icon
        return this as T
    }

    override fun withIcon(icon: Drawable?): T {
        if (icon != null) {
            this.icon = ImageHolder(icon)
        } else {
            this.icon = null
        }
        return this as T
    }

    fun withIcon(@DrawableRes iconRes: Int): T {
        this.icon = ImageHolder(iconRes)
        return this as T
    }

    fun withSelectedIcon(selectedIcon: Drawable): T {
        this.selectedIcon = ImageHolder(selectedIcon)
        return this as T
    }

    fun withSelectedIcon(@DrawableRes selectedIconRes: Int): T {
        this.selectedIcon = ImageHolder(selectedIconRes)
        return this as T
    }

    override fun withIcon(iicon: IIcon): T {
        this.icon = ImageHolder(iicon)
        //if we are on api 21 or higher we use the IconicsDrawable for selection too and color it with the correct color
        //else we use just the one drawable and enable tinting on press
        if (Build.VERSION.SDK_INT >= 21) {
            this.selectedIcon = ImageHolder(iicon)
        } else {
            this.withIconTintingEnabled(true)
        }

        return this as T
    }

    override fun withName(name: StringHolder?): T {
        this.name = name
        return this as T
    }

    override fun withName(name: String): T {
        this.name = StringHolder(name)
        return this as T
    }

    override fun withName(@StringRes nameRes: Int): T {
        this.name = StringHolder(nameRes)
        return this as T
    }

    fun withIconColor(iconColor: ColorStateList): T {
        this.iconColor = iconColor
        return this as T
    }

    /**
     * will tint the icon with the default (or set) colors
     * (default and selected state)
     *
     * @param iconTintingEnabled
     * @return
     */
    fun withIconTintingEnabled(iconTintingEnabled: Boolean): T {
        this.isIconTinted = iconTintingEnabled
        return this as T
    }

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
    fun getIconColor(ctx: Context): ColorStateList {
        return ctx.getPrimaryDrawerIconColor()
    }
}
