package com.mikepenz.materialdrawer.util

import android.R
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable

/**
 * http://stackoverflow.com/questions/7979440/android-cloning-a-drawable-in-order-to-make-a-statelistdrawable-with-filters
 * http://stackoverflow.com/users/2075875/malachiasz
 */
@SuppressLint("InlinedApi")
class FixStateListDrawable : StateListDrawable {
    var color: ColorStateList?

    constructor(drawable: Drawable, color: ColorStateList?) : super() {
        var drawable = drawable
        drawable = drawable.mutate()
        addState(intArrayOf(R.attr.state_selected), drawable)
        addState(intArrayOf(), drawable)
        this.color = color
    }

    constructor(drawable: Drawable, selectedDrawable: Drawable, color: ColorStateList?) : super() {
        var drawable = drawable
        var selectedDrawable = selectedDrawable
        drawable = drawable.mutate()
        selectedDrawable = selectedDrawable.mutate()
        addState(intArrayOf(R.attr.state_selected), selectedDrawable)
        addState(intArrayOf(), drawable)
        this.color = color
    }

    override fun onStateChange(states: IntArray): Boolean {
        val color = color
        if (color != null) {
            super.setColorFilter(color.getColorForState(states, color.defaultColor), PorterDuff.Mode.SRC_IN)
        }
        return super.onStateChange(states)
    }

    override fun isStateful(): Boolean {
        return true
    }
}