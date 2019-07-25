package com.mikepenz.materialdrawer.model

import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

class NavigationDrawerItem<VH : RecyclerView.ViewHolder> (val destination: Int, item: IDrawerItem<VH>) : IDrawerItem<VH> by item