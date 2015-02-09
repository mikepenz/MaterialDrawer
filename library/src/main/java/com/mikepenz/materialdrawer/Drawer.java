package com.mikepenz.materialdrawer;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.mikepenz.materialdrawer.adapter.DrawerAdapter;
import com.mikepenz.materialdrawer.model.IDrawerItem;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mikepenz on 03.02.15.
 */
public class Drawer {

    // the activity to use
    protected Activity mActivity;
    protected ViewGroup mRootView;

    /**
     * Pass the activity you use the drawer in ;)
     *
     * @param activity
     * @return
     */
    public Drawer withActivity(Activity activity) {
        this.mRootView = (ViewGroup) activity.findViewById(android.R.id.content);
        this.mActivity = activity;
        return this;
    }

    // the toolbar of the activity
    protected Toolbar mToolbar;

    /**
     * Pass the toolbar you would love to use with this drawer
     *
     * @param toolbar
     * @return
     */
    public Drawer withToolbar(Toolbar toolbar) {
        this.mToolbar = toolbar;
        return this;
    }

    // the drawerLayout to use
    protected DrawerLayout mDrawerLayout;

    /**
     * You can pass a custom view for the drawer lib. note this requires the same structure as the drawer.xml
     *
     * @param drawerLayout
     * @return
     */
    public Drawer withDrawerLayout(DrawerLayout drawerLayout) {
        this.mDrawerLayout = drawerLayout;
        return this;
    }

    /**
     * You can pass a custom layout for the drawer lib. see the drawer.xml in layouts of this lib on GitHub
     *
     * @param resLayout
     * @return
     */
    public Drawer withDrawerLayout(int resLayout) {
        if (mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }

        if (resLayout != -1) {
            this.mDrawerLayout = (DrawerLayout) mActivity.getLayoutInflater().inflate(resLayout, mRootView, false);
        } else {
            this.mDrawerLayout = (DrawerLayout) mActivity.getLayoutInflater().inflate(R.layout.drawer, mRootView, false);
        }

        return this;
    }

    // enable the drawer toggle / if withActionBarDrawerToggle we will autogenerate it
    protected boolean mActionBarDrawerToggleEnabled = true;

    /**
     * set to true if you want a ActionBarDrawerToggle handled by the lib
     *
     * @param actionBarDrawerToggleEnabled
     * @return
     */
    public Drawer withActionBarDrawerToggle(boolean actionBarDrawerToggleEnabled) {
        this.mActionBarDrawerToggleEnabled = actionBarDrawerToggleEnabled;
        return this;
    }

    // drawer toggle
    protected ActionBarDrawerToggle mActionBarDrawerToggle;

    /**
     * pass an ActionBarDrawerToggle you would love to use with this drawer
     *
     * @param actionBarDrawerToggle
     * @return
     */
    public Drawer withActionBarDrawerToggle(ActionBarDrawerToggle actionBarDrawerToggle) {
        this.mActionBarDrawerToggleEnabled = true;
        this.mActionBarDrawerToggle = actionBarDrawerToggle;
        return this;
    }

    // header view
    protected View mHeaderView;
    protected int mHeaderOffset = 0;

    public Drawer withHeader(View headerView) {
        this.mHeaderView = headerView;
        //set the header offset
        mHeaderOffset = 1;
        return this;
    }

    public Drawer withHeader(int headerViewRes) {
        if (mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }

        if (headerViewRes != -1) {
            //i know there should be a root, bit i got none here
            this.mHeaderView = mActivity.getLayoutInflater().inflate(headerViewRes, null, false);
            //set the headerOffset :D
            mHeaderOffset = 1;
        }

        return this;
    }

    // item to select
    protected int mSelectedItem = 0;

    /**
     * pass the item which should be selected on start
     *
     * @param selectedItem
     * @return
     */
    public Drawer withSelectedItem(int selectedItem) {
        this.mSelectedItem = selectedItem;
        return this;
    }

    // an ListView to use within the drawer :D
    protected ListView mListView;

    /**
     * Set the list which is added within the slider
     *
     * @param listView
     * @return
     */
    public Drawer withListView(ListView listView) {
        this.mListView = listView;
        return this;
    }

    // an adapter to use for the list
    protected BaseAdapter mAdapter;

    /**
     * Set the adapter to be used with the list
     *
     * @param adapter
     * @return
     */
    public Drawer withAdapter(BaseAdapter adapter) {
        this.mAdapter = adapter;
        return this;
    }

