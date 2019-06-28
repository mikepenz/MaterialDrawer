package com.mikepenz.materialdrawer.model

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.ColorfulBadgeable

/**
 * Created by mikepenz on 03.02.15.
 */
abstract class AbstractBadgeableDrawerItem<Item : AbstractBadgeableDrawerItem<Item>> : BaseDescribeableDrawerItem<Item, AbstractBadgeableDrawerItem.ViewHolder>(), ColorfulBadgeable<Item> {
    override var badge: StringHolder? = null
    override var badgeStyle: BadgeStyle? = BadgeStyle()

    override/*"PRIMARY_ITEM"*/ val type: Int
        get() = R.id.material_drawer_item_primary

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_primary

    override fun withBadge(badge: StringHolder?): Item {
        this.badge = badge
        return this as Item
    }

    override fun withBadge(badge: String): Item {
        this.badge = StringHolder(badge)
        return this as Item
    }

    override fun withBadge(@StringRes badgeRes: Int): Item {
        this.badge = StringHolder(badgeRes)
        return this as Item
    }

    override fun withBadgeStyle(badgeStyle: BadgeStyle?): Item {
        this.badgeStyle = badgeStyle
        return this as Item
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        val ctx = holder.itemView.context
        //bind the basic view parts
        bindViewHelper(holder)

        //set the text for the badge or hide
        val badgeVisible = StringHolder.applyToOrHide(badge, holder.badge)
        //style the badge if it is visible
        if (badgeVisible) {
            badgeStyle?.style(holder.badge, getTextColorStateList(getColor(ctx), getSelectedTextColor(ctx)))
            holder.badgeContainer.visibility = View.VISIBLE
        } else {
            holder.badgeContainer.visibility = View.GONE
        }

        //define the typeface for our textViews
        if (typeface != null) {
            holder.badge.typeface = typeface
        }

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    open class ViewHolder(view: View) : BaseViewHolder(view) {
        internal val badgeContainer: View = view.findViewById(R.id.material_drawer_badge_container)
        internal val badge: TextView = view.findViewById<View>(R.id.material_drawer_badge) as TextView
    }
}
