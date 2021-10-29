@file:JvmName("DrawerUtils")

package com.mikepenz.materialdrawer.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.*
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.AttrRes
import androidx.annotation.DimenRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.model.AbstractDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView

/**
 * helpful functions for working with the [MaterialDrawerSliderView]
 */


/**
 * helper function to handle the onClick of the footer
 */
internal fun onFooterDrawerItemClick(sliderView: MaterialDrawerSliderView, drawerItem: IDrawerItem<*>, v: View, fireOnClick: Boolean?) {
    val checkable = drawerItem.isSelectable
    if (checkable) {
        sliderView.resetStickyFooterSelection()

        v.isActivated = true
        v.isSelected = true

        //remove the selection in the list
        sliderView.selectExtension.deselect()

        //find the position of the clicked footer item
        if (sliderView.stickyFooterView != null && sliderView.stickyFooterView is LinearLayout) {
            val footer = sliderView.stickyFooterView as LinearLayout
            for (i in 0 until footer.childCount) {
                if (footer.getChildAt(i) === v) {
                    sliderView.currentStickyFooterSelection = i
                    break
                }
            }
        }
    }

    if (fireOnClick != null) {
        var consumed = false

        if (fireOnClick) {
            if (drawerItem is AbstractDrawerItem<*, *> && drawerItem.onDrawerItemClickListener != null) {
                consumed = drawerItem.onDrawerItemClickListener?.invoke(v, drawerItem, -1)
                        ?: false
            }

            if (sliderView.onDrawerItemClickListener != null) {
                consumed = sliderView.onDrawerItemClickListener?.invoke(v, drawerItem, -1)
                        ?: false
            }
        }

        if (!consumed) {
            //close the drawer after click
            sliderView.closeDrawerDelayed()
        }
    }
}

/**
 * helper function to handle the headerView
 */
internal fun handleHeaderView(sliderView: MaterialDrawerSliderView) {
    //use the AccountHeader if set
    sliderView.accountHeader?.let {
        if (sliderView.accountHeaderSticky) {
            sliderView.stickyHeaderView = it
        } else {
            sliderView._headerDivider = it.dividerBelowHeader
            sliderView._headerPadding = it.paddingBelowHeader
            sliderView.headerView = it
        }
    }

    //sticky header view
    sliderView.stickyHeaderView?.let {
        sliderView.findViewById<View>(R.id.material_drawer_sticky_header)?.let { header ->
            sliderView.removeView(header)
        }

        //add the sticky footer view and align it to the bottom
        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1)
        it.id = R.id.material_drawer_sticky_header
        sliderView.addView(it, 0, layoutParams)

        //now align the recyclerView below the stickyFooterView ;)
        val layoutParamsListView = sliderView.recyclerView.layoutParams as RelativeLayout.LayoutParams
        layoutParamsListView.addRule(RelativeLayout.BELOW, R.id.material_drawer_sticky_header)
        sliderView.recyclerView.layoutParams = layoutParamsListView

        if (sliderView.stickyHeaderShadow) {
            //add a shadow
            if (Build.VERSION.SDK_INT >= 21) {
                it.background = ColorDrawable(Color.WHITE) // set a background color or the elevation will not work, this is meant to be
                it.elevation = sliderView.context.resources.getDimensionPixelSize(R.dimen.material_drawer_sticky_header_elevation).toFloat()
            } else {
                val view = View(sliderView.context)
                view.setBackgroundResource(R.drawable.material_drawer_shadow_bottom)
                sliderView.addView(view, RelativeLayout.LayoutParams.MATCH_PARENT, sliderView.context.resources.getDimensionPixelSize(R.dimen.material_drawer_sticky_header_elevation))
                //now align the shadow below the stickyHeader ;)
                val lps = view.layoutParams as RelativeLayout.LayoutParams
                lps.addRule(RelativeLayout.BELOW, R.id.material_drawer_sticky_header)
                view.layoutParams = lps
            }
        }
        if (Build.VERSION.SDK_INT >= 21) {
            sliderView.elevation = 0f
        }
        //remove the padding of the recyclerView again we have the header on top of it
        sliderView.recyclerView.setPadding(0, 0, 0, 0)
    }
}

/**
 * small helper to rebuild the FooterView
 */
