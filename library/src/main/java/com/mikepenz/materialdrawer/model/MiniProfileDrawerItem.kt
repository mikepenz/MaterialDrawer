package com.mikepenz.materialdrawer.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.DimenHolder
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.IProfile

/**
 * Created by mikepenz on 03.02.15.
 */
open class MiniProfileDrawerItem : AbstractDrawerItem<MiniProfileDrawerItem, MiniProfileDrawerItem.ViewHolder>, IProfile<MiniProfileDrawerItem> {
    override var icon: ImageHolder? = null
    override var name: StringHolder? = null
    override var email: StringHolder? = null
    var customHeight: DimenHolder? = null

    override val type: Int
        get() = R.id.material_drawer_item_mini_profile

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_mini_profile

    constructor() {
        withSelectable(false)
    }

    constructor(profile: ProfileDrawerItem) {
        this.icon = profile.icon
        this.isEnabled = profile.isEnabled
        withSelectable(false)
    }

    override fun withName(name: CharSequence?): MiniProfileDrawerItem {
        this.name = name?.let { StringHolder(name) }
        return this
    }

    override fun withEmail(email: String?): MiniProfileDrawerItem {
        return this
    }

    override fun withIcon(icon: Drawable?): MiniProfileDrawerItem {
        this.icon = icon?.let { ImageHolder(icon) }
        return this
    }

    override fun withIcon(@DrawableRes iconRes: Int): MiniProfileDrawerItem {
        this.icon = ImageHolder(iconRes)
        return this
    }

    override fun withIcon(iconBitmap: Bitmap): MiniProfileDrawerItem {
        this.icon = ImageHolder(iconBitmap)
        return this
    }

    override fun withIcon(url: String): MiniProfileDrawerItem {
        this.icon = ImageHolder(url)
        return this
    }

    override fun withIcon(uri: Uri): MiniProfileDrawerItem {
        this.icon = ImageHolder(uri)
        return this
    }

    fun withCustomHeightRes(@DimenRes customHeightRes: Int): MiniProfileDrawerItem {
        this.customHeight = DimenHolder.fromResource(customHeightRes)
        return this
    }

    fun withCustomHeightDp(customHeightDp: Int): MiniProfileDrawerItem {
        this.customHeight = DimenHolder.fromDp(customHeightDp)
        return this
    }

    fun withCustomHeightPx(customHeightPx: Int): MiniProfileDrawerItem {
        this.customHeight = DimenHolder.fromPixel(customHeightPx)
        return this
    }

    fun withCustomHeight(customHeight: DimenHolder): MiniProfileDrawerItem {
        this.customHeight = customHeight
        return this
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        customHeight?.let {
            val lp = holder.itemView.layoutParams as RecyclerView.LayoutParams
            lp.height = it.asPixel(holder.itemView.context)
            holder.itemView.layoutParams = lp
        }

        //set the identifier from the drawerItem here. It can be used to run tests
        holder.itemView.id = hashCode()

        //set the item enabled if it is
        holder.itemView.isEnabled = isEnabled

        //set the icon
        ImageHolder.applyToOrSetInvisible(icon, holder.icon)

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal val icon: ImageView = view.findViewById<ImageView>(R.id.material_drawer_icon)
    }
}
