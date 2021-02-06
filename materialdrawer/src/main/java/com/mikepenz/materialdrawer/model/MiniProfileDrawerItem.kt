package com.mikepenz.materialdrawer.model

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.DimenHolder
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.util.DrawerImageLoader

/**
 * Describes a [IProfile] being used for the [com.mikepenz.materialdrawer.widget.MiniDrawerSliderView]
 */
open class MiniProfileDrawerItem : AbstractDrawerItem<MiniProfileDrawerItem, MiniProfileDrawerItem.ViewHolder>, IProfile {
    override var icon: ImageHolder? = null
    override var iconColor: ColorStateList? = null // not supported for this item
    override var name: StringHolder? = null
    override var description: StringHolder? = null
    var customHeight: DimenHolder? = null

    override val type: Int
        get() = R.id.material_drawer_item_mini_profile

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_mini_profile

    constructor() {
        this.isSelectable = false
    }

    constructor(profile: ProfileDrawerItem) {
        this.icon = profile.icon
        this.isEnabled = profile.isEnabled
        this.isSelectable = false
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withCustomHeightRes(@DimenRes customHeightRes: Int): MiniProfileDrawerItem {
        this.customHeight = DimenHolder.fromResource(customHeightRes)
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withCustomHeightDp(customHeightDp: Int): MiniProfileDrawerItem {
        this.customHeight = DimenHolder.fromDp(customHeightDp)
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withCustomHeightPx(customHeightPx: Int): MiniProfileDrawerItem {
        this.customHeight = DimenHolder.fromPixel(customHeightPx)
        return this
    }

    @Deprecated("Please consider to replace with the actual property setter")
    fun withCustomHeight(customHeight: DimenHolder): MiniProfileDrawerItem {
        this.customHeight = customHeight
        return this
    }

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
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

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)

        // reset image loading for the item
        DrawerImageLoader.instance.cancelImage(holder.icon)
        holder.icon.setImageBitmap(null)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal val icon: ImageView = view.findViewById<ImageView>(R.id.material_drawer_icon)
    }
}
