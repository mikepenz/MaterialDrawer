package com.mikepenz.materialdrawer.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.ColorHolder
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.holder.applyColor
import com.mikepenz.materialdrawer.model.interfaces.Iconable
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import com.mikepenz.materialdrawer.model.interfaces.Tagable

/**
 * Created by mikepenz on 03.02.15.
 */
abstract class BaseDrawerItem<T, VH : RecyclerView.ViewHolder> : AbstractDrawerItem<T, VH>(), Nameable<T>, Iconable<T>, Tagable<T> {
    override var icon: ImageHolder? = null
    var selectedIcon: ImageHolder? = null
    override var name: StringHolder? = null
    var isIconTinted = false
    var iconColor: ColorHolder? = null
    var selectedIconColor: ColorHolder? = null
    var disabledIconColor: ColorHolder? = null

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

    fun withIconColor(@ColorInt iconColor: Int): T {
        this.iconColor = ColorHolder.fromColor(iconColor)
        return this as T
    }

    fun withIconColorRes(@ColorRes iconColorRes: Int): T {
        this.iconColor = ColorHolder.fromColorRes(iconColorRes)
        return this as T
    }

    fun withSelectedIconColor(@ColorInt selectedIconColor: Int): T {
        this.selectedIconColor = ColorHolder.fromColor(selectedIconColor)
        return this as T
    }

    fun withSelectedIconColorRes(@ColorRes selectedColorRes: Int): T {
        this.selectedIconColor = ColorHolder.fromColorRes(selectedColorRes)
        return this as T
    }

    fun withDisabledIconColor(@ColorInt disabledIconColor: Int): T {
        this.disabledIconColor = ColorHolder.fromColor(disabledIconColor)
        return this as T
    }

    fun withDisabledIconColorRes(@ColorRes disabledIconColorRes: Int): T {
        this.disabledIconColor = ColorHolder.fromColorRes(disabledIconColorRes)
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

    @Deprecated("")
    fun withIconTinted(iconTinted: Boolean): T {
        this.isIconTinted = iconTinted
        return this as T
    }

    /**
     * for backwards compatibility - withIconTinted..
     *
     * @param iconTinted
     * @return
     */
    @Deprecated("")
    fun withTintSelectedIcon(iconTinted: Boolean): T {
        return withIconTintingEnabled(iconTinted)
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
    fun getIconColor(ctx: Context): Int {
        return if (this.isEnabled) {
            iconColor.applyColor(ctx, R.attr.material_drawer_primary_icon, R.color.material_drawer_primary_icon)
        } else {
            disabledIconColor.applyColor(ctx, R.attr.material_drawer_hint_icon, R.color.material_drawer_hint_icon)
        }
    }

    /**
     * helper method to decide for the correct color
     *
     * @param ctx
     * @return
     */
    protected fun getSelectedIconColor(ctx: Context): Int {
        return selectedIconColor.applyColor(ctx, R.attr.material_drawer_selected_text, R.color.material_drawer_selected_text)
    }
}
