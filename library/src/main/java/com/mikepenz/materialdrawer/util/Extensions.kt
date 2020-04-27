package com.mikepenz.materialdrawer.util

import android.os.Build
import android.view.View
import android.widget.LinearLayout
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView

/**
 * helper to set the vertical padding including the extra padding for deeper item hirachy level to the DrawerItems
 * this is required because on API Level 17 the padding is ignored which is set via the XML
 */
internal fun View.setDrawerVerticalPadding(level: Int) {
    val verticalPadding = context.resources.getDimensionPixelSize(R.dimen.material_drawer_vertical_padding)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        setPaddingRelative(verticalPadding * level, 0, verticalPadding, 0)
    } else {
        setPadding(verticalPadding * level, 0, verticalPadding, 0)
    }
}


/**
 * helper method to set the selection of the footer
 */
fun MaterialDrawerSliderView.setStickyFooterSelection(position: Int, fireOnClick: Boolean?) {
    if (position > -1) {
        var position = position
        if (stickyFooterView != null && stickyFooterView is LinearLayout) {
            val footer = stickyFooterView as LinearLayout
            if (stickyFooterDivider) {
                position += 1
            }
            if (footer.childCount > position && position >= 0) {
                val drawerItem = footer.getChildAt(position).getTag(R.id.material_drawer_item) as IDrawerItem<*>
                onFooterDrawerItemClick(this, drawerItem, footer.getChildAt(position), fireOnClick)
            }
        }
    }
}

/**
 * calculates the position of an drawerItem. searching by its identifier
 */
@Deprecated("Please use getPosition instead", ReplaceWith("getPosition"))
fun MaterialDrawerSliderView.getPositionByIdentifier(identifier: Long): Int {
    if (identifier != -1L) {
        for (i in 0 until adapter.itemCount) {
            if (adapter.getItem(i)?.identifier == identifier) {
                return i
            }
        }
    }

    return -1
}

/**
 * gets the drawerItem with the specific identifier from a drawerItem list
 */
fun List<IDrawerItem<*>>.getDrawerItem(identifier: Long): IDrawerItem<*>? {
    if (identifier != -1L) {
        for (drawerItem in this) {
            if (drawerItem.identifier == identifier) {
                return drawerItem
            }
        }
    }
    return null
}

/**
 * gets the drawerItem by a defined tag from a drawerItem list
 */
fun List<IDrawerItem<*>>.getDrawerItem(tag: Any?): IDrawerItem<*>? {
    if (tag != null) {
        for (drawerItem in this) {
            if (tag == drawerItem.tag) {
                return drawerItem
            }
        }
    }
    return null
}

/**
 * calculates the position of an drawerItem inside the footer. searching by its identifier
 */
fun MaterialDrawerSliderView.getStickyFooterPositionByIdentifier(identifier: Long): Int {
    if (identifier != -1L) {
        if (stickyFooterView != null && stickyFooterView is LinearLayout) {
            val footer = stickyFooterView as LinearLayout

            var shadowOffset = 0
            for (i in 0 until footer.childCount) {
                val o = footer.getChildAt(i).getTag(R.id.material_drawer_item)

                //count up the shadowOffset to return the correct position of the given item
                if (o == null && stickyFooterDivider) {
                    shadowOffset += 1
                }

                if (o != null && o is IDrawerItem<*> && o.identifier == identifier) {
                    return i - shadowOffset
                }
            }
        }
    }

    return -1
}