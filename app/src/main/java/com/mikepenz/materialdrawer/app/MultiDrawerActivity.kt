package com.mikepenz.materialdrawer.app

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesomeBrand
import com.mikepenz.materialdrawer.app.databinding.ActivityMultiSampleBinding
import com.mikepenz.materialdrawer.app.drawerItems.GmailDrawerItem
import com.mikepenz.materialdrawer.iconics.withIcon
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.*
import com.mikepenz.materialdrawer.util.updateItem

class MultiDrawerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMultiSampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState)
        binding = ActivityMultiSampleBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        // Handle Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setTitle(R.string.drawer_item_multi_drawer)

        binding.slider.apply {
            itemAdapter.add(
                GmailDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                GmailDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                GmailDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(5),
                SectionDrawerItem().withName(R.string.drawer_item_section_header),
                SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog),
                SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).withEnabled(false),
                SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesomeBrand.Icon.fab_github),
                SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_bullhorn)
            )
            adapter.onClickListener = { v, adapter, drawerItem, position ->
                if (drawerItem is Badgeable) {
                    if (drawerItem.badge != null) {
                        //note don't do this if your badge contains a "+"
                        //only use toString() if you set the test as String
                        val badge = Integer.valueOf(drawerItem.badge?.toString() ?: "0")
                        if (badge > 0) {
                            drawerItem.withBadge((badge - 1).toString())
                            binding.slider.updateItem(drawerItem)
                        }
                    }
                }
                if (drawerItem is Nameable) {
                    Toast.makeText(this@MultiDrawerActivity, drawerItem.name?.getText(this@MultiDrawerActivity), Toast.LENGTH_SHORT).show()
                }
                false
            }
            setSavedInstance(savedInstanceState)
            setSelectionAtPosition(0)
        }

        binding.sliderEnd.apply {
            stickyHeaderView = layoutInflater.inflate(R.layout.header, null)
            itemAdapter.add(
                PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(5),
                SectionDrawerItem().withName(R.string.drawer_item_section_header),
                SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog),
                SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).withEnabled(false),
                SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesomeBrand.Icon.fab_github),
                SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_bullhorn)
            )
            adapter.onClickListener = { v, adapter, drawerItem, position ->
                if (drawerItem is Nameable) {
                    Toast.makeText(this@MultiDrawerActivity, drawerItem.name?.getText(this@MultiDrawerActivity), Toast.LENGTH_SHORT).show()
                }
                false
            }
            savedInstanceKey = "end"
            setSavedInstance(savedInstanceState)
        }

        //set the back arrow in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false)
    }


    override fun onSaveInstanceState(_outState: Bundle) {
        var outState = _outState
        //add the values which need to be saved from the drawer to the bundle
        outState = binding.slider.saveInstanceState(outState)
        //add the values which need to be saved from the drawer to the bundle
        outState = binding.sliderEnd.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        when {
            binding.root.isDrawerOpen(binding.slider) -> binding.root.closeDrawer(binding.slider)
            binding.root.isDrawerOpen(binding.sliderEnd) -> binding.root.closeDrawer(binding.sliderEnd)
            else -> super.onBackPressed()
        }
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
}
