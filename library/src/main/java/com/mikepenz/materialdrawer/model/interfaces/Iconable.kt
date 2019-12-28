package com.mikepenz.materialdrawer.model.interfaces

import android.graphics.drawable.Drawable

import com.mikepenz.materialdrawer.holder.ImageHolder

/**
 * Created by mikepenz on 03.02.15.
 */
interface Iconable<T> {

    val icon: ImageHolder?

    fun withIcon(icon: Drawable?): T

    fun withIcon(icon: ImageHolder?): T
}
