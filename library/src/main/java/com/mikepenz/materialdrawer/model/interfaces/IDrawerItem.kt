package com.mikepenz.materialdrawer.model.interfaces

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.IExpandable
import com.mikepenz.fastadapter.IItem

/**
 * Created by mikepenz on 03.02.15.
 */
interface IDrawerItem<VH : RecyclerView.ViewHolder> : IItem<VH>, IExpandable<VH> {

    override var tag: Any?

    override var isEnabled: Boolean

    override var isSelected: Boolean

    override var isSelectable: Boolean

    override val type: Int

    override val layoutRes: Int

    override var identifier: Long

    override var isExpanded: Boolean

    override val isAutoExpanding: Boolean

    override fun generateView(ctx: Context): View

    override fun generateView(ctx: Context, parent: ViewGroup): View

    override fun getViewHolder(parent: ViewGroup): VH

    override fun unbindView(holder: VH)

    override fun bindView(holder: VH, payloads: MutableList<Any>)

    fun equals(id: Long): Boolean
}
