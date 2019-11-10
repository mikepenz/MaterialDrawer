package com.mikepenz.materialdrawer.app

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ListPopupWindow
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.materialdrawer.interfaces.ICrossfader
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import com.mikepenz.materialdrawer.util.DrawerUIUtils
import com.mikepenz.materialdrawer.widget.AccountHeaderView
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView
import com.mikepenz.materialdrawer.widget.MiniDrawerSliderView
import com.mikepenz.materialize.util.UIUtils
import kotlinx.android.synthetic.main.activity_sample_crossfader.*

class CrossfadeDrawerLayoutActvitiy : AppCompatActivity() {
    private lateinit var slider: MaterialDrawerSliderView
    private lateinit var miniSliderView: MiniDrawerSliderView
    private lateinit var headerView: AccountHeaderView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_crossfader)

        // Handle Toolbar
        setSupportActionBar(toolbar)
        //set the back arrow in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.drawer_item_crossfade_drawer_layout_drawer)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, root, toolbar, com.mikepenz.materialdrawer.R.string.material_drawer_open, com.mikepenz.materialdrawer.R.string.material_drawer_close)

        slider = MaterialDrawerSliderView(this)
        slider.id = R.id.slider
        slider.fitsSystemWindows = true
        val lp = DrawerLayout.LayoutParams(UIUtils.convertDpToPixel(72f, this).toInt(), ListPopupWindow.MATCH_PARENT)
        lp.gravity = GravityCompat.START
        slider.layoutParams = lp
        root.addView(slider, 1, lp)

        // Create a few sample profile
        val profile = ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(R.drawable.profile)
        val profile2 = ProfileDrawerItem().withName("Max Muster").withEmail("max.mustermann@gmail.com").withIcon(R.drawable.profile2)
        val profile3 = ProfileDrawerItem().withName("Felix House").withEmail("felix.house@gmail.com").withIcon(R.drawable.profile3)
        val profile4 = ProfileDrawerItem().withName("Mr. X").withEmail("mister.x.super@gmail.com").withIcon(R.drawable.profile4)
        val profile5 = ProfileDrawerItem().withName("Batman").withEmail("batman@gmail.com").withIcon(R.drawable.profile5)

        // Create the AccountHeader
        headerView = AccountHeaderView(this).apply {
            attachToSliderView(slider)
            addProfiles(
                    profile,
                    profile2,
                    profile3,
                    profile4,
                    profile5
            )
            withSavedInstance(savedInstanceState)
        }

        slider.apply {
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
                if (drawerItem is Nameable<*>) {
                    Toast.makeText(this@CrossfadeDrawerLayoutActvitiy, drawerItem.name?.getText(this@CrossfadeDrawerLayoutActvitiy), Toast.LENGTH_SHORT).show()
                }
                false
            }
            withSavedInstance(savedInstanceState)
        }

        miniSliderView = MiniDrawerSliderView(this).apply {
            drawer = slider
        }

        //define maxDrawerWidth
        root.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(this))
        //add second view (which is the miniDrawer)
        miniSliderView.background = slider.background
        //we do not have the MiniDrawer view during CrossfadeDrawerLayout creation so we will add it here
        root.smallView.addView(miniSliderView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniSliderView.crossFader = object : ICrossfader {

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


        /**
         * NOTE THIS IS A HIGHLY CUSTOM ANIMATION. USE CAREFULLY.
         * this animate the height of the profile to the height of the AccountHeader and
         * animates the height of the drawerItems to the normal drawerItems so the difference between Mini and normal Drawer is eliminated
         */
        /*
        final double headerHeight = DrawerUIUtils.getOptimalDrawerWidth(this) * 9d / 16d;
        final double originalProfileHeight = UIUtils.convertDpToPixel(72, this);
        final double headerDifference = headerHeight - originalProfileHeight;
        final double originalItemHeight = UIUtils.convertDpToPixel(64, this);
        final double normalItemHeight = UIUtils.convertDpToPixel(48, this);
        final double itemDifference = originalItemHeight - normalItemHeight;
        crossfadeDrawerLayout.withCrossfadeListener(new CrossfadeDrawerLayout.CrossfadeListener() {
            @Override
            public void onCrossfade(View containerView, float currentSlidePercentage, int slideOffset) {
                for (int i = 0; i < miniResult.getAdapter().getItemCount(); i++) {
                    IDrawerItem drawerItem = miniResult.getAdapter().getItem(i);
                    if (drawerItem instanceof MiniProfileDrawerItem) {
                        MiniProfileDrawerItem mpdi = (MiniProfileDrawerItem) drawerItem;
                        mpdi.withCustomHeightPx((int) (originalProfileHeight + (headerDifference * currentSlidePercentage / 100)));
                    } else if (drawerItem instanceof MiniDrawerItem) {
                        MiniDrawerItem mdi = (MiniDrawerItem) drawerItem;
                        mdi.withCustomHeightPx((int) (originalItemHeight - (itemDifference * currentSlidePercentage / 100)));
                    }
                }

                miniResult.getAdapter().notifyDataSetChanged();
            }
        });
        */

        // set the selection to the item with the identifier 5
        if (savedInstanceState == null) {
            slider.setSelection(5, false)
        }
    }


    override fun onSaveInstanceState(_outState: Bundle) {
        var outState = _outState
        //add the values which need to be saved from the drawer to the bundle
        outState = slider.saveInstanceState(outState)

        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerView.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }


    override fun onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (root.isDrawerOpen(slider.parent as View)) {
            root.closeDrawer(slider.parent as View)
        } else {
            super.onBackPressed()
        }
    }
}