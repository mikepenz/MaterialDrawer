package com.mikepenz.materialdrawer.model.interfaces

import com.mikepenz.materialdrawer.holder.StringHolder

/**
 * Created by mikepenz on 03.02.15.
 */
interface Badgeable<T : Badgeable<T>> {

    val badge: StringHolder?

    fun withBadge(badge: String): T

    fun withBadge(badgeRes: Int): T

    fun withBadge(badge: StringHolder?): T
}
