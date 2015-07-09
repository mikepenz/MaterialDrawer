package com.mikepenz.materialdrawer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mikepenz.iconics.utils.Utils;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.adapter.BaseDrawerAdapter;
import com.mikepenz.materialdrawer.adapter.DrawerAdapter;
import com.mikepenz.materialdrawer.model.interfaces.Checkable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.UIUtils;
import com.mikepenz.materialdrawer.view.ScrimInsetsFrameLayout;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mikepenz on 23.05.15.
 */
public class DrawerBuilder {

    // some internal vars
    // variable to check if a builder is only used once
    protected boolean mUsed = false;
    protected int mCurrentSelection = -1;
    protected int mCurrentFooterSelection = -1;

    // the activity to use
    protected Activity mActivity;
    protected ViewGroup mRootView;
    protected ScrimInsetsFrameLayout mDrawerContentRoot;

    /**
     * default constructor
     */
    public DrawerBuilder() {

    }

    /**
     * constructor with activity instead of
     *
     * @param activity
     */
    public DrawerBuilder(Activity activity) {
        this.mRootView = (ViewGroup) activity.findViewById(android.R.id.content);
        this.mActivity = activity;
    }

    /**
     * Pass the activity you use the drawer in ;)
     * This is required if you want to set any values by resource
     *
     * @param activity
     * @return
     */
    public DrawerBuilder withActivity(Activity activity) {
        this.mRootView = (ViewGroup) activity.findViewById(android.R.id.content);
        this.mActivity = activity;
        return this;
    }

    /**
     * Pass the rootView of the DrawerBuilder which will be used to inflate the DrawerLayout in
     *
     * @param rootView
     * @return
     */
    public DrawerBuilder withRootView(ViewGroup rootView) {
        this.mRootView = rootView;

        //disable the translucent statusBar we don't need it
        withTranslucentStatusBar(false);

        return this;
    }

    /**
     * Pass the rootView as resource of the DrawerBuilder which will be used to inflate the DrawerLayout in
     *
     * @param rootViewRes
     * @return
     */
    public DrawerBuilder withRootView(int rootViewRes) {
        if (mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }

        return withRootView((ViewGroup) mActivity.findViewById(rootViewRes));
    }

    // set actionbar Compatibility mode
    protected boolean mTranslucentActionBarCompatibility = false;

    /**
     * Set this to true to use a translucent StatusBar in an activity with a good old
     * ActionBar. Should be a rare scenario.
     *
     * @param translucentActionBarCompatibility
     * @return
     */
    public DrawerBuilder withTranslucentActionBarCompatibility(boolean translucentActionBarCompatibility) {
        this.mTranslucentActionBarCompatibility = translucentActionBarCompatibility;
        return this;
    }

    /**
     * Set this to true if you want your drawer to be displayed below the toolbar.
     * Note this will add a margin above the drawer
     *
     * @param displayBelowToolbar
     * @return
     */
    public DrawerBuilder withDisplayBelowToolbar(boolean displayBelowToolbar) {
        this.mTranslucentActionBarCompatibility = displayBelowToolbar;
        return this;
    }

    // set non translucent statusBar mode
    protected boolean mTranslucentStatusBar = true;

    /**
     * Set to false to disable the use of a translucent statusBar
     *
     * @param translucentStatusBar
     * @return
     */
    public DrawerBuilder withTranslucentStatusBar(boolean translucentStatusBar) {
        this.mTranslucentStatusBar = translucentStatusBar;

        //if we disable the translucentStatusBar it should be disabled at all
        if (!translucentStatusBar) {
            this.mTranslucentStatusBarProgrammatically = false;
        }
        return this;
    }

    // set if we want to display the specific Drawer below the statusbar
    protected Boolean mDisplayBelowStatusBar;

    /**
     * set to true if the current drawer should be displayed below the statusBar
     *
     * @param displayBelowStatusBar
     * @return
     */
    public DrawerBuilder withDisplayBelowStatusBar(boolean displayBelowStatusBar) {
        this.mDisplayBelowStatusBar = displayBelowStatusBar;
        return this;
    }


    // set to disable the translucent statusBar Programmatically
    protected boolean mTranslucentStatusBarProgrammatically = true;

    /**
     * set this to false if you want no translucent statusBar. or
     * if you want to create this behavior only by theme.
     *
     * @param translucentStatusBarProgrammatically
     * @return
     */
    public DrawerBuilder withTranslucentStatusBarProgrammatically(boolean translucentStatusBarProgrammatically) {
        this.mTranslucentStatusBarProgrammatically = translucentStatusBarProgrammatically;
        //if we enable the programmatically translucent statusBar we want also the normal statusBar behavior
        if (translucentStatusBarProgrammatically) {
            this.mTranslucentStatusBar = true;
        }
        return this;
    }

    // defines if we want the statusBarShadow to be used in the Drawer
    protected Boolean mTranslucentStatusBarShadow = null;

    /**
     * set this to true or false if you want activate or deactivate this
     * set it to null if you want the default behavior (activated for lollipop and up)
     *
     * @param translucentStatusBarShadow
     * @return
     */
    public DrawerBuilder withTranslucentStatusBarShadow(Boolean translucentStatusBarShadow) {
        this.mTranslucentStatusBarShadow = translucentStatusBarShadow;
        return this;
    }


    // the toolbar of the activity
    protected Toolbar mToolbar;

    /**
     * Add the toolbar which is used in combination with this drawer.
     * NOTE: if you use the drawer in a subActivity you don't need this, if you
     * want to display the back arrow.
     *
     * @param toolbar
     * @return
     */
    public DrawerBuilder withToolbar(Toolbar toolbar) {
        this.mToolbar = toolbar;
        return this;
    }

