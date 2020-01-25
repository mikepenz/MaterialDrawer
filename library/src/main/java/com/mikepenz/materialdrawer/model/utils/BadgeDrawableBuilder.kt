package com.mikepenz.materialdrawer.model.utils

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import androidx.appcompat.content.res.AppCompatResources
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.holder.ColorHolder

/**
 * Builder to construct the [StateListDrawable] given a [BadgeStyle]
 */
class BadgeDrawableBuilder(private val style: BadgeStyle) {

    /** creates the [StateListDrawable] given the provided [BadgeStyle] */
    fun build(ctx: Context): StateListDrawable {
        val stateListDrawable = StateListDrawable()
        val normal = AppCompatResources.getDrawable(ctx, style.gradientDrawable) as GradientDrawable?
        val selected = normal?.constantState?.newDrawable()?.mutate() as GradientDrawable?

        ColorHolder.applyToOrTransparent(style.color, ctx, normal)
        if (style.colorPressed == null) {
            ColorHolder.applyToOrTransparent(style.color, ctx, selected)
        } else {
            ColorHolder.applyToOrTransparent(style.colorPressed, ctx, selected)
        }

        style.corners?.let {
            normal?.cornerRadius = it.asPixel(ctx).toFloat()
            selected?.cornerRadius = it.asPixel(ctx).toFloat()
        }

        stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), selected)
        stateListDrawable.addState(StateSet.WILD_CARD, normal)

        return stateListDrawable
    }
}
