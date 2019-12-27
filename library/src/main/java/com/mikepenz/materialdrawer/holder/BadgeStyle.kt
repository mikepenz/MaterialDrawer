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
    var gradientDrawable = R.drawable.material_drawer_badge
    var badgeBackground: Drawable? = null
    var color: ColorHolder? = null
    var colorPressed: ColorHolder? = null
    private var _textColor: ColorHolder? = null
    var textColor: ColorHolder?
        get() = _textColor
        set(value) {
            _textColorStateList = null
            _textColor = value
        }

    private var _textColorStateList: ColorStateList? = null
    var textColorStateList: ColorStateList?
        get() = _textColorStateList
        set(value) {
            _textColor = null
            _textColorStateList = value
        }
    var corners: DimenHolder? = null
    var paddingTopBottom = DimenHolder.fromDp(2)
    var paddingLeftRight = DimenHolder.fromDp(3)
    var minWidth = DimenHolder.fromDp(20)

    constructor() {}

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

    @JvmOverloads
    open fun style(badgeTextView: TextView, colorStateList: ColorStateList? = null) {
        val ctx = badgeTextView.context
        //set background for badge
        if (badgeBackground == null) {
            ViewCompat.setBackground(badgeTextView, BadgeDrawableBuilder(this).build(ctx))
        } else {
            ViewCompat.setBackground(badgeTextView, badgeBackground)
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
    }
}
