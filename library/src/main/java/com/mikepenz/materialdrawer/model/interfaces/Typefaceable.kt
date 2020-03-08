package com.mikepenz.materialdrawer.model.interfaces

import android.graphics.Typeface

/**
 * Defines a [IDrawerItem] with support for defining the [Typeface]
 */
interface Typefaceable {
    /** the typeface used for texts */
    var typeface: Typeface?
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Typefaceable> T.withTypeface(typeface: Typeface?): T {
    this.typeface = typeface
    return this
}
