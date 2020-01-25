package com.mikepenz.materialdrawer.holder

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import androidx.annotation.Dimension.DP
import androidx.annotation.Dimension.PX


/**
 * Defines a custom holder class to support providing dimension either as pixel, dp or dimensRes. Does not require a [Context] and will resolve the value when applying.
 */
open class DimenHolder {
    /** Defines the pixel dimension */
    var pixel = Int.MIN_VALUE
        internal set
    /** Defines the dp dimension */
    var dp = Int.MIN_VALUE
        internal set
    /** Defines the resource dimension */
    var resource = Int.MIN_VALUE
        internal set

    /** Resturns this [DimenHolder]`s dimension as pixel value */
    open fun asPixel(ctx: Context): Int {
        return when {
            pixel != Int.MIN_VALUE -> pixel
            dp != Int.MIN_VALUE -> ctx.convertDpToPixel(dp)
            resource != Int.MIN_VALUE -> ctx.resources.getDimensionPixelSize(resource)
            else -> 0
        }
    }

    companion object {
        /**
         * Constructs a [DimenHolder] given a pixel value
         */
        fun fromPixel(@Dimension(unit = PX) pixel: Int): DimenHolder {
            val dimenHolder = DimenHolder()
            dimenHolder.pixel = pixel
            return dimenHolder
        }

        /**
         * Constructs a [DimenHolder] given a dp value
         */
        fun fromDp(@Dimension(unit = DP) dp: Int): DimenHolder {
            val dimenHolder = DimenHolder()
            dimenHolder.dp = dp
            return dimenHolder
        }

        /**
         * Constructs a [DimenHolder] given a resource id
         */
        fun fromResource(@DimenRes resource: Int): DimenHolder {
            val dimenHolder = DimenHolder()
            dimenHolder.resource = resource
            return dimenHolder
        }
    }
}


/**
 * This method converts dp unit to equivalent pixels, depending on device density.
 */
private fun Context.convertDpToPixel(dp: Int): Int {
    val resources: Resources = resources
    val metrics: DisplayMetrics = resources.displayMetrics
    return (dp * (metrics.densityDpi / 160.0)).toInt()
}
