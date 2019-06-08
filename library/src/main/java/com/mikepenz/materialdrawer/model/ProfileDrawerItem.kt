package com.mikepenz.materialdrawer.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.ColorHolder
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.model.interfaces.Tagable
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerUIUtils
import com.mikepenz.materialdrawer.util.DrawerUIUtils.themeDrawerItem

/**
 * Created by mikepenz on 03.02.15.
 */
class ProfileDrawerItem : AbstractDrawerItem<ProfileDrawerItem, ProfileDrawerItem.ViewHolder>(), IProfile<ProfileDrawerItem>, Tagable<ProfileDrawerItem> {
    override var icon: ImageHolder? = null
    override var name: StringHolder? = null
    override var email: StringHolder? = null
    var isNameShown = false

    override val type: Int
        get() = R.id.material_drawer_item_profile

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_profile


    override fun withIcon(icon: Drawable?): ProfileDrawerItem {
        this.icon = ImageHolder(icon)
        return this
    }

    override fun withIcon(@DrawableRes iconRes: Int): ProfileDrawerItem {
        this.icon = ImageHolder(iconRes)
        return this
    }

    override fun withIcon(bitmap: Bitmap): ProfileDrawerItem {
        this.icon = ImageHolder(bitmap)
        return this
    }

    override fun withIcon(icon: IIcon): ProfileDrawerItem {
        this.icon = ImageHolder(icon)
        return this
    }

    override fun withIcon(url: String): ProfileDrawerItem {
        this.icon = ImageHolder(url)
        return this
    }

    override fun withIcon(uri: Uri): ProfileDrawerItem {
        this.icon = ImageHolder(uri)
        return this
    }

    override fun withName(name: CharSequence?): ProfileDrawerItem {
        this.name = StringHolder(name)
        return this
    }

    fun withName(@StringRes nameRes: Int): ProfileDrawerItem {
        this.name = StringHolder(nameRes)
        return this
    }

    override fun withEmail(email: String?): ProfileDrawerItem {
        this.email = StringHolder(email)
        return this
    }

    fun withEmail(@StringRes emailRes: Int): ProfileDrawerItem {
        this.email = StringHolder(emailRes)
        return this
    }

    /**
     * Whether to show the profile name in the account switcher.
     *
     * @param nameShown show name in switcher
     * @return the [ProfileDrawerItem]
     */
    fun withNameShown(nameShown: Boolean): ProfileDrawerItem {
        this.isNameShown = nameShown
        return this
    }

    fun withSelectedColor(@ColorInt selectedColor: Int): ProfileDrawerItem {
        this.selectedColor = ColorHolder.fromColor(selectedColor)
        return this
    }

    fun withSelectedColorRes(@ColorRes selectedColorRes: Int): ProfileDrawerItem {
        this.selectedColor = ColorHolder.fromColorRes(selectedColorRes)
        return this
    }

    fun withTextColor(@ColorInt textColor: Int): ProfileDrawerItem {
        this.textColor = ColorHolder.fromColor(textColor)
        return this
    }

    fun withTextColorRes(@ColorRes textColorRes: Int): ProfileDrawerItem {
        this.textColor = ColorHolder.fromColorRes(textColorRes)
        return this
    }

    fun withSelectedTextColor(@ColorInt selectedTextColor: Int): ProfileDrawerItem {
        this.selectedTextColor = ColorHolder.fromColor(selectedTextColor)
        return this
    }

    fun withSelectedTextColorRes(@ColorRes selectedColorRes: Int): ProfileDrawerItem {
        this.selectedTextColor = ColorHolder.fromColorRes(selectedColorRes)
        return this
    }

    fun withDisabledTextColor(@ColorInt disabledTextColor: Int): ProfileDrawerItem {
        this.disabledTextColor = ColorHolder.fromColor(disabledTextColor)
        return this
    }

    fun withDisabledTextColorRes(@ColorRes disabledTextColorRes: Int): ProfileDrawerItem {
        this.disabledTextColor = ColorHolder.fromColorRes(disabledTextColorRes)
        return this
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        val ctx = holder.itemView.context

        //set the identifier from the drawerItem here. It can be used to run tests
        holder.itemView.id = hashCode()

        //set the item enabled if it is
        holder.itemView.isEnabled = isEnabled

        //set the item selected if it is
        holder.itemView.isSelected = isSelected

        //get the correct color for the background
        val selectedColor = getSelectedColor(ctx)
        //get the correct color for the text
        val color = getColor(ctx)
        val selectedTextColor = getSelectedTextColor(ctx)
        val shapeAppearanceModel = getShapeAppearanceModel(ctx)

        //set the background for the item
        themeDrawerItem(ctx, holder.view, selectedColor, isSelectedBackgroundAnimated, shapeAppearanceModel)

        if (isNameShown) {
            holder.name.visibility = View.VISIBLE
            StringHolder.applyTo(this.name, holder.name)
        } else {
            holder.name.visibility = View.GONE
        }
        //the MaterialDrawer follows the Google Apps. those only show the e-mail
        //within the profile switcher. The problem this causes some confusion for
        //some developers. And if you only set the name, the item would be empty
        //so here's a small fallback which will prevent this issue of empty items ;)
        if (!isNameShown && this.email == null && this.name != null) {
            StringHolder.applyTo(this.name, holder.email)
        } else {
            StringHolder.applyTo(this.email, holder.email)
        }

        if (typeface != null) {
            holder.name.typeface = typeface
            holder.email.typeface = typeface
        }

        if (isNameShown) {
            holder.name.setTextColor(getTextColorStateList(color, selectedTextColor))
        }
        holder.email.setTextColor(getTextColorStateList(color, selectedTextColor))

        //cancel previous started image loading processes
        DrawerImageLoader.instance.cancelImage(holder.profileIcon)
        //set the icon
        ImageHolder.applyToOrSetInvisible(icon, holder.profileIcon, DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name)

        //for android API 17 --> Padding not applied via xml
        DrawerUIUtils.setDrawerVerticalPadding(holder.view)

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder internal constructor(internal val view: View) : RecyclerView.ViewHolder(view) {
        internal val profileIcon: ImageView = view.findViewById(R.id.material_drawer_profileIcon)
        internal val name: TextView = view.findViewById(R.id.material_drawer_name)
        internal val email: TextView = view.findViewById(R.id.material_drawer_email)

    }
}
