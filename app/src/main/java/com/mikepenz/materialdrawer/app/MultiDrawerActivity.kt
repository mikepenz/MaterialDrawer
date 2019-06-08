package com.mikepenz.materialdrawer.app

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Badgeable
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import kotlinx.android.synthetic.main.activity_sample.*

class MultiDrawerActivity : AppCompatActivity() {

    private lateinit var result: Drawer
    private lateinit var resultAppended: Drawer

    override fun onCreate(savedInstanceState: Bundle?) {
        //supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        // Handle Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.drawer_item_multi_drawer)

        //first create the main drawer (this one will be used to add the second drawer on the other side)
        result = DrawerBuilder()
                .withActivity(this)
                .withHeader(R.layout.header)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withBadge("99").withIdentifier(1),
                        PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                        PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye).withBadge("6").withIdentifier(2),
                        SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog),
                        SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).withEnabled(false),
                        SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github).withBadge("12").withIdentifier(3),
                        SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_bullhorn)
                )
                .withOnDrawerListener(object : Drawer.OnDrawerListener {
                    override fun onDrawerOpened(drawerView: View) {
                        Toast.makeText(this@MultiDrawerActivity, "onDrawerOpened", Toast.LENGTH_SHORT).show()
                    }

                    override fun onDrawerClosed(drawerView: View) {
                        Toast.makeText(this@MultiDrawerActivity, "onDrawerClosed", Toast.LENGTH_SHORT).show()
                    }

                    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                    }
                })
                .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                    override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem is Nameable<*>) {
                            Toast.makeText(this@MultiDrawerActivity, drawerItem.name?.getText(this@MultiDrawerActivity), Toast.LENGTH_SHORT).show()
                        }

                        if (drawerItem is Badgeable<*>) {
                            if (drawerItem.badge != null) {
                                //note don't do this if your badge contains a "+"
                                //only use toString() if you set the test as String
                                val badge = Integer.valueOf(drawerItem.badge?.toString() ?: "0")
                                if (badge > 0) {
                                    drawerItem.withBadge((badge - 1).toString())
                                    result.updateItem(drawerItem)
                                }
                            }
                        }

                        return false
                    }
                })
                .withOnDrawerItemLongClickListener(object : Drawer.OnDrawerItemLongClickListener {
                    override fun onItemLongClick(view: View, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                        if (drawerItem is SecondaryDrawerItem) {
                            Toast.makeText(this@MultiDrawerActivity, drawerItem.name?.getText(this@MultiDrawerActivity), Toast.LENGTH_SHORT).show()
                        }
                        return false
                    }
                })
                .withOnDrawerListener(object : Drawer.OnDrawerListener {
                    override fun onDrawerOpened(drawerView: View) {
                        if (drawerView === result.slider) {
                            Log.e("sample", "left opened")
                        } else if (drawerView === resultAppended.slider) {
                            Log.e("sample", "right opened")
                        }
                    }

                    override fun onDrawerClosed(drawerView: View) {
                        if (drawerView === result.slider) {
                            Log.e("sample", "left closed")
                        } else if (drawerView === resultAppended.slider) {
                            Log.e("sample", "right closed")
                        }
                    }

                    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                    }
                })
                .build()

        //now we add the second drawer on the other site.
        //use the .append method to add this drawer to the first one
        resultAppended = DrawerBuilder()
                .withActivity(this)
                .withFooter(R.layout.footer)
                .withDisplayBelowStatusBar(true)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye),
                        PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home),
                        PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                        SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog),
                        DividerDrawerItem(),
                        SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github),
                        SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).withEnabled(false),
                        SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_bullhorn)
                )
                .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                    override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                        if (drawerItem is Nameable<*>) {
                            Toast.makeText(this@MultiDrawerActivity, drawerItem.name?.getText(this@MultiDrawerActivity), Toast.LENGTH_SHORT).show()
                        }
                        return false
                    }
                })
                .withDrawerGravity(Gravity.END)
                .append(result)

        //set the back arrow in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false)
    }


    override fun onSaveInstanceState(_outState: Bundle) {
        var outState = _outState
        //add the values which need to be saved from the drawer to the bundle
        if (::result.isInitialized) {
            outState = result.saveInstanceState(outState)
        }
        //add the values which need to be saved from the accountHeader to the bundle
        if (::resultAppended.isInitialized) {
            outState = resultAppended.saveInstanceState(outState)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result.isDrawerOpen) {
            result.closeDrawer()
        } else {
            super.onBackPressed()
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
