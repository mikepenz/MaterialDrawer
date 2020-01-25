package com.mikepenz.materialdrawer.model.interfaces

import androidx.annotation.StringRes
import com.mikepenz.materialdrawer.holder.StringHolder

/**
 * Defines a [IDrawerItem] with support for defining a name
 */
interface Nameable {
    var name: StringHolder?
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Nameable> T.withName(name: String?): T {
    this.name = StringHolder(name)
    return this
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Nameable> T.withName(@StringRes name: Int): T {
    this.name = StringHolder(name)
    return this
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Nameable> T.withName(name: StringHolder?): T {
    this.name = name
    return this
}