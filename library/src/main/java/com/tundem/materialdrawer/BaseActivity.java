package com.tundem.materialdrawer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tundem.materialdrawer.adapter.NavDrawerListAdapter;
import com.tundem.materialdrawer.model.NavDrawerItem;

import java.util.ArrayList;

public abstract class BaseActivity extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // used to store app title
    private CharSequence mTitle;


    private NavDrawerListAdapter adapter;


    /**
     * Returns the NavDrawerItems to Display
     *
     * @return
     */
    public abstract ArrayList<NavDrawerItem> getNavDrawerItems();

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    public abstract void displayView(int position);

    /**
     * getLayout
     *
     * @return the layout to set for this activity
     */
    public int getLayout() {
        return R.layout.activity_main;
    }

    /**
     * getDrawerLayout
     *
     * @return the drawerLayout
     */
    public DrawerLayout getDrawerLayout() {
        if (mDrawerLayout == null) {
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        }
        return mDrawerLayout;
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    /**
     * getDrawerListView
     *
     * @return the listview to fill with the items
     */
    public ListView getDrawerListView() {
        if (mDrawerList == null) {
            mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        }
        return mDrawerList;
    }

    public void setAdapter(NavDrawerListAdapter adapter) {
        this.adapter = adapter;
    }

    public NavDrawerListAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        mTitle = getTitle();

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Handle DrawerLayout
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Handle ActionBarDrawerToggle
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();

        // Handle different Drawer States :D
        mDrawerLayout.setStatusBarBackground(R.color.material_primary_color_dark);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //create SlideMenuClickListener
        getDrawerListView().setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(this, getNavDrawerItems());
        getDrawerListView().setAdapter(adapter);


        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /*
        mDrawerToggle = new ActionBarDrawerToggle(this, getDrawerLayout(),
                navigation_drawer_icon, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        getDrawerLayout().setDrawerListener(mDrawerToggle);
        */

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }
}
