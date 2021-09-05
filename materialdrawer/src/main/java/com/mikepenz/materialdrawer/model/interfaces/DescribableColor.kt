package com.mikepenz.materialdrawer.model.interfaces

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

/**
 * Defines a [IDrawerItem] with support for defining a description
 */
interface DescribableColor {
    /** The color for the description text */
    var descriptionTextColor: ColorStateList?
}

/**
 * Set the description color as color resource.
 *
 * This method is deprecated and no-op.
 *
 * @deprecated
 */
var DescribableColor.descriptionTextColorRes: Int
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    @Deprecated(
        "Please use `descriptionTextColor` directly, as [ColorStateList] can't be resolved without [Context].",
        level = DeprecationLevel.WARNING,
        replaceWith = ReplaceWith("descriptionTextColor")
    )
    set(@ColorRes value) {
        // no-op
    }

/** Set the description color as color int */
var DescribableColor.descriptionTextColorInt: Int
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(@ColorInt value) {
        descriptionTextColor = ColorStateList.valueOf(value)
    }
