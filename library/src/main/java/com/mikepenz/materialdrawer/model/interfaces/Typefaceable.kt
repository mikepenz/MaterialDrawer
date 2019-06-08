package com.mikepenz.materialdrawer.model.interfaces

import android.graphics.Typeface

/**
 * Created by mikepenz on 03.02.15.
 */
interface Typefaceable<T> {

    val typeface: Typeface?

    fun withTypeface(typeface: Typeface?): T
}
