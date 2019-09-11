package com.mikepenz.materialdrawer.model

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.ExperimentalNavController

@ExperimentalNavController
class NavigationDrawerItem<VH : RecyclerView.ViewHolder>(
        @IdRes val resId: Int,
        item: IDrawerItem<VH>,
        val args: Bundle? = null,
        val options: NavOptions? = defaultOptions
) : IDrawerItem<VH> by item {

    companion object {
        val defaultOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .build()
    }
}