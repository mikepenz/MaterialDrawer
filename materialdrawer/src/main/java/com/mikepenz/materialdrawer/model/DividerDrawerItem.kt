package com.mikepenz.materialdrawer.model

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.getDividerColor

/**
 * Describes a [IDrawerItem] acting as a divider in between items
 */
open class DividerDrawerItem : AbstractDrawerItem<DividerDrawerItem, DividerDrawerItem.ViewHolder>() {
    override val type: Int
        get() = R.id.material_drawer_item_divider

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_divider

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        val ctx = holder.itemView.context

        //set the identifier from the drawerItem here. It can be used to run tests
        holder.itemView.id = hashCode()

        //define how the divider should look like
        holder.itemView.isClickable = false
        holder.itemView.isEnabled = false
        holder.itemView.minimumHeight = 1
        ViewCompat.setImportantForAccessibility(holder.itemView, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO)

        //set the color for the divider
        holder.itemView.setBackgroundColor(ctx.getDividerColor())

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view)
}
