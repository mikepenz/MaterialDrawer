package com.mikepenz.materialdrawer.holder

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

/**
 * Created by mikepenz on 13.07.15.
 */
open class ColorHolder : com.mikepenz.materialize.holder.ColorHolder() {
    companion object {
        fun fromColorRes(@ColorRes colorRes: Int): ColorHolder {
            val colorHolder = ColorHolder()
            colorHolder.colorRes = colorRes
            return colorHolder
        }

        fun fromColor(@ColorInt colorInt: Int): ColorHolder {
            val colorHolder = ColorHolder()
            colorHolder.colorInt = colorInt
            return colorHolder
        }
    }
}

/**
 * a small static helper class to get the color from the colorHolder or from the theme or from the default color value
 *
 * @param colorHolder
 * @param ctx
 * @param colorStyle
 * @param colorDefault
 * @return
 */
fun ColorHolder?.applyColor(ctx: Context, @AttrRes colorStyle: Int, @ColorRes colorDefault: Int): Int {
    return com.mikepenz.materialize.holder.ColorHolder.color(this, ctx, colorStyle, colorDefault)
}

/**
 * a small static helper class to get the color from the colorHolder
 *
 * @param colorHolder
 * @param ctx
 * @return
 */
fun ColorHolder?.applyColor(ctx: Context): Int {
    return com.mikepenz.materialize.holder.ColorHolder.color(this, ctx)
}

/**
 * a small static helper to set the text color to a textView null save
 *
 * @param colorHolder
 * @param textView
 * @param colorDefault
 */
fun ColorHolder?.applyToOrDefault(textView: TextView?, colorDefault: ColorStateList) {
    com.mikepenz.materialize.holder.ColorHolder.applyToOr(this, textView, colorDefault)
}

/**
 * a small static helper to set the color to a GradientDrawable null save
 *
 * @param colorHolder
 * @param ctx
 * @param gradientDrawable
 */
fun ColorHolder?.applyToOrTransparent(ctx: Context, gradientDrawable: GradientDrawable?) {
    com.mikepenz.materialize.holder.ColorHolder.applyToOrTransparent(this, ctx, gradientDrawable)
}