internal fun rebuildStickyFooterView(sliderView: MaterialDrawerSliderView) {
    sliderView.stickyFooterView?.let {
        it.removeAllViews()

        //create the divider
        if (sliderView.stickyFooterDivider) {
            addStickyFooterDivider(it.context, it)
        }

        //fill the footer with items
        fillStickyDrawerItemFooter(sliderView, it) { v ->
            (v.getTag(R.id.material_drawer_item) as? IDrawerItem<*>)?.let { drawerItem ->
                onFooterDrawerItemClick(sliderView, drawerItem, v, true)
            }
        }

        it.visibility = View.VISIBLE
    } ?: run {
        //there was no footer yet. now just create one
        handleFooterView(sliderView) { v ->
            (v.getTag(R.id.material_drawer_item) as? IDrawerItem<*>)?.let { drawerItem ->
                onFooterDrawerItemClick(sliderView, drawerItem, v, true)
            }
        }
    }

    sliderView.setStickyFooterSelection(sliderView.currentStickyFooterSelection, false)
}

/**
 * helper function to handle the footerView
 */
internal fun handleFooterView(sliderView: MaterialDrawerSliderView, onClickListener: View.OnClickListener) {
    val ctx = sliderView.context

    //use the StickyDrawerItems if set
    if (sliderView.stickyDrawerItems.size > 0) {
        sliderView._stickyFooterView = buildStickyDrawerItemFooter(sliderView, onClickListener)
    }

    //sticky footer view
    sliderView.stickyFooterView?.let {
        //add the sticky footer view and align it to the bottom
        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1)
        it.id = R.id.material_drawer_sticky_footer
        sliderView.addView(it, layoutParams)

        /**
        if ((sliderView.mTranslucentNavigationBar || drawer.mFullscreen) && Build.VERSION.SDK_INT >= 19) {
        it.setPadding(0, 0, 0, UIUtils.getNavigationBarHeight(ctx))
        }
         **/

        //now align the recyclerView above the stickyFooterView ;)
        val layoutParamsListView = sliderView.recyclerView.layoutParams as RelativeLayout.LayoutParams
        layoutParamsListView.addRule(RelativeLayout.ABOVE, R.id.material_drawer_sticky_footer)
        sliderView.recyclerView.layoutParams = layoutParamsListView

        //handle shadow on top of the sticky footer
        if (sliderView.stickyFooterShadow) {
            sliderView.stickyFooterShadowView = View(ctx).also { stickyFooterShadowView ->
                stickyFooterShadowView.setBackgroundResource(R.drawable.material_drawer_shadow_top)
                sliderView.addView(stickyFooterShadowView, RelativeLayout.LayoutParams.MATCH_PARENT, ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_sticky_footer_elevation))
                //now align the shadow below the stickyHeader ;)
                val lps = stickyFooterShadowView.layoutParams as RelativeLayout.LayoutParams
                lps.addRule(RelativeLayout.ABOVE, R.id.material_drawer_sticky_footer)
                stickyFooterShadowView.layoutParams = lps
            }
        }

        //remove the padding of the recyclerView again we have the footer below it
        sliderView.recyclerView.setPadding(sliderView.recyclerView.paddingLeft, sliderView.recyclerView.paddingTop, sliderView.recyclerView.paddingRight, ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_padding))
    }
}


/**
 * build the sticky footer item view
 */
internal fun buildStickyDrawerItemFooter(sliderView: MaterialDrawerSliderView, onClickListener: View.OnClickListener): ViewGroup {
    //create the container view
    val linearLayout = LinearLayout(sliderView.context)
    linearLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    linearLayout.orientation = LinearLayout.VERTICAL
    //set the background color to the drawer background color (if it has alpha the shadow won't be visible)
    //linearLayout.background = sliderView.background

    //create the divider
    if (sliderView.stickyFooterDivider) {
        addStickyFooterDivider(sliderView.context, linearLayout)
    }

    fillStickyDrawerItemFooter(sliderView, linearLayout, onClickListener)

    return linearLayout
}

/**
 * adds the shadow to the stickyFooter
 *
 * @param ctx
 * @param footerView
 */