    // set non translucent NavigationBar mode
    protected boolean mTranslucentNavigationBar = false;

    /**
     * Set to true if you use a translucent NavigationBar
     *
     * @param translucentNavigationBar
     * @return
     */
    public DrawerBuilder withTranslucentNavigationBar(boolean translucentNavigationBar) {
        this.mTranslucentNavigationBar = translucentNavigationBar;

        //if we disable the translucentNavigationBar it should be disabled at all
        if (!translucentNavigationBar) {
            this.mTranslucentNavigationBarProgrammatically = false;
        }

        return this;
    }

    // set to disable the translucent statusBar Programmatically
    protected boolean mTranslucentNavigationBarProgrammatically = false;

    /**
     * set this to true if you want a translucent navigation bar.
     *
     * @param translucentNavigationBarProgrammatically
     * @return
     */
    public DrawerBuilder withTranslucentNavigationBarProgrammatically(boolean translucentNavigationBarProgrammatically) {
        this.mTranslucentNavigationBarProgrammatically = translucentNavigationBarProgrammatically;
        //if we enable the programmatically translucent navigationBar we want also the normal navigationBar behavior
        if (translucentNavigationBarProgrammatically) {
            this.mTranslucentNavigationBar = true;
        }
        return this;
    }


    // set non translucent NavigationBar mode
    protected boolean mFullscreen = false;

    /**
     * Set to true if the used theme has a translucent statusBar
     * and navigationBar and you want to manage the padding on your own.
     *
     * @param fullscreen
     * @return
     */
    public DrawerBuilder withFullscreen(boolean fullscreen) {
        this.mFullscreen = fullscreen;

        if (fullscreen) {
            withTranslucentStatusBar(false);
            withTranslucentNavigationBar(false);
        }

        return this;
    }

    // a custom view to be used instead of everything else
    protected View mCustomView;

    /**
     * Pass a custom view if you need a completely custom drawer
     * content
     *
     * @param customView
     * @return
     */
    public DrawerBuilder withCustomView(View customView) {
        this.mCustomView = customView;
        return this;
    }

    // the drawerLayout to use
    protected DrawerLayout mDrawerLayout;
    protected RelativeLayout mSliderLayout;

    /**
     * Pass a custom DrawerLayout which will be used.
     * NOTE: This requires the same structure as the drawer.xml
     *
     * @param drawerLayout
     * @return
     */
    public DrawerBuilder withDrawerLayout(DrawerLayout drawerLayout) {
        this.mDrawerLayout = drawerLayout;
        return this;
    }

    /**
     * Pass a custom DrawerLayout Resource which will be used.
     * NOTE: This requires the same structure as the drawer.xml
     *
     * @param resLayout
     * @return
     */
    public DrawerBuilder withDrawerLayout(int resLayout) {
        if (mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }

        if (resLayout != -1) {
            this.mDrawerLayout = (DrawerLayout) mActivity.getLayoutInflater().inflate(resLayout, mRootView, false);
        } else {
            this.mDrawerLayout = (DrawerLayout) mActivity.getLayoutInflater().inflate(R.layout.material_drawer, mRootView, false);
        }

        return this;
    }

    //the statusBar color
    protected int mStatusBarColor = 0;
    protected int mStatusBarColorRes = -1;

    /**
     * Set the statusBarColor color for this activity
     *
     * @param statusBarColor
     * @return
     */
    public DrawerBuilder withStatusBarColor(int statusBarColor) {
        this.mStatusBarColor = statusBarColor;
        return this;
    }

    /**
     * Set the statusBarColor color for this activity from a resource
     *
     * @param statusBarColorRes
     * @return
     */
    public DrawerBuilder withStatusBarColorRes(int statusBarColorRes) {
        this.mStatusBarColorRes = statusBarColorRes;
        return this;
    }

    //the background color for the slider
    protected int mSliderBackgroundColor = 0;
    protected int mSliderBackgroundColorRes = -1;
    protected Drawable mSliderBackgroundDrawable = null;
    protected int mSliderBackgroundDrawableRes = -1;

    /**
     * Set the background color for the Slider.
     * This is the view containing the list.
     *
     * @param sliderBackgroundColor
     * @return
     */
    public DrawerBuilder withSliderBackgroundColor(int sliderBackgroundColor) {
        this.mSliderBackgroundColor = sliderBackgroundColor;
        return this;
    }

    /**
     * Set the background color for the Slider from a Resource.
     * This is the view containing the list.
     *
     * @param sliderBackgroundColorRes
     * @return
     */
    public DrawerBuilder withSliderBackgroundColorRes(int sliderBackgroundColorRes) {
        this.mSliderBackgroundColorRes = sliderBackgroundColorRes;
        return this;
    }


    /**
     * Set the background drawable for the Slider.
     * This is the view containing the list.
     *
     * @param sliderBackgroundDrawable
     * @return
     */
    public DrawerBuilder withSliderBackgroundDrawable(Drawable sliderBackgroundDrawable) {
        this.mSliderBackgroundDrawable = sliderBackgroundDrawable;
        return this;
    }


    /**
     * Set the background drawable for the Slider from a Resource.
     * This is the view containing the list.
     *
     * @param sliderBackgroundDrawableRes
     * @return
     */
    public DrawerBuilder withSliderBackgroundDrawableRes(int sliderBackgroundDrawableRes) {
        this.mSliderBackgroundDrawableRes = sliderBackgroundDrawableRes;
        return this;
    }

    //the width of the drawer
    protected int mDrawerWidth = -1;

    /**
     * Set the DrawerBuilder width with a pixel value
     *
     * @param drawerWidthPx
     * @return
     */
    public DrawerBuilder withDrawerWidthPx(int drawerWidthPx) {
        this.mDrawerWidth = drawerWidthPx;
        return this;
    }

