package com.mikepenz.materialdrawer.app.drawerItems

import com.mikepenz.materialdrawer.holder.ColorHolder
import com.mikepenz.materialdrawer.model.AbstractBadgeableDrawerItem

class CustomPrimaryDrawerItem : AbstractBadgeableDrawerItem<CustomPrimaryDrawerItem>() {

    var background: ColorHolder? = null

    fun withBackgroundColor(backgroundColor: Int): CustomPrimaryDrawerItem {
        this.background = ColorHolder.fromColor(backgroundColor)
        return this
    }

    fun withBackgroundRes(backgroundRes: Int): CustomPrimaryDrawerItem {
        this.background = ColorHolder.fromColorRes(backgroundRes)
        return this
    }

    override fun bindView(holder: AbstractBadgeableDrawerItem.ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)
        background?.applyToBackground(holder.itemView)
    }
}
