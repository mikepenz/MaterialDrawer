package com.mikepenz.materialdrawer.iconics

import android.os.Build
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.materialdrawer.model.BaseDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Iconable
import com.mikepenz.materialdrawer.model.interfaces.withIconTintingEnabled

var <T : BaseDrawerItem<*, *>> T.iconicsIcon: IIcon
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(value) {
        this.iconicsIconHolder = IconicsImageHolder(value)
    }

var <T : BaseDrawerItem<*, *>> T.iconicsIconHolder: IconicsImageHolder?
    get() = icon as? IconicsImageHolder
    set(value) {
        this.icon = value
        //if we are on api 21 or higher we use the IconicsDrawable for selection too and color it with the correct color
        //else we use just the one drawable and enable tinting on press
        if (Build.VERSION.SDK_INT >= 21) {
            this.selectedIcon = value
        } else if (value != null) {
            this.withIconTintingEnabled(true)
        }
    }

@Deprecated("Please consider to replace with the actual property setter")
fun <T : BaseDrawerItem<*, *>> T.withIcon(icon: IIcon): T {
    this.iconicsIconHolder = IconicsImageHolder(icon)
    return this
}

var <T : Iconable> T.iconicsIcon: IIcon
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not readable")
    get() = throw UnsupportedOperationException("Please use the direct property")
    set(value) {
        this.icon = IconicsImageHolder(value)
    }

@Deprecated("Please consider to replace with the actual property setter")
fun <T : Iconable> T.withIcon(icon: IIcon): T {
    this.icon = IconicsImageHolder(icon)
    return this
}