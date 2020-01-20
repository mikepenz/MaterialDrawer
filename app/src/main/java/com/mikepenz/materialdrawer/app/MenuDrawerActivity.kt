package com.mikepenz.materialdrawer.app

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import com.mikepenz.materialdrawer.util.inflateMenu
import kotlinx.android.synthetic.main.activity_sample.*

class MenuDrawerActivity : AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        // Handle Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.drawer_item_menu_drawer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, root, toolbar, com.mikepenz.materialdrawer.R.string.material_drawer_open, com.mikepenz.materialdrawer.R.string.material_drawer_close)

        slider.apply {
            inflateMenu(R.menu.example_menu)
            onDrawerItemClickListener = { v, drawerItem, position ->
                if (drawerItem is Nameable) {
                    Toast.makeText(this@MenuDrawerActivity, (drawerItem as Nameable).name!!.getText(this@MenuDrawerActivity), Toast.LENGTH_SHORT).show()
                }
                false
            }
            setSavedInstance(savedInstanceState)
        }

        // set the selection to the item with the identifier 5
        if (savedInstanceState == null) {
            slider.setSelection(5, false)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        actionBarDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarDrawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(_outState: Bundle) {
        var outState = _outState
        //add the values which need to be saved from the drawer to the bundle
        outState = slider.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (root.isDrawerOpen(slider)) {
            root.closeDrawer(slider)
        } else {
            super.onBackPressed()
        }
    }
}
