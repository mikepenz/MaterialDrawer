package com.mikepenz.materialdrawer.model

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import com.mikepenz.iconics.IconicsColor.Companion.colorInt
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.IconicsSize.Companion.dp
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.holder.ColorHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont
import com.mikepenz.materialdrawer.model.interfaces.ColorfulBadgeable
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

/**
 * Created by mikepenz on 03.02.15.
 * NOTE: The arrow will just animate (and rotate) on APIs higher than 11 as the ViewCompat will skip this on API 10
 */
class ExpandableBadgeDrawerItem : BaseDescribeableDrawerItem<ExpandableBadgeDrawerItem, ExpandableBadgeDrawerItem.ViewHolder>(), ColorfulBadgeable<ExpandableBadgeDrawerItem> {

    var mOnDrawerItemClickListener: Drawer.OnDrawerItemClickListener? = null
    var arrowColor: ColorHolder? = null
    var arrowRotationAngleStart = 0
    var arrowRotationAngleEnd = 180
    override var badge: StringHolder? = null
    override var badgeStyle: BadgeStyle? = BadgeStyle()

    override val type: Int
        get() = R.id.material_drawer_item_expandable_badge

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_expandable_badge

    /**
     * our internal onDrawerItemClickListener which will handle the arrow animation
     */
    override var onDrawerItemClickListener: Drawer.OnDrawerItemClickListener? = object : Drawer.OnDrawerItemClickListener {
        override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
            if (drawerItem is AbstractDrawerItem<*, *> && drawerItem.isEnabled) {
                if (drawerItem.subItems != null) {
                    view?.let {
                        if (drawerItem.isExpanded) {
                            ViewCompat.animate(view.findViewById(R.id.material_drawer_arrow)).rotation(this@ExpandableBadgeDrawerItem.arrowRotationAngleEnd.toFloat()).start()
                        } else {
                            ViewCompat.animate(view.findViewById(R.id.material_drawer_arrow))
                                    .rotation(this@ExpandableBadgeDrawerItem.arrowRotationAngleStart.toFloat())
                                    .start()
                        }
                    }
                }
            }
            return mOnDrawerItemClickListener?.onItemClick(view, position, drawerItem) ?: false
        }
    }

    override fun bindView(holder: ExpandableBadgeDrawerItem.ViewHolder, payloads: MutableList<Any>) {
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

        //make sure all animations are stopped
        if (holder.arrow.drawable is IconicsDrawable) {
            (holder.arrow.drawable as IconicsDrawable).color(colorInt(this.arrowColor?.color(ctx) ?: getIconColor(ctx)))
        }
        holder.arrow.clearAnimation()
        if (!isExpanded) {
            holder.arrow.rotation = this.arrowRotationAngleStart.toFloat()
        } else {
            holder.arrow.rotation = this.arrowRotationAngleEnd.toFloat()
        }

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun withOnDrawerItemClickListener(onDrawerItemClickListener: Drawer.OnDrawerItemClickListener): ExpandableBadgeDrawerItem {
        mOnDrawerItemClickListener = onDrawerItemClickListener
        return this
    }

    override fun withBadge(badge: StringHolder?): ExpandableBadgeDrawerItem {
        this.badge = badge
        return this
    }

    override fun withBadge(badge: String): ExpandableBadgeDrawerItem {
        this.badge = StringHolder(badge)
        return this
    }

    override fun withBadge(@StringRes badgeRes: Int): ExpandableBadgeDrawerItem {
        this.badge = StringHolder(badgeRes)
        return this
    }

    override fun withBadgeStyle(badgeStyle: BadgeStyle?): ExpandableBadgeDrawerItem {
        this.badgeStyle = badgeStyle
        return this
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : BaseViewHolder(view) {
        var arrow: ImageView = view.findViewById(R.id.material_drawer_arrow)
        var badgeContainer: View = view.findViewById(R.id.material_drawer_badge_container)
        var badge: TextView = view.findViewById(R.id.material_drawer_badge)

        init {
            arrow.setImageDrawable(IconicsDrawable(view.context, MaterialDrawerFont.Icon.mdf_expand_more).size(dp(16)).padding(dp(2)).color(colorInt(Color.BLACK)))
        }
    }
}
