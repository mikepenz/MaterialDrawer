package com.mikepenz.materialdrawer.model.interfaces

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.mikepenz.materialdrawer.holder.ColorHolder

/**
 * Defines a [IDrawerItem] with support for having a different color when selected
 */
interface SelectableColor {
    /** The background color of a selectable item */
    var selectedColor: ColorHolder?
}


@Deprecated("Please consider to replace with the actual property setter")
fun <T : SelectableColor> T.withSelectedColor(@ColorInt selectedColor: Int): T {
    this.selectedColor = ColorHolder.fromColor(selectedColor)
    return this
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : SelectableColor> T.withSelectedColorRes(@ColorRes selectedColorRes: Int): T {
    this.selectedColor = ColorHolder.fromColorRes(selectedColorRes)
    return this
}

/** Set the selected color as color resource */
var SelectableColor.selectedColorRes: Int
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(@ColorRes value) {
        selectedColor = ColorHolder.fromColorRes(value)
    }

/** Set the selected color as color int */
var SelectableColor.selectedColorInt: Int
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(@ColorInt value) {
        selectedColor = ColorHolder.fromColor(value)
    }
