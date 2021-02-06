package com.mikepenz.materialdrawer.model

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

class NavigationDrawerItem<VH : RecyclerView.ViewHolder>(
        @IdRes val resId: Int,
        val item: IDrawerItem<VH>,
        val args: Bundle? = null,
        val options: NavOptions? = defaultOptions
) : IDrawerItem<VH> by item {

    init {
        item.identifier = resId.toLong()
    }

    /**
     * generates a view by the defined LayoutRes
     */
    override fun generateView(ctx: Context): View {
        val viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(layoutRes, null, false) as ViewGroup)
        bindView(viewHolder, mutableListOf())
        return viewHolder.itemView
    }

    /**
     * generates a view by the defined LayoutRes and pass the LayoutParams from the parent
     */
    override fun generateView(ctx: Context, parent: ViewGroup): View {
        val viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(layoutRes, parent, false) as ViewGroup)
        bindView(viewHolder, mutableListOf())
        return viewHolder.itemView
    }

    @CallSuper
    override fun bindView(holder: VH, payloads: List<Any>) {
        item.bindView(holder, payloads)
        holder.itemView.setTag(R.id.material_drawer_item, this)
    }

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