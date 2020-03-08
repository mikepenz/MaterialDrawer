package com.mikepenz.materialdrawer.app.drawerItems

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.app.R
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.AbstractDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.util.getDividerColor

class AccountDividerDrawerItem : AbstractDrawerItem<AccountDividerDrawerItem, AccountDividerDrawerItem.ViewHolder>(), IProfile {
    override val type: Int
        get() = R.id.material_drawer_profile_item_divider

    override val layoutRes: Int
        @LayoutRes
        get() = com.mikepenz.materialdrawer.R.layout.material_drawer_item_divider

    override var name: StringHolder? = null
    override var description: StringHolder? = null
    override var icon: ImageHolder? = null
    override var iconColor: ColorStateList? = null

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        val ctx = holder.itemView.context

        //set the identifier from the drawerItem here. It can be used to run tests
        holder.itemView.id = hashCode()

        //define how the divider should look like
        holder.view.isClickable = false
        holder.view.isEnabled = false
        holder.view.minimumHeight = 1
        ViewCompat.setImportantForAccessibility(holder.view,
                ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO)

        //set the color for the divider
        holder.divider.setBackgroundColor(ctx.getDividerColor())

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder internal constructor(internal val view: View) : RecyclerView.ViewHolder(view) {
        internal val divider: View = view.findViewById(com.mikepenz.materialdrawer.R.id.material_drawer_divider)
    }
}