private fun addStickyFooterDivider(ctx: Context, footerView: ViewGroup) {
    val divider = LinearLayout(ctx)
    val dividerParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    divider.minimumHeight = ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_sticky_footer_divider)
    divider.orientation = LinearLayout.VERTICAL
    divider.setBackgroundColor(ctx.getDividerColor())
    footerView.addView(divider, dividerParams)
}

/**
 * helper function to fill the sticky footer with its elements
 */
internal fun fillStickyDrawerItemFooter(sliderView: MaterialDrawerSliderView, container: ViewGroup, onClickListener: View.OnClickListener) {
    //add all drawer items
    for (drawerItem in sliderView.stickyDrawerItems) {
        val view = drawerItem.generateView(container.context, container)
        view.tag = drawerItem

        if (drawerItem.isEnabled) {
            //UIUtils.setBackground(view, UIUtils.getSelectableBackground(container.getContext(), selected_color, drawerItem.isSelectedBackgroundAnimated()));
            view.setOnClickListener(onClickListener)
        }

        container.addView(view)

        //for android API 17 --> Padding not applied via xml
        setDrawerVerticalPadding(view)
    }
    //and really. don't ask about this. it won't set the padding if i don't set the padding for the container
    container.setPadding(0, 0, 0, 0)
}


/**
 * helper to extend the layoutParams of the drawer
 *
 * @param params
 * @return
 */
@SuppressLint("RtlHardcoded")
fun processDrawerLayoutParams(drawer: MaterialDrawerSliderView, params: DrawerLayout.LayoutParams?): DrawerLayout.LayoutParams? {
    if (params != null) {
        val drawerLayout = drawer.drawerLayout ?: return null
        val ctx = drawerLayout.context

        val lp = drawerLayout.layoutParams as DrawerLayout.LayoutParams
        if (lp.gravity == Gravity.RIGHT || lp.gravity == Gravity.END) {
            params.rightMargin = 0
            if (Build.VERSION.SDK_INT >= 17) {
                params.marginEnd = 0
            }

            params.leftMargin = ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_margin)
            if (Build.VERSION.SDK_INT >= 17) {
                params.marginEnd = ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_margin)
            }
        }

        val customWidth = drawer.customWidth ?: -1
        if (customWidth > -1) {
            params.width = customWidth
        } else {
            params.width = getOptimalDrawerWidth(ctx)
        }
    }

    return params
}

/**
 * helper function to get a person placeHolder drawable
 */
fun getPlaceHolder(context: Context): Drawable {
    val accountDrawable = AppCompatResources.getDrawable(context, R.drawable.material_drawer_ico_account_layer) as LayerDrawable
    val placeholderSize = context.resources.getDimensionPixelSize(R.dimen.material_drawer_profile_icon_placeholder)
    if (Build.VERSION.SDK_INT >= 23) {
        accountDrawable.setLayerWidth(0, placeholderSize)
        accountDrawable.setLayerHeight(0, placeholderSize)
    }
    DrawableCompat.wrap(accountDrawable.getDrawable(0)).let {
        DrawableCompat.setTint(it, context.getThemeColor(R.attr.colorPrimary))
        accountDrawable.setDrawableByLayerId(R.id.background, it)
    }
    val iconSize = context.resources.getDimensionPixelSize(R.dimen.material_drawer_profile_icon_placeholder_icon)
    if (Build.VERSION.SDK_INT >= 23) {
        accountDrawable.setLayerWidth(1, iconSize)
        accountDrawable.setLayerHeight(1, iconSize)
        accountDrawable.setLayerGravity(1, Gravity.CENTER)
    }
    DrawableCompat.wrap(accountDrawable.getDrawable(1)).let {
        DrawableCompat.setTint(it, context.getThemeColor(R.attr.colorAccent))
        accountDrawable.setDrawableByLayerId(R.id.account, it)
    }
    return accountDrawable
    //IconicsDrawable(ctx, MaterialDrawerFont.Icon.mdf_person).color(IconicsColor.colorInt(ctx.getThemeColor(R.attr.colorAccent))).backgroundColor(IconicsColor.colorInt(ctx.getThemeColor(R.attr.colorPrimary))).size(IconicsSize.dp(56)).padding(IconicsSize.dp(16))
}


