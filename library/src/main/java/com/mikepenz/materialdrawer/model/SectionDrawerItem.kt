package com.mikepenz.materialdrawer.model

import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import com.mikepenz.materialdrawer.model.interfaces.NameableColor
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable
import com.mikepenz.materialdrawer.util.getDividerColor
import com.mikepenz.materialdrawer.util.getSecondaryDrawerTextColor

/**
 * Describes a [IDrawerItem] acting as a divider with description to describe a section.
 */
open class SectionDrawerItem : AbstractDrawerItem<SectionDrawerItem, SectionDrawerItem.ViewHolder>(), Nameable, NameableColor, Typefaceable {
    var divider = true
    override var name: StringHolder? = null
    override var textColor: ColorStateList? = null
    override var isEnabled: Boolean = false
    override var isSelected: Boolean = false

    override val type: Int
        get() = R.id.material_drawer_item_section

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_section

    @Deprecated("Please consider to replace with the actual property setter")
    fun withDivider(divider: Boolean): SectionDrawerItem {
        this.divider = divider
        return this
    }

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        val ctx = holder.itemView.context

        //set the identifier from the drawerItem here. It can be used to run tests
        holder.itemView.id = hashCode()

        //define this item to be not clickable nor enabled
        holder.view.isClickable = false
        holder.view.isEnabled = false

        val color = textColor ?: ctx.getSecondaryDrawerTextColor()

        //define the text color
        holder.name.setTextColor(color)

        //set the text for the name
        StringHolder.applyTo(this.name, holder.name)

        //define the typeface for our textViews
        if (typeface != null) {
            holder.name.typeface = typeface
        }

        //hide the divider if we do not need one
        if (this.divider) {
            holder.divider.visibility = View.VISIBLE
        } else {
            holder.divider.visibility = View.GONE
        }

        //set the color for the divider
        holder.divider.setBackgroundColor(ctx.getDividerColor())

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder internal constructor(internal val view: View) : RecyclerView.ViewHolder(view) {
        internal val divider: View = view.findViewById(R.id.material_drawer_divider)
        internal val name: TextView = view.findViewById(R.id.material_drawer_name)
    }
}
