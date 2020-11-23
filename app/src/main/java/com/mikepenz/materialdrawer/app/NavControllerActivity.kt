package com.mikepenz.materialdrawer.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.NavigationDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.withName
import com.mikepenz.materialdrawer.util.addItems
import com.mikepenz.materialdrawer.util.addStickyDrawerItems
import com.mikepenz.materialdrawer.util.setupWithNavController
import kotlinx.android.synthetic.main.activity_sample.*

class NavControllerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_nav)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)

        slider.apply {
            addItems(
                    NavigationDrawerItem(R.id.action_global_fragmentHome, PrimaryDrawerItem().withName("Home"), null, null),
                    DividerDrawerItem(),
                    NavigationDrawerItem(R.id.messageFragment1, PrimaryDrawerItem().withName("Fragment1")),
                    NavigationDrawerItem(R.id.messageFragment2, PrimaryDrawerItem().withName("Fragment2"))
            )
            addStickyDrawerItems(
                    NavigationDrawerItem(R.id.messageFragment3, PrimaryDrawerItem().withName("Fragment3"))
            )
        }


        // setup the drawer with navigation controller
        slider.setupWithNavController(navController)
    }
}