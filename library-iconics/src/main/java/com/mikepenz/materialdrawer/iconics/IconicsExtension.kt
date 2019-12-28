package com.mikepenz.materialdrawer.iconics

import android.os.Build
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.materialdrawer.model.BaseDrawerItem
import com.mikepenz.materialdrawer.model.MiniProfileDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem

fun <T : BaseDrawerItem<*, *>> T.withIcon(icon: IIcon): T {
    this.icon = IconicsImageHolder(icon)
    //if we are on api 21 or higher we use the IconicsDrawable for selection too and color it with the correct color
    //else we use just the one drawable and enable tinting on press
    if (Build.VERSION.SDK_INT >= 21) {
        this.selectedIcon = IconicsImageHolder(icon)
    } else {
        this.withIconTintingEnabled(true)
    }
    return this
}

fun ProfileDrawerItem.withIcon(icon: IIcon): ProfileDrawerItem {
    this.icon = IconicsImageHolder(icon)
    return this
}

fun ProfileSettingDrawerItem.withIcon(icon: IIcon): ProfileSettingDrawerItem {
    this.icon = IconicsImageHolder(icon)
    return this
}

fun MiniProfileDrawerItem.withIcon(icon: IIcon): MiniProfileDrawerItem {
    this.icon = IconicsImageHolder(icon)
    return this
}