/**
 * helper to set the vertical padding to the DrawerItems
 * this is required because on API Level 17 the padding is ignored which is set via the XML
 */
fun setDrawerVerticalPadding(view: View) {
    val verticalPadding = view.context.resources.getDimensionPixelSize(R.dimen.material_drawer_vertical_padding)
    view.setPadding(verticalPadding, 0, verticalPadding, 0)
}

/**
 * Util method to theme the drawer item view's background (and foreground if possible)
 *
 * @param ctx            the context to use
 * @param view           the view to theme
 * @param selectedColor the selected color to use
 * @param animate        true if we want to animate the StateListDrawable
 * @param shapeAppearanceModel defines the shape appearance to use for items starting API 21
 * @param paddingTopBottomRes padding on top and bottom of the drawable for selection drawable
 * @param paddingStartRes padding to the beginning of the selection drawable
 * @param paddingEndRes padding to the end of the selection drawable
 * @param highlightColorRes the color for the highlight to use (e.g. touch the item, when it get's filled)
 */
fun themeDrawerItem(
        ctx: Context,
        view: View,
        selectedColor: Int,
        animate: Boolean,
        shapeAppearanceModel: ShapeAppearanceModel,
        @DimenRes paddingTopBottomRes: Int = R.dimen.material_drawer_item_background_padding_top_bottom,
        @DimenRes paddingStartRes: Int = R.dimen.material_drawer_item_background_padding_start,
        @DimenRes paddingEndRes: Int = R.dimen.material_drawer_item_background_padding_end,
        @AttrRes highlightColorRes: Int = R.attr.colorControlHighlight,
        /* a hint for the drawable if it should already be selected at the very moment */
        isSelected: Boolean = false
) {
    val selected: Drawable
    val unselected: Drawable

    // Material 3.0 styling
    val paddingTopBottom = ctx.resources.getDimensionPixelSize(paddingTopBottomRes)
    val paddingStart = ctx.resources.getDimensionPixelSize(paddingStartRes)
    val paddingEnd = ctx.resources.getDimensionPixelSize(paddingEndRes)

    // define normal selected background
    val gradientDrawable = MaterialShapeDrawable(shapeAppearanceModel)
    gradientDrawable.fillColor = ColorStateList.valueOf(selectedColor)
    selected = InsetDrawable(gradientDrawable, paddingStart, paddingTopBottom, paddingEnd, paddingTopBottom)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        // define mask for ripple
        val gradientMask = MaterialShapeDrawable(shapeAppearanceModel)
        gradientMask.fillColor = ColorStateList.valueOf(Color.BLACK)
        val mask = InsetDrawable(gradientMask, paddingStart, paddingTopBottom, paddingEnd, paddingTopBottom)

        unselected = RippleDrawable(ColorStateList(arrayOf(intArrayOf()), intArrayOf(ctx.getThemeColor(highlightColorRes))), null, mask)
    } else {
        // define touch drawable
        val touchDrawable = MaterialShapeDrawable(shapeAppearanceModel)
        touchDrawable.fillColor = ColorStateList.valueOf(ctx.getThemeColor(highlightColorRes))
        val touchInsetDrawable = InsetDrawable(touchDrawable, paddingStart, paddingTopBottom, paddingEnd, paddingTopBottom)

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

    if (isSelected && animate) {
        states.state = intArrayOf(android.R.attr.state_selected)
        states.jumpToCurrentState()
    }
}

/**
 * helper to create a stateListDrawable for the icon
 */
internal fun getIconStateList(icon: Drawable, selectedIcon: Drawable): StateListDrawable {
    val iconStateListDrawable = StateListDrawable()
    iconStateListDrawable.addState(intArrayOf(android.R.attr.state_selected), selectedIcon)
    iconStateListDrawable.addState(intArrayOf(), icon)
    return iconStateListDrawable
}

/**
 * helper to calculate the optimal drawer width
 */
fun getOptimalDrawerWidth(context: Context): Int {
    val possibleMinDrawerWidth = context.getScreenWidth() - context.getActionBarHeight()
    val maxDrawerWidth = context.resources.getDimensionPixelSize(R.dimen.material_drawer_width)
    return possibleMinDrawerWidth.coerceAtMost(maxDrawerWidth)
}