    // list in drawer
    protected ArrayList<IDrawerItem> mDrawerItems;

    /**
     * @param drawerItems
     * @return
     */
    public Drawer withDrawerItems(ArrayList<IDrawerItem> drawerItems) {
        this.mDrawerItems = drawerItems;
        return this;
    }

    /**
     * add single ore more DrawerItems to the Drawer
     *
     * @param drawerItems
     * @return
     */
    public Drawer addDrawerItems(IDrawerItem... drawerItems) {
        if (this.mDrawerItems == null) {
            this.mDrawerItems = new ArrayList<IDrawerItem>();
        }

        if (drawerItems != null) {
            Collections.addAll(this.mDrawerItems, drawerItems);
        }
        return this;
    }

    // close drawer on click
    protected boolean mCloseOnClick = true;

    public Drawer withCloseOnClick(boolean closeOnClick) {
        this.mCloseOnClick = closeOnClick;
        return this;
    }

    // onDrawerItemClickListeners
    protected OnDrawerItemClickListener mOnDrawerItemClickListener;

    /**
     * @param onDrawerItemClickListener
     * @return
     */
    public Drawer withOnDrawerItemClickListener(OnDrawerItemClickListener onDrawerItemClickListener) {
        this.mOnDrawerItemClickListener = onDrawerItemClickListener;
        return this;
    }

    // onDrawerItemClickListeners
    protected OnDrawerItemLongClickListener mOnDrawerItemLongClickListener;

    /**
     * @param onDrawerItemLongClickListener
     * @return
     */
    public Drawer withOnDrawerItemLongClickListener(OnDrawerItemLongClickListener onDrawerItemLongClickListener) {
        this.mOnDrawerItemLongClickListener = onDrawerItemLongClickListener;
        return this;
    }

    // onDrawerItemClickListeners
    protected OnDrawerItemSelectedListener mOnDrawerItemSelectedListener;

    /**
     * @param onDrawerItemSelectedListener
     * @return
     */
    public Drawer withOnDrawerItemSelectedListener(OnDrawerItemSelectedListener onDrawerItemSelectedListener) {
        this.mOnDrawerItemSelectedListener = onDrawerItemSelectedListener;
        return this;
    }

