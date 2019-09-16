package com.mikepenz.materialdrawer.holder

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.mikepenz.iconics.IconicsColor
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.IconicsSize
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.materialdrawer.util.FixStateListDrawable
import com.mikepenz.materialize.util.UIUtils
import java.io.FileNotFoundException

/**
 * Created by mikepenz on 13.07.15.
 */
class ImageHolder {
    var uri: Uri? = null
    var icon: Drawable? = null
    var bitmap: Bitmap? = null
    var iconRes = -1
    var iIcon: IIcon? = null

    constructor(url: String) {
        this.uri = Uri.parse(url)
    }

    constructor(uri: Uri) {
        this.uri = uri
    }

    constructor(icon: Drawable?) {
        this.icon = icon
    }

    constructor(bitmap: Bitmap) {
        this.bitmap = bitmap
    }

    constructor(@DrawableRes iconRes: Int) {
        this.iconRes = iconRes
    }

    constructor(iicon: IIcon) {
        this.iIcon = iicon
    }

    /**
     * sets an existing image to the imageView
     *
     * @param imageView
     * @param tag       used to identify imageViews and define different placeholders
     * @return true if an image was set
     */
    @JvmOverloads
    fun applyTo(imageView: ImageView, tag: String? = null): Boolean {
        val ii = iIcon
        when {
            uri != null -> imageView.setImageURI(uri)
            icon != null -> imageView.setImageDrawable(icon)
            bitmap != null -> imageView.setImageBitmap(bitmap)
            iconRes != -1 -> imageView.setImageResource(iconRes)
            ii != null -> imageView.setImageDrawable(IconicsDrawable(imageView.context, ii).actionBar())
            else -> {
                imageView.setImageBitmap(null)
                return false
            }
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
    fun decideIcon(ctx: Context, iconColor: ColorStateList, tint: Boolean, paddingDp: Int = 1): Drawable? {
        var icon = this.icon
        val iicon = iIcon

        when {
            iicon != null -> icon = IconicsDrawable(ctx, iicon).color(IconicsColor.colorList(iconColor)).size(IconicsSize.dp(24)).padding(IconicsSize.dp(paddingDp))
            iconRes != -1 -> icon = ContextCompat.getDrawable(ctx, iconRes)
            uri != null -> try {
                val inputStream = ctx.contentResolver.openInputStream(uri!!)
                icon = Drawable.createFromStream(inputStream, uri!!.toString())
            } catch (e: FileNotFoundException) {
                //no need to handle this
            }
        }

        //if we got an icon AND we have auto tinting enabled AND it is no IIcon, tint it ;)

        //if we got an icon AND we have auto tinting enabled AND it is no IIcon, tint it ;)
        if (icon != null && tint) {
            icon = icon.mutate()
            icon.setColorFilter(iconColor.defaultColor, PorterDuff.Mode.SRC_IN)
        }

        return icon
    }

    companion object {

        /**
         * a small static helper to set the image from the imageHolder nullSave to the imageView
         *
         * @param imageHolder
         * @param imageView
         * @param tag         used to identify imageViews and define different placeholders
         * @return true if an image was set
         */
        @JvmOverloads
        fun applyTo(imageHolder: ImageHolder?, imageView: ImageView?, tag: String? = null): Boolean {
            return if (imageHolder != null && imageView != null) {
                imageHolder.applyTo(imageView, tag)
            } else false
        }

        /**
         * a small static helper to set the image from the imageHolder nullSave to the imageView and hide the view if no image was set
         *
         * @param imageHolder
         * @param imageView
         * @param tag         used to identify imageViews and define different placeholders
         */
        @JvmOverloads
        fun applyToOrSetInvisible(imageHolder: ImageHolder?, imageView: ImageView?, tag: String? = null) {
            val imageSet = applyTo(imageHolder, imageView, tag)
            if (imageView != null) {
                if (imageSet) {
                    imageView.visibility = View.VISIBLE
                } else {
                    imageView.visibility = View.INVISIBLE
                }
            }
        }

        /**
         * a small static helper to set the image from the imageHolder nullSave to the imageView and hide the view if no image was set
         *
         * @param imageHolder
         * @param imageView
         * @param tag         used to identify imageViews and define different placeholders
         */
        @JvmOverloads
        fun applyToOrSetGone(imageHolder: ImageHolder, imageView: ImageView?, tag: String? = null) {
            val imageSet = applyTo(imageHolder, imageView, tag)
            if (imageView != null) {
                if (imageSet) {
                    imageView.visibility = View.VISIBLE
                } else {
                    imageView.visibility = View.GONE
                }
            }
        }

        /**
         * a small static helper which catches nulls for us
         *
         * @param imageHolder
         * @param ctx
         * @param iconColor
         * @param tint
         * @return
         */
        fun decideIcon(imageHolder: ImageHolder?, ctx: Context, iconColor: ColorStateList, tint: Boolean, paddingDp: Int = 1): Drawable? {
            return imageHolder?.decideIcon(ctx, iconColor, tint, paddingDp)
        }

        /**
         * decides which icon to apply or hide this view
         *
         * @param imageHolder
         * @param imageView
         * @param iconColor
         * @param tint
         */
        fun applyDecidedIconOrSetGone(imageHolder: ImageHolder?, imageView: ImageView?, iconColor: ColorStateList, tint: Boolean, paddingDp: Int = 1) {
            if (imageHolder != null && imageView != null) {
                val drawable = decideIcon(imageHolder, imageView.context, iconColor, tint, paddingDp)
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
        fun applyMultiIconTo(icon: Drawable?, selectedIcon: Drawable?, iconColor: ColorStateList, tinted: Boolean, imageView: ImageView) {
            //if we have an icon then we want to set it
            if (icon != null) {
                //if we got a different color for the selectedIcon we need a StateList
                if (selectedIcon != null) {
                    if (tinted) {
                        imageView.setImageDrawable(FixStateListDrawable(icon, selectedIcon, iconColor))
                    } else {
                        imageView.setImageDrawable(UIUtils.getIconStateList(icon, selectedIcon))
                    }
                } else if (tinted) {
                    imageView.setImageDrawable(FixStateListDrawable(icon, iconColor))
                } else {
                    imageView.setImageDrawable(icon)
                }
                //make sure we display the icon
                imageView.visibility = View.VISIBLE
            } else {
                //hide the icon
                imageView.visibility = View.GONE
            }
        }
    }
}