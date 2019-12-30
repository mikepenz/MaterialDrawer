package com.mikepenz.materialdrawer.model.interfaces

/**
 * Defines a [IDrawerItem] with support for being selected
 */
interface Checkable : Selectable {
    var isChecked: Boolean
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Checkable> T.withChecked(checked: Boolean): T {
    this.isChecked = checked
    return this
}


@Deprecated("Please consider to replace with the actual property setter")
fun <T : Checkable> T.withCheckable(checkable: Boolean): T {
    this.isSelectable = checkable
    return this
}
