package com.mikepenz.materialdrawer.app.drawerItems

import android.view.View
import android.widget.ImageButton
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.PopupMenu
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.iconics.utils.sizeDp
import com.mikepenz.materialdrawer.app.R
import com.mikepenz.materialdrawer.model.BaseDescribeableDrawerItem
import com.mikepenz.materialdrawer.model.BaseViewHolder

/**
 * Created by mikepenz on 03.02.15.
 */
class OverflowMenuDrawerItem : BaseDescribeableDrawerItem<OverflowMenuDrawerItem, OverflowMenuDrawerItem.ViewHolder>() {
    var menu: Int = 0
    var onMenuItemClickListener: PopupMenu.OnMenuItemClickListener? = null
    var onDismissListener: PopupMenu.OnDismissListener? = null

    override val type: Int
        get() = R.id.material_drawer_item_overflow_menu

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_overflow_menu_primary

    @Deprecated("Use property setter instead")
    fun withMenu(menu: Int): OverflowMenuDrawerItem {
        this.menu = menu
        return this
    }

    @Deprecated("Use property setter instead")
    fun withOnMenuItemClickListener(onMenuItemClickListener: PopupMenu.OnMenuItemClickListener): OverflowMenuDrawerItem {
        this.onMenuItemClickListener = onMenuItemClickListener
        return this
    }

    @Deprecated("Use property setter instead")
    fun withOnDismissListener(onDismissListener: PopupMenu.OnDismissListener): OverflowMenuDrawerItem {
        this.onDismissListener = onDismissListener
        return this
    }

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        val ctx = holder.itemView.context

        //bind the basic view parts
        bindViewHelper(holder)

        //handle menu click
        holder.menu.setOnClickListener { view ->
            val popup = PopupMenu(view.context, view)
            val inflater = popup.menuInflater
            inflater.inflate(menu, popup.menu)

            popup.setOnMenuItemClickListener(onMenuItemClickListener)
            popup.setOnDismissListener(onDismissListener)

            popup.show()
        }

        //handle image
        holder.menu.setImageDrawable(IconicsDrawable(ctx, GoogleMaterial.Icon.gmd_more_vert).apply { sizeDp = 12; colorList = getIconColor(ctx) })

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : BaseViewHolder(view) {
        internal val menu: ImageButton = view.findViewById<ImageButton>(R.id.material_drawer_menu_overflow)
    }
}
