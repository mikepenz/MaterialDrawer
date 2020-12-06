package com.mikepenz.materialdrawer.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.mikepenz.aboutlibraries.LibsBuilder
import com.mikepenz.materialdrawer.app.databinding.ActivitySampleNavBinding
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.NavigationDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.withName
import com.mikepenz.materialdrawer.util.addItems
import com.mikepenz.materialdrawer.util.addStickyDrawerItems
import com.mikepenz.materialdrawer.util.setupWithNavController
import java.io.Serializable

class NavControllerActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySampleNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySampleNavBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)

        binding.slider.apply {
            addItems(
                    NavigationDrawerItem(R.id.action_global_fragmentHome, PrimaryDrawerItem().withName("Home"), null, null),
                    DividerDrawerItem(),
                    NavigationDrawerItem(R.id.messageFragment1, PrimaryDrawerItem().withName("Fragment1")),
                    NavigationDrawerItem(R.id.messageFragment2, PrimaryDrawerItem().withName("Fragment2"))
            )
            addStickyDrawerItems(
                    NavigationDrawerItem(R.id.messageFragment3, PrimaryDrawerItem().withName("Fragment3")),
                    NavigationDrawerItem(R.id.about_libraries, PrimaryDrawerItem().withName("AboutLibs"), bundleOf("data" to (LibsBuilder() as Serializable)))
            )
        }


        // setup the drawer with navigation controller
        binding.slider.setupWithNavController(navController)
    }
}