package com.mikepenz.materialdrawer.util

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.TypedValue
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.mikepenz.materialdrawer.R


private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
internal val SELECTED_STATE_SET = intArrayOf(android.R.attr.state_selected)
private val DISABLED_STATE_SET = intArrayOf(-android.R.attr.state_enabled)
private val EMPTY_STATE_SET = intArrayOf()


internal fun Context.getPrimaryDrawerTextColor(): ColorStateList {
    return createDrawerItemColorStateList(this, R.styleable.MaterialDrawerSliderView_materialDrawerPrimaryText)!!
}

internal fun Context.getPrimaryDrawerIconColor(): ColorStateList {
    return createDrawerItemColorStateList(this, R.styleable.MaterialDrawerSliderView_materialDrawerPrimaryIcon)!!
}

internal fun Context.getSecondaryDrawerTextColor(): ColorStateList {
    return createDrawerItemColorStateList(this, R.styleable.MaterialDrawerSliderView_materialDrawerSecondaryText)!!
}

internal fun Context.getSecondaryDrawerIconColor(): ColorStateList {
    return createDrawerItemColorStateList(this, R.styleable.MaterialDrawerSliderView_materialDrawerSecondaryIcon)!!
}

fun createDrawerItemColorStateList(ctx: Context, @StyleableRes styleableRes: Int): ColorStateList? {
    val a = ctx.obtainStyledAttributes(null, R.styleable.MaterialDrawerSliderView, R.attr.materialDrawerStyle, R.style.Widget_MaterialDrawerStyle)
    val baseColor = a.getColorStateList(styleableRes)
    a.recycle()

    return baseColor
}

@ColorInt
fun Context.getDividerColor(): Int {
    return resolveStyledValue {
        it.getColor(
            R.styleable.MaterialDrawerSliderView_materialDrawerDividerColor,
            getThemeColor(R.attr.materialDrawerDividerColor, getSupportColor(R.color.material_drawer_divider))
        )
    }
}

@ColorInt
internal fun Context.getSelectedColor(): Int {
    val color = resolveStyledValue {
        it.getColor(
            R.styleable.MaterialDrawerSliderView_materialDrawerSelectedBackgroundColor,
            getThemeColor(R.attr.materialDrawerSelectedBackgroundColor, getSupportColor(R.color.material_drawer_selected))
        )
    }
    return color
}

internal fun Context.getHeaderSelectionTextColor(): ColorStateList {
    return resolveStyledHeaderValue {
        it.getColorStateList(R.styleable.AccountHeaderView_materialDrawerHeaderSelectionText)!!
    }
}

internal fun Context.getHeaderSelectionSubTextColor(): ColorStateList {
    return resolveStyledHeaderValue {
        it.getColorStateList(R.styleable.AccountHeaderView_materialDrawerHeaderSelectionSubtext)!!
    }
}

internal fun <T> Context.resolveStyledHeaderValue(resolver: (typedArray: TypedArray) -> T): T {
    return resolveStyledValue(R.styleable.AccountHeaderView, R.attr.materialDrawerHeaderStyle, R.style.Widget_MaterialDrawerHeaderStyle, resolver)
}

internal fun <T> Context.resolveStyledValue(
    attrs: IntArray = R.styleable.MaterialDrawerSliderView,
    defStyleAttr: Int = R.attr.materialDrawerStyle,
    defStyleRes: Int = R.style.Widget_MaterialDrawerStyle,
    resolver: (typedArray: TypedArray) -> T
): T {
    val a = obtainStyledAttributes(null, attrs, defStyleAttr, defStyleRes)
    val value = resolver.invoke(a)
    a.recycle()
    return value
}

/**
 * a helper method to get the color from the context
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal fun Context.getSupportColor(@ColorRes def: Int = 0): Int {
    return ContextCompat.getColor(this, def)
}


/**
 * helper to retrieve a float from the resources class
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal fun Context.getSupportFloat(@DimenRes dimen: Int): Float {
    return ResourcesCompat.getFloat(this.resources, dimen)
}

/**
 * a helper method to get the color from an attribute
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal fun Context.getThemeColor(@AttrRes attr: Int, @ColorInt def: Int = 0): Int {
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
internal fun Context.getThemeColorFromAttrOrRes(@AttrRes attr: Int, @ColorRes res: Int): Int {
    var color = getThemeColor(attr)
    if (color == 0) {
        color = ResourcesCompat.getColor(resources, res, theme)
    }
    return color
}

/**
 * helper to get the system default selectable background res
 */
internal fun Context.getSelectableBackgroundRes(): Int {
    val outValue = TypedValue()
    //it is important here to not use the android.R because this wouldn't add the latest drawable
    this.theme.resolveAttribute(R.attr.selectableItemBackground, outValue, true)
    return outValue.resourceId
}


/**
 * helper to get the system default selectable background
 */
internal fun Context.getSelectableBackground(): Drawable? {
    val selectableBackgroundRes = getSelectableBackgroundRes()
    return ContextCompat.getDrawable(this, selectableBackgroundRes)
}

/**
 * helper to get the system default selectable background inclusive an active state
 *
 * @param selected_color the selected color
 * @param animate        true if you want to fade over the states (only animates if API newer than Build.VERSION_CODES.HONEYCOMB)
 * @return the StateListDrawable
 */
internal fun Context.getSelectableBackground(selected_color: Int, animate: Boolean): StateListDrawable? {
    val states = StateListDrawable()
    val clrActive = ColorDrawable(selected_color)
    states.addState(intArrayOf(android.R.attr.state_selected), clrActive)
    states.addState(intArrayOf(), getSelectableBackground())
    //if possible and wanted we enable animating across states
    if (animate) {
        val duration = resources.getInteger(android.R.integer.config_shortAnimTime)
        states.setEnterFadeDuration(duration)
        states.setExitFadeDuration(duration)
    }
    return states
}

/**
 * Returns the screen width in pixels
 *
 * @return the screen width in pixels
 */
internal fun Context.getScreenWidth(): Int {
    val metrics = resources.displayMetrics
    return metrics.widthPixels
}

/**
 * helper to calculate the actionBar height
 */
internal fun Context.getActionBarHeight(): Int {
    var actionBarHeight: Int = getThemeAttributeDimensionSize(R.attr.actionBarSize)
    if (actionBarHeight == 0) {
        actionBarHeight = resources.getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material)
    }
    return actionBarHeight
}

/**
 * Returns the size in pixels of an attribute dimension
 *
 * @param attr    is the attribute dimension we want to know the size from
 * @return the size in pixels of an attribute dimension
 */
internal fun Context.getThemeAttributeDimensionSize(@AttrRes attr: Int): Int {
    var a: TypedArray? = null
    return try {
        a = theme.obtainStyledAttributes(intArrayOf(attr))
        a.getDimensionPixelSize(0, 0)
    } finally {
        a?.recycle()
    }
}