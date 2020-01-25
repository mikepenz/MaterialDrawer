package com.mikepenz.materialdrawer.model.interfaces

import com.mikepenz.materialdrawer.holder.StringHolder

/**
 * Defines a [IDrawerItem] which allows to have a badge
 */
interface Badgeable {
    var badge: StringHolder?
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Badgeable> T.withBadge(badge: String): T {
    this.badge = StringHolder(badge)
    return this
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Badgeable> T.withBadge(badgeRes: Int): T {
    this.badge = StringHolder(badgeRes)
    return this
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Badgeable> T.withBadge(badge: StringHolder?): T {
    this.badge = badge
    return this
}
