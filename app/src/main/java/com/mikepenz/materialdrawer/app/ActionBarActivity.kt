package com.mikepenz.materialdrawer.app

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import kotlinx.android.synthetic.main.activity_sample_actionbar.*

class ActionBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_actionbar)
        setTitle(R.string.drawer_item_action_bar_drawer)

        slider.itemAdapter.add(
                PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home),
                SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog)
        )
        slider.adapter.onClickListener = { v, adapter, drawerItem, position ->
            if (drawerItem is Nameable<*>) {
                Toast.makeText(this@ActionBarActivity, (drawerItem as Nameable<*>).name!!.getText(this@ActionBarActivity), Toast.LENGTH_SHORT).show()
            }
            false
        }
        slider.withSavedInstance(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false)
    }

    override fun onSaveInstanceState(_outState: Bundle) {
        //add the values which need to be saved from the drawer to the bundle
        super.onSaveInstanceState(slider?.saveInstanceState(_outState) ?: _outState)
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
        if (root?.isDrawerOpen(slider) == true) {
            root?.closeDrawer(slider)
        } else {
            super.onBackPressed()
        }
    }
}
