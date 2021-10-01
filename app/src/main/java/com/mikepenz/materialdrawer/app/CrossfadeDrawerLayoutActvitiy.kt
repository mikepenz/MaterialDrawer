package com.mikepenz.materialdrawer.app

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesomeBrand
import com.mikepenz.materialdrawer.app.databinding.ActivitySampleCrossfaderBinding
import com.mikepenz.materialdrawer.iconics.iconicsIcon
import com.mikepenz.materialdrawer.interfaces.ICrossfader
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.*
import com.mikepenz.materialdrawer.util.getOptimalDrawerWidth
import com.mikepenz.materialdrawer.widget.AccountHeaderView

class CrossfadeDrawerLayoutActvitiy : AppCompatActivity() {
    private lateinit var binding: ActivitySampleCrossfaderBinding
    private lateinit var headerView: AccountHeaderView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySampleCrossfaderBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        // Handle Toolbar
        setSupportActionBar(binding.toolbar)
        //set the back arrow in the toolbars
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setTitle(R.string.drawer_item_crossfade_drawer_layout_drawer)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.root, binding.toolbar, com.mikepenz.materialdrawer.R.string.material_drawer_open, com.mikepenz.materialdrawer.R.string.material_drawer_close)

        // Create a few sample profile
        val profile = ProfileDrawerItem().apply { nameText = "Mike Penz"; descriptionText = "mikepenz@gmail.com"; iconRes = R.drawable.profile }
        val profile2 = ProfileDrawerItem().apply { nameText = "Max Muster"; descriptionText = "max.mustermann@gmail.com"; iconRes = R.drawable.profile2 }
        val profile3 = ProfileDrawerItem().apply { nameText = "Felix House"; descriptionText = "felix.house@gmail.com"; iconRes = R.drawable.profile3 }
        val profile4 = ProfileDrawerItem().apply { nameText = "Mr. X"; descriptionText = "mister.x.super@gmail.com"; iconRes = R.drawable.profile4 }
        val profile5 = ProfileDrawerItem().apply { nameText = "Batman"; descriptionText = "batman@gmail.com"; iconRes = R.drawable.profile5 }

        // Create the AccountHeader
        headerView = AccountHeaderView(this).apply {
            attachToSliderView(binding.crossFadeLargeView)
            addProfiles(
                    profile,
                    profile2,
                    profile3,
                    profile4,
                    profile5
            )
            withSavedInstance(savedInstanceState)
        }

        binding.crossFadeLargeView.apply {
            itemAdapter.add(
                PrimaryDrawerItem().apply { nameRes = R.string.drawer_item_home; iconicsIcon = FontAwesome.Icon.faw_home; identifier = 1 },
                PrimaryDrawerItem().apply { nameRes = R.string.drawer_item_free_play; iconicsIcon = FontAwesome.Icon.faw_gamepad },
                PrimaryDrawerItem().apply { nameRes = R.string.drawer_item_custom; iconicsIcon = FontAwesome.Icon.faw_eye; identifier = 5 },
                SectionDrawerItem().apply { nameRes = R.string.drawer_item_section_header },
                SecondaryDrawerItem().apply { nameRes = R.string.drawer_item_settings; iconicsIcon = FontAwesome.Icon.faw_cog },
                SecondaryDrawerItem().apply { nameRes = R.string.drawer_item_help; iconicsIcon = FontAwesome.Icon.faw_question; isEnabled = false },
                SecondaryDrawerItem().apply { nameRes = R.string.drawer_item_open_source; iconicsIcon = FontAwesomeBrand.Icon.fab_github },
                SecondaryDrawerItem().apply { nameRes = R.string.drawer_item_contact; iconicsIcon = FontAwesome.Icon.faw_bullhorn }
            )
            onDrawerItemClickListener = { v, drawerItem, position ->
                if (drawerItem is Nameable) {
                    Toast.makeText(this@CrossfadeDrawerLayoutActvitiy, drawerItem.name?.getText(this@CrossfadeDrawerLayoutActvitiy), Toast.LENGTH_SHORT).show()
                }
                false
            }
            setSavedInstance(savedInstanceState)
        }

        binding.crossFadeSmallView.drawer = binding.crossFadeLargeView

        //define maxDrawerWidth
        binding.root.maxWidthPx = getOptimalDrawerWidth(this)
        binding.crossFadeSmallView.background = binding.crossFadeLargeView.background

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        binding.crossFadeSmallView.crossFader = object : ICrossfader {
            override val isCrossfaded: Boolean
                get() = binding.root.isCrossfaded

            override fun crossfade() {
                val isFaded = isCrossfaded
                binding.root.crossfade(400)

                //only close the drawer if we were already faded and want to close it now
                if (isFaded) {
                    binding.root.closeDrawer(GravityCompat.START)
                }
            }
        }

        // set the selection to the item with the identifier 5
        if (savedInstanceState == null) {
            binding.crossFadeLargeView.setSelection(5, false)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        actionBarDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarDrawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(_outState: Bundle) {
        var outState = _outState
        //add the values which need to be saved from the drawer to the bundle
        outState = binding.crossFadeLargeView.saveInstanceState(outState)

        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerView.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }


    override fun onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (binding.root.isDrawerOpen(binding.crossFadeSlider)) {
            binding.root.closeDrawer(binding.crossFadeSlider)
        } else {
            super.onBackPressed()
        }
    }
}