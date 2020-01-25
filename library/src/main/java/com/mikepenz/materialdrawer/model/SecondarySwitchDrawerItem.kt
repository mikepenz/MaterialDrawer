package com.mikepenz.materialdrawer.model

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.LayoutRes
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.getSecondaryDrawerIconColor
import com.mikepenz.materialdrawer.util.getSecondaryDrawerTextColor

/**
 * Describes a [IDrawerItem] bing used as secondary item, offering a switch.
 */
open class SecondarySwitchDrawerItem : AbstractSwitchableDrawerItem<SecondarySwitchDrawerItem>() {

    override val type: Int
        get() = R.id.material_drawer_item_secondary_switch

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_secondary_switch

    override fun getColor(ctx: Context): ColorStateList {
        return ctx.getSecondaryDrawerTextColor()
    }

    override fun getIconColor(ctx: Context): ColorStateList {
        return ctx.getSecondaryDrawerIconColor()
    }
}
