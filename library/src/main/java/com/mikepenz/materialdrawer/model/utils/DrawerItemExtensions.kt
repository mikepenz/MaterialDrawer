package com.mikepenz.materialdrawer.model.utils

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.mikepenz.materialdrawer.holder.ColorHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.AbstractDrawerItem
import com.mikepenz.materialdrawer.model.BaseDescribeableDrawerItem
import com.mikepenz.materialdrawer.model.BaseDrawerItem

/** Set the name */
var BaseDrawerItem<*, *>.nameRes: Int
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(value) {
        name = StringHolder(value)
    }

/** Set the name */
var BaseDrawerItem<*, *>.nameText: CharSequence
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(value) {
        name = StringHolder(value)
    }

/** Set the description */
var BaseDescribeableDrawerItem<*, *>.descriptionRes: Int
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(value) {
        description = StringHolder(value)
    }

/** Set the description */
var BaseDescribeableDrawerItem<*, *>.descriptionText: CharSequence
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(value) {
        description = StringHolder(value)
    }

/** Set the selected color as color resource */
var AbstractDrawerItem<*, *>.selectedColorRes: Int
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(@ColorRes value) {
        selectedColor = ColorHolder.fromColorRes(value)
    }

/** Set the selected color as color resource */
var AbstractDrawerItem<*, *>.selectedColorInt: Int
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(@ColorInt value) {
        selectedColor = ColorHolder.fromColor(value)
    }