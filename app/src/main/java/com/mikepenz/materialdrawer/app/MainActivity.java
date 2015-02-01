package com.mikepenz.materialdrawer.app;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.BaseActivity;
import com.mikepenz.materialdrawer.model.DrawerItem;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    // slide menu items
    private String[] navMenuTitles;

    boolean enabledSecond = false;

    @Override
    public ArrayList<DrawerItem> getDrawerItems() {
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        ArrayList<DrawerItem> drawerItems = new ArrayList<DrawerItem>();
        // adding nav drawer items to array
        // Home
        drawerItems.add(new DrawerItem(navMenuTitles[0]));
        // Freeplay
        drawerItems.add(new DrawerItem(navMenuTitles[1], DrawerItem.PRIMARY, enabledSecond));
        // Custom
        drawerItems.add(new DrawerItem(navMenuTitles[2]));
        // SPACER
        drawerItems.add(new DrawerItem(DrawerItem.SPACER));
        // Settings
        drawerItems.add(new DrawerItem(navMenuTitles[4], FontAwesome.Icon.faw_cog, DrawerItem.SECONDARY));
        // Help
        drawerItems.add(new DrawerItem(navMenuTitles[5], FontAwesome.Icon.faw_question, DrawerItem.SECONDARY));
        // Open Source
        drawerItems.add(new DrawerItem(navMenuTitles[6], FontAwesome.Icon.faw_github, DrawerItem.SECONDARY));
        // Contact
        drawerItems.add(new DrawerItem(navMenuTitles[7], FontAwesome.Icon.faw_bullhorn, DrawerItem.SECONDARY));
        return drawerItems;
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    @Override
    public void displayView(Fragment fragment, String title, int position) {
        title = navMenuTitles[position];

        // update the main content by replacing fragments
        switch (position) {
            case 0:
                fragment = new SampleFragment(title);
                break;
            case 1:
                fragment = new SampleFragment(title);
                break;
            case 2:
                fragment = new SampleFragment(title);
                break;
            case 4:
                fragment = new SampleFragment(title);
                break;
            case 5:
                fragment = new SampleFragment(title);
                break;
            case 6:
                fragment = new Libs.Builder().withFields(R.string.class.getFields()).fragment();
                break;
            case 7:
                fragment = new SampleFragment(title);

                //Not a good idea. but it should work for demonstration
                enabledSecond = !enabledSecond;

                getAdapter().setDrawerItems(getDrawerItems());
                getAdapter().notifyDataSetChanged();

                break;

            default:
                break;
        }

        super.displayView(fragment, title, position);
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
