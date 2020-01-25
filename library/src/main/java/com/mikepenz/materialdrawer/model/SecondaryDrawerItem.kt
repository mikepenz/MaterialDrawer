package com.mikepenz.materialdrawer.model

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.LayoutRes
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.getSecondaryDrawerIconColor
import com.mikepenz.materialdrawer.util.getSecondaryDrawerTextColor

/**
 * Describes the secondary [IDrawerItem] bing used as primary item. Slightly smaller than the primary items
 */
open class SecondaryDrawerItem : AbstractBadgeableDrawerItem<SecondaryDrawerItem>() {

    override val type: Int
        get() = R.id.material_drawer_item_secondary

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_secondary

    override fun getColor(ctx: Context): ColorStateList {
        return ctx.getSecondaryDrawerTextColor()
    }

    override fun getIconColor(ctx: Context): ColorStateList {
        return ctx.getSecondaryDrawerIconColor()
    }
}
