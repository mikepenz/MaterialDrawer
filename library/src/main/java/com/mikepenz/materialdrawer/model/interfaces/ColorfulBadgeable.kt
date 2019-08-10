package com.mikepenz.materialdrawer.model.interfaces

import com.mikepenz.materialdrawer.holder.BadgeStyle

/**
 * Created by mikepenz on 03.02.15.
 */
interface ColorfulBadgeable<T: ColorfulBadgeable<T>> : Badgeable<T> {

    val badgeStyle: BadgeStyle?

    fun withBadgeStyle(badgeStyle: BadgeStyle?): T

}
