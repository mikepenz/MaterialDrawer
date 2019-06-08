package com.mikepenz.materialdrawer.app

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import kotlinx.android.synthetic.main.activity_sample_dark_toolbar.*

class MenuDrawerActivity : AppCompatActivity() {

    private var result: Drawer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_dark_toolbar)

        // Handle Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.drawer_item_menu_drawer)

        //Create the drawer
        result = DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .inflateMenu(R.menu.example_menu)
                .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                    override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                        if (drawerItem is Nameable<*>) {
                            Toast.makeText(this@MenuDrawerActivity, (drawerItem as Nameable<*>).name!!.getText(this@MenuDrawerActivity), Toast.LENGTH_SHORT).show()
                        }

                        return false
                    }
                }).build()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result?.isDrawerOpen == true) {
            result?.closeDrawer()
        } else {
            super.onBackPressed()
        }
    }
}
