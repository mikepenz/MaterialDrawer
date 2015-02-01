package com.mikepenz.materialdrawer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.mikepenz.materialdrawer.adapter.DrawerAdapter;
import com.mikepenz.materialdrawer.model.DrawerItem;
import com.mikepenz.materialdrawer.util.Utils;

import org.lucasr.twowayview.ItemClickSupport;
import org.lucasr.twowayview.ItemSelectionSupport;
import org.lucasr.twowayview.TwoWayLayoutManager;
import org.lucasr.twowayview.widget.ListLayoutManager;
import org.lucasr.twowayview.widget.TwoWayView;

import java.util.ArrayList;

public abstract class BaseActivity extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private LinearLayout mSlider;


    private TwoWayView mRecyclerView;
    private ItemClickSupport mItemClick;
    private ItemSelectionSupport mItemSelection;

    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;

    // the adapter :D
    private DrawerAdapter mAdapter;


    /**
     * Returns the NavDrawerItems to Display
     *
     * @return
     */
    public abstract ArrayList<DrawerItem> getDrawerItems();

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    public void displayView(Fragment fragment, String title, int position) {
        if (getAdapter() == null) {
            return;
        }

        DrawerItem item = Utils.getItem(getAdapter().getDrawerItems(), position);
        if (fragment != null && item != null && item.isEnabled()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mItemSelection.setItemChecked(position, true);

            //set the title and close the drawer
            setTitle(title);
            getDrawerLayout().closeDrawer(getSlider());
        }
    }

    /**
     * getLayout
     *
     * @return the layout to set for this activity
     */
    public int getLayout() {
        return R.layout.activity_main;
    }

    /**
     * getSlider (This is the container of the drawer listView)
     *
     * @return the slider which contains the drawer list
     */
    public LinearLayout getSlider() {
        if (mSlider == null) {
            mSlider = (LinearLayout) findViewById(R.id.slider);
        }
        return mSlider;
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
     * getRecyclerView
     *
     * @return the listview to fill with the items
     */
    public RecyclerView getRecyclerView() {
        if (mRecyclerView == null) {
            mRecyclerView = (TwoWayView) findViewById(R.id.list_slidermenu);
            mRecyclerView.setLayoutManager(new ListLayoutManager(this, TwoWayLayoutManager.Orientation.VERTICAL));
        }
        return mRecyclerView;
    }

    public void setAdapter(DrawerAdapter adapter) {
        this.mAdapter = adapter;
    }

    public DrawerAdapter getAdapter() {
        return mAdapter;
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
        mDrawerLayout.setStatusBarBackground(R.color.material_drawer_primary_dark);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //create SlideMenuClickListener
        //getRecyclerView().setO.setOnItemClickListener(new SlideMenuClickListener());
        mItemClick = ItemClickSupport.addTo(getRecyclerView());
        mItemClick.setOnItemClickListener(new SlideMenuClickListener());

        mItemSelection = ItemSelectionSupport.addTo(getRecyclerView());
        mItemSelection.setChoiceMode(ItemSelectionSupport.ChoiceMode.SINGLE);

        // setting the nav drawer list mAdapter
        mAdapter = new DrawerAdapter(this);
        mAdapter.addDrawerItems(getDrawerItems());
        getRecyclerView().setAdapter(mAdapter);

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(null, "", 0);
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Pass any configuration change to the drawer toggle's
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements ItemClickSupport.OnItemClickListener {
        @Override
        public void onItemClick(RecyclerView recyclerView, View view, int position, long l) {
            displayView(null, "", position);
        }
    }
}
