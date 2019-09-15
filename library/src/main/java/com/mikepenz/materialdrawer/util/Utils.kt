package com.mikepenz.materialdrawer.util

import android.content.Context
import android.content.res.ColorStateList
import android.util.TypedValue
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat
import com.mikepenz.materialdrawer.R

private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
internal val SELECTED_STATE_SET = intArrayOf(android.R.attr.state_selected)
private val DISABLED_STATE_SET = intArrayOf(-android.R.attr.state_enabled)
private val EMPTY_STATE_SET = intArrayOf()


fun Context.getPrimaryDrawerTextColor(): ColorStateList {
    return createDefaultColorStateList(R.styleable.MaterialDrawerSliderView_materialDrawerPrimaryText)!!
}

fun Context.getPrimaryDrawerIconColor(): ColorStateList {
    return createDefaultColorStateList(R.styleable.MaterialDrawerSliderView_materialDrawerPrimaryIcon)!!
}

fun Context.getSecondaryDrawerTextColor(): ColorStateList {
    return createDefaultColorStateList(R.styleable.MaterialDrawerSliderView_materialDrawerSecondaryText)!!
}

fun Context.createDefaultColorStateList(@StyleableRes styleableRes: Int, @AttrRes selectedColorAttr: Int = R.attr.colorPrimary): ColorStateList? {
    val a = obtainStyledAttributes(null, R.styleable.MaterialDrawerSliderView, R.attr.materialDrawerStyle, R.style.Widget_MaterialDrawerStyle)
    val baseColor = a.getColorStateList(styleableRes) ?: return null
    a.recycle()

    val value = TypedValue()
    if (!this.theme.resolveAttribute(selectedColorAttr, value, true)) {
        return null
    }
    val colorPrimary = value.data
    val defaultColor = baseColor.defaultColor
    return ColorStateList(
            arrayOf(DISABLED_STATE_SET, CHECKED_STATE_SET, SELECTED_STATE_SET, EMPTY_STATE_SET),
            intArrayOf(baseColor.getColorForState(DISABLED_STATE_SET, defaultColor), colorPrimary, colorPrimary, defaultColor))
}

/**
 * a helper method to get the color from an attribute
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun Context.getThemeColor(@AttrRes attr: Int, @ColorInt def: Int = 0): Int {
    val tv = TypedValue()
    return if (theme.resolveAttribute(attr, tv, true)) {
        if (tv.resourceId != 0) ResourcesCompat.getColor(resources, tv.resourceId, theme) else tv.data
    } else def
}

/**
 * helper method to get the color by attr (which is defined in the style) or by resource.
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun Context.getThemeColorFromAttrOrRes(@AttrRes attr: Int, @ColorRes res: Int): Int {
    var color = getThemeColor(attr)
    if (color == 0) {
        color = ResourcesCompat.getColor(resources, res, theme)
    }
    return color
}