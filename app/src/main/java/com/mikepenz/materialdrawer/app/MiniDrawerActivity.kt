package com.mikepenz.materialdrawer.app

import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mikepenz.crossfader.Crossfader
import com.mikepenz.crossfader.util.UIUtils
import com.mikepenz.crossfader.view.CrossFadeSlidingPaneLayout
import com.mikepenz.iconics.IconicsColor.Companion.colorInt
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.materialdrawer.*
import com.mikepenz.materialdrawer.app.utils.CrossfadeWrapper
import com.mikepenz.materialdrawer.app.utils.SystemUtils
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import kotlinx.android.synthetic.main.activity_mini_drawer.*

class MiniDrawerActivity : AppCompatActivity() {

    //save our header or result
    private lateinit var headerResult: AccountHeader
    private lateinit var result: Drawer
    private lateinit var miniResult: MiniDrawer
    private lateinit var crossFader: Crossfader<*>

    private val onCheckedChangeListener = object : OnCheckedChangeListener {
        override fun onCheckedChanged(drawerItem: IDrawerItem<*>, buttonView: CompoundButton, isChecked: Boolean) {
            if (drawerItem is Nameable<*>) {
                Log.i("material-drawer", "DrawerItem: " + (drawerItem as Nameable<*>).name + " - toggleChecked: " + isChecked)
            } else {
                Log.i("material-drawer", "toggleChecked: $isChecked")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mini_drawer)

        // Handle Toolbar
        setSupportActionBar(toolbar)
        //set the back arrow in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.drawer_item_mini_drawer)

        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        val profile = ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460")
        val profile2 = ProfileDrawerItem().withName("Bernat Borras").withEmail("alorma@github.com").withIcon(Uri.parse("https://avatars3.githubusercontent.com/u/887462?v=3&s=460"))
        val profile3 = ProfileDrawerItem().withName("Max Muster").withEmail("max.mustermann@gmail.com").withIcon(resources.getDrawable(R.drawable.profile2))
        val profile4 = ProfileDrawerItem().withName("Felix House").withEmail("felix.house@gmail.com").withIcon(resources.getDrawable(R.drawable.profile3))
        val profile5 = ProfileDrawerItem().withName("Mr. X").withEmail("mister.x.super@gmail.com").withIcon(resources.getDrawable(R.drawable.profile4)).withIdentifier(4)
        val profile6 = ProfileDrawerItem().withName("Batman").withEmail("batman@gmail.com").withIcon(resources.getDrawable(R.drawable.profile5))

        // Create the AccountHeader
        headerResult = AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .addProfiles(
                        profile,
                        profile2,
                        profile3,
                        profile4,
                        profile5,
                        profile6,
                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                        ProfileSettingDrawerItem().withName("Add Account").withDescription("Add new GitHub Account").withIcon(GoogleMaterial.Icon.gmd_add).withIdentifier(PROFILE_SETTING.toLong()),
                        ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings)
                )
                .withOnAccountHeaderListener(object : AccountHeader.OnAccountHeaderListener {
                    override fun onProfileChanged(view: View?, profile: IProfile<*>, current: Boolean): Boolean {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile is IDrawerItem<*> && (profile as IDrawerItem<*>).identifier == PROFILE_SETTING.toLong()) {
                            val newProfile = ProfileDrawerItem().withNameShown(true).withName("Batman").withEmail("batman@gmail.com").withIcon(resources.getDrawable(R.drawable.profile5))
                            headerResult.profiles?.let {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, it.size - 2)
                            } ?: headerResult.addProfiles(newProfile)
                        }

                        //false if you have not consumed the event and it should close the drawer
                        return false
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build()

        result = DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withTranslucentStatusBar(false)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        PrimaryDrawerItem().withName(R.string.drawer_item_compact_header).withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(1),
                        PrimaryDrawerItem().withName(R.string.drawer_item_action_bar_drawer).withIcon(FontAwesome.Icon.faw_home).withBadge("22").withBadgeStyle(BadgeStyle(Color.RED, Color.RED)).withIdentifier(2).withSelectable(false),
                        PrimaryDrawerItem().withName(R.string.drawer_item_multi_drawer).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(3),
                        PrimaryDrawerItem().withName(R.string.drawer_item_non_translucent_status_drawer).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(4),
                        PrimaryDrawerItem().withDescription("A more complex sample").withName(R.string.drawer_item_advanced_drawer).withIcon(GoogleMaterial.Icon.gmd_adb).withIdentifier(5),
                        SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github),
                        SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(GoogleMaterial.Icon.gmd_format_color_fill).withTag("Bullhorn"),
                        DividerDrawerItem(),
                        SwitchDrawerItem().withName("Switch").withIcon(GoogleMaterial.Icon.gmd_pan_tool).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                        ToggleDrawerItem().withName("Toggle").withIcon(GoogleMaterial.Icon.gmd_pan_tool).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                    override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                        if (drawerItem is Nameable<*>) {
                            Toast.makeText(this@MiniDrawerActivity, drawerItem.name?.getText(this@MiniDrawerActivity), Toast.LENGTH_SHORT).show()
                        }
                        return false
                    }
                })
                .withGenerateMiniDrawer(true)
                .withSavedInstance(savedInstanceState)
                // build only the view of the Drawer (don't inflate it automatically in our layout which is done with .build())
                .buildView()


        //the MiniDrawer is managed by the Drawer and we just get it to hook it into the Crossfader
        miniResult = result.miniDrawer!!

        //get the widths in px for the first and second panel
        val firstWidth = UIUtils.convertDpToPixel(300f, this).toInt()
        val secondWidth = UIUtils.convertDpToPixel(72f, this).toInt()

        //create and build our crossfader (see the MiniDrawer is also builded in here, as the build method returns the view to be used in the crossfader)
        //the crossfader library can be found here: https://github.com/mikepenz/Crossfader
        crossFader = Crossfader<CrossFadeSlidingPaneLayout>()
                .withContent(findViewById<View>(R.id.crossfade_content))
                .withFirst(result.slider, firstWidth)
                .withSecond(miniResult.build(this), secondWidth)
                .withSavedInstance(savedInstanceState)
                .build()

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(CrossfadeWrapper(crossFader))

        //define a shadow (this is only for normal LTR layouts if you have a RTL app you need to define the other one
        crossFader.getCrossFadeSlidingPaneLayout().setShadowResourceLeft(R.drawable.material_drawer_shadow_left)
    }

    override fun onSaveInstanceState(_outState: Bundle?) {
        var outState = _outState
        //add the values which need to be saved from the drawer to the bundle
        if (::result.isInitialized) {
            outState = result.saveInstanceState(outState)
        }
        //add the values which need to be saved from the accountHeader to the bundle
        if (::headerResult.isInitialized) {
            outState = headerResult.saveInstanceState(outState)
        }
        //add the values which need to be saved from the crossFader to the bundle
        if (::crossFader.isInitialized) {
            outState = crossFader.saveInstanceState(outState)
        }
        super.onSaveInstanceState(outState)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        if (SystemUtils.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            inflater.inflate(R.menu.embedded, menu)
            menu.findItem(R.id.menu_1).icon = IconicsDrawable(this, GoogleMaterial.Icon.gmd_sort).color(colorInt(Color.WHITE)).actionBar()
        }
        return true
    }

    override fun onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (crossFader.isCrossFaded()) {
            crossFader.crossFade()
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //handle the click on the back arrow click
        return when (item.itemId) {
            R.id.menu_1 -> {
                crossFader?.crossFade()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val PROFILE_SETTING = 1
    }
}
