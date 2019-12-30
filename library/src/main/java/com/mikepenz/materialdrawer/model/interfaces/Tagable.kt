package com.mikepenz.materialdrawer.model.interfaces

/**
 * Defines a [IDrawerItem] with support for being tagged
 */
interface Tagable {
    var tag: Any?
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Tagable> T.withTag(tag: Any?): T {
    this.tag = tag
    return this
}