    /**
     * Set the DrawerBuilder width with a dp value
     *
     * @param drawerWidthDp
     * @return
     */
    public DrawerBuilder withDrawerWidthDp(int drawerWidthDp) {
        if (mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }

        this.mDrawerWidth = Utils.convertDpToPx(mActivity, drawerWidthDp);
        return this;
    }

    /**
     * Set the DrawerBuilder width with a dimension resource
     *
     * @param drawerWidthRes
     * @return
     */
    public DrawerBuilder withDrawerWidthRes(int drawerWidthRes) {
        if (mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }

        this.mDrawerWidth = mActivity.getResources().getDimensionPixelSize(drawerWidthRes);
        return this;
    }

    //the gravity of the drawer
    protected Integer mDrawerGravity = GravityCompat.START;

    /**
     * Set the gravity for the drawer. START, LEFT | RIGHT, END
     *
     * @param gravity
     * @return
     */
    public DrawerBuilder withDrawerGravity(int gravity) {
        this.mDrawerGravity = gravity;
        return this;
    }

    //the account selection header to use
    protected AccountHeader mAccountHeader;
    protected boolean mAccountHeaderSticky = false;

    /**
     * Add a AccountSwitcherHeader which will be used in this drawer instance.
     * NOTE: This will overwrite any set headerView.
     *
     * @param accountHeader
     * @return
     */
    public DrawerBuilder withAccountHeader(AccountHeader accountHeader) {
        return withAccountHeader(accountHeader, false);
    }

    /**
     * Add a AccountSwitcherHeader which will be used in this drawer instance. Pass true if it should be sticky
     * NOTE: This will overwrite any set headerView or stickyHeaderView (depends on the boolean).
     *
     * @param accountHeader
     * @param accountHeaderSticky
     * @return
     */
    public DrawerBuilder withAccountHeader(AccountHeader accountHeader, boolean accountHeaderSticky) {
        this.mAccountHeader = accountHeader;
        this.mAccountHeaderSticky = accountHeaderSticky;

        //set the header offset
        if (!accountHeaderSticky) {
            mHeaderOffset = 1;
        }
        return this;
    }

    // enable/disable the actionBarDrawerToggle animation
    protected boolean mAnimateActionBarDrawerToggle = false;

    /**
     * Set this to true if you want the ActionBarDrawerToggle to be animated.
     * NOTE: This will only work if the built in ActionBarDrawerToggle is used.
     * Enable it by setting withActionBarDrawerToggle to true
     *
     * @param actionBarDrawerToggleAnimated
     * @return
     */
    public DrawerBuilder withActionBarDrawerToggleAnimated(boolean actionBarDrawerToggleAnimated) {
        this.mAnimateActionBarDrawerToggle = actionBarDrawerToggleAnimated;
        return this;
    }


    // enable the drawer toggle / if withActionBarDrawerToggle we will autoGenerate it
    protected boolean mActionBarDrawerToggleEnabled = true;

    /**
     * Set this to false if you don't need the included ActionBarDrawerToggle
     *
     * @param actionBarDrawerToggleEnabled
     * @return
     */
    public DrawerBuilder withActionBarDrawerToggle(boolean actionBarDrawerToggleEnabled) {
        this.mActionBarDrawerToggleEnabled = actionBarDrawerToggleEnabled;
        return this;
    }

    // drawer toggle
    protected ActionBarDrawerToggle mActionBarDrawerToggle;

    /**
     * Add a custom ActionBarDrawerToggle which will be used in combination with this drawer.
     *
     * @param actionBarDrawerToggle
     * @return
     */
    public DrawerBuilder withActionBarDrawerToggle(ActionBarDrawerToggle actionBarDrawerToggle) {
        this.mActionBarDrawerToggleEnabled = true;
        this.mActionBarDrawerToggle = actionBarDrawerToggle;
        return this;
    }

    // header view
    protected View mHeaderView;
    protected int mHeaderOffset = 0;
    protected boolean mHeaderDivider = true;
    protected boolean mHeaderClickable = false;

    /**
     * Add a header to the DrawerBuilder ListView. This can be any view
     *
     * @param headerView
     * @return
     */
    public DrawerBuilder withHeader(View headerView) {
        this.mHeaderView = headerView;
        //set the header offset
        mHeaderOffset = 1;
        return this;
    }

