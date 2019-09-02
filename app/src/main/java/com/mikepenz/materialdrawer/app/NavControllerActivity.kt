package com.mikepenz.materialdrawer.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.NavigationDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.util.ExperimentalNavController
import com.mikepenz.materialdrawer.util.setupWithNavController

@UseExperimental(ExperimentalNavController::class)
class NavControllerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_controller)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)

        val drawer = DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        NavigationDrawerItem(R.id.action_global_fragmentHome, PrimaryDrawerItem().withName("Home")),
                        DividerDrawerItem(),
                        NavigationDrawerItem(R.id.messageFragment1, PrimaryDrawerItem().withName("Fragment1")),
                        NavigationDrawerItem(R.id.messageFragment2, PrimaryDrawerItem().withName("Fragment2")),
                        NavigationDrawerItem(R.id.messageFragment3, PrimaryDrawerItem().withName("Fragment3"))
                )
                .build()

        // setup the drawer with navigation controller
        drawer.setupWithNavController(navController)
        // setup Action Bar
        NavigationUI.setupActionBarWithNavController(this, navController, drawer.drawerLayout)
    }
}