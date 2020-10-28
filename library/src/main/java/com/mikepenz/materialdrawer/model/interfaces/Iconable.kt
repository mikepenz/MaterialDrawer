package com.mikepenz.materialdrawer.model.interfaces

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.DrawableRes
import com.mikepenz.materialdrawer.holder.ImageHolder

/**
 * Defines a [IDrawerItem] with support for an icon
 */
interface Iconable {
    /** the icon to show in the drawer */
    var icon: ImageHolder?

    /** the color of the icon */
    var iconColor: ColorStateList?
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Iconable> T.withIconColor(iconColor: ColorStateList): T {
    this.iconColor = iconColor
    return this
}

var <T : Iconable> T.iconDrawable: Drawable?
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(value) {
        if (value != null) {
            this.icon = ImageHolder(value)
        } else {
            this.icon = null
        }
    }

var <T : Iconable> T.iconBitmap: Bitmap
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(value) {
        this.icon = ImageHolder(value)
    }

var <T : Iconable> T.iconRes: Int
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(@DrawableRes value) {
        this.icon = ImageHolder(value)
    }

var <T : Iconable> T.iconUrl: String
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(value) {
        this.icon = ImageHolder(value)
    }

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Iconable> T.withIcon(icon: Drawable?): T {
    this.icon = ImageHolder(icon)
    return this
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Iconable> T.withIcon(icon: Bitmap): T {
    this.icon = ImageHolder(icon)
    return this
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Iconable> T.withIcon(@DrawableRes imageRes: Int): T {
    this.icon = ImageHolder(imageRes)
    return this
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Iconable> T.withIcon(url: String): T {
    this.icon = ImageHolder(url)
    return this
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Iconable> T.withIcon(uri: Uri): T {
    this.icon = ImageHolder(uri)
    return this
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Iconable> T.withIcon(icon: ImageHolder?): T {
    this.icon = icon
    return this
}