    /**
     * Add a header to the DrawerBuilder ListView defined by a resource.
     *
     * @param headerViewRes
     * @return
     */
    public DrawerBuilder withHeader(int headerViewRes) {
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

    /**
     * Set this to true if you want the header to be clickable
     *
     * @param headerClickable
     * @return
     */
    public DrawerBuilder withHeaderClickable(boolean headerClickable) {
        this.mHeaderClickable = headerClickable;
        return this;
    }

    /**
     * Set this to false if you don't need the divider below the header
     *
     * @param headerDivider
     * @return
     */
    public DrawerBuilder withHeaderDivider(boolean headerDivider) {
        this.mHeaderDivider = headerDivider;
        return this;
    }

    // sticky view
    protected View mStickyHeaderView;

    /**
     * Add a sticky header below the DrawerBuilder ListView. This can be any view
     *
     * @param stickyHeader
     * @return
     */
    public DrawerBuilder withStickyHeader(View stickyHeader) {
        this.mStickyHeaderView = stickyHeader;
        return this;
    }

    /**
     * Add a sticky header below the DrawerBuilder ListView defined by a resource.
     *
     * @param stickyHeaderRes
     * @return
     */
    public DrawerBuilder withStickyHeader(int stickyHeaderRes) {
        if (mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }

        if (stickyHeaderRes != -1) {
            //i know there should be a root, bit i got none here
            this.mStickyHeaderView = mActivity.getLayoutInflater().inflate(stickyHeaderRes, null, false);
        }

        return this;
    }

    // footer view
    protected View mFooterView;
    protected boolean mFooterDivider = true;
    protected boolean mFooterClickable = false;

    /**
     * Add a footer to the DrawerBuilder ListView. This can be any view
     *
     * @param footerView
     * @return
     */
    public DrawerBuilder withFooter(View footerView) {
        this.mFooterView = footerView;
        return this;
    }

    /**
     * Add a footer to the DrawerBuilder ListView defined by a resource.
     *
     * @param footerViewRes
     * @return
     */
    public DrawerBuilder withFooter(int footerViewRes) {
        if (mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }

        if (footerViewRes != -1) {
            //i know there should be a root, bit i got none here
            this.mFooterView = mActivity.getLayoutInflater().inflate(footerViewRes, null, false);
        }

        return this;
    }

    /**
     * Set this to true if you want the footer to be clickable
     *
     * @param footerClickable
     * @return
     */
    public DrawerBuilder withFooterClickable(boolean footerClickable) {
        this.mFooterClickable = footerClickable;
        return this;
    }

    /**
     * Set this to false if you don't need the divider above the footer
     *
     * @param footerDivider
     * @return
     */
    public DrawerBuilder withFooterDivider(boolean footerDivider) {
        this.mFooterDivider = footerDivider;
        return this;
    }

    // sticky view
    protected ViewGroup mStickyFooterView;
    protected Boolean mStickyFooterDivider = null;

    /**
     * Add a sticky footer below the DrawerBuilder ListView. This can be any view
     *
     * @param stickyFooter
     * @return
     */
    public DrawerBuilder withStickyFooter(ViewGroup stickyFooter) {
        this.mStickyFooterView = stickyFooter;
        return this;
    }

    /**
     * Add a sticky footer below the DrawerBuilder ListView defined by a resource.
     *
     * @param stickyFooterRes
     * @return
     */
    public DrawerBuilder withStickyFooter(int stickyFooterRes) {
        if (mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }

        if (stickyFooterRes != -1) {
            //i know there should be a root, bit i got none here
            this.mStickyFooterView = (ViewGroup) mActivity.getLayoutInflater().inflate(stickyFooterRes, null, false);
        }

        return this;
    }

    /**
     * Set this to false if you don't need the divider above the sticky footer
     *
     * @param stickyFooterDivider
     * @return
     */
    public DrawerBuilder withStickyFooterDivider(Boolean stickyFooterDivider) {
        this.mStickyFooterDivider = stickyFooterDivider;
        return this;
    }

    // fire onClick after build
    protected boolean mFireInitialOnClick = false;

    /**
     * Set this to true if you love to get an initial onClick event after the build method is called
     *
     * @param fireOnInitialOnClick
     * @return
     */
    public DrawerBuilder withFireOnInitialOnClick(boolean fireOnInitialOnClick) {
        this.mFireInitialOnClick = fireOnInitialOnClick;
        return this;
    }

    // item to select
    protected int mSelectedItem = 0;

    /**
     * Set this to the index of the item, you would love to select upon start
     *
     * @param selectedItem
     * @return
     */
    public DrawerBuilder withSelectedItem(int selectedItem) {
        this.mSelectedItem = selectedItem;
        return this;
    }

    // an ListView to use within the drawer :D
    protected ListView mListView;

    /**
     * Define a custom ListView which will be used in the drawer
     * NOTE: this is not recommended
     *
     * @param listView
     * @return
     */
    public DrawerBuilder withListView(ListView listView) {
        this.mListView = listView;
        return this;
    }

    // an adapter to use for the list
    protected BaseDrawerAdapter mAdapter;

    /**
     * Define a custom Adapter which will be used in the drawer
     * NOTE: this is not recommended
     *
     * @param adapter
     * @return
     */
    public DrawerBuilder withAdapter(BaseDrawerAdapter adapter) {
        this.mAdapter = adapter;
        return this;
    }

    // animate the drawerItems
    protected boolean mAnimateDrawerItems = false;

    /**
     * define if the items should be animated on their first view / and when switching the drawer
     *
     * @param animateDrawerItems
     * @return
     */
    public DrawerBuilder withAnimateDrawerItems(boolean animateDrawerItems) {
        this.mAnimateDrawerItems = animateDrawerItems;
        return this;
    }

    // list in drawer
    protected ArrayList<IDrawerItem> mDrawerItems = new ArrayList<>();

    /**
     * Set the initial List of IDrawerItems for the Drawer
     *
     * @param drawerItems
     * @return
     */
    public DrawerBuilder withDrawerItems(ArrayList<IDrawerItem> drawerItems) {
        this.mDrawerItems = drawerItems;
        return this;
    }

    /**
     * Add a initial DrawerItem or a DrawerItem Array  for the Drawer
     *
     * @param drawerItems
     * @return
     */
    public DrawerBuilder addDrawerItems(IDrawerItem... drawerItems) {
        if (this.mDrawerItems == null) {
            this.mDrawerItems = new ArrayList<>();
        }

        if (drawerItems != null) {
            Collections.addAll(this.mDrawerItems, drawerItems);
        }
        return this;
    }

    // always visible list in drawer
    protected ArrayList<IDrawerItem> mStickyDrawerItems = new ArrayList<>();

    /**
     * Set the initial List of IDrawerItems for the StickyDrawerFooter
     *
     * @param stickyDrawerItems
     * @return
     */
    public DrawerBuilder withStickyDrawerItems(ArrayList<IDrawerItem> stickyDrawerItems) {
        this.mStickyDrawerItems = stickyDrawerItems;
        return this;
    }

    /**
     * Add a initial DrawerItem or a DrawerItem Array for the StickyDrawerFooter
     *
     * @param stickyDrawerItems
     * @return
     */
    public DrawerBuilder addStickyDrawerItems(IDrawerItem... stickyDrawerItems) {
        if (this.mStickyDrawerItems == null) {
            this.mStickyDrawerItems = new ArrayList<>();
        }

        if (stickyDrawerItems != null) {
            Collections.addAll(this.mStickyDrawerItems, stickyDrawerItems);
        }
        return this;
    }

    // close drawer on click
    protected boolean mCloseOnClick = true;

    /**
     * Set this to false if the drawer should stay opened after an item was clicked
     *
     * @param closeOnClick
     * @return this
     */
    public DrawerBuilder withCloseOnClick(boolean closeOnClick) {
        this.mCloseOnClick = closeOnClick;
        return this;
    }

    // delay drawer close to prevent lag
    protected int mDelayOnDrawerClose = 150;

    /**
     * Define the delay for the drawer close operation after a click.
     * This is a small trick to improve the speed (and remove lag) if you open a new activity after a DrawerItem
     * was selected.
     * NOTE: Disable this by passing -1
     *
     * @param delayOnDrawerClose -1 to disable
     * @return this
     */
    public DrawerBuilder withDelayOnDrawerClose(int delayOnDrawerClose) {
        this.mDelayOnDrawerClose = delayOnDrawerClose;
        return this;
    }


    // onDrawerListener
    protected Drawer.OnDrawerListener mOnDrawerListener;

    /**
     * Define a OnDrawerListener for this Drawer
     *
     * @param onDrawerListener
     * @return this
     */
    public DrawerBuilder withOnDrawerListener(Drawer.OnDrawerListener onDrawerListener) {
        this.mOnDrawerListener = onDrawerListener;
        return this;
    }

    // onDrawerItemClickListeners
    protected Drawer.OnDrawerItemClickListener mOnDrawerItemClickListener;

    /**
     * Define a OnDrawerItemClickListener for this Drawer
     *
     * @param onDrawerItemClickListener
     * @return
     */
    public DrawerBuilder withOnDrawerItemClickListener(Drawer.OnDrawerItemClickListener onDrawerItemClickListener) {
        this.mOnDrawerItemClickListener = onDrawerItemClickListener;
        return this;
    }

    // onDrawerItemClickListeners
    protected Drawer.OnDrawerItemLongClickListener mOnDrawerItemLongClickListener;

    /**
     * Define a OnDrawerItemLongClickListener for this Drawer
     *
     * @param onDrawerItemLongClickListener
     * @return
     */
    public DrawerBuilder withOnDrawerItemLongClickListener(Drawer.OnDrawerItemLongClickListener onDrawerItemLongClickListener) {
        this.mOnDrawerItemLongClickListener = onDrawerItemLongClickListener;
        return this;
    }

    // onDrawerItemClickListeners
    protected Drawer.OnDrawerItemSelectedListener mOnDrawerItemSelectedListener;

    /**
     * Define a OnDrawerItemSelectedListener for this Drawer
     *
     * @param onDrawerItemSelectedListener
     * @return
     */
    public DrawerBuilder withOnDrawerItemSelectedListener(Drawer.OnDrawerItemSelectedListener onDrawerItemSelectedListener) {
        this.mOnDrawerItemSelectedListener = onDrawerItemSelectedListener;
        return this;
    }

    // onDrawerListener
    protected Drawer.OnDrawerNavigationListener mOnDrawerNavigationListener;

    /**
     * Define a OnDrawerNavigationListener for this Drawer
     *
     * @param onDrawerNavigationListener
     * @return this
     */
    public DrawerBuilder withOnDrawerNavigationListener(Drawer.OnDrawerNavigationListener onDrawerNavigationListener) {
        this.mOnDrawerNavigationListener = onDrawerNavigationListener;
        return this;
    }

    //show the drawer on the first launch to show the user its there
    protected boolean mShowDrawerOnFirstLaunch = false;

    /**
     * define if the DrawerBuilder is shown on the first launch
     *
     * @param showDrawerOnFirstLaunch
     * @return
     */
    public DrawerBuilder withShowDrawerOnFirstLaunch(boolean showDrawerOnFirstLaunch) {
        this.mShowDrawerOnFirstLaunch = showDrawerOnFirstLaunch;
        return this;
    }

    // savedInstance to restore state
    protected Bundle mSavedInstance;

    /**
     * Set the Bundle (savedInstance) which is passed by the activity.
     * No need to null-check everything is handled automatically
     *
     * @param savedInstance
     * @return
     */
    public DrawerBuilder withSavedInstance(Bundle savedInstance) {
        this.mSavedInstance = savedInstance;
        return this;
    }

    /**
     * helper method to handle when the drawer should be shown on the first launch
     */
    private void handleShowOnFirstLaunch() {
        //check if it should be shown on first launch (and we have a drawerLayout)
        if (mActivity != null && mDrawerLayout != null && mShowDrawerOnFirstLaunch) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
            //if it was not shown yet
            if (!preferences.getBoolean(Drawer.PREF_USER_LEARNED_DRAWER, false)) {
                //open the drawer
                mDrawerLayout.openDrawer(mSliderLayout);

                //save that it showed up once ;)
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(Drawer.PREF_USER_LEARNED_DRAWER, true);
                editor.apply();
            }
        }
    }

