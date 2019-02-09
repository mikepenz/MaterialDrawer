package com.mikepenz.materialdrawer.model.utils

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import androidx.appcompat.content.res.AppCompatResources
import com.mikepenz.materialdrawer.holder.BadgeStyle

/**
 * Created by mikepenz on 02.07.15.
 */
class BadgeDrawableBuilder(private val mStyle: BadgeStyle) {

    fun build(ctx: Context): StateListDrawable {
        val stateListDrawable = StateListDrawable()
        val normal = AppCompatResources.getDrawable(ctx, mStyle.gradientDrawable) as GradientDrawable?
        val selected = normal?.constantState?.newDrawable()?.mutate() as GradientDrawable?

        com.mikepenz.materialize.holder.ColorHolder.applyToOrTransparent(mStyle.color, ctx, normal)
        if (mStyle.colorPressed == null) {
            com.mikepenz.materialize.holder.ColorHolder.applyToOrTransparent(mStyle.color, ctx, selected)
        } else {
            com.mikepenz.materialize.holder.ColorHolder.applyToOrTransparent(mStyle.colorPressed, ctx, selected)
        }

        mStyle.corners?.let {
            normal?.cornerRadius = it.asPixel(ctx).toFloat()
            selected?.cornerRadius = it.asPixel(ctx).toFloat()
        }

        stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), selected)
        stateListDrawable.addState(StateSet.WILD_CARD, normal)

        return stateListDrawable
    }
}
