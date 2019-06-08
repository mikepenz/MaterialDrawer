package com.mikepenz.materialdrawer.holder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.mikepenz.iconics.IconicsColor
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.IconicsSize
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import java.io.FileNotFoundException

/**
 * Created by mikepenz on 13.07.15.
 */

class ImageHolder : com.mikepenz.materialize.holder.ImageHolder {
    var iIcon: IIcon? = null

    constructor(url: String) : super(url) {}

    constructor(uri: Uri) : super(uri) {}

    constructor(icon: Drawable?) : super(icon) {}

    constructor(bitmap: Bitmap?) : super(bitmap) {}

    constructor(@DrawableRes iconRes: Int) : super(iconRes) {}

    constructor(iicon: IIcon) : super(null as Bitmap?) {
        this.iIcon = iicon
    }

    /**
     * sets an existing image to the imageView
     *
     * @param imageView
     * @param tag       used to identify imageViews and define different placeholders
     * @return true if an image was set
     */
    override fun applyTo(imageView: ImageView, tag: String?): Boolean {
        val ii = iIcon

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
    fun decideIcon(ctx: Context, iconColor: Int, tint: Boolean, paddingDp: Int): Drawable? {
        var icon: Drawable? = icon
        val ii = iIcon

        when {
            ii != null -> icon = IconicsDrawable(ctx, ii).color(IconicsColor.colorInt(iconColor)).size(IconicsSize.dp(24)).padding(IconicsSize.dp(paddingDp))
            iconRes != -1 -> icon = AppCompatResources.getDrawable(ctx, iconRes)
            uri != null -> try {
                val inputStream = ctx.contentResolver.openInputStream(uri)
                icon = Drawable.createFromStream(inputStream, uri.toString())
            } catch (e: FileNotFoundException) {
                //no need to handle this
            }
        }

        //if we got an icon AND we have auto tinting enabled AND it is no IIcon, tint it ;)

        //if we got an icon AND we have auto tinting enabled AND it is no IIcon, tint it ;)
        if (icon != null && tint && iIcon == null) {
            icon = icon.mutate()
            icon?.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN)
        }

        return icon
    }

    companion object {

        /**
         * a small static helper which catches nulls for us
         *
         * @param imageHolder
         * @param ctx
         * @param iconColor
         * @param tint
         * @return
         */
        fun decideIcon(imageHolder: ImageHolder?, ctx: Context, iconColor: Int, tint: Boolean, paddingDp: Int): Drawable? {
            return imageHolder?.decideIcon(ctx, iconColor, tint, paddingDp)
        }

        /**
         * decides which icon to apply or hide this view
         *
         * @param imageHolder
         * @param imageView
         * @param iconColor
         * @param tint
         * @param paddingDp
         */
        fun applyDecidedIconOrSetGone(imageHolder: ImageHolder?, imageView: ImageView?, iconColor: Int, tint: Boolean, paddingDp: Int) {
            if (imageHolder != null && imageView != null) {
                val drawable = ImageHolder.decideIcon(imageHolder, imageView.context, iconColor, tint, paddingDp)
                when {
                    drawable != null -> {
                        imageView.setImageDrawable(drawable)
                        imageView.visibility = View.VISIBLE
                    }
                    imageHolder.bitmap != null -> {
                        imageView.setImageBitmap(imageHolder.bitmap)
                        imageView.visibility = View.VISIBLE
                    }
                    else -> imageView.visibility = View.GONE
                }
            } else if (imageView != null) {
                imageView.visibility = View.GONE
            }
        }


        /**
         * a small static helper to set a multi state drawable on a view
         *
         * @param icon
         * @param iconColor
         * @param selectedIcon
         * @param selectedIconColor
         * @param tinted
         * @param imageView
         */
        fun applyMultiIconTo(icon: Drawable?, iconColor: Int, selectedIcon: Drawable?, selectedIconColor: Int, tinted: Boolean, imageView: ImageView) {
            com.mikepenz.materialize.holder.ImageHolder.applyMultiIconTo(icon, iconColor, selectedIcon, selectedIconColor, tinted, imageView)
        }


        fun applyToOrSetInvisible(imageHolder: ImageHolder?, imageView: ImageView?, tag: String? = null) {
            com.mikepenz.materialize.holder.ImageHolder.applyToOrSetInvisible(imageHolder, imageView, tag)
        }
    }

}
