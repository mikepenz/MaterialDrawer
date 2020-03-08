package com.mikepenz.materialdrawer.model.interfaces

import com.mikepenz.materialdrawer.holder.BadgeStyle

/**
 * Defines a [IDrawerItem] which allows to have a colorful badge
 */
interface ColorfulBadgeable : Badgeable {
    /** defines the style for the badge in the item */
    var badgeStyle: BadgeStyle?
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : ColorfulBadgeable> T.withBadgeStyle(badgeStyle: BadgeStyle?): T {
    this.badgeStyle = badgeStyle
    return this
}