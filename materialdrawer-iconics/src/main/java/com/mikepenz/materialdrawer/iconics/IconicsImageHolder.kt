package com.mikepenz.materialdrawer.iconics

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.utils.actionBar
import com.mikepenz.iconics.utils.paddingDp
import com.mikepenz.iconics.utils.sizeDp
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import java.io.FileNotFoundException

class IconicsImageHolder(iicon: IIcon) : ImageHolder() {

    var iicon: IIcon? = iicon
        internal set


    /**
     * sets an existing image to the imageView
     *
     * @param imageView
     * @param tag       used to identify imageViews and define different placeholders
     * @return true if an image was set
     */
    override fun applyTo(imageView: ImageView, tag: String?): Boolean {
        val ii = iicon
        val uri = uri

        if (uri != null) {
            val consumed = DrawerImageLoader.instance.setImage(imageView, uri, tag)
            if (!consumed) {
                imageView.setImageURI(uri)
            }
        } else if (icon != null) {
            imageView.setImageDrawable(icon)
        } else if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
        } else if (iconRes != -1) {
            imageView.setImageResource(iconRes)
        } else if (ii != null) {
            imageView.setImageDrawable(IconicsDrawable(imageView.context, ii).actionBar())
        } else {
            imageView.setImageBitmap(null)
            return false
        }
        return true
    }

    /**
     * this only handles Drawables
     *
     * @param ctx
     * @param iconColor
     * @param tint
     * @return
     */
    override fun decideIcon(ctx: Context, iconColor: ColorStateList, tint: Boolean, paddingDp: Int): Drawable? {
        var icon: Drawable? = icon
        val ii = iicon
        val uri = uri

        when {
            ii != null -> icon = IconicsDrawable(ctx, ii).apply {
                colorList = iconColor
                sizeDp = 24
                this.paddingDp = paddingDp
            }
            iconRes != -1 -> icon = AppCompatResources.getDrawable(ctx, iconRes)
            uri != null -> try {
                val inputStream = ctx.contentResolver.openInputStream(uri)
                icon = Drawable.createFromStream(inputStream, uri.toString())
            } catch (e: FileNotFoundException) {
                //no need to handle this
            }
        }
        //if we got an icon AND we have auto tinting enabled AND it is no IIcon, tint it ;)
        if (icon != null && tint && iicon == null) {
            icon = icon.mutate()
            icon.setColorFilter(iconColor.defaultColor, PorterDuff.Mode.SRC_IN)
        }
        return icon
    }
}