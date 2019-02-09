package com.mikepenz.materialdrawer.app.drawerItems

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.materialdrawer.app.R
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.AbstractDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialize.util.UIUtils

class AccountDividerDrawerItem : AbstractDrawerItem<AccountDividerDrawerItem, AccountDividerDrawerItem.ViewHolder>(), IProfile<AccountDividerDrawerItem> {
    override val type: Int
        get() = R.id.material_drawer_profile_item_divider

    override val layoutRes: Int
        @LayoutRes
        get() = com.mikepenz.materialdrawer.R.layout.material_drawer_item_divider

    override val name: StringHolder?
        get() = null

    override val email: StringHolder?
        get() = null

    override val icon: ImageHolder?
        get() = null

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
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
        holder.divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, com.mikepenz.materialdrawer.R.attr.material_drawer_divider, com.mikepenz.materialdrawer.R.color.material_drawer_divider))

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }


    override fun withName(name: CharSequence?): AccountDividerDrawerItem {
        return this
    }

    override fun withEmail(email: String?): AccountDividerDrawerItem {
        return this
    }

    override fun withIcon(icon: Drawable?): AccountDividerDrawerItem {
        return this
    }

    override fun withIcon(bitmap: Bitmap): AccountDividerDrawerItem {
        return this
    }

    override fun withIcon(@DrawableRes iconRes: Int): AccountDividerDrawerItem {
        return this
    }

    override fun withIcon(url: String): AccountDividerDrawerItem {
        return this
    }

    override fun withIcon(uri: Uri): AccountDividerDrawerItem {
        return this
    }

    override fun withIcon(icon: IIcon): AccountDividerDrawerItem {
        return this
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder internal constructor(internal val view: View) : RecyclerView.ViewHolder(view) {
        internal val divider: View = view.findViewById(com.mikepenz.materialdrawer.R.id.material_drawer_divider)
    }
}