package com.mikepenz.materialdrawer.model.interfaces

import com.mikepenz.materialdrawer.holder.StringHolder

/**
 * Defines a [IDrawerItem] which allows to have a badge
 */
interface Badgeable {
    /** the badge text to show for this item */
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

/** Set the badge name */
var Badgeable.badgeRes: Int
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(value) {
        badge = StringHolder(value)
    }

/** Set the badge name */
var Badgeable.badgeText: CharSequence
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(value) {
        badge = StringHolder(value)
    }
