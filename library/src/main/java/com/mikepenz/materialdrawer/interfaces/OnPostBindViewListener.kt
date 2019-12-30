package com.mikepenz.materialdrawer.interfaces

import android.view.View
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

/**
 * Defines the listener fired after binding a view
 */
interface OnPostBindViewListener {
    /**
     * allows you to hook in the BindView method and modify the view after binding
     */
    fun onBindView(drawerItem: IDrawerItem<*>, view: View)
}
