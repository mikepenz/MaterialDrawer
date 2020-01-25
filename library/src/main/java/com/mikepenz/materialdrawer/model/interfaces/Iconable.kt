package com.mikepenz.materialdrawer.model.interfaces

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.DrawableRes
import com.mikepenz.materialdrawer.holder.ImageHolder

/**
 * Defines a [IDrawerItem] with support for an icon
 */
interface Iconable {
    var icon: ImageHolder?
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
