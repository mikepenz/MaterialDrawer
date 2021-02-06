package com.mikepenz.materialdrawer.holder

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.view.ViewCompat
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.model.utils.BadgeDrawableBuilder

/**
 * Class to allow defining a BadgeStyle for the `BadgeDrawerItem`
 */
open class BadgeStyle {
    /** defines the drawable to use to define the rounded corners */
    var gradientDrawable = R.drawable.material_drawer_badge

    /** defines the background drawable */
    var badgeBackground: Drawable? = null

    /** the default color */
    var color: ColorHolder? = null

    /** the pressed color */
    var colorPressed: ColorHolder? = null

    /**
     * the text size
     * NOTE: Will only apply on 21+, also if applying, ensure to apply to all views
     */
    var textSizeSp: Float? = null
    private var _textColor: ColorHolder? = null

    /** defines the default text color */
    var textColor: ColorHolder?
        get() = _textColor
        set(value) {
            _textColorStateList = null
            _textColor = value
        }

    private var _textColorStateList: ColorStateList? = null

    /** defines the alternative text color state list */
    var textColorStateList: ColorStateList?
        get() = _textColorStateList
        set(value) {
            _textColor = null
            _textColorStateList = value
        }

    /** the corner radious */
    var corners: DimenHolder? = null

    /** dcustom padding to the bottom (default 2dp) */
    var paddingTopBottom = DimenHolder.fromDp(2)

    /** custom padding to the right (default 3dp) */
    var paddingLeftRight = DimenHolder.fromDp(3)

    /** the min width to set (default 20dp) */
    var minWidth = DimenHolder.fromDp(20)

    /**
     * elevation to apply on the view
     * NOTE: Will only apply on 21+, also if applying, ensure to apply to all views
     */
    var elevation: DimenHolder? = null

    constructor()

    constructor(@ColorInt color: Int, @ColorInt colorPressed: Int) {
        this.color = ColorHolder.fromColor(color)
        this.colorPressed = ColorHolder.fromColor(colorPressed)
    }

    constructor(@DrawableRes gradientDrawable: Int, @ColorInt color: Int, @ColorInt colorPressed: Int, @ColorInt textColor: Int) {
        this.gradientDrawable = gradientDrawable
        this.color = ColorHolder.fromColor(color)
        this.colorPressed = ColorHolder.fromColor(colorPressed)
        this.textColor = ColorHolder.fromColor(textColor)
    }

    /** styles theprovided textView with this style, and the provided colorStateList */
    @JvmOverloads
    open fun style(badgeTextView: TextView, colorStateList: ColorStateList? = null) {
        val ctx = badgeTextView.context
        //set background for badge
        if (badgeBackground == null) {
            ViewCompat.setBackground(badgeTextView, BadgeDrawableBuilder(this).build(ctx))
        } else {
            ViewCompat.setBackground(badgeTextView, badgeBackground)
        }

        val textSizeSp = textSizeSp
        if (textSizeSp != null) {
            badgeTextView.textSize = textSizeSp
        } else {
            // keep the size it is defined in the layout
        }

        //set the badge text color
        when {
            textColor != null -> textColor?.applyToOr(badgeTextView, null)
            textColorStateList != null -> badgeTextView.setTextColor(textColorStateList)
            colorStateList != null -> badgeTextView.setTextColor(colorStateList)
        }

        //set the padding
        val paddingLeftRight = this.paddingLeftRight.asPixel(ctx)
        val paddingTopBottom = this.paddingTopBottom.asPixel(ctx)
        badgeTextView.setPadding(paddingLeftRight, paddingTopBottom, paddingLeftRight, paddingTopBottom)

        //set the min width
        badgeTextView.minWidth = minWidth.asPixel(ctx)

        // set elevation if expected
        elevation?.let { elev ->
            ViewCompat.setElevation(badgeTextView, elev.asPixel(ctx).toFloat())
        }
    }
}
