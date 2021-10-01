package com.mikepenz.materialdrawer.app

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import com.mikepenz.crossfader.Crossfader
import com.mikepenz.crossfader.util.UIUtils
import com.mikepenz.crossfader.view.CrossFadeSlidingPaneLayout
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesomeBrand
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.iconics.utils.colorInt
import com.mikepenz.iconics.utils.sizeDp
import com.mikepenz.materialdrawer.app.databinding.ActivityPersistentDrawerBinding
import com.mikepenz.materialdrawer.app.utils.CrossfadeWrapper
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.iconics.withIcon
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.*
import com.mikepenz.materialdrawer.widget.AccountHeaderView
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView
import com.mikepenz.materialdrawer.widget.MiniDrawerSliderView

class PersistentDrawerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersistentDrawerBinding

    //save our header or result
    private lateinit var headerView: AccountHeaderView
    private lateinit var miniSliderView: MiniDrawerSliderView
    private lateinit var sliderView: MaterialDrawerSliderView
    private lateinit var crossFader: Crossfader<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersistentDrawerBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        //Remove line to test RTL support
        // getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        //example how to implement a persistentDrawer as shown in the google material design guidelines
        //https://material-design.storage.googleapis.com/publish/material_v_4/material_ext_publish/0Bx4BSt6jniD7YVdKQlF3TEo2S3M/patterns_navdrawer_behavior_persistent2.png
        //https://www.google.com/design/spec/patterns/navigation-drawer.html#navigation-drawer-behavior

        // Handle Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setTitle(R.string.drawer_item_persistent_compact_header)

        // Create a few sample profile
        val profile = ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(R.drawable.profile)
        val profile2 = ProfileDrawerItem().withName("Max Muster").withEmail("max.mustermann@gmail.com").withIcon(R.drawable.profile2)
        val profile3 = ProfileDrawerItem().withName("Felix House").withEmail("felix.house@gmail.com").withIcon(R.drawable.profile3)
        val profile4 = ProfileDrawerItem().withName("Mr. X").withEmail("mister.x.super@gmail.com").withIcon(R.drawable.profile4)
        val profile5 = ProfileDrawerItem().withName("Batman").withEmail("batman@gmail.com").withIcon(R.drawable.profile5)

        // Create the AccountHeader
        headerView = AccountHeaderView(this).apply {
            addProfiles(
                    profile,
                    profile2,
                    profile3,
                    profile4,
                    profile5
            )
            headerBackground = ImageHolder(ColorDrawable(Color.parseColor("#FDFDFD")))
        }
        // UIUtils.getActionBarHeight(this))
        //.withAccountHeader(R.layout.material_drawer_compact_persistent_header)

        //Create the drawer
        sliderView = MaterialDrawerSliderView(this).apply {
            accountHeader = this@PersistentDrawerActivity.headerView
            customWidth = ViewGroup.LayoutParams.MATCH_PARENT
            itemAdapter.add(
                PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye),
                SectionDrawerItem().withName(R.string.drawer_item_section_header),
                SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog),
                SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).withEnabled(false),
                SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesomeBrand.Icon.fab_github),
                SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_bullhorn)
            )
        }

        // create the MiniDrawer and define the drawer and header to be used (it will automatically use the items from them)
        miniSliderView = MiniDrawerSliderView(this).apply {
            includeSecondaryDrawerItems = true
            drawer = sliderView
        }

        //set the back arrow in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false)

        //get the widths in px for the first and second panel
        val firstWidth = UIUtils.convertDpToPixel(300f, this).toInt()
        val secondWidth = UIUtils.convertDpToPixel(72f, this).toInt()

        //create and build our crossfader (see the MiniDrawer is also builded in here, as the build method returns the view to be used in the crossfader)
        crossFader = Crossfader<CrossFadeSlidingPaneLayout>()
                .withContent(findViewById<View>(R.id.crossfade_content))
                .withFirst(sliderView as View, firstWidth)
                .withSecond(miniSliderView, secondWidth)
                .withSavedInstance(savedInstanceState)
                .build()

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniSliderView.crossFader = CrossfadeWrapper(crossFader)

        //define and create the arrow ;)
        val toggle = headerView.findViewById<ImageView>(R.id.material_drawer_account_header_toggle)
        //for RTL you would have to define the other arrow
        toggle.setImageDrawable(IconicsDrawable(this, GoogleMaterial.Icon.gmd_chevron_left).apply { sizeDp = 16; colorInt = Color.BLACK })
        toggle.setOnClickListener { crossFader.crossFade() }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            binding.toolbar.updatePadding(top = insets.systemWindowInsetTop)
            insets
        }
    }

    override fun onSaveInstanceState(_outState: Bundle) {
        var outState = _outState
        //add the values, which need to be saved from the drawer to the bundle
        if (::sliderView.isInitialized) {
            outState = sliderView.saveInstanceState(outState)
        }
        //add the values, which need to be saved from the accountHeader to the bundle
        if (::headerView.isInitialized) {
            outState = headerView.saveInstanceState(outState)
        }
        //add the values, which need to be saved from the crossFader to the bundle
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
        if (crossFader.isCrossFaded) {
            crossFader.crossFade()
        } else {
            super.onBackPressed()
        }
    }
}
