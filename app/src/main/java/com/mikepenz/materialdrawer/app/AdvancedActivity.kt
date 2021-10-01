package com.mikepenz.materialdrawer.app

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesomeBrand
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.iconics.utils.actionBar
import com.mikepenz.iconics.utils.backgroundColorRes
import com.mikepenz.iconics.utils.paddingDp
import com.mikepenz.iconics.utils.sizeDp
import com.mikepenz.materialdrawer.app.databinding.ActivitySampleBinding
import com.mikepenz.materialdrawer.app.drawerItems.CustomPrimaryDrawerItem
import com.mikepenz.materialdrawer.app.drawerItems.CustomUrlPrimaryDrawerItem
import com.mikepenz.materialdrawer.app.drawerItems.OverflowMenuDrawerItem
import com.mikepenz.materialdrawer.holder.ColorHolder
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.iconics.iconicsIcon
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.*
import com.mikepenz.materialdrawer.util.addStickyDrawerItems
import com.mikepenz.materialdrawer.widget.AccountHeaderView

class AdvancedActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySampleBinding

    private lateinit var headerView: AccountHeaderView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private lateinit var profile: IProfile
    private lateinit var profile2: IProfile
    private lateinit var profile3: IProfile
    private lateinit var profile4: IProfile
    private lateinit var profile5: IProfile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySampleBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        // Handle Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setTitle(R.string.drawer_item_advanced_drawer)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.root, binding.toolbar, com.mikepenz.materialdrawer.R.string.material_drawer_open, com.mikepenz.materialdrawer.R.string.material_drawer_close)

        // Create a few sample profile
        profile = ProfileDrawerItem().apply { nameText = "Mike Penz"; descriptionText = "mikepenz@gmail.com"; iconRes = R.drawable.profile }
        profile2 = ProfileDrawerItem().apply { nameText = "Max Muster"; descriptionText = "max.mustermann@gmail.com"; iconRes = R.drawable.profile2; identifier = 2 }
        profile3 = ProfileDrawerItem().apply { nameText = "Felix House"; descriptionText = "felix.house@gmail.com"; iconRes = R.drawable.profile3 }
        profile4 = ProfileDrawerItem().apply { nameText = "Mr. X"; descriptionText = "mister.x.super@gmail.com"; iconRes = R.drawable.profile4; identifier = 4 }
        profile5 = ProfileDrawerItem().apply { nameText = "Batman"; descriptionText = "batman@gmail.com"; iconRes = R.drawable.profile5 }

        // Create the AccountHeader
        buildHeader(false, savedInstanceState)

        binding.slider.apply {
            itemAdapter.add(
                    PrimaryDrawerItem().apply { nameRes = R.string.drawer_item_home; iconicsIcon = FontAwesome.Icon.faw_home },
                    //here we use a customPrimaryDrawerItem we defined in our sample app
                    //this custom DrawerItem extends the PrimaryDrawerItem so it just overwrites some methods
                    OverflowMenuDrawerItem().apply {
                        nameRes = R.string.drawer_item_menu_drawer_item; descriptionRes = R.string.drawer_item_menu_drawer_item_desc; menu = R.menu.fragment_menu
                        onMenuItemClickListener = PopupMenu.OnMenuItemClickListener { item ->
                            Toast.makeText(this@AdvancedActivity, item.title, Toast.LENGTH_SHORT).show()
                            false
                        }
                        iconicsIcon = GoogleMaterial.Icon.gmd_filter_center_focus
                    },
                CustomPrimaryDrawerItem().apply {
                    nameRes = R.string.drawer_item_free_play; iconicsIcon = FontAwesome.Icon.faw_gamepad; background =
                    ColorHolder.fromColorRes(R.color.colorAccent)
                },
                PrimaryDrawerItem().apply {
                    nameRes = R.string.drawer_item_custom; descriptionText = "This is a description"; iconicsIcon = FontAwesome.Icon.faw_eye
                },
                CustomUrlPrimaryDrawerItem().apply {
                    nameRes = R.string.drawer_item_fragment_drawer; description = StringHolder(R.string.drawer_item_fragment_drawer_desc); iconUrl =
                    "https://avatars3.githubusercontent.com/u/1476232?v=3&s=460"
                },
                SectionDrawerItem().apply { nameRes = R.string.drawer_item_section_header },
                SecondaryDrawerItem().apply { nameRes = R.string.drawer_item_settings; iconicsIcon = FontAwesome.Icon.faw_cart_plus },
                SecondaryDrawerItem().apply { nameRes = R.string.drawer_item_help; iconicsIcon = FontAwesome.Icon.faw_database; isEnabled = false },
                SecondaryDrawerItem().apply { nameRes = R.string.drawer_item_open_source; iconicsIcon = FontAwesomeBrand.Icon.fab_github },
                SecondaryDrawerItem().apply {
                    nameRes = R.string.drawer_item_contact; tag = "Bullhorn"; iconDrawable =
                    IconicsDrawable(this@AdvancedActivity, GoogleMaterial.Icon.gmd_add).apply { actionBar(); paddingDp = 5 }; isIconTinted = true
                },
                SecondaryDrawerItem().apply { nameRes = R.string.drawer_item_help; iconicsIcon = FontAwesome.Icon.faw_question; isEnabled = false }
            )
            addStickyDrawerItems(
                    SecondaryDrawerItem().apply { nameRes = R.string.drawer_item_settings; iconicsIcon = FontAwesome.Icon.faw_cog; identifier = 10 },
                SecondaryDrawerItem().apply { nameRes = R.string.drawer_item_open_source; iconicsIcon = FontAwesomeBrand.Icon.fab_github }
            )
            onDrawerItemClickListener = { _, drawerItem, _ ->
                if (drawerItem is Nameable) {
                    Toast.makeText(this@AdvancedActivity, drawerItem.name?.getText(this@AdvancedActivity), Toast.LENGTH_SHORT).show()
                }
                false
            }
            setSavedInstance(savedInstanceState)
        }
    }

    /**
     * small helper method to reuse the logic to build the AccountHeader
     * this will be used to replace the header of the drawer with a compact/normal header
     *
     * @param compact
     * @param savedInstanceState
     */
    private fun buildHeader(compact: Boolean, savedInstanceState: Bundle?) {
        // Create the AccountHeader
        headerView = AccountHeaderView(this, compact = compact).apply {
            attachToSliderView(binding.slider)
            headerBackground = ImageHolder(R.drawable.header)
            addProfiles(
                    profile,
                    profile2,
                    profile3,
                    profile4,
                    profile5,
                    //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                    ProfileSettingDrawerItem().apply { nameText = "Add Account"; descriptionText = "Add new GitHub Account"; iconDrawable = IconicsDrawable(this@AdvancedActivity, GoogleMaterial.Icon.gmd_add).apply { actionBar(); paddingDp = 5 }; identifier = PROFILE_SETTING.toLong() },
                    ProfileSettingDrawerItem().apply { nameText = "Manage Account"; iconicsIcon = GoogleMaterial.Icon.gmd_settings }
            )
            onAccountHeaderListener = { _, profile, _ ->
                //sample usage of the onProfileChanged listener
                //if the clicked item has the identifier 1 add a new profile ;)
                if (profile is IDrawerItem<*> && (profile as IDrawerItem<*>).identifier == PROFILE_SETTING.toLong()) {
                    val newProfile = ProfileDrawerItem().apply { nameText = "Batman"; descriptionText = "batman@gmail.com"; iconRes = R.drawable.profile5; isNameShown = true }
                    val profiles = headerView.profiles
                    if (profiles != null) {
                        //we know that there are 2 setting elements. set the new profile above them ;)
                        headerView.addProfile(newProfile, profiles.size - 2)
                    } else {
                        headerView.addProfiles(newProfile)
                    }
                }

                //false if you have not consumed the event and it should close the drawer
                false
            }
            withSavedInstance(savedInstanceState)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.menu_1 -> {
                //update the profile2 and set a new image.
                profile2.iconDrawable = IconicsDrawable(this, GoogleMaterial.Icon.gmd_android).apply { backgroundColorRes = R.color.colorAccent; paddingDp = 4; sizeDp = 48 }
                headerView.updateProfile(profile2)
                return true
            }
            R.id.menu_4 -> {
                //we want to replace our current header with a compact header
                //build the new compact header
                buildHeader(true, null)
                return true
            }
            R.id.menu_5 -> {
                //we want to replace our current header with a normal header
                //build the new compact header
                buildHeader(false, null)
                binding.slider.invalidate()
                return true
            }
            else -> {
                return actionBarDrawerToggle.onOptionsItemSelected(item)
            }
        }
    }

    override fun onSaveInstanceState(_outState: Bundle) {
        var outState = _outState
        //add the values which need to be saved from the drawer to the bundle
        outState = binding.slider.saveInstanceState(outState)

        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerView.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (binding.root.isDrawerOpen(binding.slider)) {
            binding.root.closeDrawer(binding.slider)
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val PROFILE_SETTING = 1
    }
}
