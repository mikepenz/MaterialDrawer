package com.mikepenz.materialdrawer.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.R

/**
 *the base [RecyclerView.ViewHolder] for drawerItems
 */
open class BaseViewHolder(internal var view: View) : RecyclerView.ViewHolder(view) {
    internal var icon: ImageView = view.findViewById(R.id.material_drawer_icon)
    internal var name: TextView = view.findViewById(R.id.material_drawer_name)
    internal var description: TextView = view.findViewById(R.id.material_drawer_description)
}