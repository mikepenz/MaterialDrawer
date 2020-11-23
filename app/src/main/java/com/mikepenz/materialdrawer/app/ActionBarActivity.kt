package com.mikepenz.materialdrawer.app

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.materialdrawer.app.databinding.ActivitySampleActionbarBinding
import com.mikepenz.materialdrawer.iconics.iconicsIcon
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import com.mikepenz.materialdrawer.model.interfaces.nameRes

class ActionBarActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySampleActionbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState)
        binding = ActivitySampleActionbarBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        setTitle(R.string.drawer_item_action_bar_drawer)

        binding.slider.apply {
            itemAdapter.add(
                    PrimaryDrawerItem().apply {
                        nameRes = R.string.drawer_item_home
                        iconicsIcon = FontAwesome.Icon.faw_home
                    },
                    SecondaryDrawerItem().apply {
                        nameRes = R.string.drawer_item_settings
                        iconicsIcon = FontAwesome.Icon.faw_cog
                    }
            )
            onDrawerItemClickListener = { _, drawerItem, _ ->
                if (drawerItem is Nameable) {
                    Toast.makeText(this@ActionBarActivity, (drawerItem as Nameable).name!!.getText(this@ActionBarActivity), Toast.LENGTH_SHORT).show()
                }
                false
            }
            setSavedInstance(savedInstanceState)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false)
    }

    override fun onSaveInstanceState(_outState: Bundle) {
        //add the values which need to be saved from the drawer to the bundle
        super.onSaveInstanceState(binding.slider.saveInstanceState(_outState))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
        if (binding.root.isDrawerOpen(binding.slider)) {
            binding.root.closeDrawer(binding.slider)
        } else {
            super.onBackPressed()
        }
    }
}
