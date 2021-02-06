package com.mikepenz.materialdrawer.model.interfaces

import androidx.annotation.StringRes
import com.mikepenz.materialdrawer.holder.StringHolder

/**
 * Defines a [IDrawerItem] with support for defining a description
 */
interface Describable {
    /** The text to show as description */
    var description: StringHolder?
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Describable> T.withDescription(description: String): T {
    this.description = StringHolder(description)
    return this
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Describable> T.withDescription(@StringRes descriptionRes: Int): T {
    this.description = StringHolder(descriptionRes)
    return this
}

/** Set the description */
var Describable.descriptionRes: Int
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(value) {
        description = StringHolder(value)
    }

/** Set the description */
var Describable.descriptionText: CharSequence
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(value) {
        description = StringHolder(value)
    }