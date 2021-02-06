package com.mikepenz.materialdrawer.model

import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.ColorHolder
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.FixStateListDrawable

/**
 * Describes a [IDrawerItem] supporting child items.
 */
open class ExpandableDrawerItem : BaseDescribeableDrawerItem<ExpandableDrawerItem, ExpandableDrawerItem.ViewHolder>() {

    var mOnDrawerItemClickListener: ((v: View?, item: IDrawerItem<*>, position: Int) -> Boolean)? = null
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
    override var onDrawerItemClickListener: ((View?, IDrawerItem<*>, Int) -> Boolean)? = { view, drawerItem, position ->
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

        mOnDrawerItemClickListener?.invoke(view, drawerItem, position) ?: false
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withArrowColor(@ColorInt arrowColor: Int): ExpandableDrawerItem {
        this.arrowColor = ColorHolder.fromColor(arrowColor)
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withArrowColorRes(@ColorRes arrowColorRes: Int): ExpandableDrawerItem {
        this.arrowColor = ColorHolder.fromColorRes(arrowColorRes)
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withArrowRotationAngleStart(angle: Int): ExpandableDrawerItem {
        this.arrowRotationAngleStart = angle
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withArrowRotationAngleEnd(angle: Int): ExpandableDrawerItem {
        this.arrowRotationAngleEnd = angle
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    override fun withOnDrawerItemClickListener(onDrawerItemClickListener: ((v: View?, item: IDrawerItem<*>, position: Int) -> Boolean)?): ExpandableDrawerItem {
        mOnDrawerItemClickListener = onDrawerItemClickListener
        return this
    }

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        val ctx = holder.itemView.context
        //bind the basic view parts
        bindViewHelper(holder)

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

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : BaseViewHolder(view) {
        var arrow: ImageView = view.findViewById(R.id.material_drawer_arrow)

        init {
            arrow.setImageDrawable(AppCompatResources.getDrawable(view.context, R.drawable.material_drawer_ico_chevron_down))
        }
    }
}
