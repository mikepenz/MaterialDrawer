package com.mikepenz.materialdrawer

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout

import androidx.drawerlayout.widget.DrawerLayout

import com.mikepenz.materialdrawer.model.AbstractDrawerItem
import com.mikepenz.materialdrawer.model.ContainerDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Selectable
import com.mikepenz.materialdrawer.util.DrawerUIUtils
import com.mikepenz.materialize.util.UIUtils

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
    fun onFooterDrawerItemClick(drawer: DrawerBuilder, drawerItem: IDrawerItem<*>, v: View, fireOnClick: Boolean?) {
        val checkable = !(drawerItem is Selectable<*> && !drawerItem.isSelectable)
        if (checkable) {
            drawer.resetStickyFooterSelection()

            v.isActivated = true
            v.isSelected = true

            //remove the selection in the list
            drawer.selectExtension.deselect()

            //find the position of the clicked footer item
            if (drawer.mStickyFooterView != null && drawer.mStickyFooterView is LinearLayout) {
                val footer = drawer.mStickyFooterView as LinearLayout
                for (i in 0 until footer.childCount) {
                    if (footer.getChildAt(i) === v) {
                        drawer.mCurrentStickyFooterSelection = i
                        break
                    }
                }
            }
        }


        if (fireOnClick != null) {
            var consumed = false

            if (fireOnClick) {
                if (drawerItem is AbstractDrawerItem<*, *> && drawerItem.onDrawerItemClickListener != null) {
                    consumed = drawerItem.onDrawerItemClickListener?.onItemClick(v, -1, drawerItem)
                            ?: false
                }

                if (drawer.mOnDrawerItemClickListener != null) {
                    consumed = drawer.mOnDrawerItemClickListener?.onItemClick(v, -1, drawerItem)
                            ?: false
                }
            }

            if (!consumed) {
                //close the drawer after click
                drawer.closeDrawerDelayed()
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
    fun setStickyFooterSelection(drawer: DrawerBuilder, _position: Int, fireOnClick: Boolean?) {
        var position = _position
        if (position > -1) {
            if (drawer.mStickyFooterView != null && drawer.mStickyFooterView is LinearLayout) {
                val footer = drawer.mStickyFooterView as LinearLayout
                if (drawer.mStickyFooterDivider) {
                    position += 1
                }
                if (footer.childCount > position && position >= 0) {
                    val drawerItem = footer.getChildAt(position).getTag(R.id.material_drawer_item) as IDrawerItem<*>
                    onFooterDrawerItemClick(drawer, drawerItem, footer.getChildAt(position), fireOnClick)
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
    fun getPositionByIdentifier(drawer: DrawerBuilder, identifier: Long): Int {
        if (identifier != -1L) {
            for (i in 0 until drawer.adapter.itemCount) {
                if (drawer.adapter.getItem(i)?.identifier == identifier) {
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
    fun getStickyFooterPositionByIdentifier(drawer: DrawerBuilder, identifier: Long): Int {
        if (identifier != -1L) {
            if (drawer.mStickyFooterView != null && drawer.mStickyFooterView is LinearLayout) {
                val footer = drawer.mStickyFooterView as LinearLayout

                var shadowOffset = 0
                for (i in 0 until footer.childCount) {
                    val o = footer.getChildAt(i).getTag(R.id.material_drawer_item)

                    //count up the shadowOffset to return the correct position of the given item
                    if (o == null && drawer.mStickyFooterDivider) {
                        shadowOffset = shadowOffset + 1
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
    fun handleHeaderView(drawer: DrawerBuilder) {
        //use the AccountHeader if set
        drawer.mAccountHeader?.let {
            if (drawer.mAccountHeaderSticky) {
                drawer.mStickyHeaderView = it.view
            } else {
                drawer.mHeaderView = it.view
                drawer.mHeaderDivider = it.accountHeaderBuilder.dividerBelowHeader
                drawer.mHeaderPadding = it.accountHeaderBuilder.paddingBelowHeader
            }
        }

        //sticky header view
        drawer.mStickyHeaderView?.let {
            //add the sticky footer view and align it to the bottom
            val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1)
            it.id = R.id.material_drawer_sticky_header
            drawer.mSliderLayout.addView(it, 0, layoutParams)

            //now align the recyclerView below the stickyFooterView ;)
            val layoutParamsListView = drawer.mRecyclerView.layoutParams as RelativeLayout.LayoutParams
            layoutParamsListView.addRule(RelativeLayout.BELOW, R.id.material_drawer_sticky_header)
            drawer.mRecyclerView.layoutParams = layoutParamsListView

            //set a background color or the elevation will not work
            it.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(drawer.mActivity, R.attr.material_drawer_background, R.color.material_drawer_background))

            if (drawer.mStickyHeaderShadow) {
                //add a shadow
                if (Build.VERSION.SDK_INT >= 21) {
                    it.elevation = UIUtils.convertDpToPixel(4f, drawer.mActivity)
                } else {
                    val view = View(drawer.mActivity)
                    view.setBackgroundResource(R.drawable.material_drawer_shadow_bottom)
                    drawer.mSliderLayout.addView(view, RelativeLayout.LayoutParams.MATCH_PARENT, UIUtils.convertDpToPixel(4f, drawer.mActivity).toInt())
                    //now align the shadow below the stickyHeader ;)
                    val lps = view.layoutParams as RelativeLayout.LayoutParams
                    lps.addRule(RelativeLayout.BELOW, R.id.material_drawer_sticky_header)
                    view.layoutParams = lps
                }
            }

            //remove the padding of the recyclerView again we have the header on top of it
            drawer.mRecyclerView.setPadding(0, 0, 0, 0)
        }

        // set the header (do this before the setAdapter because some devices will crash else
        drawer.mHeaderView?.let {
            if (drawer.mHeaderPadding) {
                drawer.headerAdapter.add(ContainerDrawerItem().withView(it).withHeight(drawer.mHeiderHeight).withDivider(drawer.mHeaderDivider).withViewPosition(ContainerDrawerItem.Position.TOP))
            } else {
                drawer.headerAdapter.add(ContainerDrawerItem().withView(it).withHeight(drawer.mHeiderHeight).withDivider(drawer.mHeaderDivider).withViewPosition(ContainerDrawerItem.Position.NONE))
            }
            //set the padding on the top to 0
            drawer.mRecyclerView.setPadding(drawer.mRecyclerView.paddingLeft, 0, drawer.mRecyclerView.paddingRight, drawer.mRecyclerView.paddingBottom)
        }
    }

    /**
     * small helper to rebuild the FooterView
     *
     * @param drawer
     */
    fun rebuildStickyFooterView(drawer: DrawerBuilder) {
        drawer.mStickyFooterView?.let {
            it.removeAllViews()

            //create the divider
            if (drawer.mStickyFooterDivider) {
                addStickyFooterDivider(it.context, it)
            }

            //fill the footer with items
            DrawerUtils.fillStickyDrawerItemFooter(drawer, it, View.OnClickListener { v ->
                val drawerItem = v.getTag(R.id.material_drawer_item) as IDrawerItem<*>
                com.mikepenz.materialdrawer.DrawerUtils.onFooterDrawerItemClick(drawer, drawerItem, v, true)
            })

            it.visibility = View.VISIBLE
        } ?: run {
            //there was no footer yet. now just create one
            DrawerUtils.handleFooterView(drawer, View.OnClickListener { v ->
                val drawerItem = v.getTag(R.id.material_drawer_item) as IDrawerItem<*>
                DrawerUtils.onFooterDrawerItemClick(drawer, drawerItem, v, true)
            })
        }

        setStickyFooterSelection(drawer, drawer.mCurrentStickyFooterSelection, false)
    }

    /**
     * helper method to handle the footerView
     *
     * @param drawer
     */
    fun handleFooterView(drawer: DrawerBuilder, onClickListener: View.OnClickListener) {
        val ctx = drawer.mSliderLayout.context

        //use the StickyDrawerItems if set
        if (drawer.mStickyDrawerItems.size > 0) {
            drawer.mStickyFooterView = DrawerUtils.buildStickyDrawerItemFooter(ctx, drawer, onClickListener)
        }

        //sticky footer view
        drawer.mStickyFooterView?.let {
            //add the sticky footer view and align it to the bottom
            val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1)
            it.id = R.id.material_drawer_sticky_footer
            drawer.mSliderLayout.addView(it, layoutParams)

            if ((drawer.mTranslucentNavigationBar || drawer.mFullscreen) && Build.VERSION.SDK_INT >= 19) {
                it.setPadding(0, 0, 0, UIUtils.getNavigationBarHeight(ctx))
            }

            //now align the recyclerView above the stickyFooterView ;)
            val layoutParamsListView = drawer.mRecyclerView.layoutParams as RelativeLayout.LayoutParams
            layoutParamsListView.addRule(RelativeLayout.ABOVE, R.id.material_drawer_sticky_footer)
            drawer.mRecyclerView.layoutParams = layoutParamsListView

            //handle shadow on top of the sticky footer
            if (drawer.mStickyFooterShadow) {
                drawer.mStickyFooterShadowView = View(ctx).also { stickyFooterShadowView ->
                    stickyFooterShadowView.setBackgroundResource(R.drawable.material_drawer_shadow_top)
                    drawer.mSliderLayout.addView(stickyFooterShadowView, RelativeLayout.LayoutParams.MATCH_PARENT, ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_sticky_footer_elevation))
                    //now align the shadow below the stickyHeader ;)
                    val lps = stickyFooterShadowView.layoutParams as RelativeLayout.LayoutParams
                    lps.addRule(RelativeLayout.ABOVE, R.id.material_drawer_sticky_footer)
                    stickyFooterShadowView.layoutParams = lps
                }
            }

            //remove the padding of the recyclerView again we have the footer below it
            drawer.mRecyclerView.setPadding(drawer.mRecyclerView.paddingLeft, drawer.mRecyclerView.paddingTop, drawer.mRecyclerView.paddingRight, ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_padding))
        }

        // set the footer (do this before the setAdapter because some devices will crash else
        drawer.mFooterView?.let {
            if (drawer.mFooterDivider) {
                drawer.footerAdapter.add(ContainerDrawerItem().withView(it).withViewPosition(ContainerDrawerItem.Position.BOTTOM))
            } else {
                drawer.footerAdapter.add(ContainerDrawerItem().withView(it).withViewPosition(ContainerDrawerItem.Position.NONE))
            }
        }
    }


    /**
     * build the sticky footer item view
     *
     * @return
     */
    fun buildStickyDrawerItemFooter(ctx: Context, drawer: DrawerBuilder, onClickListener: View.OnClickListener): ViewGroup {
        //create the container view
        val linearLayout = LinearLayout(ctx)
        linearLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        linearLayout.orientation = LinearLayout.VERTICAL
        //set the background color to the drawer background color (if it has alpha the shadow won't be visible)
        linearLayout.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_background, R.color.material_drawer_background))

        //create the divider
        if (drawer.mStickyFooterDivider) {
            addStickyFooterDivider(ctx, linearLayout)
        }

        fillStickyDrawerItemFooter(drawer, linearLayout, onClickListener)

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
        divider.minimumHeight = UIUtils.convertDpToPixel(1f, ctx).toInt()
        divider.orientation = LinearLayout.VERTICAL
        divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_divider, R.color.material_drawer_divider))
        footerView.addView(divider, dividerParams)
    }

    /**
     * helper method to fill the sticky footer with it's elements
     *
     * @param drawer
     * @param container
     * @param onClickListener
     */
    fun fillStickyDrawerItemFooter(drawer: DrawerBuilder, container: ViewGroup, onClickListener: View.OnClickListener) {
        //add all drawer items
        for (drawerItem in drawer.mStickyDrawerItems) {
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
    fun processDrawerLayoutParams(drawer: DrawerBuilder, params: DrawerLayout.LayoutParams?): DrawerLayout.LayoutParams? {
        if (params != null) {
            val ctx = drawer.mDrawerLayout.context
            if (drawer.mDrawerGravity == Gravity.RIGHT || drawer.mDrawerGravity == Gravity.END) {
                params.rightMargin = 0
                if (Build.VERSION.SDK_INT >= 17) {
                    params.marginEnd = 0
                }

                params.leftMargin = ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_margin)
                if (Build.VERSION.SDK_INT >= 17) {
                    params.marginEnd = ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_margin)
                }
            }

            if (drawer.mDrawerWidth > -1) {
                params.width = drawer.mDrawerWidth
            } else {
                params.width = DrawerUIUtils.getOptimalDrawerWidth(ctx)
            }
        }

        return params
    }
}
