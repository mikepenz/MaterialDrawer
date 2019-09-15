package com.mikepenz.materialdrawer.model

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import com.mikepenz.iconics.IconicsColor
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.IconicsSize
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.ColorHolder
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

/**
 * Created by mikepenz on 03.02.15.
 * NOTE: The arrow will just animate (and rotate) on APIs higher than 11 as the ViewCompat will skip this on API 10
 */
open class ExpandableDrawerItem : BaseDescribeableDrawerItem<ExpandableDrawerItem, ExpandableDrawerItem.ViewHolder>() {

    var mOnDrawerItemClickListener: Drawer.OnDrawerItemClickListener? = null
    var arrowColor: ColorHolder? = null
    var arrowRotationAngleStart = 0
    var arrowRotationAngleEnd = 180

    override val type: Int
        get() = R.id.material_drawer_item_expandable

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_expandable

    /**
     * our internal onDrawerItemClickListener which will handle the arrow animation
     */
    override var onDrawerItemClickListener: Drawer.OnDrawerItemClickListener? = object : Drawer.OnDrawerItemClickListener {
        override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
            if (drawerItem is AbstractDrawerItem<*, *> && drawerItem.isEnabled) {
                view?.let {
                    if (drawerItem.subItems != null) {
                        if (drawerItem.isExpanded) {
                            ViewCompat.animate(view.findViewById(R.id.material_drawer_arrow)).rotation(this@ExpandableDrawerItem.arrowRotationAngleEnd.toFloat()).start()
                        } else {
                            ViewCompat.animate(view.findViewById(R.id.material_drawer_arrow)).rotation(this@ExpandableDrawerItem.arrowRotationAngleStart.toFloat()).start()
                        }
                    }
                }
            }

            return mOnDrawerItemClickListener?.onItemClick(view, position, drawerItem) ?: false
        }
    }

    fun withArrowColor(@ColorInt arrowColor: Int): ExpandableDrawerItem {
        this.arrowColor = ColorHolder.fromColor(arrowColor)
        return this
    }

    fun withArrowColorRes(@ColorRes arrowColorRes: Int): ExpandableDrawerItem {
        this.arrowColor = ColorHolder.fromColorRes(arrowColorRes)
        return this
    }

    fun withArrowRotationAngleStart(angle: Int): ExpandableDrawerItem {
        this.arrowRotationAngleStart = angle
        return this
    }

    fun withArrowRotationAngleEnd(angle: Int): ExpandableDrawerItem {
        this.arrowRotationAngleEnd = angle
        return this
    }

    override fun withOnDrawerItemClickListener(onDrawerItemClickListener: Drawer.OnDrawerItemClickListener): ExpandableDrawerItem {
        mOnDrawerItemClickListener = onDrawerItemClickListener
        return this
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        val ctx = holder.itemView.context
        //bind the basic view parts
        bindViewHelper(holder)

        //make sure all animations are stopped
        if (holder.arrow.drawable is IconicsDrawable) {
            (holder.arrow.drawable as IconicsDrawable).color(this.arrowColor?.color(ctx)?.let { IconicsColor.colorInt(it) } ?: IconicsColor.colorList(getIconColor(ctx)))
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

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : BaseViewHolder(view) {
        var arrow: ImageView = view.findViewById(R.id.material_drawer_arrow)

        init {
            arrow.setImageDrawable(IconicsDrawable(view.context, MaterialDrawerFont.Icon.mdf_expand_more).size(IconicsSize.dp(16)).padding(IconicsSize.dp(2)).color(IconicsColor.colorInt(Color.BLACK)))
        }
    }
}
