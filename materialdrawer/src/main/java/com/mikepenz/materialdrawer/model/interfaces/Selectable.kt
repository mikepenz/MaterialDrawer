package com.mikepenz.materialdrawer.model.interfaces

/**
 * Defines a [IDrawerItem] with support for being selected
 */
interface Selectable {
    /** If the item is selectable */
    var isSelectable: Boolean
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Selectable> T.withSelectable(selectable: Boolean): T {
    this.isSelectable = selectable
    return this
}
