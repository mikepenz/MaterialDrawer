package com.mikepenz.materialdrawer.interfaces

/**
 * Helper interface to allow providing crossfade functionality with different implementations to the drawer
 */
interface ICrossfader {

    /**
     * returns true if currently crossfaded
     */
    val isCrossfaded: Boolean

    /**
     * allows to toggle the crossfade state
     */
    fun crossfade()
}
