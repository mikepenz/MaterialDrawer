package com.mikepenz.materialdrawer.model.interfaces

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.mikepenz.materialdrawer.holder.ColorHolder

/**
 * Defines a [IDrawerItem] with support for defining a name
 */
interface NameableColor {
    /** defines the color for the text */
    var textColor: ColorStateList?
}

/** Set the selected text color as color resource */
var SelectableColor.textColorRes: Int
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(@ColorRes value) {
        selectedColor = ColorHolder.fromColorRes(value)
    }

/** Set the selected text color as color int */
var SelectableColor.textColorInt: Int
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(@ColorInt value) {
        selectedColor = ColorHolder.fromColor(value)
    }
