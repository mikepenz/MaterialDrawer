package com.mikepenz.materialdrawer.app

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.centerCropTransform
import com.mikepenz.iconics.IconicsColor.Companion.colorInt
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import kotlinx.android.synthetic.main.activity_sample_collapsing_toolbar.*

class CollapsingToolbarActivity : AppCompatActivity() {

    private var headerResult: AccountHeader? = null
    private var result: Drawer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_collapsing_toolbar)

        setSupportActionBar(toolbar)
        collapsingToolbar.title = getString(R.string.drawer_item_collapsing_toolbar_drawer)

        headerResult = AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withHeaderBackground(R.drawable.header)
                .withSavedInstance(savedInstanceState)
                .build()

        result = DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult!!)
                .withToolbar(toolbar)
                .withFullscreen(true)
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
                .withSavedInstance(savedInstanceState)
                .build()

        fillFab()
        loadBackdrop()
    }

    private fun loadBackdrop() {
        Glide.with(this).load("https://unsplash.it/600/300/?random").apply(centerCropTransform()).into(backdrop)
    }

    private fun fillFab() {
        floatingActionButton.setImageDrawable(IconicsDrawable(this, GoogleMaterial.Icon.gmd_favorite).actionBar().color(colorInt(Color.WHITE)))
    }

    override fun onSaveInstanceState(_outState: Bundle?) {
        var outState = _outState
        //add the values which need to be saved from the drawer to the bundle
        outState = result?.saveInstanceState(outState) ?: outState
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult?.saveInstanceState(outState) ?: outState
        super.onSaveInstanceState(outState)
    }
}
