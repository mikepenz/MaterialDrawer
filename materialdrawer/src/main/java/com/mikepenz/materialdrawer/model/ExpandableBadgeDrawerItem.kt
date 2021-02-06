package com.mikepenz.materialdrawer.model

import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.holder.ColorHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.ColorfulBadgeable
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.FixStateListDrawable

/**
 * Describes a [IDrawerItem] supporting child items (an expandable hierarchy) and badges.
 */
open class ExpandableBadgeDrawerItem : BaseDescribeableDrawerItem<ExpandableBadgeDrawerItem, ExpandableBadgeDrawerItem.ViewHolder>(), ColorfulBadgeable {

    private var mOnDrawerItemClickListener: ((v: View?, item: IDrawerItem<*>, position: Int) -> Boolean)? = null
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
    override var onDrawerItemClickListener: ((View?, IDrawerItem<*>, Int) -> Boolean)? = { view, drawerItem, position ->
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
        mOnDrawerItemClickListener?.invoke(view, drawerItem, position) ?: false
    }

    override fun bindView(holder: ExpandableBadgeDrawerItem.ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        val ctx = holder.itemView.context
        //bind the basic view parts
        bindViewHelper(holder)

        //set the text for the badge or hide
        val badgeVisible = StringHolder.applyToOrHide(badge, holder.badge)
        //style the badge if it is visible
        if (badgeVisible) {
            badgeStyle?.style(holder.badge, getColor(ctx))
            holder.badge.visibility = View.VISIBLE
        } else {
            holder.badge.visibility = View.GONE
        }

        //define the typeface for our textViews
        if (typeface != null) {
            holder.badge.typeface = typeface
        }

        val arrowColor = this.arrowColor?.color(ctx)?.let { ColorStateList.valueOf(it) } ?: getIconColor(ctx)
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                holder.arrow.imageTintList = arrowColor
            }
            holder.arrow.drawable is FixStateListDrawable -> {
                (holder.arrow.drawable as FixStateListDrawable).color = arrowColor
            }
            else -> {
                holder.arrow.setImageDrawable(FixStateListDrawable(holder.arrow.drawable, arrowColor))
            }
        }

        //make sure all animations are stopped
        holder.arrow.clearAnimation()
        if (!isExpanded) {
            holder.arrow.rotation = this.arrowRotationAngleStart.toFloat()
        } else {
            holder.arrow.rotation = this.arrowRotationAngleEnd.toFloat()
        }

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    @Deprecated("Please consider to replace with the actual property setter")
    override fun withOnDrawerItemClickListener(onDrawerItemClickListener: ((v: View?, item: IDrawerItem<*>, position: Int) -> Boolean)?): ExpandableBadgeDrawerItem {
        mOnDrawerItemClickListener = onDrawerItemClickListener
        return this
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val arrow: ImageView = view.findViewById(R.id.material_drawer_arrow)
        val badge: TextView = view.findViewById(R.id.material_drawer_badge)

        init {
            arrow.setImageDrawable(AppCompatResources.getDrawable(view.context, R.drawable.material_drawer_ico_chevron_down))
        }
    }
}