    /**
     * Build and add the DrawerBuilder to your activity
     *
     * @return
     */
    public Drawer build() {
        if (mUsed) {
            throw new RuntimeException("you must not reuse a DrawerBuilder builder");
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

        //check if the activity was initialized correctly
        if (mRootView == null || mRootView.getChildCount() == 0) {
            throw new RuntimeException("You have to set your layout for this activity with setContentView() first. Or you build the drawer on your own with .buildView()");
        }

        //get the content view
        View contentView = mRootView.getChildAt(0);
        boolean alreadyInflated = contentView instanceof DrawerLayout;

        //get the drawer root
        mDrawerContentRoot = (ScrimInsetsFrameLayout) mDrawerLayout.getChildAt(0);

        //do some magic specific to the statusBar
        if (!alreadyInflated && mTranslucentStatusBar) {
            if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
                DrawerUtils.setTranslucentStatusFlag(mActivity, true);
            }
            if (Build.VERSION.SDK_INT >= 19) {
                if (mTranslucentStatusBarProgrammatically) {
                    mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }
            }
            if (Build.VERSION.SDK_INT >= 21) {
                DrawerUtils.setTranslucentStatusFlag(mActivity, false);
                if (mTranslucentStatusBarProgrammatically) {
                    mActivity.getWindow().setStatusBarColor(Color.TRANSPARENT);
                }
            }
            mDrawerContentRoot.setPadding(0, UIUtils.getStatusBarHeight(mActivity), 0, 0);

            // define the statusBarColor
            if (mStatusBarColor == 0 && mStatusBarColorRes != -1) {
                mStatusBarColor = mActivity.getResources().getColor(mStatusBarColorRes);
            } else if (mStatusBarColor == 0) {
                mStatusBarColor = UIUtils.getThemeColorFromAttrOrRes(mActivity, R.attr.colorPrimaryDark, R.color.material_drawer_primary_dark);
            }
            mDrawerContentRoot.setInsetForeground(mStatusBarColor);
        }

        //do some magic specific to the navigationBar
        if (!alreadyInflated && mTranslucentNavigationBar) {
            if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
                DrawerUtils.setTranslucentNavigationFlag(mActivity, true);
            }
            if (Build.VERSION.SDK_INT >= 19) {
                if (mTranslucentNavigationBarProgrammatically) {
                    mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    DrawerUtils.setTranslucentNavigationFlag(mActivity, true);
                }
            }
            if (Build.VERSION.SDK_INT >= 21) {
                if (mTranslucentNavigationBarProgrammatically) {
                    mActivity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
                }
            }
        }

