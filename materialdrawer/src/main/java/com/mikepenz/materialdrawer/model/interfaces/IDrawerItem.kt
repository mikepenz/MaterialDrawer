package com.mikepenz.materialdrawer.model.interfaces

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.*

/**
 * Defines a general [IDrawerItem] to be displayed in the [com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView]
 */
interface IDrawerItem<VH : RecyclerView.ViewHolder> : IItem<VH>, IItemVHFactory<VH>, IItemViewGenerator, IExpandable<VH>, IIdentifyable, Selectable, Tagable {

    override var isEnabled: Boolean

    override var isSelected: Boolean

    override val type: Int

    val layoutRes: Int

    override var identifier: Long

    override var isExpanded: Boolean

    override val isAutoExpanding: Boolean

    override fun generateView(ctx: Context): View

    override fun generateView(ctx: Context, parent: ViewGroup): View

    override fun getViewHolder(parent: ViewGroup): VH

    override fun unbindView(holder: VH)

    override fun bindView(holder: VH, payloads: List<Any>)

    fun equals(id: Long): Boolean
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : IDrawerItem<*>> T.withIdentifier(identifier: Long): T {
    this.identifier = identifier
    return this
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : IDrawerItem<*>> T.withEnabled(enabled: Boolean): T {
    this.isEnabled = enabled
    return this
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : IDrawerItem<*>> T.withSelected(selected: Boolean): T {
    this.isSelected = selected
    return this
}