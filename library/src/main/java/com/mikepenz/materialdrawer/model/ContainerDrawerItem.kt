package com.mikepenz.materialdrawer.model

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.DimenHolder
import com.mikepenz.materialize.util.UIUtils

/**
 * Created by mikepenz on 03.02.15.
 */
open class ContainerDrawerItem : AbstractDrawerItem<ContainerDrawerItem, ContainerDrawerItem.ViewHolder>() {

    var height: DimenHolder? = null
    var view: View? = null
    var viewPosition = Position.TOP
    var divider = true

    override val type: Int
        get() = R.id.material_drawer_item_container

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_container

    fun withHeight(height: DimenHolder?): ContainerDrawerItem {
        this.height = height
        return this
    }

    fun withView(view: View): ContainerDrawerItem {
        this.view = view
        return this
    }

    enum class Position {
        TOP,
        BOTTOM,
        NONE
    }

    fun withViewPosition(position: Position): ContainerDrawerItem {
        this.viewPosition = position
        return this
    }

    fun withDivider(_divider: Boolean): ContainerDrawerItem {
        this.divider = _divider
        return this
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        val ctx = holder.itemView.context

        //set the identifier from the drawerItem here. It can be used to run tests
        holder.itemView.id = hashCode()

        //define how the divider should look like
        holder.view.isEnabled = false

        //make sure our view is not used in another parent
        view?.parent?.let {
            (it as ViewGroup).removeView(view)
        }

        //set the height
        var height = ViewGroup.LayoutParams.WRAP_CONTENT

        this.height?.let {
            val lp = holder.view.layoutParams as RecyclerView.LayoutParams
            height = it.asPixel(ctx)
            lp.height = height
            holder.view.layoutParams = lp
        }

        //make sure the header view is empty
        (holder.view as ViewGroup).removeAllViews()

        var dividerHeight = 0
        if (divider) {
            dividerHeight = 1
        }

        val divider = View(ctx)
        divider.minimumHeight = dividerHeight
        divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_divider, R.color.material_drawer_divider))

        val dividerParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.convertDpToPixel(dividerHeight.toFloat(), ctx).toInt())
        val viewParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, if (this.height != null) height - UIUtils.convertDpToPixel(dividerHeight.toFloat(), ctx).toInt() else height)

        //depending on the position we add the view
        when (viewPosition) {
            Position.TOP -> {
                holder.view.addView(view, viewParams)
                dividerParams.bottomMargin = ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_padding)
                holder.view.addView(divider, dividerParams)
            }
            Position.BOTTOM -> {
                dividerParams.topMargin = ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_padding)
                holder.view.addView(divider, dividerParams)
                holder.view.addView(view, viewParams)
            }
            else -> holder.view.addView(view, viewParams)
        }

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder internal constructor(internal val view: View) : RecyclerView.ViewHolder(view)
}
