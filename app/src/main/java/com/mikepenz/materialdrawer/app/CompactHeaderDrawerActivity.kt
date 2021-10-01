package com.mikepenz.materialdrawer.app

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesomeBrand
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.iconics.utils.actionBar
import com.mikepenz.iconics.utils.paddingDp
import com.mikepenz.materialdrawer.app.databinding.ActivitySampleBinding
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.holder.ColorHolder
import com.mikepenz.materialdrawer.iconics.iconicsIcon
import com.mikepenz.materialdrawer.iconics.withIcon
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.*
import com.mikepenz.materialdrawer.widget.AccountHeaderView

class CompactHeaderDrawerActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySampleBinding

    //save our header or result
    private lateinit var headerView: AccountHeaderView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySampleBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        // Handle Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setTitle(R.string.drawer_item_compact_header)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.root, binding.toolbar, com.mikepenz.materialdrawer.R.string.material_drawer_open, com.mikepenz.materialdrawer.R.string.material_drawer_close)

        // Create a few sample profile
        val profile = ProfileDrawerItem().apply {
            nameText = "Mike Penz"; descriptionText = "mikepenz@gmail.com"; iconRes = R.drawable.profile; badgeText = "123"
            badgeStyle = BadgeStyle().apply {
                textColor = ColorHolder.fromColor(Color.WHITE)
                color = ColorHolder.fromColorRes(R.color.colorAccent)
            }
        }
        val profile2 = ProfileDrawerItem().apply { nameText = "Max Muster"; descriptionText = "max.mustermann@gmail.com"; iconRes = R.drawable.profile2 }
        val profile3 = ProfileDrawerItem().apply { nameText = "Felix House"; descriptionText = "felix.house@gmail.com"; iconRes = R.drawable.profile3 }
        val profile4 = ProfileDrawerItem().apply { nameText = "Mr. X"; descriptionText = "mister.x.super@gmail.com"; iconRes = R.drawable.profile4 }
        val profile5 = ProfileDrawerItem().apply { nameText = "Batman"; descriptionText = "batman@gmail.com"; iconRes = R.drawable.profile5 }

        // Create the AccountHeader
        headerView = AccountHeaderView(this).apply {
            attachToSliderView(binding.slider)
            addProfiles(
                    profile,
                    profile2,
                    profile3,
                    profile4,
                    profile5,
                    //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                    ProfileSettingDrawerItem().withName("Add Account").withDescription("Add new GitHub Account").withIcon(IconicsDrawable(context, GoogleMaterial.Icon.gmd_add).apply { actionBar(); paddingDp = 5 }).withIconTinted(true).withIdentifier(PROFILE_SETTING.toLong()),
                    ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(100001)
            )
            withSavedInstance(savedInstanceState)
        }

        binding.slider.apply {
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
            onDrawerItemClickListener = { _, drawerItem, _ ->
                if (drawerItem.identifier == 1L) {
                    startSupportActionMode(ActionBarCallBack())
                }
                if (drawerItem is Nameable) {
                    binding.toolbar.title = drawerItem.name?.getText(this@CompactHeaderDrawerActivity)
                }
                false
            }
            setSavedInstance(savedInstanceState)
        }

        // set the selection to the item with the identifier 5
        if (savedInstanceState == null) {
            binding.slider.setSelection(5, false)
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

    internal inner class ActionBarCallBack : ActionMode.Callback {

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return false
        }

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = getThemeColor(R.attr.colorPrimaryDark, ContextCompat.getColor(this@CompactHeaderDrawerActivity, R.color.colorPrimaryDark))
            }

            mode.menuInflater.inflate(R.menu.cab, menu)
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = Color.TRANSPARENT
            }
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        private fun Context.getThemeColor(@AttrRes attr: Int, @ColorInt def: Int = 0): Int {
            val tv = TypedValue()
            return if (theme.resolveAttribute(attr, tv, true)) {
                if (tv.resourceId != 0) ResourcesCompat.getColor(resources, tv.resourceId, theme) else tv.data
            } else def
        }
    }

    companion object {
        private const val PROFILE_SETTING = 1
    }
}
