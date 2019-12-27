package com.mikepenz.materialdrawer.util

import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.Badgeable
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Iconable
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView

/**
 * calculates the position of an drawerItem. searching by it's identifier
 *
 * @param drawerItem
 * @return
 */
fun MaterialDrawerSliderView.getPosition(drawerItem: IDrawerItem<*>): Int {
    return getPosition(drawerItem.identifier)
}

/**
 * calculates the position of an drawerItem. searching by it's identifier
 *
 * @param identifier
 * @return
 */
fun MaterialDrawerSliderView.getPosition(identifier: Long): Int {
    return DrawerUtils.getPositionByIdentifier(this, identifier)
}

/**
 * returns the DrawerItem by the given identifier
 *
 * @param identifier
 * @return
 */
fun MaterialDrawerSliderView.getDrawerItem(identifier: Long): IDrawerItem<*>? {
    val res = adapter.getItemById(identifier)
    return res?.first
}

/**
 * returns the found drawerItem by the given tag
 *
 * @param tag
 * @return
 */
fun MaterialDrawerSliderView.getDrawerItem(tag: Any): IDrawerItem<*>? {
    return DrawerUtils.getDrawerItem(itemAdapter.adapterItems, tag)
}

/**
 * update a specific drawer item :D
 * automatically identified by its id
 *
 * @param drawerItem
 */
fun MaterialDrawerSliderView.updateItem(drawerItem: IDrawerItem<*>) {
    updateItemAtPosition(drawerItem, getPosition(drawerItem))
}

/**
 * update the badge for a specific drawerItem
 * identified by its id
 *
 * @param identifier
 * @param badge
 */
fun MaterialDrawerSliderView.updateBadge(identifier: Long, badge: StringHolder) {
    val drawerItem = getDrawerItem(identifier)
    if (drawerItem is Badgeable<*>) {
        drawerItem.withBadge(badge)
        updateItem(drawerItem)
    }
}

/**
 * update the name for a specific drawerItem
 * identified by its id
 *
 * @param identifier
 * @param name
 */
fun MaterialDrawerSliderView.updateName(identifier: Long, name: StringHolder) {
    val drawerItem = getDrawerItem(identifier)
    if (drawerItem is Nameable<*>) {
        drawerItem.withName(name)
        updateItem(drawerItem)
    }
}

/**
 * update the name for a specific drawerItem
 * identified by its id
 *
 * @param identifier
 * @param image
 */
fun MaterialDrawerSliderView.updateIcon(identifier: Long, image: ImageHolder) {
    val drawerItem = getDrawerItem(identifier)
    if (drawerItem is Iconable<*>) {
        drawerItem.withIcon(image)
        updateItem(drawerItem)
    }
}

/**
 * Update a drawerItem at a specific position
 *
 * @param drawerItem
 * @param position
 */
fun MaterialDrawerSliderView.updateItemAtPosition(drawerItem: IDrawerItem<*>, position: Int) {
    if (checkDrawerItem(position, false)) {
        itemAdapter[position] = drawerItem
    }
}

/**
 * check if the item is within the bounds of the list
 *
 * @param position
 * @param includeOffset
 * @return
 */
internal fun MaterialDrawerSliderView.checkDrawerItem(position: Int, includeOffset: Boolean): Boolean {
    return adapter.getItem(position) != null
}