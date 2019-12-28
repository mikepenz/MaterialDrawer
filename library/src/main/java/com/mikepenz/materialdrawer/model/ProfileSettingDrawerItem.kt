package com.mikepenz.materialdrawer.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.ColorHolder
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.model.interfaces.Tagable
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable
import com.mikepenz.materialdrawer.util.DrawerUIUtils
import com.mikepenz.materialdrawer.util.getPrimaryDrawerIconColor
import com.mikepenz.materialdrawer.util.getSelectableBackground

/**
 * Created by mikepenz on 03.02.15.
 */
open class ProfileSettingDrawerItem : AbstractDrawerItem<ProfileSettingDrawerItem, ProfileSettingDrawerItem.ViewHolder>(), IProfile<ProfileSettingDrawerItem>, Tagable<ProfileSettingDrawerItem>, Typefaceable<ProfileSettingDrawerItem> {
    override var icon: ImageHolder? = null
    override var name: StringHolder? = null
    override var email: StringHolder? = null

    var isIconTinted = false
    var iconColor: ColorHolder? = null
    var descriptionTextColor: ColorHolder? = null

    override var isSelectable = false

    override val type: Int
        get() = R.id.material_drawer_item_profile_setting

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_profile_setting

    override fun withIcon(icon: Drawable?): ProfileSettingDrawerItem {
        this.icon = ImageHolder(icon)
        return this
    }

    override fun withIcon(@DrawableRes iconRes: Int): ProfileSettingDrawerItem {
        this.icon = ImageHolder(iconRes)
        return this
    }

    override fun withIcon(bitmap: Bitmap): ProfileSettingDrawerItem {
        this.icon = ImageHolder(bitmap)
        return this
    }

    override fun withIcon(url: String): ProfileSettingDrawerItem {
        this.icon = ImageHolder(url)
        return this
    }

    override fun withIcon(uri: Uri): ProfileSettingDrawerItem {
        this.icon = ImageHolder(uri)
        return this
    }

    override fun withName(name: CharSequence?): ProfileSettingDrawerItem {
        this.name = StringHolder(name)
        return this
    }

    fun withName(@StringRes nameRes: Int): ProfileSettingDrawerItem {
        this.name = StringHolder(nameRes)
        return this
    }

    fun withDescription(description: String): ProfileSettingDrawerItem {
        this.email = StringHolder(description)
        return this
    }

    fun withDescription(@StringRes descriptionRes: Int): ProfileSettingDrawerItem {
        this.email = StringHolder(descriptionRes)
        return this
    }

    //NOTE we reuse the IProfile here to allow custom items within the AccountSwitcher. There is an alias method withDescription for this
    override fun withEmail(email: String?): ProfileSettingDrawerItem {
        this.email = StringHolder(email)
        return this
    }

    fun withDescriptionTextColor(@ColorInt descriptionColor: Int): ProfileSettingDrawerItem {
        this.descriptionTextColor = ColorHolder.fromColor(descriptionColor)
        return this
    }

    fun withDescriptionTextColorRes(@ColorRes descriptionColorRes: Int): ProfileSettingDrawerItem {
        this.descriptionTextColor = ColorHolder.fromColorRes(descriptionColorRes)
        return this
    }

    fun withIconColor(@ColorInt iconColor: Int): ProfileSettingDrawerItem {
        this.iconColor = ColorHolder.fromColor(iconColor)
        return this
    }

    fun withIconColorRes(@ColorRes iconColorRes: Int): ProfileSettingDrawerItem {
        this.iconColor = ColorHolder.fromColorRes(iconColorRes)
        return this
    }

    fun withIconTinted(iconTinted: Boolean): ProfileSettingDrawerItem {
        this.isIconTinted = iconTinted
        return this
    }

    fun getDescription(): StringHolder? {
        return email
    }

    fun setDescription(description: String) {
        this.email = StringHolder(description)
    }

    override fun withSelectable(selectable: Boolean): ProfileSettingDrawerItem {
        this.isSelectable = selectable
        return this
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        //get the context
        val ctx = holder.itemView.context

        //set the identifier from the drawerItem here. It can be used to run tests
        holder.itemView.id = hashCode()

        //set the item enabled if it is
        holder.itemView.isEnabled = isEnabled
        holder.name.isEnabled = isEnabled
        holder.description.isEnabled = isEnabled
        holder.icon.isEnabled = isEnabled

        //set the item selected if it is
        holder.itemView.isSelected = isSelected
        holder.name.isSelected = isSelected
        holder.description.isSelected = isSelected
        holder.icon.isSelected = isSelected

        //get the correct color for the background
        val selectedColor = getSelectedColor(ctx)
        //get the correct color for the text
        val color = getColor(ctx)
        val iconColor = ctx.getPrimaryDrawerIconColor()
        val descriptionColor = getColor(ctx)

        ViewCompat.setBackground(holder.view, ctx.getSelectableBackground(selectedColor, isSelectedBackgroundAnimated))

        StringHolder.applyTo(this.name, holder.name)
        holder.name.setTextColor(color)

        StringHolder.applyToOrHide(this.getDescription(), holder.description)
        holder.description.setTextColor(descriptionColor)

        if (typeface != null) {
            holder.name.typeface = typeface
            holder.description.typeface = typeface
        }

        //set the correct icon
        ImageHolder.applyDecidedIconOrSetGone(icon, holder.icon, iconColor, isIconTinted, 2)

        //for android API 17 --> Padding not applied via xml
        DrawerUIUtils.setDrawerVerticalPadding(holder.view)

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    open class ViewHolder internal constructor(internal val view: View) : RecyclerView.ViewHolder(view) {
        internal val icon: ImageView = view.findViewById<ImageView>(R.id.material_drawer_icon)
        internal val name: TextView = view.findViewById<TextView>(R.id.material_drawer_name)
        internal val description: TextView = view.findViewById<TextView>(R.id.material_drawer_description)
    }
}
