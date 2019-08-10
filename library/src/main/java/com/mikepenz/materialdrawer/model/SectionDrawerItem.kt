package com.mikepenz.materialdrawer.model

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.holder.applyColor
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable
import com.mikepenz.materialize.util.UIUtils

/**
 * Created by mikepenz on 03.02.15.
 */
open class SectionDrawerItem : AbstractDrawerItem<SectionDrawerItem, SectionDrawerItem.ViewHolder>(), Nameable<SectionDrawerItem>, Typefaceable<SectionDrawerItem> {

    var divider = true
    override var name: StringHolder? = null
    override var isEnabled: Boolean = false
    override var isSelected: Boolean = false

    override val type: Int
        get() = R.id.material_drawer_item_section

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_section

    override fun withName(name: StringHolder?): SectionDrawerItem {
        this.name = name
        return this
    }

    override fun withName(name: String): SectionDrawerItem {
        this.name = StringHolder(name)
        return this
    }

    override fun withName(@StringRes nameRes: Int): SectionDrawerItem {
        this.name = StringHolder(nameRes)
        return this
    }

    fun withDivider(divider: Boolean): SectionDrawerItem {
        this.divider = divider
        return this
    }

    fun hasDivider(): Boolean {
        return divider
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        val ctx = holder.itemView.context

        //set the identifier from the drawerItem here. It can be used to run tests
        holder.itemView.id = hashCode()

        //define this item to be not clickable nor enabled
        holder.view.isClickable = false
        holder.view.isEnabled = false

        //define the text color
        holder.name.setTextColor(textColor.applyColor(ctx, R.attr.material_drawer_secondary_text, R.color.material_drawer_secondary_text))

        //set the text for the name
        StringHolder.applyTo(this.name, holder.name)

        //define the typeface for our textViews
        if (typeface != null) {
            holder.name.typeface = typeface
        }

        //hide the divider if we do not need one
        if (this.hasDivider()) {
            holder.divider.visibility = View.VISIBLE
        } else {
            holder.divider.visibility = View.GONE
        }

        //set the color for the divider
        holder.divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_divider, R.color.material_drawer_divider))

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder internal constructor(internal val view: View) : RecyclerView.ViewHolder(view) {
        internal val divider: View = view.findViewById(R.id.material_drawer_divider)
        internal val name: TextView = view.findViewById<TextView>(R.id.material_drawer_name)

    }
}
