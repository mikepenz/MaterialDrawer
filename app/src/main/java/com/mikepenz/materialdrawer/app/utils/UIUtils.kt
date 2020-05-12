package com.mikepenz.materialdrawer.app.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics


/**
 * This method converts dp unit to equivalent pixels, depending on device density.
 *
 * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent px equivalent to dp depending on device density
 */
fun convertDpToPixel(dp: Float, context: Context): Float {
    val resources: Resources = context.resources
    val metrics: DisplayMetrics = resources.displayMetrics
    return dp * (metrics.densityDpi / 160f)
}
