package com.mikepenz.materialdrawer.util

import android.annotation.SuppressLint
import android.view.Menu
import androidx.annotation.MenuRes
import androidx.appcompat.view.SupportMenuInflater
import androidx.appcompat.view.menu.MenuBuilder
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.iconDrawable
import com.mikepenz.materialdrawer.model.interfaces.nameText
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView

/**
 * Inflates the DrawerItems from a menu.xml
 */
@SuppressLint("RestrictedApi")
fun MaterialDrawerSliderView.inflateMenu(@MenuRes menuRes: Int) {
    val menuInflater = SupportMenuInflater(context)
    val mMenu = MenuBuilder(context)

    menuInflater.inflate(menuRes, mMenu)

    addMenuItems(mMenu, false)
}

/**
 * helper method to init the drawerItems from a menu
 */
private fun MaterialDrawerSliderView.addMenuItems(mMenu: Menu, subMenu: Boolean) {
    var groupId = R.id.material_drawer_menu_default_group
    for (i in 0 until mMenu.size()) {
        val mMenuItem = mMenu.getItem(i)
        var iDrawerItem: IDrawerItem<*>
        if (!subMenu && mMenuItem.groupId != groupId && mMenuItem.groupId != 0) {
            groupId = mMenuItem.groupId
            iDrawerItem = DividerDrawerItem()
            itemAdapter.add(iDrawerItem)
        }
        if (mMenuItem.hasSubMenu()) {
            iDrawerItem = PrimaryDrawerItem().apply {
                nameText = mMenuItem.title
                iconDrawable = mMenuItem.icon
                identifier = mMenuItem.itemId.toLong()
                isEnabled = mMenuItem.isEnabled
                isSelectable = false
            }
            itemAdapter.add(iDrawerItem)
            addMenuItems(mMenuItem.subMenu, true)
        } else if (mMenuItem.groupId != 0 || subMenu) {
            iDrawerItem = SecondaryDrawerItem().apply {
                nameText = mMenuItem.title
                iconDrawable = mMenuItem.icon
                identifier = mMenuItem.itemId.toLong()
                isEnabled = mMenuItem.isEnabled
            }
            itemAdapter.add(iDrawerItem)
        } else {
            iDrawerItem = PrimaryDrawerItem().apply {
                nameText = mMenuItem.title
                iconDrawable = mMenuItem.icon
                identifier = mMenuItem.itemId.toLong()
                isEnabled = mMenuItem.isEnabled
            }
            itemAdapter.add(iDrawerItem)
        }
    }
}