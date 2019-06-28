package com.mikepenz.materialdrawer.model

import android.content.Context
import androidx.annotation.LayoutRes
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.applyColor

/**
 * Created by mikepenz on 03.02.15.
 */
open class SecondarySwitchDrawerItem : AbstractSwitchableDrawerItem<SecondarySwitchDrawerItem>() {

    override val type: Int
        get() = R.id.material_drawer_item_secondary_switch

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_secondary_switch

    /**
     * helper method to decide for the correct color
     * OVERWRITE to get the correct secondary color
     *
     * @param ctx
     * @return
     */
    override fun getColor(ctx: Context): Int {
        return if (isEnabled) {
            textColor.applyColor(ctx, R.attr.material_drawer_secondary_text, R.color.material_drawer_secondary_text)
        } else {
            disabledTextColor.applyColor(ctx, R.attr.material_drawer_hint_text, R.color.material_drawer_hint_text)
        }
    }
}
