package com.mikepenz.materialdrawer.util

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.util.TypedValue
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import com.mikepenz.materialdrawer.R

private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
internal val SELECTED_STATE_SET = intArrayOf(android.R.attr.state_selected)
private val DISABLED_STATE_SET = intArrayOf(-android.R.attr.state_enabled)
private val EMPTY_STATE_SET = intArrayOf()


fun Context.getPrimaryDrawerTextColor(): ColorStateList {
    return createDrawerItemColorStateList(R.styleable.MaterialDrawerSliderView_materialDrawerPrimaryText)!!
}

fun Context.getPrimaryDrawerIconColor(): ColorStateList {
    return createDrawerItemColorStateList(R.styleable.MaterialDrawerSliderView_materialDrawerPrimaryIcon)!!
}

fun Context.getSecondaryDrawerTextColor(): ColorStateList {
    return createDrawerItemColorStateList(R.styleable.MaterialDrawerSliderView_materialDrawerSecondaryText)!!
}

fun Context.getSecondaryDrawerIconColor(): ColorStateList {
    return createDrawerItemColorStateList(R.styleable.MaterialDrawerSliderView_materialDrawerSecondaryIcon)!!
}

fun Context.createDrawerItemColorStateList(@StyleableRes styleableRes: Int, @StyleableRes selectedStyleable: Int = R.styleable.MaterialDrawerSliderView_materialDrawerSelected): ColorStateList? {
    val a = obtainStyledAttributes(null, R.styleable.MaterialDrawerSliderView, R.attr.materialDrawerStyle, R.style.Widget_MaterialDrawerStyle)
    val baseColor = a.getColorStateList(styleableRes) ?: return null
    val selectedColor = a.getColor(selectedStyleable, getThemeColor(R.attr.colorPrimary))
    a.recycle()

    val defaultColor = baseColor.defaultColor
    return ColorStateList(
            arrayOf(DISABLED_STATE_SET, CHECKED_STATE_SET, SELECTED_STATE_SET, EMPTY_STATE_SET),
            intArrayOf(baseColor.getColorForState(DISABLED_STATE_SET, defaultColor), selectedColor, selectedColor, defaultColor)
    )
}

@ColorInt
fun Context.getDividerColor(): Int {
    return resolveStyledValue {
        it.getColor(R.styleable.MaterialDrawerSliderView_materialDrawerDivider, getThemeColor(R.attr.materialDrawerDivider, getSupportColor(R.color.material_drawer_divider)))
    }
}

@ColorInt
fun Context.getLegacySelectColor(): Int {
    val color = resolveStyledValue {
        it.getColor(R.styleable.MaterialDrawerSliderView_materialDrawerSelectedLegacy, getThemeColor(R.attr.materialDrawerSelectedLegacy, getSupportColor(R.color.material_drawer_selected_legacy)))
    }
    return ColorUtils.setAlphaComponent(color, (255 * getSupportFloat(R.dimen.material_drawer_selected_background_alpha)).toInt())
}

@ColorInt
fun Context.getSelectedColor(): Int {
    val color = resolveStyledValue {
        it.getColor(R.styleable.MaterialDrawerSliderView_materialDrawerSelected, getThemeColor(R.attr.materialDrawerSelected, getSupportColor(R.color.material_drawer_selected)))
    }
    return ColorUtils.setAlphaComponent(color, (255 * getSupportFloat(R.dimen.material_drawer_selected_background_alpha)).toInt())
}

internal fun <T> Context.resolveStyledValue(resolver: (typedArray: TypedArray) -> T): T {
    val a = obtainStyledAttributes(null, R.styleable.MaterialDrawerSliderView, R.attr.materialDrawerStyle, R.style.Widget_MaterialDrawerStyle)
    val value = resolver.invoke(a)
    a.recycle()
    return value
}

/**
 * a helper method to get the color from the context
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun Context.getSupportColor(@ColorRes def: Int = 0): Int {
    return ContextCompat.getColor(this, def)
}


/**
 * helper to retrieve a float from the resources class
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun Context.getSupportFloat(@DimenRes dimen: Int): Float {
    return ResourcesCompat.getFloat(this.resources, dimen)
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