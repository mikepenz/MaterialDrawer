package com.mikepenz.materialdrawer.util

import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.*
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView
import java.util.*

/**
 * Add a drawerItem at a specific position
 *
 * @param drawerItem
 * @param position
 */
fun MaterialDrawerSliderView.addItemAtPosition(position: Int, drawerItem: IDrawerItem<*>) {
    itemAdapter.add(position, drawerItem)
}

/**
 * Set a drawerItem at a specific position
 */
fun MaterialDrawerSliderView.setItemAtPosition(position: Int, drawerItem: IDrawerItem<*>) {
    itemAdapter[position] = drawerItem
}

/**
 * Remove a drawerItem at a specific position
 */
fun MaterialDrawerSliderView.removeItemByPosition(position: Int) {
    if (checkDrawerItem(position, false)) {
        itemAdapter.remove(position)
    }
}

/**
 * remove a list of drawerItems by their identifiers
 */
fun MaterialDrawerSliderView.removeItems(vararg identifiers: Long) {
    identifiers.forEach {
        itemAdapter.removeByIdentifier(it)
    }
}

/**
 * remove all items from this drawer
 */
fun MaterialDrawerSliderView.removeAllItems() {
    itemAdapter.clear()
}

/**
 * remove single ore more drawerItems via their identifier
 */
fun MaterialDrawerSliderView.removeItems(vararg drawerItems: IDrawerItem<*>) {
    drawerItems.forEach {
        itemAdapter.removeByIdentifier(it.identifier)
    }
}

/**
 * set single ore more DrawerItems to the Drawer
 */
fun MaterialDrawerSliderView.setItems(vararg drawerItems: IDrawerItem<*>) {
    itemAdapter.set(drawerItems.asList())
}

/**
 * add single ore more DrawerItems to the Drawer
 */
fun MaterialDrawerSliderView.addItems(vararg drawerItems: IDrawerItem<*>) {
    itemAdapter.add(*drawerItems)
}

/**
 * add single ore more DrawerItems to the Drawer
 */
fun MaterialDrawerSliderView.addItemsAtPosition(position: Int, vararg drawerItems: IDrawerItem<*>) {
    itemAdapter.add(position, *drawerItems)
}

/**
 * calculates the position of an drawerItem. searching by its identifier
 *
 * @param drawerItem
 * @return
 */
fun MaterialDrawerSliderView.getPosition(drawerItem: IDrawerItem<*>): Int {
    return getPosition(drawerItem.identifier)
}

/**
 * calculates the position of an drawerItem. searching by its identifier
 *
 * @param identifier
 * @return
 */
fun MaterialDrawerSliderView.getPosition(identifier: Long): Int {
    return this.getPositionByIdentifier(identifier)
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
    return itemAdapter.adapterItems.getDrawerItem(tag)
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
    if (drawerItem is Badgeable) {
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
    if (drawerItem is Nameable) {
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
    if (drawerItem is Iconable) {
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

/**
 * calculates the position of an drawerItem. searching by it's identifier
 *
 * @param drawerItem
 * @return
 */
fun MaterialDrawerSliderView.getStickyFooterPosition(drawerItem: IDrawerItem<*>): Int {
    return getStickyFooterPositionByIdentifier(drawerItem.identifier)
}

/**
 * update a specific footerDrawerItem :D
 * automatically identified by its id
 *
 * @param drawerItem
 */
fun MaterialDrawerSliderView.updateStickyFooterItem(drawerItem: IDrawerItem<*>) {
    updateStickyFooterItemAtPosition(drawerItem, getStickyFooterPosition(drawerItem))
}

/**
 * update a footerDrawerItem at a specific position
 *
 * @param drawerItem
 * @param position
 */
fun MaterialDrawerSliderView.updateStickyFooterItemAtPosition(drawerItem: IDrawerItem<*>, position: Int) {
    if (stickyDrawerItems.size > position) {
        stickyDrawerItems[position] = drawerItem
    }
    handleStickyFooterView()
}


/**
 * Add a footerDrawerItem at the end
 *
 * @param drawerItem
 */
fun MaterialDrawerSliderView.addStickyFooterItem(drawerItem: IDrawerItem<*>) {
    stickyDrawerItems.add(drawerItem)
    handleStickyFooterView()
}

/**
 * Add an initial DrawerItem or a DrawerItem Array for the StickyDrawerFooter
 */
fun MaterialDrawerSliderView.addStickyDrawerItems(vararg stickyDrawerItems: IDrawerItem<*>) {
    Collections.addAll(this.stickyDrawerItems, *stickyDrawerItems)
    handleStickyFooterView()
}

/**
 * Add a footerDrawerItem at a specific position
 *
 * @param drawerItem
 * @param position
 */
fun MaterialDrawerSliderView.addStickyFooterItemAtPosition(drawerItem: IDrawerItem<*>, position: Int) {
    stickyDrawerItems.add(position, drawerItem)
    handleStickyFooterView()
}

/**
 * Set a footerDrawerItem at a specific position
 *
 * @param drawerItem
 * @param position
 */
fun MaterialDrawerSliderView.setStickyFooterItemAtPosition(drawerItem: IDrawerItem<*>, position: Int) {
    if (stickyDrawerItems.size > position) {
        stickyDrawerItems[position] = drawerItem
    }
    handleStickyFooterView()
}


/**
 * Remove a footerDrawerItem at a specific position
 *
 * @param position
 */
fun MaterialDrawerSliderView.removeStickyFooterItemAtPosition(position: Int) {
    if (stickyDrawerItems.size > position) {
        stickyDrawerItems.removeAt(position)
    }
    handleStickyFooterView()
}

/**
 * Removes all footerItems from drawer
 */
fun MaterialDrawerSliderView.removeAllStickyFooterItems() {
    stickyDrawerItems.clear()
    handleStickyFooterView()
}
