package com.mikepenz.materialdrawer.model.interfaces

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.mikepenz.materialdrawer.holder.ColorHolder

/**
 * Defines a [IDrawerItem] with support for defining a description
 */
interface DescribableColor {
    /** The color for the description text */
    var descriptionTextColor: ColorStateList?
}

/** Set the description color as color resource */
var SelectableColor.descriptionTextColorRes: Int
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(@ColorRes value) {
        selectedColor = ColorHolder.fromColorRes(value)
    }

/** Set the description color as color int */
var SelectableColor.descriptionTextColorInt: Int
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(@ColorInt value) {
        selectedColor = ColorHolder.fromColor(value)
    }
