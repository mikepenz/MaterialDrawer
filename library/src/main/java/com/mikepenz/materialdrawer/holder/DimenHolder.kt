package com.mikepenz.materialdrawer.holder

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import androidx.annotation.Dimension.DP
import androidx.annotation.Dimension.PX


/**
 * Created by mikepenz on 13.07.15.
 */
open class DimenHolder {
    var pixel = Int.MIN_VALUE
        internal set
    var dp = Int.MIN_VALUE
        internal set
    var resource = Int.MIN_VALUE
        internal set

    open fun asPixel(ctx: Context): Int {
        return when {
            pixel != Int.MIN_VALUE -> pixel
            dp != Int.MIN_VALUE -> ctx.convertDpToPixel(dp)
            resource != Int.MIN_VALUE -> ctx.resources.getDimensionPixelSize(resource)
            else -> 0
        }
    }

    companion object {
        fun fromPixel(@Dimension(unit = PX) pixel: Int): DimenHolder {
            val dimenHolder = DimenHolder()
            dimenHolder.pixel = pixel
            return dimenHolder
        }

        fun fromDp(@Dimension(unit = DP) dp: Int): DimenHolder {
            val dimenHolder = DimenHolder()
            dimenHolder.dp = dp
            return dimenHolder
        }

        fun fromResource(@DimenRes resource: Int): DimenHolder {
            val dimenHolder = DimenHolder()
            dimenHolder.resource = resource
            return dimenHolder
        }
    }
}


/**
 * This method converts dp unit to equivalent pixels, depending on device density.
 *
 * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
 * @return A float value to represent px equivalent to dp depending on device density
 */
private fun Context.convertDpToPixel(dp: Int): Int {
    val resources: Resources = resources
    val metrics: DisplayMetrics = resources.displayMetrics
    return (dp * (metrics.densityDpi / 160.0)).toInt()
}
