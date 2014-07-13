package com.tundem.holokitkatdrawer;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.tundem.holokitkatdrawer.adapter.NavDrawerListAdapter;
import com.tundem.holokitkatdrawer.model.NavDrawerItem;
import com.tundem.holokitkatdrawer.util.UIUtils;
import com.tundem.holokitkatdrawer.view.DrawInsetsFrameLayout;

import java.util.ArrayList;

public abstract class BaseActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;


    // nav drawer title
    private CharSequence mDrawerTitle;

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

        //init the activity and make it beautiful :D
        UIUtils.getInstance().initActivity(this);

        mTitle = mDrawerTitle = getTitle();

        getDrawerListView().setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(this, getNavDrawerItems());
        getDrawerListView().setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, getDrawerLayout(),
                R.drawable.ic_navigation_drawer, //nav menu toggle icon
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

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }

        //init ui margins to make our activity beautiful!
        DrawInsetsFrameLayout drawInsetsFrameLayout = (DrawInsetsFrameLayout) findViewById(R.id.drawinsetsframelayout);
        drawInsetsFrameLayout.setOnInsetsCallback(new DrawInsetsFrameLayout.OnInsetsCallback() {
            @Override
            public void onInsetsChanged(Rect insets) {
                getDrawerLayout().setLayoutParams(UIUtils.getInstance().handleTranslucentDecorMargins(((FrameLayout.LayoutParams) getDrawerLayout().getLayoutParams()), insets));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = getDrawerLayout().isDrawerOpen(getDrawerListView());
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
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
