package com.tundem.holokitkatdrawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.joanzapata.android.iconify.Iconify;
import com.tundem.holokitkatdrawer.model.NavDrawerItem;

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
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], true, enabledSecond));
        // Custom
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));
        // Settings
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], Iconify.IconValue.fa_cog, false));
        // Help
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], Iconify.IconValue.fa_question, false));
        // Open Source
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], Iconify.IconValue.fa_github, false));
        // Contact
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], Iconify.IconValue.fa_bullhorn, false));
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
                fragment = new SampleFragment(navMenuTitles[position]);
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
            getDrawerLayout().closeDrawer(getDrawerListView());
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }
}
