package com.mikepenz.materialdrawer.app

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.materialdrawer.iconics.withIcon
import com.mikepenz.materialdrawer.interfaces.ICrossfader
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.*
import com.mikepenz.materialdrawer.util.getOptimalDrawerWidth
import com.mikepenz.materialdrawer.widget.AccountHeaderView
import kotlinx.android.synthetic.main.activity_sample_crossfader.*

class CrossfadeDrawerLayoutActvitiy : AppCompatActivity() {
    private lateinit var headerView: AccountHeaderView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_crossfader)

        // Handle Toolbar
        setSupportActionBar(toolbar)
        //set the back arrow in the toolbars
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setTitle(R.string.drawer_item_crossfade_drawer_layout_drawer)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, root, toolbar, com.mikepenz.materialdrawer.R.string.material_drawer_open, com.mikepenz.materialdrawer.R.string.material_drawer_close)

        // Create a few sample profile
        val profile = ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(R.drawable.profile)
        val profile2 = ProfileDrawerItem().withName("Max Muster").withEmail("max.mustermann@gmail.com").withIcon(R.drawable.profile2)
        val profile3 = ProfileDrawerItem().withName("Felix House").withEmail("felix.house@gmail.com").withIcon(R.drawable.profile3)
        val profile4 = ProfileDrawerItem().withName("Mr. X").withEmail("mister.x.super@gmail.com").withIcon(R.drawable.profile4)
        val profile5 = ProfileDrawerItem().withName("Batman").withEmail("batman@gmail.com").withIcon(R.drawable.profile5)

        // Create the AccountHeader
        headerView = AccountHeaderView(this).apply {
            attachToSliderView(crossFadeLargeView)
            addProfiles(
                    profile,
                    profile2,
                    profile3,
                    profile4,
                    profile5
            )
            withSavedInstance(savedInstanceState)
        }

        crossFadeLargeView.apply {
            itemAdapter.add(
                    PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                    PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                    PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(5),
                    SectionDrawerItem().withName(R.string.drawer_item_section_header),
                    PrimaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog),
                    PrimaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).withEnabled(false),
                    PrimaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github),
                    PrimaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_bullhorn)
            )
            onDrawerItemClickListener = { v, drawerItem, position ->
                if (drawerItem is Nameable) {
                    Toast.makeText(this@CrossfadeDrawerLayoutActvitiy, drawerItem.name?.getText(this@CrossfadeDrawerLayoutActvitiy), Toast.LENGTH_SHORT).show()
                }
                false
            }
            setSavedInstance(savedInstanceState)
        }

        crossFadeSmallView.drawer = crossFadeLargeView

        //define maxDrawerWidth
        root.maxWidthPx = getOptimalDrawerWidth(this)
        crossFadeSmallView.background = crossFadeLargeView.background

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        crossFadeSmallView.crossFader = object : ICrossfader {

            override val isCrossfaded: Boolean
                get() = root.isCrossfaded

            override fun crossfade() {
                val isFaded = isCrossfaded
                root.crossfade(400)

                //only close the drawer if we were already faded and want to close it now
                if (isFaded) {
                    root.closeDrawer(GravityCompat.START)
                }
            }
        }

        // set the selection to the item with the identifier 5
        if (savedInstanceState == null) {
            crossFadeLargeView.setSelection(5, false)
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
        outState = crossFadeLargeView.saveInstanceState(outState)

        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerView.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }


    override fun onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (root.isDrawerOpen(crossFadeSlider)) {
            root.closeDrawer(crossFadeSlider)
        } else {
            super.onBackPressed()
        }
    }
}