        //if we are fullscreen disable the ScrimInsetsLayout
        if (mFullscreen && Build.VERSION.SDK_INT >= 19) {
            mDrawerContentRoot.setEnabled(false);
        }

        //only add the new layout if it wasn't done before
        if (!alreadyInflated) {
            // remove the contentView
            mRootView.removeView(contentView);
        } else {
            //if it was already inflated we have to clean up again
            mRootView.removeAllViews();
        }

        //create the layoutParams to use for the contentView
        FrameLayout.LayoutParams layoutParamsContentView = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        //if we have a translucent navigation bar set the bottom margin
        if (mTranslucentNavigationBar && Build.VERSION.SDK_INT >= 19) {
            layoutParamsContentView.bottomMargin = UIUtils.getNavigationBarHeight(mActivity);
        }

        //add the contentView to the drawer content frameLayout
        mDrawerContentRoot.addView(contentView, layoutParamsContentView);

        //add the drawerLayout to the root
        mRootView.addView(mDrawerLayout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        //set the navigationOnClickListener
        final View.OnClickListener toolbarNavigationListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean handled = false;

                if (mOnDrawerNavigationListener != null && (mActionBarDrawerToggle != null && !mActionBarDrawerToggle.isDrawerIndicatorEnabled())) {
                    handled = mOnDrawerNavigationListener.onNavigationClickListener(v);
                }
                if (!handled) {
                    if (mDrawerLayout.isDrawerOpen(mDrawerGravity)) {
                        mDrawerLayout.closeDrawer(mDrawerGravity);
                    } else {
                        mDrawerLayout.openDrawer(mDrawerGravity);
                    }
                }
            }
        };

        // create the ActionBarDrawerToggle if not set and enabled and if we have a toolbar
        if (mActionBarDrawerToggleEnabled && mActionBarDrawerToggle == null && mToolbar != null) {
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

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    if (mOnDrawerListener != null) {
                        mOnDrawerListener.onDrawerSlide(drawerView, slideOffset);
                    }

                    if (!mAnimateActionBarDrawerToggle) {
                        super.onDrawerSlide(drawerView, 0);
                    } else {
                        super.onDrawerSlide(drawerView, slideOffset);
                    }
                }
            };
            this.mActionBarDrawerToggle.syncState();
        }

        //if we got a toolbar set a toolbarNavigationListener
        //we also have to do this after setting the ActionBarDrawerToggle as this will overwrite this
        if (mToolbar != null) {
            this.mToolbar.setNavigationOnClickListener(toolbarNavigationListener);
        }

        //handle the ActionBarDrawerToggle
        if (mActionBarDrawerToggle != null) {
            mActionBarDrawerToggle.setToolbarNavigationClickListener(toolbarNavigationListener);
            mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        } else {
            mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    if (mOnDrawerListener != null) {
                        mOnDrawerListener.onDrawerSlide(drawerView, slideOffset);
                    }
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    if (mOnDrawerListener != null) {
                        mOnDrawerListener.onDrawerOpened(drawerView);
                    }
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    if (mOnDrawerListener != null) {
                        mOnDrawerListener.onDrawerClosed(drawerView);
                    }
                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            });
        }

        //build the view which will be set to the drawer
        Drawer result = buildView();

        // add the slider to the drawer
        mDrawerLayout.addView(mSliderLayout, 1);

        return result;
    }

    /**
     * build the drawers content only. This will still return a Result object, but only with the content set. No inflating of a DrawerLayout.
     *
     * @return Result object with only the content set
     */
    public Drawer buildView() {
        // get the slider view
        mSliderLayout = (RelativeLayout) mActivity.getLayoutInflater().inflate(R.layout.material_drawer_slider, mDrawerLayout, false);
        mSliderLayout.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(mActivity, R.attr.material_drawer_background, R.color.material_drawer_background));
        // get the layout params
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mSliderLayout.getLayoutParams();
        if (params != null) {
            // if we've set a custom gravity set it
            params.gravity = mDrawerGravity;
            // if this is a drawer from the right, change the margins :D
            params = DrawerUtils.processDrawerLayoutParams(this, params);
            // set the new layout params
            mSliderLayout.setLayoutParams(params);
        }

        // set the background
        if (mSliderBackgroundColor != 0) {
            mSliderLayout.setBackgroundColor(mSliderBackgroundColor);
        } else if (mSliderBackgroundColorRes != -1) {
            mSliderLayout.setBackgroundColor(mActivity.getResources().getColor(mSliderBackgroundColorRes));
        } else if (mSliderBackgroundDrawable != null) {
            UIUtils.setBackground(mSliderLayout, mSliderBackgroundDrawable);
        } else if (mSliderBackgroundDrawableRes != -1) {
            UIUtils.setBackground(mSliderLayout, mSliderBackgroundColorRes);
        }

        //set the shadow for the drawer
        if (Build.VERSION.SDK_INT < 21) {
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, mDrawerGravity);
        }

        //create the content
        createContent();

        //create the result object
        Drawer result = new Drawer(this);
        //set the drawer for the accountHeader if set
        if (mAccountHeader != null) {
            mAccountHeader.setDrawer(result);
        }

        //handle if the drawer should be shown on first launch
        handleShowOnFirstLaunch();

        //forget the reference to the activity
        mActivity = null;

        return result;
    }

    /**
     * Call this method to append a new DrawerBuilder to a existing Drawer.
     *
     * @param result the Drawer.Result of an existing Drawer
     * @return
     */
    public Drawer append(Drawer result) {
        if (mUsed) {
            throw new RuntimeException("you must not reuse a DrawerBuilder builder");
        }
        if (mDrawerGravity == null) {
            throw new RuntimeException("please set the gravity for the drawer");
        }

        //set that this builder was used. now you have to create a new one
        mUsed = true;

        //get the drawer layout from the previous drawer
        mDrawerLayout = result.getDrawerLayout();

        // get the slider view
        mSliderLayout = (RelativeLayout) mActivity.getLayoutInflater().inflate(R.layout.material_drawer_slider, mDrawerLayout, false);
        mSliderLayout.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(mActivity, R.attr.material_drawer_background, R.color.material_drawer_background));
        // get the layout params
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mSliderLayout.getLayoutParams();
        // set the gravity of this drawerGravity
        params.gravity = mDrawerGravity;
        // if this is a drawer from the right, change the margins :D
        params = DrawerUtils.processDrawerLayoutParams(this, params);
        // set the new params
        mSliderLayout.setLayoutParams(params);
        // add the slider to the drawer
        mDrawerLayout.addView(mSliderLayout, 1);

        //create the content
        createContent();

        //forget the reference to the activity
        mActivity = null;

        return new Drawer(this);
    }

    /**
     * the helper method to create the content for the drawer
     */
    private void createContent() {
        //if we have a customView use this
        if (mCustomView != null) {
            LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            contentParams.weight = 1f;
            mSliderLayout.addView(mCustomView, contentParams);
            return;
        }

        // if we have an adapter (either by defining a custom one or the included one add a list :D
        if (mListView == null) {
            mListView = new ListView(mActivity);
            mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            mListView.setDivider(null);
            //some style improvements on older devices
            mListView.setFadingEdgeLength(0);
            mListView.setCacheColorHint(Color.TRANSPARENT);
            //set the drawing cache background to the same color as the slider to improve performance
            mListView.setDrawingCacheBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(mActivity, R.attr.material_drawer_background, R.color.material_drawer_background));
            //only draw the selector on top if we are on a newer api than 21 because this makes only sense for ripples
            if (Build.VERSION.SDK_INT > 21) {
                mListView.setDrawSelectorOnTop(true);
            }
            mListView.setClipToPadding(false);

            int paddingTop = 0;
            if ((mTranslucentStatusBar && !mTranslucentActionBarCompatibility) || mFullscreen) {
                paddingTop = UIUtils.getStatusBarHeight(mActivity);
            }
            int paddingBottom = 0;
            if ((mTranslucentNavigationBar || mFullscreen) && Build.VERSION.SDK_INT >= 19) {
                paddingBottom = UIUtils.getNavigationBarHeight(mActivity);
            }

            mListView.setPadding(0, paddingTop, 0, paddingBottom);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.weight = 1f;
        mSliderLayout.addView(mListView, params);

        //find the shadow view
        View statusBarShadow = mSliderLayout.findViewById(R.id.shadow_top);
        RelativeLayout.LayoutParams shadowLayoutParams = (RelativeLayout.LayoutParams) statusBarShadow.getLayoutParams();
        shadowLayoutParams.height = UIUtils.getStatusBarHeight(mActivity, true);
        statusBarShadow.setLayoutParams(shadowLayoutParams);

        //some extra stuff to beautify the whole thing ;)
        if ((mTranslucentStatusBar && !mTranslucentActionBarCompatibility) || (mTranslucentStatusBarShadow != null && mTranslucentStatusBarShadow)) {
            if (mTranslucentStatusBarShadow == null) {
                //if we use the default behavior show it only if we are above API Level 20
                if (Build.VERSION.SDK_INT > 20) {
                    //bring shadow bar to front again
                    statusBarShadow.bringToFront();
                } else {
                    //disable the shadow if  we are on a lower sdk
                    statusBarShadow.setVisibility(View.GONE);
                }
            } else {
                //bring shadow bar to front again
                statusBarShadow.bringToFront();
            }
        } else {
            //disable the shadow if we don't use a translucent activity
            statusBarShadow.setVisibility(View.GONE);
        }

        // initialize list if there is an adapter or set items
        if (mDrawerItems != null && mAdapter == null) {
            mAdapter = new DrawerAdapter(mActivity, mDrawerItems, mAnimateDrawerItems);
        }

        //handle the header
        DrawerUtils.handleHeaderView(this);

        //handle the footer
        DrawerUtils.handleFooterView(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IDrawerItem drawerItem = (IDrawerItem) v.getTag();
                DrawerUtils.onFooterDrawerItemClick(DrawerBuilder.this, drawerItem, v, true);
            }
        });

        //after adding the header do the setAdapter and set the selection
        if (mAdapter != null) {
            //set the adapter on the listView
            mListView.setAdapter(mAdapter);

            //predefine selection (should be the first element
            DrawerUtils.setListSelection(this, mSelectedItem, false);
        }

        // add the onDrawerItemClickListener if set
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IDrawerItem i = getDrawerItem(position, true);

                if (i != null && i instanceof Checkable && !((Checkable) i).isCheckable()) {
                    mListView.setSelection(mCurrentSelection + mHeaderOffset);
                    mListView.setItemChecked(mCurrentSelection + mHeaderOffset, true);
                } else {
                    resetStickyFooterSelection();
                    mCurrentSelection = position - mHeaderOffset;
                    mCurrentFooterSelection = -1;
                }

                boolean consumed = false;
                if (mOnDrawerItemClickListener != null) {
                    consumed = mOnDrawerItemClickListener.onItemClick(parent, view, position - mHeaderOffset, id, i);
                }

                if (!consumed) {
                    //close the drawer after click
                    closeDrawerDelayed();
                }
            }
        });

        // add the onDrawerItemLongClickListener if set
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnDrawerItemLongClickListener != null) {
                    return mOnDrawerItemLongClickListener.onItemLongClick(parent, view, position - mHeaderOffset, id, getDrawerItem(position, true));
                }
                return false;
            }
        });

        // add the onDrawerItemSelectedListener if set
        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mOnDrawerItemSelectedListener != null) {
                    mOnDrawerItemSelectedListener.onItemSelected(parent, view, position - mHeaderOffset, id, getDrawerItem(position, true));
                }
                mCurrentSelection = position - mHeaderOffset;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (mOnDrawerItemSelectedListener != null) {
                    mOnDrawerItemSelectedListener.onNothingSelected(parent);
                }
            }
        });

        if (mListView != null) {
            mListView.smoothScrollToPosition(0);
        }

        // try to restore all saved values again
        if (mSavedInstance != null) {
            int selection = mSavedInstance.getInt(Drawer.BUNDLE_SELECTION, -1);
            DrawerUtils.setListSelection(this, selection, false);
            int footerSelection = mSavedInstance.getInt(Drawer.BUNDLE_FOOTER_SELECTION, -1);
            DrawerUtils.setFooterSelection(this, footerSelection, false);
        }

        // call initial onClick event to allow the dev to init the first view
        if (mFireInitialOnClick && mOnDrawerItemClickListener != null) {
            mOnDrawerItemClickListener.onItemClick(null, null, mCurrentSelection, mCurrentSelection, getDrawerItem(mCurrentSelection, false));
        }
    }

    /**
     * helper method to close the drawer delayed
     */
    protected void closeDrawerDelayed() {
        if (mCloseOnClick && mDrawerLayout != null) {
            if (mDelayOnDrawerClose > -1) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDrawerLayout.closeDrawers();
                    }
                }, mDelayOnDrawerClose);
            } else {
                mDrawerLayout.closeDrawers();
            }
        }
    }

    /**
     * get the drawerItem at a specific position
     *
     * @param position
     * @return
     */
    protected IDrawerItem getDrawerItem(int position, boolean includeOffset) {
        if (includeOffset) {
            if (mDrawerItems != null && mDrawerItems.size() > (position - mHeaderOffset) && (position - mHeaderOffset) > -1) {
                return mDrawerItems.get(position - mHeaderOffset);
            }
        } else {
            if (mDrawerItems != null && mDrawerItems.size() > position && position > -1) {
                return mDrawerItems.get(position);
            }
        }
        return null;
    }

    /**
     * check if the item is within the bounds of the list
     *
     * @param position
     * @param includeOffset
     * @return
     */
    protected boolean checkDrawerItem(int position, boolean includeOffset) {
        if (includeOffset) {
            if (mDrawerItems != null && mDrawerItems.size() > (position - mHeaderOffset) && (position - mHeaderOffset) > -1) {
                return true;
            }
        } else {
            if (mDrawerItems != null && mDrawerItems.size() > position && position > -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * simple helper method to reset the selection of the sticky footer
     */
    protected void resetStickyFooterSelection() {
        if (mStickyFooterView instanceof LinearLayout) {
            for (int i = 0; i < ((LinearLayout) mStickyFooterView).getChildCount(); i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    ((LinearLayout) mStickyFooterView).getChildAt(i).setActivated(false);
                }
                ((LinearLayout) mStickyFooterView).getChildAt(i).setSelected(false);
            }
        }
    }
}
