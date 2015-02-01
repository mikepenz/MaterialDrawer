package com.mikepenz.materialdrawer.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.BaseActivity;
import com.mikepenz.materialdrawer.model.NavDrawerItem;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    // slide menu items
    private String[] navMenuTitles;

    boolean enabledSecond = false;

    @Override
    public ArrayList<NavDrawerItem> getNavDrawerItems() {
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();
        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
        // Freeplay
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], NavDrawerItem.PRIMARY, enabledSecond));
        // Custom
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));
        // SPACER
        navDrawerItems.add(new NavDrawerItem(NavDrawerItem.SPACER));
        // Settings
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], FontAwesome.Icon.faw_cog, NavDrawerItem.SECONDARY));
        // Help
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], FontAwesome.Icon.faw_question, NavDrawerItem.SECONDARY));
        // Open Source
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], FontAwesome.Icon.faw_github, NavDrawerItem.SECONDARY));
        // Contact
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], FontAwesome.Icon.faw_bullhorn, NavDrawerItem.SECONDARY));
        return navDrawerItems;
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    public void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new SampleFragment(navMenuTitles[position]);
                break;
            case 1:
                fragment = new SampleFragment(navMenuTitles[position]);
                break;
            case 2:
                fragment = new SampleFragment(navMenuTitles[position]);
                break;
            case 3:
                fragment = new SampleFragment(navMenuTitles[position]);
                break;
            case 4:
                fragment = new SampleFragment(navMenuTitles[position]);
                break;
            case 5:
                fragment = new Libs.Builder().withFields(R.string.class.getFields()).fragment();
                break;
            case 6:
                fragment = new SampleFragment(navMenuTitles[position]);

                //Not a good idea. but it should work for demonstration
                enabledSecond = !enabledSecond;

                getAdapter().updateData(getNavDrawerItems());
                getAdapter().notifyDataSetChanged();

                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            getDrawerListView().setItemChecked(position, true);
            getDrawerListView().setSelection(position);
            setTitle(navMenuTitles[position]);
            getDrawerLayout().closeDrawer(getSlider());
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (getDrawerToggle().onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else {
        }

        return super.onOptionsItemSelected(item);
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = getDrawerLayout().isDrawerOpen(getSlider());

        if (menu.findItem(R.id.action_settings) != null) {
            menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
