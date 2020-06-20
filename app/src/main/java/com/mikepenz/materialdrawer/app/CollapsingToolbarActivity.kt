package com.mikepenz.materialdrawer.app

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.centerCropTransform
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.iconics.utils.actionBar
import com.mikepenz.iconics.utils.colorInt
import com.mikepenz.materialdrawer.app.utils.convertDpToPixel
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.iconics.withIcon
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.withEnabled
import com.mikepenz.materialdrawer.model.interfaces.withIdentifier
import com.mikepenz.materialdrawer.model.interfaces.withName
import com.mikepenz.materialdrawer.util.removeAllItems
import com.mikepenz.materialdrawer.widget.AccountHeaderView
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView
import kotlinx.android.synthetic.main.activity_sample_collapsing_toolbar.*

class CollapsingToolbarActivity : AppCompatActivity() {

    private lateinit var headerView: AccountHeaderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_collapsing_toolbar)

        setSupportActionBar(toolbar)
        collapsingToolbar.title = getString(R.string.drawer_item_collapsing_toolbar_drawer)

        material_drawer_swipe_refresh.setProgressViewOffset(true, 0, convertDpToPixel(48f, this).toInt())
        material_drawer_swipe_refresh.setOnRefreshListener {
            material_drawer_swipe_refresh.postDelayed({
                slider.setDrawerItems()
                slider.setSelection(5, false)
                material_drawer_swipe_refresh.isRefreshing = false
            }, 3000)
        }

        // Create the AccountHeader
        headerView = AccountHeaderView(this).apply {
            attachToSliderView(slider)
            headerBackground = ImageHolder(R.drawable.header)
            withSavedInstance(savedInstanceState)
        }

        slider.apply {
            setDrawerItems()
            setSelection(1, false)
            setSavedInstance(savedInstanceState)
        }

        fillFab()
        loadBackdrop()
    }

    private fun MaterialDrawerSliderView.setDrawerItems() {
        removeAllItems()
        itemAdapter.add(
                PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(5),
                SectionDrawerItem().withName(R.string.drawer_item_section_header),
                SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog),
                SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).withEnabled(false),
                SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github),
                SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_bullhorn)
        )
    }

    private fun loadBackdrop() {
        Glide.with(this).load("https://unsplash.it/600/300/?random").apply(centerCropTransform()).into(backdrop)
    }

    private fun fillFab() {
        floatingActionButton.setImageDrawable(IconicsDrawable(this, GoogleMaterial.Icon.gmd_favorite).apply { actionBar(); colorInt = Color.WHITE })
    }

    override fun onSaveInstanceState(_outState: Bundle) {
        var outState = _outState
        //add the values which need to be saved from the drawer to the bundle
        outState = slider.saveInstanceState(outState)
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerView.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }
}
