package com.mikepenz.materialdrawer.model.utils

import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

/** Define tags associated to specific tag items */
private val drawerItemTags = mutableMapOf<Any, Any>()

/** Define if this IDrawerItem should / can be shown inside the MiniDrawer */
var <T : IDrawerItem<*>> T.hiddenInMiniDrawer: Boolean
    get() = drawerItemTags[this] as? Boolean == true
    set(value) {
        drawerItemTags[this] = value
    }

@Deprecated("Please consider to replace with the actual property setter")
fun <T : IDrawerItem<*>> T.withIsHiddenInMiniDrawer(hidden: Boolean): T {
    hiddenInMiniDrawer = hidden
    return this
}
