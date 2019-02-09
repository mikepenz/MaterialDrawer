package com.mikepenz.materialdrawer.app

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.mikepenz.crossfader.Crossfader
import com.mikepenz.crossfader.view.CrossFadeSlidingPaneLayout
import com.mikepenz.iconics.IconicsColor.Companion.colorInt
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.IconicsSize.Companion.dp
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.materialdrawer.*
import com.mikepenz.materialdrawer.app.utils.CrossfadeWrapper
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialize.util.UIUtils
import kotlinx.android.synthetic.main.activity_persistent_drawer.*

class PersistentDrawerActivity : AppCompatActivity() {

    //save our header or result
    private lateinit var headerResult: AccountHeader
    private lateinit var result: Drawer
    private lateinit var miniResult: MiniDrawer
    private lateinit var crossFader: Crossfader<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_persistent_drawer)

        //Remove line to test RTL support
        // getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        //example how to implement a persistentDrawer as shown in the google material design guidelines
        //https://material-design.storage.googleapis.com/publish/material_v_4/material_ext_publish/0Bx4BSt6jniD7YVdKQlF3TEo2S3M/patterns_navdrawer_behavior_persistent2.png
        //https://www.google.com/design/spec/patterns/navigation-drawer.html#navigation-drawer-behavior

        // Handle Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.drawer_item_persistent_compact_header)

        // Create a few sample profile
        val profile = ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(R.drawable.profile)
        val profile2 = ProfileDrawerItem().withName("Max Muster").withEmail("max.mustermann@gmail.com").withIcon(R.drawable.profile2)
        val profile3 = ProfileDrawerItem().withName("Felix House").withEmail("felix.house@gmail.com").withIcon(R.drawable.profile3)
        val profile4 = ProfileDrawerItem().withName("Mr. X").withEmail("mister.x.super@gmail.com").withIcon(R.drawable.profile4)
        val profile5 = ProfileDrawerItem().withName("Batman").withEmail("batman@gmail.com").withIcon(R.drawable.profile5)

        // Create the AccountHeader
        headerResult = AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(ColorDrawable(Color.parseColor("#FDFDFD")))
                .withHeightPx(UIUtils.getActionBarHeight(this))
                .withAccountHeader(R.layout.material_drawer_compact_persistent_header)
                .withTextColor(Color.BLACK)
                .addProfiles(
                        profile,
                        profile2,
                        profile3,
                        profile4,
                        profile5
                )
                .withSavedInstance(savedInstanceState)
                .build()

        //Create the drawer
        result = DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                        PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                        PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye),
                        SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog),
                        SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).withEnabled(false),
                        SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github),
                        SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_bullhorn)
                )
                .withGenerateMiniDrawer(true)
                .withSavedInstance(savedInstanceState)
                .buildView()

        // create the MiniDrawer and define the drawer and header to be used (it will automatically use the items from them)
        miniResult = result.miniDrawer!!.withIncludeSecondaryDrawerItems(true)

        //set the back arrow in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false)

        //get the widths in px for the first and second panel
        val firstWidth = com.mikepenz.crossfader.util.UIUtils.convertDpToPixel(300f, this).toInt()
        val secondWidth = com.mikepenz.crossfader.util.UIUtils.convertDpToPixel(72f, this).toInt()

        //create and build our crossfader (see the MiniDrawer is also builded in here, as the build method returns the view to be used in the crossfader)
        crossFader = Crossfader<CrossFadeSlidingPaneLayout>()
                .withContent(findViewById<View>(R.id.crossfade_content))
                .withFirst(result.slider, firstWidth)
                .withSecond(miniResult.build(this), secondWidth)
                .withSavedInstance(savedInstanceState)
                .build()

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(CrossfadeWrapper(crossFader))

        //define and create the arrow ;)
        val toggle = headerResult.view.findViewById<View>(R.id.material_drawer_account_header_toggle) as ImageView
        //for RTL you would have to define the other arrow
        toggle.setImageDrawable(IconicsDrawable(this, GoogleMaterial.Icon.gmd_chevron_left).size(dp(16)).color(colorInt(Color.BLACK)))
        toggle.setOnClickListener { crossFader.crossFade() }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //handle the click on the back arrow click
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
        if (result.isDrawerOpen) {
            result.closeDrawer()
        } else {
            super.onBackPressed()
        }
    }
}
