package com.mikepenz.materialdrawer.interfaces

import android.widget.CompoundButton

import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

/**
 * Interface definition for a callback to be invoked when the checked state
 * of a compound button changed.
 */
interface OnCheckedChangeListener {
    /**
     * Called when the checked state of a compound button has changed.
     */
    fun onCheckedChanged(drawerItem: IDrawerItem<*>, buttonView: CompoundButton, isChecked: Boolean)
}