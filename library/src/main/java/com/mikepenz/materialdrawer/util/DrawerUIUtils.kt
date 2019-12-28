package com.mikepenz.materialdrawer.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.*
import android.os.Build
import android.view.Gravity
import android.view.View
import androidx.annotation.StyleableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.mikepenz.materialdrawer.R

/**
 * Created by mikepenz on 15.03.14.
 */
@SuppressLint("InlinedApi")
object DrawerUIUtils {

    /**
     * Get the boolean value of a given styleable.
     *
     * @param ctx
     * @param styleable
     * @param def
     * @return
     */
    fun getBooleanStyleable(ctx: Context, @StyleableRes styleable: Int, def: Boolean): Boolean {
        val ta = ctx.theme.obtainStyledAttributes(R.styleable.MaterialDrawerSliderView)
        return ta.getBoolean(styleable, def)
    }

    /**
     * Util method to theme the drawer item view's background (and foreground if possible)
     *
     * @param ctx            the context to use
     * @param view           the view to theme
     * @param selected_color the selected color to use
     * @param animate        true if we want to animate the StateListDrawable
     */
    fun themeDrawerItem(ctx: Context, view: View, selected_color: Int, animate: Boolean, shapeAppearanceModel: ShapeAppearanceModel) {
        val selected: Drawable
        val unselected: Drawable

        // Material 2.0 styling
        val paddingTopBottom = ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_item_background_padding_top_bottom)
        val paddingStartEnd = ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_item_background_padding_start_end)

        // define normal selected background
        val gradientDrawable = MaterialShapeDrawable(shapeAppearanceModel)
        gradientDrawable.fillColor = ColorStateList.valueOf(selected_color)
        selected = InsetDrawable(gradientDrawable, paddingStartEnd, paddingTopBottom, paddingStartEnd, paddingTopBottom)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // define mask for ripple
            val gradientMask = MaterialShapeDrawable(shapeAppearanceModel)
            gradientMask.fillColor = ColorStateList.valueOf(Color.BLACK)
            val mask = InsetDrawable(gradientMask, paddingStartEnd, paddingTopBottom, paddingStartEnd, paddingTopBottom)

            unselected = RippleDrawable(ColorStateList(arrayOf(intArrayOf()), intArrayOf(ctx.getThemeColor(R.attr.colorControlHighlight))), null, mask)
        } else {
            // define touch drawable
            val touchDrawable = MaterialShapeDrawable(shapeAppearanceModel)
            touchDrawable.fillColor = ColorStateList.valueOf(ctx.getThemeColor(R.attr.colorControlHighlight))
            val touchInsetDrawable = InsetDrawable(touchDrawable, paddingStartEnd, paddingTopBottom, paddingStartEnd, paddingTopBottom)

            val unselectedStates = StateListDrawable()
            //if possible and wanted we enable animating across states
            if (animate) {
                val duration = ctx.resources.getInteger(android.R.integer.config_shortAnimTime)
                unselectedStates.setEnterFadeDuration(duration)
                unselectedStates.setExitFadeDuration(duration)
            }
            unselectedStates.addState(intArrayOf(android.R.attr.state_pressed), touchInsetDrawable)
            unselectedStates.addState(intArrayOf(), ColorDrawable(Color.TRANSPARENT))
            unselected = unselectedStates
        }

        val states = StateListDrawable()

        //if possible and wanted we enable animating across states
        if (animate) {
            val duration = ctx.resources.getInteger(android.R.integer.config_shortAnimTime)
            states.setEnterFadeDuration(duration)
            states.setExitFadeDuration(duration)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            states.addState(intArrayOf(android.R.attr.state_selected), selected)
            states.addState(intArrayOf(), ColorDrawable(Color.TRANSPARENT))

            ViewCompat.setBackground(view, states)
            view.foreground = unselected
        } else {
            states.addState(intArrayOf(android.R.attr.state_selected), selected)
            states.addState(intArrayOf(), unselected)

            ViewCompat.setBackground(view, states)
        }
    }

    /**
     * helper to create a colorStateList for the text
     *
     * @param text_color
     * @param selected_text_color
     * @return
     */
    fun getTextColorStateList(text_color: Int, selected_text_color: Int): ColorStateList {
        return ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf()),
                intArrayOf(selected_text_color, text_color)
        )
    }

    /**
     * helper to create a stateListDrawable for the icon
     *
     * @param icon
     * @param selectedIcon
     * @return
     */
    fun getIconStateList(icon: Drawable, selectedIcon: Drawable): StateListDrawable {
        val iconStateListDrawable = StateListDrawable()
        iconStateListDrawable.addState(intArrayOf(android.R.attr.state_selected), selectedIcon)
        iconStateListDrawable.addState(intArrayOf(), icon)
        return iconStateListDrawable
    }

    /**
     * helper to create a StateListDrawable for the drawer item background
     *
     * @param selected_color
     * @return
     */
    fun getDrawerItemBackground(selected_color: Int): StateListDrawable {
        val clrActive = ColorDrawable(selected_color)
        val states = StateListDrawable()
        states.addState(intArrayOf(android.R.attr.state_selected), clrActive)
        return states
    }

    /**
     * helper to calculate the optimal drawer width
     *
     * @param context
     * @return
     */
    fun getOptimalDrawerWidth(context: Context): Int {
        val possibleMinDrawerWidth = context.getScreenWidth() - context.getActionBarHeight()
        val maxDrawerWidth = context.resources.getDimensionPixelSize(R.dimen.material_drawer_width)
        return possibleMinDrawerWidth.coerceAtMost(maxDrawerWidth)
    }

    /**
     * helper method to get a person placeHolder drawable
     *
     * @param ctx
     * @return
     */
    fun getPlaceHolder(ctx: Context): Drawable {
        val accountDrawable = AppCompatResources.getDrawable(ctx, R.drawable.material_drawer_ico_account_layer) as LayerDrawable
        val placeholderSize = ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_profile_icon_placeholder)
        if (Build.VERSION.SDK_INT >= 23) {
            accountDrawable.setLayerWidth(0, placeholderSize)
            accountDrawable.setLayerHeight(0, placeholderSize)
        }
        DrawableCompat.wrap(accountDrawable.getDrawable(0)).let {
            DrawableCompat.setTint(it, ctx.getThemeColor(R.attr.colorPrimary))
            accountDrawable.setDrawableByLayerId(R.id.background, it)
        }
        val iconSize = ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_profile_icon_placeholder_icon)
        if (Build.VERSION.SDK_INT >= 23) {
            accountDrawable.setLayerWidth(1, iconSize)
            accountDrawable.setLayerHeight(1, iconSize)
            accountDrawable.setLayerGravity(1, Gravity.CENTER)
        }
        DrawableCompat.wrap(accountDrawable.getDrawable(1)).let {
            DrawableCompat.setTint(it, ctx.getThemeColor(R.attr.colorAccent))
            accountDrawable.setDrawableByLayerId(R.id.account, it)
        }
        return accountDrawable
        //IconicsDrawable(ctx, MaterialDrawerFont.Icon.mdf_person).color(IconicsColor.colorInt(ctx.getThemeColor(R.attr.colorAccent))).backgroundColor(IconicsColor.colorInt(ctx.getThemeColor(R.attr.colorPrimary))).size(IconicsSize.dp(56)).padding(IconicsSize.dp(16))
    }

    /**
     * helper to set the vertical padding to the DrawerItems
     * this is required because on API Level 17 the padding is ignored which is set via the XML
     *
     * @param v
     */
    fun setDrawerVerticalPadding(v: View) {
        val verticalPadding = v.context.resources.getDimensionPixelSize(R.dimen.material_drawer_vertical_padding)
        v.setPadding(verticalPadding, 0, verticalPadding, 0)
    }

    /**
     * helper to set the vertical padding including the extra padding for deeper item hirachy level to the DrawerItems
     * this is required because on API Level 17 the padding is ignored which is set via the XML
     *
     * @param v
     * @param level
     */
    fun setDrawerVerticalPadding(v: View, level: Int) {
        val verticalPadding = v.context.resources.getDimensionPixelSize(R.dimen.material_drawer_vertical_padding)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            v.setPaddingRelative(verticalPadding * level, 0, verticalPadding, 0)
        } else {
            v.setPadding(verticalPadding * level, 0, verticalPadding, 0)
        }
    }
}
