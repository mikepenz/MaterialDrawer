package com.mikepenz.materialdrawer.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialize.util.UIUtils
import java.util.*

/**
 * Created by mikepenz on 27.03.15.
 */
open class DrawerItemViewHelper(private val mContext: Context) {

    private var mDrawerItems = ArrayList<IDrawerItem<*>>()

    private var mDivider = true

    private var mOnDrawerItemClickListener: OnDrawerItemClickListener? = null

    fun withDrawerItems(drawerItems: ArrayList<IDrawerItem<*>>): DrawerItemViewHelper {
        this.mDrawerItems = drawerItems
        return this
    }

    fun withDrawerItems(vararg drawerItems: IDrawerItem<*>): DrawerItemViewHelper {
        Collections.addAll(this.mDrawerItems, *drawerItems)
        return this
    }

    fun withDivider(divider: Boolean): DrawerItemViewHelper {
        this.mDivider = divider
        return this
    }

    fun withOnDrawerItemClickListener(onDrawerItemClickListener: OnDrawerItemClickListener): DrawerItemViewHelper {
        mOnDrawerItemClickListener = onDrawerItemClickListener
        return this
    }

    fun build(): View {
        //create the container view
        val linearLayout = LinearLayout(mContext)
        linearLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        linearLayout.orientation = LinearLayout.VERTICAL

        //create the divider
        if (mDivider) {
            val divider = LinearLayout(mContext)
            divider.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            divider.minimumHeight = UIUtils.convertDpToPixel(1f, mContext).toInt()
            divider.orientation = LinearLayout.VERTICAL
            divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(mContext, R.attr.materialDrawerDivider, R.color.material_drawer_divider))
            linearLayout.addView(divider)
        }

        //add all drawer items
        for (drawerItem in mDrawerItems) {
            val view = drawerItem.generateView(mContext)
            view.tag = drawerItem

            if (drawerItem.isEnabled) {
                view.setBackgroundResource(UIUtils.getSelectableBackgroundRes(mContext))
                view.setOnClickListener { v ->
                    mOnDrawerItemClickListener?.onItemClick(v, v.getTag(R.id.material_drawer_item) as IDrawerItem<*>)
                }
            }

            linearLayout.addView(view)
        }

        return linearLayout
    }


    interface OnDrawerItemClickListener {
        fun onItemClick(view: View, drawerItem: IDrawerItem<*>)
    }
}
