package com.mikepenz.materialdrawer.holder

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.*
import androidx.annotation.Dimension.DP
import androidx.annotation.Dimension.PX
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
    var textColor: ColorHolder? = null
    private var mTextColorStateList: ColorStateList? = null
    var corners: DimenHolder? = null
    var paddingTopBottom = DimenHolder.fromDp(2)
    var paddingLeftRight = DimenHolder.fromDp(3)
    var minWidth = DimenHolder.fromDp(20)

    fun withGradientDrawable(@DrawableRes gradientDrawable: Int): BadgeStyle {
        this.gradientDrawable = gradientDrawable
        this.badgeBackground = null
        return this
    }

    fun withBadgeBackground(badgeBackground: Drawable): BadgeStyle {
        this.badgeBackground = badgeBackground
        this.gradientDrawable = -1
        return this
    }

    fun withColor(@ColorInt color: Int): BadgeStyle {
        this.color = ColorHolder.fromColor(color)
        return this
    }

    fun withColorRes(@ColorRes color: Int): BadgeStyle {
        this.color = ColorHolder.fromColorRes(color)
        return this
    }

    fun withColorPressed(@ColorInt colorPressed: Int): BadgeStyle {
        this.colorPressed = ColorHolder.fromColor(colorPressed)
        return this
    }

    fun withColorPressedRes(@ColorRes colorPressed: Int): BadgeStyle {
        this.colorPressed = ColorHolder.fromColorRes(colorPressed)
        return this
    }

    fun withTextColor(@ColorInt textColor: Int): BadgeStyle {
        this.textColor = ColorHolder.fromColor(textColor)
        return this
    }

    fun withTextColorRes(@ColorRes textColor: Int): BadgeStyle {
        this.textColor = ColorHolder.fromColorRes(textColor)
        return this
    }

    fun withTextColorStateList(textColorStateList: ColorStateList): BadgeStyle {
        this.textColor = null
        this.mTextColorStateList = textColorStateList
        return this
    }

    fun withCorners(@Dimension(unit = PX) cornersPx: Int): BadgeStyle {
        this.corners = DimenHolder.fromPixel(cornersPx)
        return this
    }

    fun withCornersDp(@Dimension(unit = DP) corners: Int): BadgeStyle {
        this.corners = DimenHolder.fromDp(corners)
        return this
    }

    fun withCorners(corners: DimenHolder): BadgeStyle {
        this.corners = corners
        return this
    }

    fun withPaddingLeftRightPx(@Dimension(unit = PX) paddingLeftRight: Int): BadgeStyle {
        this.paddingLeftRight = DimenHolder.fromPixel(paddingLeftRight)
        return this
    }

    fun withPaddingLeftRightDp(@Dimension(unit = DP) paddingLeftRight: Int): BadgeStyle {
        this.paddingLeftRight = DimenHolder.fromDp(paddingLeftRight)
        return this
    }

    fun withPaddingLeftRightRes(@DimenRes paddingLeftRight: Int): BadgeStyle {
        this.paddingLeftRight = DimenHolder.fromResource(paddingLeftRight)
        return this
    }

    fun withPaddingTopBottomPx(@Dimension(unit = PX) paddingTopBottom: Int): BadgeStyle {
        this.paddingTopBottom = DimenHolder.fromPixel(paddingTopBottom)
        return this
    }

    fun withPaddingTopBottomDp(@Dimension(unit = DP) paddingTopBottom: Int): BadgeStyle {
        this.paddingTopBottom = DimenHolder.fromDp(paddingTopBottom)
        return this
    }

    fun withPaddingTopBottomRes(@DimenRes paddingTopBottom: Int): BadgeStyle {
        this.paddingTopBottom = DimenHolder.fromResource(paddingTopBottom)
        return this
    }

    fun withPadding(@Dimension(unit = PX) padding: Int): BadgeStyle {
        this.paddingLeftRight = DimenHolder.fromPixel(padding)
        this.paddingTopBottom = DimenHolder.fromPixel(padding)
        return this
    }

    fun withPadding(padding: DimenHolder): BadgeStyle {
        this.paddingLeftRight = padding
        this.paddingTopBottom = padding
        return this
    }

    fun withMinWidth(@Dimension(unit = PX) minWidth: Int): BadgeStyle {
        this.minWidth = DimenHolder.fromPixel(minWidth)
        return this
    }

    fun withMinWidth(minWidth: DimenHolder): BadgeStyle {
        this.minWidth = minWidth
        return this
    }

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
            mTextColorStateList != null -> badgeTextView.setTextColor(mTextColorStateList)
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
