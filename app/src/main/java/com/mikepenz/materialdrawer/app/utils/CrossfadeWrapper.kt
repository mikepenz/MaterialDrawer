package com.mikepenz.materialdrawer.app.utils

import com.mikepenz.crossfader.Crossfader
import com.mikepenz.materialdrawer.interfaces.ICrossfader

/**
 * Created by mikepenz on 18.07.15.
 */
class CrossfadeWrapper(private val crossfader: Crossfader<*>) : ICrossfader {

    override val isCrossfaded: Boolean
        get() = crossfader.isCrossFaded()

    override fun crossfade() {
        crossfader.crossFade()
    }
}
