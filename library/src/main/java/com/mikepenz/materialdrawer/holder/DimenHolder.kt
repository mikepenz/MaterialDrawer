package com.mikepenz.materialdrawer.holder

import androidx.annotation.DimenRes
import androidx.annotation.Dimension

import androidx.annotation.Dimension.DP
import androidx.annotation.Dimension.PX

/**
 * Created by mikepenz on 13.07.15.
 */
class DimenHolder : com.mikepenz.materialize.holder.DimenHolder() {
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
