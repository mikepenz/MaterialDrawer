package com.mikepenz.materialdrawer.app.drawerItems

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.materialdrawer.app.R

open class CustomBaseViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
    var icon: ImageView = view.findViewById<ImageView>(R.id.material_drawer_icon)
    var name: TextView = view.findViewById<TextView>(R.id.material_drawer_name)
    var description: TextView = view.findViewById<TextView>(R.id.material_drawer_description)
}