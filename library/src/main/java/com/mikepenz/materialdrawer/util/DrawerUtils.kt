package com.mikepenz.materialdrawer.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.model.AbstractDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Selectable
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView

/**
 * Created by mikepenz on 23.05.15.
 */
internal object DrawerUtils {
    /**
     * helper method to handle the onClick of the footer
     *
     * @param drawer
     * @param drawerItem
     * @param v
     * @param fireOnClick true if we should call the listener, false if not, null to not call the listener and not close the drawer
     */
    fun onFooterDrawerItemClick(sliderView: MaterialDrawerSliderView, drawerItem: IDrawerItem<*>, v: View, fireOnClick: Boolean?) {
        val checkable = !(drawerItem is Selectable<*> && !drawerItem.isSelectable)
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
     * helper method to set the selection of the footer
     *
     * @param drawer
     * @param position
     * @param fireOnClick
     */
    fun setStickyFooterSelection(sliderView: MaterialDrawerSliderView, _position: Int, fireOnClick: Boolean?) {
        var position = _position
        if (position > -1) {
            if (sliderView.stickyFooterView != null && sliderView.stickyFooterView is LinearLayout) {
                val footer = sliderView.stickyFooterView as LinearLayout
                if (sliderView.stickyFooterDivider) {
                    position += 1
                }
                if (footer.childCount > position && position >= 0) {
                    val drawerItem = footer.getChildAt(position).getTag(R.id.material_drawer_item) as IDrawerItem<*>
                    onFooterDrawerItemClick(sliderView, drawerItem, footer.getChildAt(position), fireOnClick)
                }
            }
        }
    }

    /**
     * calculates the position of an drawerItem. searching by it's identifier
     *
     * @param identifier
     * @return
     */
    fun getPositionByIdentifier(sliderView: MaterialDrawerSliderView, identifier: Long): Int {
        if (identifier != -1L) {
            for (i in 0 until sliderView.adapter.itemCount) {
                if (sliderView.adapter.getItem(i)?.identifier == identifier) {
                    return i
                }
            }
        }

        return -1
    }

    /**
     * gets the drawerItem with the specific identifier from a drawerItem list
     *
     * @param drawerItems
     * @param identifier
     * @return
     */
    fun getDrawerItem(drawerItems: List<IDrawerItem<*>>, identifier: Long): IDrawerItem<*>? {
        if (identifier != -1L) {
            for (drawerItem in drawerItems) {
                if (drawerItem.identifier == identifier) {
                    return drawerItem
                }
            }
        }
        return null
    }

    /**
     * gets the drawerItem by a defined tag from a drawerItem list
     *
     * @param drawerItems
     * @param tag
     * @return
     */
    fun getDrawerItem(drawerItems: List<IDrawerItem<*>>, tag: Any?): IDrawerItem<*>? {
        if (tag != null) {
            for (drawerItem in drawerItems) {
                if (tag == drawerItem.tag) {
                    return drawerItem
                }
            }
        }
        return null
    }

    /**
     * calculates the position of an drawerItem inside the footer. searching by it's identifier
     *
     * @param identifier
     * @return
     */
    fun getStickyFooterPositionByIdentifier(drawer: MaterialDrawerSliderView, identifier: Long): Int {
        if (identifier != -1L) {
            if (drawer.stickyFooterView != null && drawer.stickyFooterView is LinearLayout) {
                val footer = drawer.stickyFooterView as LinearLayout

                var shadowOffset = 0
                for (i in 0 until footer.childCount) {
                    val o = footer.getChildAt(i).getTag(R.id.material_drawer_item)

                    //count up the shadowOffset to return the correct position of the given item
                    if (o == null && drawer.stickyFooterDivider) {
                        shadowOffset += 1
                    }

                    if (o != null && o is IDrawerItem<*> && o.identifier == identifier) {
                        return i - shadowOffset
                    }
                }
            }
        }

        return -1
    }

    /**
     * helper method to handle the headerView
     *
     * @param drawer
     */
    fun handleHeaderView(sliderView: MaterialDrawerSliderView) {
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

            //set a background color or the elevation will not work
            it.background = sliderView.background

            if (sliderView.stickyHeaderShadow) {
                //add a shadow
                if (Build.VERSION.SDK_INT >= 21) {
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

            //remove the padding of the recyclerView again we have the header on top of it
            sliderView.recyclerView.setPadding(0, 0, 0, 0)
        }
    }

    /**
     * small helper to rebuild the FooterView
     *
     * @param drawer
     */
    fun rebuildStickyFooterView(sliderView: MaterialDrawerSliderView) {
        sliderView.stickyFooterView?.let {
            it.removeAllViews()

            //create the divider
            if (sliderView.stickyFooterDivider) {
                addStickyFooterDivider(it.context, it)
            }

            //fill the footer with items
            fillStickyDrawerItemFooter(sliderView, it, View.OnClickListener { v ->
                val drawerItem = v.getTag(R.id.material_drawer_item) as IDrawerItem<*>
                onFooterDrawerItemClick(sliderView, drawerItem, v, true)
            })

            it.visibility = View.VISIBLE
        } ?: run {
            //there was no footer yet. now just create one
            handleFooterView(sliderView, View.OnClickListener { v ->
                val drawerItem = v.getTag(R.id.material_drawer_item) as IDrawerItem<*>
                onFooterDrawerItemClick(sliderView, drawerItem, v, true)
            })
        }

        setStickyFooterSelection(sliderView, sliderView.currentStickyFooterSelection, false)
    }

    /**
     * helper method to handle the footerView
     *
     * @param drawer
     */
    fun handleFooterView(sliderView: MaterialDrawerSliderView, onClickListener: View.OnClickListener) {
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
     *
     * @return
     */
    fun buildStickyDrawerItemFooter(sliderView: MaterialDrawerSliderView, onClickListener: View.OnClickListener): ViewGroup {
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
     * helper method to fill the sticky footer with it's elements
     *
     * @param drawer
     * @param container
     * @param onClickListener
     */
    fun fillStickyDrawerItemFooter(sliderView: MaterialDrawerSliderView, container: ViewGroup, onClickListener: View.OnClickListener) {
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
            DrawerUIUtils.setDrawerVerticalPadding(view)
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
                params.width = DrawerUIUtils.getOptimalDrawerWidth(ctx)
            }
        }

        return params
    }
}
