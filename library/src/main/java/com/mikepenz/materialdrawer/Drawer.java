package com.mikepenz.materialdrawer;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.utils.Utils;
import com.mikepenz.materialdrawer.adapter.DrawerAdapter;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Iconable;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mikepenz on 03.02.15.
 */
public class Drawer {
    private static final String BUNDLE_SELECTION = "bundle_selection";

    // some internal vars
    // variable to check if a builder is only used once
    protected boolean mUsed = false;
    protected int mCurrentSelection = -1;

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

    // set actionbar Compatibility mode
    protected boolean mActionBarCompatibility = false;

    /**
     * @param actionBarCompatibility
     * @return
     */
    public Drawer withActionBarCompatibility(boolean actionBarCompatibility) {
        this.mActionBarCompatibility = actionBarCompatibility;
        return this;
    }

    // set non translucent statusbar mode
    protected boolean mTranslucentStatusBar = true;

    /**
     * @param translucentStatusBar
     * @return
     */
    public Drawer withTranslucentStatusBar(boolean translucentStatusBar) {
        this.mTranslucentStatusBar = translucentStatusBar;
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
    protected LinearLayout mSliderLayout;

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

    //the width of the drawer
    protected int mDrawerWidth = -1;

    /**
     * set the drawer width as px
     *
     * @param drawerWidthPx
     * @return
     */
    public Drawer withDrawerWidthPx(int drawerWidthPx) {
        this.mDrawerWidth = drawerWidthPx;
        return this;
    }

    /**
     * set the drawer width as dp
     *
     * @param drawerWidthDp
     * @return
     */
    public Drawer withDrawerWidthDp(int drawerWidthDp) {
        if (mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }

        this.mDrawerWidth = Utils.convertDpToPx(mActivity, drawerWidthDp);
        return this;
    }

    /**
     * set the drawer width from resource
     *
     * @param drawerWidthRes
     * @return
     */
    public Drawer withDrawerWidthRes(int drawerWidthRes) {
        if (mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }

        this.mDrawerWidth = mActivity.getResources().getDimensionPixelSize(drawerWidthRes);
        return this;
    }

    //the gravity of the drawer
    protected Integer mDrawerGravity = null;

    public Drawer withDrawerGravity(int gravity) {
        this.mDrawerGravity = gravity;
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
    protected boolean mHeaderDivider = true;

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

    public Drawer withHeaderDivider(boolean headerDivider) {
        this.mHeaderDivider = headerDivider;
        return this;
    }

    // footer view
    protected View mFooterView;
    protected boolean mFooterDivider = true;

    public Drawer withFooter(View footerView) {
        this.mFooterView = footerView;
        return this;
    }

    public Drawer withFooter(int footerViewRes) {
        if (mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }

        if (footerViewRes != -1) {
            //i know there should be a root, bit i got none here
            this.mFooterView = mActivity.getLayoutInflater().inflate(footerViewRes, null, false);
        }

        return this;
    }

    public Drawer withFooterDivider(boolean footerDivider) {
        this.mFooterDivider = footerDivider;
        return this;
    }

    // sticky view
    protected View mStickyFooterView;

    public Drawer withStickyFooter(View stickyFooter) {
        this.mStickyFooterView = stickyFooter;
        return this;
    }

    public Drawer withStickyFooter(int stickyFooterRes) {
        if (mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }

        if (stickyFooterRes != -1) {
            //i know there should be a root, bit i got none here
            this.mStickyFooterView = mActivity.getLayoutInflater().inflate(stickyFooterRes, null, false);
        }

        return this;
    }

    // fire onClick after build
    protected boolean mFireInitialOnClick = false;

    /**
     * @param fireOnInitialOnClick
     * @return
     */
    public Drawer withFireOnInitialOnClick(boolean fireOnInitialOnClick) {
        this.mFireInitialOnClick = fireOnInitialOnClick;
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

    // onDrawerListener
    protected OnDrawerListener mOnDrawerListener;

    /**
     * @param onDrawerListener
     * @return
     */
    public Drawer withOnDrawerListener(OnDrawerListener onDrawerListener) {
        this.mOnDrawerListener = onDrawerListener;
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

    // savedInstance to restore state
    protected Bundle mSavedInstance;

    /**
     * @param savedInstance
     * @return
     */
    public Drawer withSavedInstance(Bundle savedInstance) {
        this.mSavedInstance = savedInstance;
        return this;
    }

    /**
     * Build everything and get a Result
     *
     * @return
     */
    public Result build() {
        if (mUsed) {
            throw new RuntimeException("you must not reuse a Drawer builder");
        }
        if (mActivity == null) {
            throw new RuntimeException("please pass an activity");
        }

        //set that this builder was used. now you have to create a new one
        mUsed = true;

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
                this.mActionBarDrawerToggle = new ActionBarDrawerToggle(mActivity, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        if (mOnDrawerListener != null) {
                            mOnDrawerListener.onDrawerOpened(drawerView);
                        }
                        super.onDrawerOpened(drawerView);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        if (mOnDrawerListener != null) {
                            mOnDrawerListener.onDrawerClosed(drawerView);
                        }
                        super.onDrawerClosed(drawerView);
                    }
                };
            } else {
                this.mActionBarDrawerToggle = new ActionBarDrawerToggle(mActivity, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        if (mOnDrawerListener != null) {
                            mOnDrawerListener.onDrawerOpened(drawerView);
                        }
                        super.onDrawerOpened(drawerView);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        if (mOnDrawerListener != null) {
                            mOnDrawerListener.onDrawerClosed(drawerView);
                        }
                        super.onDrawerClosed(drawerView);
                    }
                };
            }
            this.mActionBarDrawerToggle.syncState();
        }

        //handle the ActionBarDrawerToggle
        if (mActionBarDrawerToggle != null) {
            mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        }

        // get the slider view
        mSliderLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.drawer_slider, mDrawerLayout, false);
        // get the layout params
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mSliderLayout.getLayoutParams();
        // if we've set a custom gravity set it
        if (mDrawerGravity != null) {
            params.gravity = mDrawerGravity;
        }
        // if this is a drawer from the right, change the margins :D
        params = processDrawerLayoutParams(params);
        // set the new layout params
        mSliderLayout.setLayoutParams(params);

