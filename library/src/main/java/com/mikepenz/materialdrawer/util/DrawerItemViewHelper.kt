package com.mikepenz.materialdrawer.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import java.util.*

/**
 * Created by mikepenz on 27.03.15.
 */
open class DrawerItemViewHelper(private val context: Context) {

    val drawerItems = ArrayList<IDrawerItem<*>>()
    var divider = true
    var onDrawerItemClickListener: OnDrawerItemClickListener? = null

    fun build(): View {
        //create the container view
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        linearLayout.orientation = LinearLayout.VERTICAL

        //create the divider
        if (divider) {
            val divider = LinearLayout(context)
            divider.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            divider.minimumHeight = context.resources.getDimensionPixelSize(R.dimen.material_drawer_sticky_footer_divider)
            divider.orientation = LinearLayout.VERTICAL
            divider.setBackgroundColor(context.getDividerColor())
            linearLayout.addView(divider)
        }

        //add all drawer items
        for (drawerItem in drawerItems) {
            val view = drawerItem.generateView(context)
            view.tag = drawerItem

            if (drawerItem.isEnabled) {
                view.setBackgroundResource(context.getSelectableBackgroundRes())
                view.setOnClickListener { v ->
                    onDrawerItemClickListener?.onItemClick(v, v.getTag(R.id.material_drawer_item) as IDrawerItem<*>)
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