    /**
     * Build everything and get a Result
     *
     * @return
     */
    public Result build() {
        if (mActivity == null) {
            throw new RuntimeException("please pass an activity");
        }

        // if the user has not set a drawerLayout use the default one :D
        if (mDrawerLayout == null) {
            withDrawerLayout(-1);
        }

        //get the drawer root
        ViewGroup drawerContentRoot = (ViewGroup) mDrawerLayout.getChildAt(0);
        //get the content view
        View contentView = mRootView.getChildAt(0);

        // remove the contentView
        mRootView.removeView(contentView);

        //add the contentView to the drawer content frameLayout
        drawerContentRoot.addView(contentView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        //add the drawerLayout to the root
        mRootView.addView(mDrawerLayout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // create the ActionBarDrawerToggle if not set and enabled
        if (mActionBarDrawerToggleEnabled && mActionBarDrawerToggle == null) {
            if (mToolbar == null) {
                throw new RuntimeException("you have to pass a toolbar for the ActionBarDrawerToggle auto generation");
            }
            this.mActionBarDrawerToggle = new ActionBarDrawerToggle(mActivity, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
            this.mActionBarDrawerToggle.syncState();
        }

        //handle the ActionBarDrawerToggle
        if (mActionBarDrawerToggle != null) {
            mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        }

        //TODO don't require an adapter or drawerItems to create a ListView

        // initialize list if there is an adapter or set items
        if (mDrawerItems != null && mAdapter == null) {
            mAdapter = new DrawerAdapter(mActivity, mDrawerItems);
        }

        // get the slider view
        FrameLayout slider = (FrameLayout) mDrawerLayout.findViewById(R.id.slider_layout);

        // if we have an adapter (either by defining a custom one or the included one add a list :D
        if (mAdapter != null) {
            if (mListView == null) {
                mListView = new ListView(mActivity);
                mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                mListView.setDivider(null);
                mListView.setClipToPadding(false);
                mListView.setPadding(0, mActivity.getResources().getDimensionPixelSize(R.dimen.tool_bar_top_padding), 0, 0);
            }

            slider.addView(mListView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
        }

        // set the header (do this before the setAdapter because some devices will crash else
        if (mHeaderView != null) {
            if (mListView == null) {
                throw new RuntimeException("can't use a headerView without a listView");
            }

            mListView.addHeaderView(mHeaderView);
            mListView.setPadding(0, 0, 0, 0);
        }

        //after adding the header do the setAdapter and set the selection
        if(mAdapter != null) {
            //set the adapter on the listView
            mListView.setAdapter(mAdapter);

            //predefine selection (should be the first element
            if (mListView != null && (mSelectedItem + mHeaderOffset) > -1) {
                mListView.setSelection(mSelectedItem + mHeaderOffset);
                mListView.setItemChecked(mSelectedItem + mHeaderOffset, true);
            }
        }

        // add the onDrawerItemClickListener if set
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnDrawerItemClickListener != null) {
                    if (mDrawerItems != null && mDrawerItems.size() > (position - mHeaderOffset) && (position - mHeaderOffset) > -1) {
                        mOnDrawerItemClickListener.onItemClick(parent, view, position, id, mDrawerItems.get(position - mHeaderOffset));
                    } else {
                        mOnDrawerItemClickListener.onItemClick(parent, view, position, id, null);
                    }
                }
                if (mCloseOnClick) {
                    mDrawerLayout.closeDrawers();
                }
            }
        });

        // add the onDrawerItemLongClickListener if set
        if (mOnDrawerItemLongClickListener != null) {
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mDrawerItems != null && mDrawerItems.size() > (position - mHeaderOffset) && (position - mHeaderOffset) > -1) {
                        return mOnDrawerItemLongClickListener.onItemLongClick(parent, view, position, id, mDrawerItems.get(position - mHeaderOffset));
                    } else {
                        return mOnDrawerItemLongClickListener.onItemLongClick(parent, view, position, id, null);
                    }
                }
            });
        }

        // add the onDrawerItemSelectedListener if set
        if (mOnDrawerItemSelectedListener != null) {
            mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mDrawerItems != null && mDrawerItems.size() > (position - mHeaderOffset) && (position - mHeaderOffset) > -1) {
                        mOnDrawerItemSelectedListener.onItemSelected(parent, view, position, id, mDrawerItems.get(position - mHeaderOffset));
                    } else {
                        mOnDrawerItemSelectedListener.onItemSelected(parent, view, position, id, null);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    mOnDrawerItemSelectedListener.onNothingSelected(parent);
                }
            });
        }

        return new Result(this);
    }

    public static class Result {
        private Drawer mDrawer;

        //views
        private FrameLayout mSliderView;
        private FrameLayout mContentView;

        public Result(Drawer drawer) {
            this.mDrawer = drawer;
        }

        public DrawerLayout getDrawerLayout() {
            return this.mDrawer.mDrawerLayout;
        }

        public FrameLayout getSlider() {
            if (mSliderView == null) {
                mSliderView = (FrameLayout) this.mDrawer.mDrawerLayout.findViewById(R.id.slider_layout);
            }
            return mSliderView;
        }

        public FrameLayout getContent() {
            if (mContentView == null) {
                mContentView = (FrameLayout) this.mDrawer.mDrawerLayout.findViewById(R.id.content_layout);
            }
            return mContentView;
        }

        public ListView getListView() {
            return mDrawer.mListView;
        }

        public Adapter getAdapter() {
            return mDrawer.mAdapter;
        }

        /**
         * set the current selection in the drawer
         * NOTE: This will trigger onDrawerItemSelected without a view!
         *
         * @param position the position to select
         */
        public void setSelection(int position) {
            if (mDrawer.mListView != null) {
                mDrawer.mListView.setSelection(position + mDrawer.mHeaderOffset);
                mDrawer.mListView.setItemChecked(position + mDrawer.mHeaderOffset, true);

                if (mDrawer.mOnDrawerItemSelectedListener != null) {
                    if (mDrawer.mDrawerItems != null && mDrawer.mDrawerItems.size() > (position - mDrawer.mHeaderOffset) && (position - mDrawer.mHeaderOffset) > -1) {
                        mDrawer.mOnDrawerItemSelectedListener.onItemSelected(null, null, position, position, mDrawer.mDrawerItems.get(position - mDrawer.mHeaderOffset));
                    } else {
                        mDrawer.mOnDrawerItemSelectedListener.onItemSelected(null, null, position, position, null);
                    }
                }
            }
        }
    }


    public interface OnDrawerItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem);
    }

    public interface OnDrawerItemLongClickListener {
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem);
    }

    public interface OnDrawerItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem);

        public void onNothingSelected(AdapterView<?> parent);
    }
}
