package com.mikepenz.materialdrawer.util

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.mikepenz.materialdrawer.model.NavigationDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView
import java.lang.ref.WeakReference

/**
 * Sets up a {@link Drawer} for use with a {@link NavController}.
 * The selected item in the Drawer will automatically be updated when the destination
 * changes.
 *
 * @param navController The NavController that hosts the destination
 * @param fallBackListener the listener to handle no navigationDrawerItems
 * @return
 */
@Deprecated("Added new successListener", ReplaceWith("setupWithNavController(navController, null, fallBackListener)"))
fun MaterialDrawerSliderView.setupWithNavController(
    navController: NavController,
    fallBackListener: ((v: View?, item: IDrawerItem<*>, position: Int) -> Boolean)? = null,
) {
    setupWithNavController(navController, null, fallBackListener)
}

/**
 * Sets up a {@link Drawer} for use with a {@link NavController}.
 * The selected item in the Drawer will automatically be updated when the destination
 * changes.
 *
 * @param navController The NavController that hosts the destination
 * @param successListener listener to retrieve a notification on successful navigation
 * @param fallBackListener the listener to handle no navigationDrawerItems
 * @return
 */
fun MaterialDrawerSliderView.setupWithNavController(
        navController: NavController,
        successListener: ((v: View?, item: IDrawerItem<*>, position: Int) -> Boolean)? = null,
        fallBackListener: ((v: View?, item: IDrawerItem<*>, position: Int) -> Boolean)? = null,
) {
    DrawerNavigationUI.setupWithNavController(this, navController, successListener, fallBackListener)
}

/**
 * Created by petretiandrea on 19.07.19.
 */
object DrawerNavigationUI {

    /**
     * Sets up a {@link Drawer} for use with a {@link NavController}. It allow to navigate
     * to a destination when an item {@link NavigationDrawerItem} of drawer is selected.
     * The selected item in the Drawer will automatically be updated when the destination
     * changes.
     *
     * It automatically close the Drawer.
     *
     * @param drawer The drawer that should be kept in sync with changes to the NavController.
     * @param navController The NavController that allow to perform the navigation actions, relying on the item selected in the Drawer
     * @param fallBackListener A listener called when perform navigation fails
     * @return
     */
    fun setupWithNavController(
            drawer: MaterialDrawerSliderView,
            navController: NavController,
            successListener: ((v: View?, item: IDrawerItem<*>, position: Int) -> Boolean)? = null,
            fallBackListener: ((v: View?, item: IDrawerItem<*>, position: Int) -> Boolean)? = null
    ) {
        drawer.onDrawerItemClickListener = { v, item, position ->
            val success = performNavigation(item, navController)
            if (success) {
                successListener?.invoke(v, item, position)
                drawer.drawerLayout?.closeDrawer(drawer)
            } else {
                fallBackListener?.invoke(v, item, position)
            }
            success
        }
        val weakReference: WeakReference<MaterialDrawerSliderView> = WeakReference(drawer)
        navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
                val drawerWeak: MaterialDrawerSliderView? = weakReference.get()
                if (drawerWeak == null) {
                    navController.removeOnDestinationChangedListener(this)
                }

                fun Iterable<NavigationDrawerItem<*>>.handleSelection(): Boolean {
                    var matched = false
                    for (element in this) {
                        // A ResId may refers to 3 intents.
                        val destinationId = controller.graph.getAction(element.resId)?.let { action ->
                            if (action.destinationId != 0) action.destinationId // an action navigate to a destination
                            else action.navOptions?.popUpToId // an action pop to a destination
                        } ?: element.resId // a destination

                        if (matchDestination(destination, destinationId)) {
                            drawerWeak?.selectExtension?.deselect()
                            drawerWeak?.setSelection(element.identifier, false)

                            drawerWeak?.getStickyFooterPosition(element)?.let {
                                drawerWeak.setStickyFooterSelection(it, false)
                            }

                            matched = true
                        }
                    }
                    return matched
                }

                if (drawerWeak?.itemAdapter?.adapterItems?.filterIsInstance<NavigationDrawerItem<*>>()?.handleSelection() != true) {
                    // if we did not match the normal items, also check the footer
                    if (drawerWeak?.footerAdapter?.adapterItems?.filterIsInstance<NavigationDrawerItem<*>>()?.handleSelection() != true) {
                        // if footer also did not match, go to sticky
                        drawerWeak?.stickyDrawerItems?.filterIsInstance<NavigationDrawerItem<*>>()?.handleSelection()
                    }
                }
            }
        })
    }

    /***
     * Try to perform a navigation using the NavController to destination associated to IDrawerItem.
     *
     * Importantly, it assumes that the item type's is {@link NavigationDrawerItem} and that
     * the resId matches a valid {@link NavDestination#getAction(int) action id} or {@link NavDestination#getId() destination id}
     * to be navigated to.
     *
     * @param item The selected drawer item
     * @param navController The NavController that hosts the destination.
     * @return True if the navigation is performed, False otherwise.
     */
    private fun performNavigation(item: IDrawerItem<*>, navController: NavController): Boolean {
        return when (item) {
            is NavigationDrawerItem -> {
                try {
                    navController.navigate(item.resId, item.args, item.options)
                    true
                } catch (e: IllegalArgumentException) {
                    false
                }
            }
            else -> false
        }
    }

    private fun matchDestination(destination: NavDestination, @IdRes destId: Int): Boolean {
        var currentDestination = destination
        while (currentDestination.id != destId && currentDestination.parent != null) {
            currentDestination = currentDestination.parent!!
        }
        return currentDestination.id == destId
    }
}
