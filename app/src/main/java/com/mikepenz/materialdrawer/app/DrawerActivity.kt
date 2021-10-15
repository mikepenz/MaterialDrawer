package com.mikepenz.materialdrawer.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.mikepenz.materialdrawer.app.databinding.ActivityMainBinding
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.model.NavigationDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.iconRes
import com.mikepenz.materialdrawer.model.interfaces.nameText
import com.mikepenz.materialdrawer.util.addItems
import com.mikepenz.materialdrawer.util.addStickyDrawerItems
import com.mikepenz.materialdrawer.util.setupWithNavController

class DrawerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val navController by lazy {
        //Find the NavHostFragment for this activity
        (supportFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment)
            //return the navController
            .navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Inflate & bind the layout for the activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Create drawer with configuration
        binding.materialSlider.apply {
            addItems(
                NavigationDrawerItem(
                    1,
                    PrimaryDrawerItem().apply {
                        nameText = "Catalogue"
                        icon = ImageHolder(R.drawable.ic_catalogue)
                        iconColor = ContextCompat.getColorStateList(context, R.color.teal_200)
                        isIconTinted = true
                    }
                ),
                NavigationDrawerItem(
                    2,
                    PrimaryDrawerItem().apply {
                        nameText = "Market Status"
                        iconRes = R.drawable.ic_market_status
                        iconColor = ContextCompat.getColorStateList(context, R.color.teal_200)
                        isIconTinted = true
                    }
                ),
                NavigationDrawerItem(
                    3,
                    PrimaryDrawerItem().apply {
                        nameText = "Settings"
                        iconRes = R.drawable.ic_settings
                        iconColor = ContextCompat.getColorStateList(context, R.color.teal_200)
                        isIconTinted = true
                    }
                )
            )
            addStickyDrawerItems(
                SecondaryDrawerItem().apply {
                    nameText = "Logout"
                    iconRes = R.drawable.ic_logout
                    iconColor = ContextCompat.getColorStateList(context, R.color.teal_200)
                },
            )
        }
        //Setup Drawer with Navigation Controller
        binding.materialSlider.setupWithNavController(navController)
    }
}