package com.mikepenz.materialdrawer.model.interfaces

import com.mikepenz.materialdrawer.holder.StringHolder

/**
 * Created by mikepenz on 03.02.15.
 */
interface Nameable<T> {

    val name: StringHolder?

    fun withName(name: String): T

    fun withName(nameRes: Int): T

    fun withName(name: StringHolder?): T
}
