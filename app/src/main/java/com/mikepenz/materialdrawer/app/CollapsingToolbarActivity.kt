package com.mikepenz.materialdrawer.app

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesomeBrand
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.iconics.utils.actionBar
import com.mikepenz.iconics.utils.colorInt
import com.mikepenz.materialdrawer.app.databinding.ActivitySampleCollapsingToolbarBinding
import com.mikepenz.materialdrawer.app.utils.convertDpToPixel
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.iconics.iconicsIcon
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.nameRes
import com.mikepenz.materialdrawer.util.removeAllItems
import com.mikepenz.materialdrawer.widget.AccountHeaderView
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView

class CollapsingToolbarActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySampleCollapsingToolbarBinding

    private lateinit var headerView: AccountHeaderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySampleCollapsingToolbarBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        setSupportActionBar(binding.toolbar)
        binding.collapsingToolbar.title = getString(R.string.drawer_item_collapsing_toolbar_drawer)

        binding.materialDrawerSwipeRefresh.setProgressViewOffset(true, 0, convertDpToPixel(48f, this).toInt())
        binding.materialDrawerSwipeRefresh.setOnRefreshListener {
            binding.materialDrawerSwipeRefresh.postDelayed({
                binding.slider.setDrawerItems()
                binding.slider.setSelection(5, false)
                binding.materialDrawerSwipeRefresh.isRefreshing = false
            }, 3000)
        }

        // Create the AccountHeader
        headerView = AccountHeaderView(this).apply {
            attachToSliderView(binding.slider)
            headerBackground = ImageHolder(R.drawable.header)
            withSavedInstance(savedInstanceState)
        }

        binding.slider.apply {
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
            PrimaryDrawerItem().apply { nameRes = R.string.drawer_item_home; iconicsIcon = FontAwesome.Icon.faw_home; identifier = 1 },
            PrimaryDrawerItem().apply { nameRes = R.string.drawer_item_free_play; iconicsIcon = FontAwesome.Icon.faw_gamepad },
            PrimaryDrawerItem().apply { nameRes = R.string.drawer_item_custom; iconicsIcon = FontAwesome.Icon.faw_eye; identifier = 5 },
            SectionDrawerItem().apply { nameRes = R.string.drawer_item_section_header },
            SecondaryDrawerItem().apply { nameRes = R.string.drawer_item_settings; iconicsIcon = FontAwesome.Icon.faw_cog },
            SecondaryDrawerItem().apply { nameRes = R.string.drawer_item_help; iconicsIcon = FontAwesome.Icon.faw_question; isEnabled = false },
            SecondaryDrawerItem().apply { nameRes = R.string.drawer_item_open_source; iconicsIcon = FontAwesomeBrand.Icon.fab_github },
            SecondaryDrawerItem().apply { nameRes = R.string.drawer_item_contact; iconicsIcon = FontAwesome.Icon.faw_bullhorn }
        )
    }

    private fun loadBackdrop() {
        binding.backdrop.load("https://unsplash.it/600/300/?random")
    }

    private fun fillFab() {
        binding.floatingActionButton.setImageDrawable(IconicsDrawable(this, GoogleMaterial.Icon.gmd_favorite).apply { actionBar(); colorInt = Color.WHITE })
    }

    override fun onSaveInstanceState(_outState: Bundle) {
        var outState = _outState
        //add the values which need to be saved from the drawer to the bundle
        outState = binding.slider.saveInstanceState(outState)
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerView.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }
}
