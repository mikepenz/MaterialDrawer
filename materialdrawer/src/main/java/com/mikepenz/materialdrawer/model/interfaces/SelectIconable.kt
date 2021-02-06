package com.mikepenz.materialdrawer.model.interfaces

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.mikepenz.materialdrawer.holder.ImageHolder

/**
 * Defines a [IDrawerItem] with support for an icon
 */
interface SelectIconable {
    /** the icon to show when this item gets selected */
    var selectedIcon: ImageHolder?

    /** defines if the icon should get proper tinting with the defined color */
    var isIconTinted: Boolean
}

var <T : Iconable> T.selectedIconDrawable: Drawable
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(value) {
        this.icon = ImageHolder(value)
    }

var <T : Iconable> T.selectedIconBitmap: Bitmap
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(value) {
        this.icon = ImageHolder(value)
    }

var <T : Iconable> T.selectedIconRes: Int
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(@DrawableRes value) {
        this.icon = ImageHolder(value)
    }

var <T : Iconable> T.selectedIconUrl: String
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(value) {
        this.icon = ImageHolder(value)
    }

@Deprecated("Please consider to replace with the actual property setter")
fun <T : SelectIconable> T.withSelectedIcon(selectedIcon: Drawable): T {
    this.selectedIcon = ImageHolder(selectedIcon)
    return this as T
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : SelectIconable> T.withSelectedIcon(@DrawableRes selectedIconRes: Int): T {
    this.selectedIcon = ImageHolder(selectedIconRes)
    return this as T
}

/** will tint the icon with the default (or set) colors (default and selected state) */
@Deprecated("Please consider to replace with the actual property setter")
fun <T : SelectIconable> T.withIconTintingEnabled(iconTintingEnabled: Boolean): T {
    this.isIconTinted = iconTintingEnabled
    return this as T
}

/** will tint the icon with the default (or set) colors (default and selected state) */
@Deprecated("Please consider to replace with the actual property setter")
fun <T : SelectIconable> T.withIconTinted(iconTintingEnabled: Boolean): T {
    this.isIconTinted = iconTintingEnabled
    return this as T
}