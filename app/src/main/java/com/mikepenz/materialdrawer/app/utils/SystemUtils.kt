package com.mikepenz.materialdrawer.app.utils

import android.content.res.Resources

object SystemUtils {
    val screenOrientation: Int
        get() = Resources.getSystem().configuration.orientation
}
