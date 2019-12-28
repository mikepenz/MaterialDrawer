package com.mikepenz.materialdrawer.holder

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.mikepenz.materialdrawer.util.getThemeColorFromAttrOrRes


/**
 * Created by mikepenz on 13.07.15.
 */
open class ColorHolder {
    var colorInt = 0
        internal set
    var colorRes = -1
        internal set

    /**
     * set the textColor of the ColorHolder to an drawable
     *
     * @param ctx
     * @param drawable
     */
    open fun applyTo(ctx: Context, drawable: GradientDrawable) {
        if (colorInt != 0) {
            drawable.setColor(colorInt)
        } else if (colorRes != -1) {
            drawable.setColor(ContextCompat.getColor(ctx, colorRes))
        }
    }


    /**
     * set the textColor of the ColorHolder to a view
     *
     * @param view
     */
    open fun applyToBackground(view: View) {
        if (colorInt != 0) {
            view.setBackgroundColor(colorInt)
        } else if (colorRes != -1) {
            view.setBackgroundResource(colorRes)
        }
    }

    /**
     * a small helper to set the text color to a textView null save
     *
     * @param textView
     * @param colorDefault
     */
    open fun applyToOr(textView: TextView, colorDefault: ColorStateList?) {
        when {
            colorInt != 0 -> {
                textView.setTextColor(colorInt)
            }
            colorRes != -1 -> {
                textView.setTextColor(ContextCompat.getColor(textView.context, colorRes))
            }
            colorDefault != null -> {
                textView.setTextColor(colorDefault)
            }
        }
    }

    /**
     * a small helper class to get the color from the colorHolder or from the theme or from the default color value
     *
     * @param ctx
     * @param colorStyle
     * @param colorDefaultRes
     * @return
     */
    open fun color(ctx: Context, @AttrRes colorStyle: Int, @ColorRes colorDefaultRes: Int): Int { //get the color from the holder else from the theme
        val color = color(ctx)
        return if (color == 0) {
            ctx.getThemeColorFromAttrOrRes(colorStyle, colorDefaultRes)
        } else {
            color
        }
    }

    /**
     * a small helper to get the color from the colorHolder
     *
     * @param ctx
     * @return
     */
    open fun color(ctx: Context): Int {
        if (colorInt == 0 && colorRes != -1) {
            colorInt = ContextCompat.getColor(ctx, colorRes)
        }
        return colorInt
    }

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

        /**
         * a small static helper class to get the color from the colorHolder or from the theme or from the default color value
         *
         * @param colorHolder
         * @param ctx
         * @param colorStyle
         * @param colorDefault
         * @return
         */
        fun color(colorHolder: ColorHolder?, ctx: Context, @AttrRes colorStyle: Int, @ColorRes colorDefault: Int): Int {
            return colorHolder?.color(ctx, colorStyle, colorDefault)
                    ?: ctx.getThemeColorFromAttrOrRes(colorStyle, colorDefault)
        }

        /**
         * a small static helper class to get the color from the colorHolder
         *
         * @param colorHolder
         * @param ctx
         * @return
         */
        fun color(colorHolder: ColorHolder?, ctx: Context): Int {
            return colorHolder?.color(ctx) ?: 0
        }

        /**
         * a small static helper to set the text color to a textView null save
         *
         * @param colorHolder
         * @param textView
         * @param colorDefault
         */
        fun applyToOr(colorHolder: ColorHolder?, textView: TextView?, colorDefault: ColorStateList?) {
            if (colorHolder != null && textView != null) {
                colorHolder.applyToOr(textView, colorDefault)
            } else textView?.setTextColor(colorDefault)
        }

        /**
         * a small static helper to set the color to a GradientDrawable null save
         *
         * @param colorHolder
         * @param ctx
         * @param gradientDrawable
         */
        fun applyToOrTransparent(colorHolder: ColorHolder?, ctx: Context, gradientDrawable: GradientDrawable?) {
            if (colorHolder != null && gradientDrawable != null) {
                colorHolder.applyTo(ctx, gradientDrawable)
            } else gradientDrawable?.setColor(Color.TRANSPARENT)
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
    return ColorHolder.color(this, ctx, colorStyle, colorDefault)
}

/**
 * a small static helper class to get the color from the colorHolder
 *
 * @param colorHolder
 * @param ctx
 * @return
 */
fun ColorHolder?.applyColor(ctx: Context): Int {
    return ColorHolder.color(this, ctx)
}

/**
 * a small static helper to set the text color to a textView null save
 *
 * @param colorHolder
 * @param textView
 * @param colorDefault
 */
fun ColorHolder?.applyToOrDefault(textView: TextView?, colorDefault: ColorStateList) {
    ColorHolder.applyToOr(this, textView, colorDefault)
}

/**
 * a small static helper to set the color to a GradientDrawable null save
 *
 * @param colorHolder
 * @param ctx
 * @param gradientDrawable
 */
fun ColorHolder?.applyToOrTransparent(ctx: Context, gradientDrawable: GradientDrawable?) {
    ColorHolder.applyToOrTransparent(this, ctx, gradientDrawable)
}