        // add the slider to the drawer
        mDrawerLayout.addView(mSliderLayout, 1);

        //create the content
        createContent();

        //forget the reference to the activity
        mActivity = null;

        return new Result(this);
    }


    public Result append(Result result) {
        if (mUsed) {
            throw new RuntimeException("you must not reuse a Drawer builder");
        }
        if (mDrawerGravity == null) {
            throw new RuntimeException("please set the gravity for the drawer");
        }

        //set that this builder was used. now you have to create a new one
        mUsed = true;

        //get the drawer layout from the previous drawer
        mDrawerLayout = result.getDrawerLayout();

        // get the slider view
        mSliderLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.drawer_slider, mDrawerLayout, false);
        // get the layout params
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mSliderLayout.getLayoutParams();
        // set the gravity of this drawerGravity
        params.gravity = mDrawerGravity;
        // if this is a drawer from the right, change the margins :D
        params = processDrawerLayoutParams(params);
        // set the new params
        mSliderLayout.setLayoutParams(params);
        // add the slider to the drawer
        mDrawerLayout.addView(mSliderLayout, 1);

        //create the content
        createContent();

        //forget the reference to the activity
        mActivity = null;

        return new Result(this);
    }

    private void createContent() {
        // if we have an adapter (either by defining a custom one or the included one add a list :D
        if (mListView == null) {
            mListView = new ListView(mActivity);
            mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            mListView.setDivider(null);
            mListView.setClipToPadding(false);

            if (mTranslucentStatusBar) {
                mListView.setPadding(0, mActivity.getResources().getDimensionPixelSize(R.dimen.tool_bar_top_padding), 0, 0);
            }
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.weight = 1f;
        mSliderLayout.addView(mListView, params);

        // initialize list if there is an adapter or set items
        if (mDrawerItems != null && mAdapter == null) {
            mAdapter = new DrawerAdapter(mActivity, mDrawerItems);
        }

        //sticky footer view
        if (mStickyFooterView != null) {
            mSliderLayout.addView(mStickyFooterView);
        }

        // set the header (do this before the setAdapter because some devices will crash else
        if (mHeaderView != null) {
            if (mListView == null) {
                throw new RuntimeException("can't use a headerView without a listView");
            }

            if (mHeaderDivider) {
                LinearLayout headerContainer = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.drawer_item_header, mListView, false);
                headerContainer.addView(mHeaderView, 0);
                mListView.addHeaderView(headerContainer);
                mListView.setPadding(0, 0, 0, 0);
            } else {
                mListView.addHeaderView(mHeaderView);
                mListView.setPadding(0, 0, 0, 0);
            }
        }

        // set the footer (do this before the setAdapter because some devices will crash else
        if (mFooterView != null) {
            if (mListView == null) {
                throw new RuntimeException("can't use a footerView without a listView");
            }

            if (mFooterDivider) {
                LinearLayout footerContainer = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.drawer_item_footer, mListView, false);
                footerContainer.addView(mFooterView, 1);
                mListView.addFooterView(footerContainer);
            } else {
                mListView.addFooterView(mFooterView);
            }
        }

        //after adding the header do the setAdapter and set the selection
        if (mAdapter != null) {
            //set the adapter on the listView
            mListView.setAdapter(mAdapter);

            //predefine selection (should be the first element
            if (mListView != null && (mSelectedItem + mHeaderOffset) > -1) {
                mListView.setSelection(mSelectedItem + mHeaderOffset);
                mListView.setItemChecked(mSelectedItem + mHeaderOffset, true);
                mCurrentSelection = mSelectedItem + mHeaderOffset;
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
                mCurrentSelection = position;
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
                    mCurrentSelection = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    mOnDrawerItemSelectedListener.onNothingSelected(parent);
                }
            });
        }

        if (mListView != null) {
            mListView.smoothScrollToPosition(0);
        }


        // try to restore all saved values again
        if (mSavedInstance != null) {
            int selection = mSavedInstance.getInt(BUNDLE_SELECTION, -1);
            if (selection != -1) {
                //predefine selection (should be the first element
                if (mListView != null && (selection) > -1) {
                    mListView.setSelection(selection);
                    mListView.setItemChecked(selection, true);
                    mCurrentSelection = selection;
                }
            }
        }

        // call initial onClick event to allow the dev to init the first view
        if (mFireInitialOnClick && mOnDrawerItemClickListener != null) {
            if (mDrawerItems != null && mDrawerItems.size() > mCurrentSelection && mCurrentSelection > -1) {
                mOnDrawerItemClickListener.onItemClick(null, null, mCurrentSelection, mCurrentSelection, mDrawerItems.get(mCurrentSelection));
            } else {
                mOnDrawerItemClickListener.onItemClick(null, null, mCurrentSelection, mCurrentSelection, null);
            }
        }
    }

    /**
     * helper to extend the layoutParams of the drawer
     *
     * @param params
     * @return
     */
    private DrawerLayout.LayoutParams processDrawerLayoutParams(DrawerLayout.LayoutParams params) {
        if (mDrawerGravity != null && (mDrawerGravity == Gravity.RIGHT || mDrawerGravity == Gravity.END)) {
            params.rightMargin = 0;
            if (Build.VERSION.SDK_INT >= 17) {
                params.setMarginEnd(0);
            }

            params.leftMargin = mActivity.getResources().getDimensionPixelSize(R.dimen.material_drawer_margin);
            if (Build.VERSION.SDK_INT >= 17) {
                params.setMarginEnd(mActivity.getResources().getDimensionPixelSize(R.dimen.material_drawer_margin));
            }
        }

        if (mActionBarCompatibility) {
            TypedValue tv = new TypedValue();
            if (mActivity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                params.topMargin = TypedValue.complexToDimensionPixelSize(tv.data, mActivity.getResources().getDisplayMetrics());
            }
        }

        if (mDrawerWidth > -1) {
            params.width = mDrawerWidth;
        } else {
            params.width = mActivity.getResources().getDimensionPixelSize(R.dimen.material_drawer_width);
        }

        return params;
    }

    public static class Result {
        private Drawer mDrawer;

        //views
        private FrameLayout mContentView;

        public Result(Drawer drawer) {
            this.mDrawer = drawer;
        }

        public DrawerLayout getDrawerLayout() {
            return this.mDrawer.mDrawerLayout;
        }

        public void openDrawer() {
            if (mDrawer.mDrawerLayout != null && mDrawer.mSliderLayout != null) {
                mDrawer.mDrawerLayout.openDrawer(mDrawer.mSliderLayout);
            }
        }

        public void closeDrawer() {
            if (mDrawer.mDrawerLayout != null) {
                mDrawer.mDrawerLayout.closeDrawers();
            }
        }

        public boolean isDrawerOpen() {
            if (mDrawer.mDrawerLayout != null && mDrawer.mSliderLayout != null) {
                return mDrawer.mDrawerLayout.isDrawerOpen(mDrawer.mSliderLayout);
            }
            return false;
        }

        public LinearLayout getSlider() {
            return mDrawer.mSliderLayout;
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

        public BaseAdapter getAdapter() {
            return mDrawer.mAdapter;
        }

        public ArrayList<IDrawerItem> getDrawerItems() {
            return mDrawer.mDrawerItems;
        }

        public View getHeader() {
            return mDrawer.mHeaderView;
        }

        public View getFooter() {
            return mDrawer.mFooterView;
        }

        public View getStickyFooter() {
            return mDrawer.mStickyFooterView;
        }

        public ActionBarDrawerToggle getActionBarDrawerToggle() {
            return mDrawer.mActionBarDrawerToggle;
        }

        /**
         * calculates the position of an drawerItem. searching by it's identifier
         *
         * @param drawerItem
         * @return
         */
        public int getPositionFromIdentifier(IDrawerItem drawerItem) {
            return getPositionFromIdentifier(drawerItem.getIdentifier());
        }

        /**
         * calculates the position of an drawerItem. searching by it's identifier
         *
         * @param identifier
         * @return
         */
        public int getPositionFromIdentifier(int identifier) {
            if (identifier > 0) {
                if (mDrawer.mDrawerItems != null) {
                    int position = 0;
                    for (IDrawerItem i : mDrawer.mDrawerItems) {
                        if (i.getIdentifier() == identifier) {
                            return position;
                        }
                        position = position + 1;
                    }
                }
            } else {
                throw new RuntimeException("the item requires a unique identifier to use this method");
            }

            return -1;
        }


        /**
         * set the current selection in the drawer
         * NOTE: This will trigger onDrawerItemSelected without a view!
         *
         * @param identifier
         */
        public void setSelectionByIdentifier(int identifier) {
            setSelection(getPositionFromIdentifier(identifier), true);
        }

        /**
         * set the current selection in the drawer
         * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
         *
         * @param identifier
         * @param fireOnClick
         */
        public void setSelectionByIdentifier(int identifier, boolean fireOnClick) {
            setSelection(getPositionFromIdentifier(identifier), fireOnClick);
        }

        /**
         * set the current selection in the drawer
         * NOTE: This will trigger onDrawerItemSelected without a view!
         *
         * @param drawerItem
         */
        public void setSelection(IDrawerItem drawerItem) {
            setSelection(getPositionFromIdentifier(drawerItem), true);
        }

        /**
         * set the current selection in the drawer
         * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
         *
         * @param drawerItem
         * @param fireOnClick
         */
        public void setSelection(IDrawerItem drawerItem, boolean fireOnClick) {
            setSelection(getPositionFromIdentifier(drawerItem), fireOnClick);
        }

        /**
         * set the current selection in the drawer
         * NOTE: This will trigger onDrawerItemSelected without a view!
         *
         * @param position the position to select
         */
        public void setSelection(int position) {
            setSelection(position, true);
        }

        /**
         * set the current selection in the drawer
         * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
         *
         * @param position
         * @param fireOnClick
         */
        public void setSelection(int position, boolean fireOnClick) {
            if (mDrawer.mListView != null) {
                mDrawer.mListView.setSelection(position + mDrawer.mHeaderOffset);
                mDrawer.mListView.setItemChecked(position + mDrawer.mHeaderOffset, true);

                if (fireOnClick && mDrawer.mOnDrawerItemClickListener != null) {
                    if (mDrawer.mDrawerItems != null && mDrawer.mDrawerItems.size() > position && position > -1) {
                        mDrawer.mOnDrawerItemClickListener.onItemClick(null, null, position, position, mDrawer.mDrawerItems.get(position));
                    } else {
                        mDrawer.mOnDrawerItemClickListener.onItemClick(null, null, position, position, null);
                    }
                }

                mDrawer.mCurrentSelection = position + mDrawer.mHeaderOffset;
            }
        }

        /**
         * update a specific drawer item :D
         * automatically identified by its id
         *
         * @param drawerItem
         */
        public void updateItem(IDrawerItem drawerItem) {
            updateItem(drawerItem, getPositionFromIdentifier(drawerItem));
        }

        /**
         * @param drawerItem
         * @param position
         */
        public void updateItem(IDrawerItem drawerItem, int position) {
            if (mDrawer.mDrawerItems != null && mDrawer.mDrawerItems.size() > position && position > -1) {
                mDrawer.mDrawerItems.set(position, drawerItem);
                mDrawer.mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * @param drawerItem
         */
        public void addItem(IDrawerItem drawerItem) {
            if (mDrawer.mDrawerItems != null) {
                mDrawer.mDrawerItems.add(drawerItem);
                mDrawer.mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * @param drawerItem
         * @param position
         */
        public void addItem(IDrawerItem drawerItem, int position) {
            if (mDrawer.mDrawerItems != null) {
                mDrawer.mDrawerItems.set(position, drawerItem);
                mDrawer.mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * @param position
         */
        public void removeItem(int position) {
            if (mDrawer.mDrawerItems != null && mDrawer.mDrawerItems.size() > position && position > -1) {
                mDrawer.mDrawerItems.remove(position);
                mDrawer.mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * @param drawerItems
         */
        public void addItems(IDrawerItem... drawerItems) {
            if (mDrawer.mDrawerItems != null) {
                Collections.addAll(mDrawer.mDrawerItems, drawerItems);
                mDrawer.mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * @param drawerItems
         */
        public void setItems(ArrayList<IDrawerItem> drawerItems) {
            mDrawer.mDrawerItems = drawerItems;
            mDrawer.mAdapter.notifyDataSetChanged();
        }

        /**
         * @param nameRes
         * @param position
         */
        public void updateName(int nameRes, int position) {
            if (mDrawer.mDrawerItems != null && mDrawer.mDrawerItems.size() > position && position > -1) {
                IDrawerItem drawerItem = mDrawer.mDrawerItems.get(position);

                if (drawerItem instanceof Nameable) {
                    ((Nameable) drawerItem).setNameRes(nameRes);
                }

                mDrawer.mDrawerItems.set(position, drawerItem);
                mDrawer.mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * @param name
         * @param position
         */
        public void updateName(String name, int position) {
            if (mDrawer.mDrawerItems != null && mDrawer.mDrawerItems.size() > position && position > -1) {
                IDrawerItem drawerItem = mDrawer.mDrawerItems.get(position);

                if (drawerItem instanceof Nameable) {
                    ((Nameable) drawerItem).setName(name);
                }

                mDrawer.mDrawerItems.set(position, drawerItem);
                mDrawer.mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * @param badge
         * @param position
         */
        public void updateBadge(String badge, int position) {
            if (mDrawer.mDrawerItems != null && mDrawer.mDrawerItems.size() > position && position > -1) {
                IDrawerItem drawerItem = mDrawer.mDrawerItems.get(position);

                if (drawerItem instanceof Badgeable) {
                    ((Badgeable) drawerItem).setBadge(badge);
                }

                mDrawer.mDrawerItems.set(position, drawerItem);
                mDrawer.mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * @param icon
         * @param position
         */
        public void updateIcon(Drawable icon, int position) {
            if (mDrawer.mDrawerItems != null && mDrawer.mDrawerItems.size() > position && position > -1) {
                IDrawerItem drawerItem = mDrawer.mDrawerItems.get(position);

                if (drawerItem instanceof Iconable) {
                    ((Iconable) drawerItem).setIcon(icon);
                }

                mDrawer.mDrawerItems.set(position, drawerItem);
                mDrawer.mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * @param icon
         * @param position
         */
        public void updateIcon(IIcon icon, int position) {
            if (mDrawer.mDrawerItems != null && mDrawer.mDrawerItems.size() > position && position > -1) {
                IDrawerItem drawerItem = mDrawer.mDrawerItems.get(position);

                if (drawerItem instanceof Iconable) {
                    ((Iconable) drawerItem).setIIcon(icon);
                }

                mDrawer.mDrawerItems.set(position, drawerItem);
                mDrawer.mAdapter.notifyDataSetChanged();
            }
        }

        public Bundle saveInstanceState(Bundle savedInstanceState) {
            if (savedInstanceState != null) {
                if (getListView() != null) {
                    savedInstanceState.putInt(BUNDLE_SELECTION, mDrawer.mCurrentSelection);
                }
            }
            return savedInstanceState;
        }
    }


    public interface OnDrawerItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem);
    }

    public interface OnDrawerItemLongClickListener {
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem);
    }

    public interface OnDrawerListener {
        public void onDrawerOpened(View drawerView);

        public void onDrawerClosed(View drawerView);
    }

    public interface OnDrawerItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem);

        public void onNothingSelected(AdapterView<?> parent);
    }
}
