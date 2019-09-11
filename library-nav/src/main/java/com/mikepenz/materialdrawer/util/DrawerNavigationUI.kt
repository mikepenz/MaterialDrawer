package com.mikepenz.materialdrawer.util

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.NavigationDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import java.lang.ref.WeakReference

// Notify user that the DSL is currently experimental
@Experimental(level = Experimental.Level.WARNING)
annotation class ExperimentalNavController

/**
 * Sets up a {@link Drawer} for use with a {@link NavController}.
 * The selected item in the Drawer will automatically be updated when the destination
 * changes.
 *
 * @param navController The NavController that hosts the destination.
 * @return
 */
@ExperimentalNavController
fun Drawer.setupWithNavController(navController: NavController) {
    DrawerNavigationUI.setupWithNavController(this, navController)
}

/**
 * Created by petretiandrea on 19.07.19.
 */
@ExperimentalNavController
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
     * @return
     */
    fun setupWithNavController(drawer: Drawer, navController: NavController) {
        drawer.onDrawerItemClickListener = object : Drawer.OnDrawerItemClickListener {
            override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                val success = performNavigation(drawerItem, navController)
                if (success) {
                    drawer.closeDrawer()
                }
                return success
            }
        }
        val weakReference: WeakReference<Drawer> = WeakReference(drawer)
        navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
                val drawerWeak: Drawer? = weakReference.get()
                if (drawerWeak == null) {
                    navController.removeOnDestinationChangedListener(this)
                }
                drawerWeak?.drawerItems?.filterIsInstance<NavigationDrawerItem<*>>()?.forEach {
                    // A ResId may refers to 3 intents.
                    val destinationId = controller.graph.getAction(it.resId)?.let { action ->
                        if (action.destinationId != 0) action.destinationId // an action navigate to a destination
                        else action.navOptions?.popUpTo // an action pop to a destination
                    } ?: it.resId // a destination

                    if (matchDestination(destination, destinationId)) drawerWeak.setSelection(it, false)
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