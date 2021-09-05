package com.mikepenz.materialdrawer.model.interfaces

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

/**
 * Defines a [IDrawerItem] with support for defining a name
 */
interface NameableColor {
    /** defines the color for the text */
    var textColor: ColorStateList?
}

/**
 * Set the selected text color as color resource.
 *
 * This method is deprecated and no-op.
 *
 * @deprecated
 */
var NameableColor.textColorRes: Int
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    @Deprecated(
        "Please use `textColor` directly, as [ColorStateList] can't be resolved without [Context].",
        level = DeprecationLevel.WARNING,
        replaceWith = ReplaceWith("descriptionTextColor")
    )
    set(@ColorRes value) {
        // no-op
    }

/** Set the selected text color as color int */
var NameableColor.textColorInt: Int
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(@ColorInt value) {
        textColor = ColorStateList.valueOf(value)
    }
