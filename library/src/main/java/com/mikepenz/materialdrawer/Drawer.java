package com.mikepenz.materialdrawer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.utils.Utils;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.adapter.BaseDrawerAdapter;
import com.mikepenz.materialdrawer.adapter.DrawerAdapter;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.Checkable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Iconable;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.KeyboardUtil;
import com.mikepenz.materialdrawer.util.UIUtils;
import com.mikepenz.materialdrawer.view.ScrimInsetsFrameLayout;

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
    protected ScrimInsetsFrameLayout mDrawerContentRoot;

    /**
     * Pass the activity you use the drawer in ;)
     * This is required if you want to set any values by resource
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
    protected boolean mTranslucentActionBarCompatibility = false;

    /**
     * Set this to true to use a translucent StatusBar in an activity with a good old
     * ActionBar. Should be a rare scenario.
     *
     * @param translucentActionBarCompatibility
     * @return
     */
    public Drawer withTranslucentActionBarCompatibility(boolean translucentActionBarCompatibility) {
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
    public Drawer withDisplayBelowToolbar(boolean displayBelowToolbar) {
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
    public Drawer withTranslucentStatusBar(boolean translucentStatusBar) {
        this.mTranslucentStatusBar = translucentStatusBar;

        //if we disable the translucentStatusBar it should be disabled at all
        if (!translucentStatusBar) {
            this.mTranslucentStatusBarProgrammatically = false;
        }
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
    public Drawer withTranslucentStatusBarProgrammatically(boolean translucentStatusBarProgrammatically) {
        this.mTranslucentStatusBarProgrammatically = translucentStatusBarProgrammatically;
        //if we enable the programmatically translucent statusBar we want also the normal statusBar behavior
        if (translucentStatusBarProgrammatically) {
            this.mTranslucentStatusBar = true;
        }
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
    public Drawer withToolbar(Toolbar toolbar) {
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
    public Drawer withTranslucentNavigationBar(boolean translucentNavigationBar) {
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
    public Drawer withTranslucentNavigationBarProgrammatically(boolean translucentNavigationBarProgrammatically) {
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
    public Drawer withFullscreen(boolean fullscreen) {
        this.mFullscreen = fullscreen;

        if (fullscreen) {
            withTranslucentStatusBar(false);
            withTranslucentNavigationBar(false);
        }

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
    public Drawer withDrawerLayout(DrawerLayout drawerLayout) {
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
    public Drawer withDrawerLayout(int resLayout) {
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
    public Drawer withStatusBarColor(int statusBarColor) {
        this.mStatusBarColor = statusBarColor;
        return this;
    }

    /**
     * Set the statusBarColor color for this activity from a resource
     *
     * @param statusBarColorRes
     * @return
     */
    public Drawer withStatusBarColorRes(int statusBarColorRes) {
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
    public Drawer withSliderBackgroundColor(int sliderBackgroundColor) {
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
    public Drawer withSliderBackgroundColorRes(int sliderBackgroundColorRes) {
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
    public Drawer withSliderBackgroundDrawable(Drawable sliderBackgroundDrawable) {
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
    public Drawer withSliderBackgroundDrawableRes(int sliderBackgroundDrawableRes) {
        this.mSliderBackgroundDrawableRes = sliderBackgroundDrawableRes;
        return this;
    }

    //the width of the drawer
    protected int mDrawerWidth = -1;

    /**
     * Set the Drawer width with a pixel value
     *
     * @param drawerWidthPx
     * @return
     */
    public Drawer withDrawerWidthPx(int drawerWidthPx) {
        this.mDrawerWidth = drawerWidthPx;
        return this;
    }

    /**
     * Set the Drawer width with a dp value
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
     * Set the Drawer width with a dimension resource
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

    /**
     * Set the gravity for the drawer. START, LEFT | RIGHT, END
     *
     * @param gravity
     * @return
     */
    public Drawer withDrawerGravity(int gravity) {
        this.mDrawerGravity = gravity;
        return this;
    }

    //the account selection header to use
    protected AccountHeader.Result mAccountHeader;
    protected boolean mAccountHeaderSticky = false;

    /**
     * Add a AccountSwitcherHeader which will be used in this drawer instance.
     * NOTE: This will overwrite any set headerView.
     *
     * @param accountHeader
     * @return
     */
    public Drawer withAccountHeader(AccountHeader.Result accountHeader) {
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
    public Drawer withAccountHeader(AccountHeader.Result accountHeader, boolean accountHeaderSticky) {
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
    public Drawer withActionBarDrawerToggleAnimated(boolean actionBarDrawerToggleAnimated) {
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
    public Drawer withActionBarDrawerToggle(boolean actionBarDrawerToggleEnabled) {
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
    public Drawer withActionBarDrawerToggle(ActionBarDrawerToggle actionBarDrawerToggle) {
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
     * Add a header to the Drawer ListView. This can be any view
     *
     * @param headerView
     * @return
     */
    public Drawer withHeader(View headerView) {
        this.mHeaderView = headerView;
        //set the header offset
        mHeaderOffset = 1;
        return this;
    }

    /**
     * Add a header to the Drawer ListView defined by a resource.
     *
     * @param headerViewRes
     * @return
     */
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

    /**
     * Set this to true if you want the header to be clickable
     *
     * @param headerClickable
     * @return
     */
    public Drawer withHeaderClickable(boolean headerClickable) {
        this.mHeaderClickable = headerClickable;
        return this;
    }

    /**
     * Set this to false if you don't need the divider below the header
     *
     * @param headerDivider
     * @return
     */
    public Drawer withHeaderDivider(boolean headerDivider) {
        this.mHeaderDivider = headerDivider;
        return this;
    }

    // sticky view
    protected View mStickyHeaderView;

    /**
     * Add a sticky header below the Drawer ListView. This can be any view
     *
     * @param stickyHeader
     * @return
     */
    public Drawer withStickyHeader(View stickyHeader) {
        this.mStickyHeaderView = stickyHeader;
        return this;
    }

    /**
     * Add a sticky header below the Drawer ListView defined by a resource.
     *
     * @param stickyHeaderRes
     * @return
     */
    public Drawer withStickyHeader(int stickyHeaderRes) {
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
     * Add a footer to the Drawer ListView. This can be any view
     *
     * @param footerView
     * @return
     */
    public Drawer withFooter(View footerView) {
        this.mFooterView = footerView;
        return this;
    }

    /**
     * Add a footer to the Drawer ListView defined by a resource.
     *
     * @param footerViewRes
     * @return
     */
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

    /**
     * Set this to true if you want the footer to be clickable
     *
     * @param footerClickable
     * @return
     */
    public Drawer withFooterClickable(boolean footerClickable) {
        this.mFooterClickable = footerClickable;
        return this;
    }

    /**
     * Set this to false if you don't need the divider above the footer
     *
     * @param footerDivider
     * @return
     */
    public Drawer withFooterDivider(boolean footerDivider) {
        this.mFooterDivider = footerDivider;
        return this;
    }

    // sticky view
    protected View mStickyFooterView;
    protected boolean mStickyFooterDivider = true;

    /**
     * Add a sticky footer below the Drawer ListView. This can be any view
     *
     * @param stickyFooter
     * @return
     */
    public Drawer withStickyFooter(View stickyFooter) {
        this.mStickyFooterView = stickyFooter;
        return this;
    }

    /**
     * Add a sticky footer below the Drawer ListView defined by a resource.
     *
     * @param stickyFooterRes
     * @return
     */
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

    /**
     * Set this to false if you don't need the divider above the sticky footer
     *
     * @param stickyFooterDivider
     * @return
     */
    public Drawer withStickyFooterDivider(boolean stickyFooterDivider) {
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
    public Drawer withFireOnInitialOnClick(boolean fireOnInitialOnClick) {
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
    public Drawer withSelectedItem(int selectedItem) {
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
    public Drawer withListView(ListView listView) {
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
    public Drawer withAdapter(BaseDrawerAdapter adapter) {
        this.mAdapter = adapter;
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
    public Drawer withDrawerItems(ArrayList<IDrawerItem> drawerItems) {
        this.mDrawerItems = drawerItems;
        return this;
    }

    /**
     * Add a initial DrawerItem or a DrawerItem Array  for the Drawer
     *
     * @param drawerItems
     * @return
     */
    public Drawer addDrawerItems(IDrawerItem... drawerItems) {
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
    public Drawer withStickyDrawerItems(ArrayList<IDrawerItem> stickyDrawerItems) {
        this.mStickyDrawerItems = stickyDrawerItems;
        return this;
    }

    /**
     * Add a initial DrawerItem or a DrawerItem Array for the StickyDrawerFooter
     *
     * @param stickyDrawerItems
     * @return
     */
    public Drawer addStickyDrawerItems(IDrawerItem... stickyDrawerItems) {
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
    public Drawer withCloseOnClick(boolean closeOnClick) {
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
    public Drawer withDelayOnDrawerClose(int delayOnDrawerClose) {
        this.mDelayOnDrawerClose = delayOnDrawerClose;
        return this;
    }


    // onDrawerListener
    protected OnDrawerListener mOnDrawerListener;

    /**
     * Define a OnDrawerListener for this Drawer
     *
     * @param onDrawerListener
     * @return this
     */
    public Drawer withOnDrawerListener(OnDrawerListener onDrawerListener) {
        this.mOnDrawerListener = onDrawerListener;
        return this;
    }

    // onDrawerItemClickListeners
    protected OnDrawerItemClickListener mOnDrawerItemClickListener;

    /**
     * Define a OnDrawerItemClickListener for this Drawer
     *
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
     * Define a OnDrawerItemLongClickListener for this Drawer
     *
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
     * Define a OnDrawerItemSelectedListener for this Drawer
     *
     * @param onDrawerItemSelectedListener
     * @return
     */
    public Drawer withOnDrawerItemSelectedListener(OnDrawerItemSelectedListener onDrawerItemSelectedListener) {
        this.mOnDrawerItemSelectedListener = onDrawerItemSelectedListener;
        return this;
    }

    // onDrawerListener
    protected OnDrawerNavigationListener mOnDrawerNavigationListener;

    /**
     * Define a OnDrawerNavigationListener for this Drawer
     *
     * @param onDrawerNavigationListener
     * @return this
     */
    public Drawer withOnDrawerNavigationListener(OnDrawerNavigationListener onDrawerNavigationListener) {
        this.mOnDrawerNavigationListener = onDrawerNavigationListener;
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
    public Drawer withSavedInstance(Bundle savedInstance) {
        this.mSavedInstance = savedInstance;
        return this;
    }


    /**
     * helper method to set the TranslucentStatusFlag
     *
     * @param on
     */
    private void setTranslucentStatusFlag(boolean on) {
        if (Build.VERSION.SDK_INT >= 19) {
            setFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, on);
        }
    }

    /**
     * helper method to set the TranslucentNavigationFlag
     *
     * @param on
     */
    private void setTranslucentNavigationFlag(boolean on) {
        if (Build.VERSION.SDK_INT >= 19) {
            setFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, on);
        }
    }

    /**
     * helper method to activate or deactivate a specific flag
     *
     * @param bits
     * @param on
     */
    private void setFlag(final int bits, boolean on) {
        Window win = mActivity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * Build and add the Drawer to your activity
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

        //get the content view
        View contentView = mRootView.getChildAt(0);
        boolean alreadyInflated = contentView instanceof DrawerLayout;

        //get the drawer root
        mDrawerContentRoot = (ScrimInsetsFrameLayout) mDrawerLayout.getChildAt(0);

        //do some magic specific to the statusBar
        if (!alreadyInflated && mTranslucentStatusBar) {
            if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
                setTranslucentStatusFlag(true);
            }
            if (Build.VERSION.SDK_INT >= 19) {
                if (mTranslucentStatusBarProgrammatically) {
                    mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }
            }
            if (Build.VERSION.SDK_INT >= 21) {
                setTranslucentStatusFlag(false);
                if (mTranslucentStatusBarProgrammatically) {
                    mActivity.getWindow().setStatusBarColor(Color.TRANSPARENT);
                }
            }
            mDrawerContentRoot.setPadding(0, mActivity.getResources().getDimensionPixelSize(R.dimen.tool_bar_top_padding), 0, 0);

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
                setTranslucentNavigationFlag(true);
            }
            if (Build.VERSION.SDK_INT >= 19) {
                if (mTranslucentNavigationBarProgrammatically) {
                    mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    setTranslucentNavigationFlag(true);
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
        View.OnClickListener toolbarNavigationListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean handled = false;

                if (mOnDrawerNavigationListener != null && (mActionBarDrawerToggle != null && !mActionBarDrawerToggle.isDrawerIndicatorEnabled())) {
                    handled = mOnDrawerNavigationListener.onNavigationClickListener(v);
                }
                if (!handled) {
                    if (mDrawerLayout.isDrawerOpen(mSliderLayout)) {
                        mDrawerLayout.closeDrawer(mSliderLayout);
                    } else {
                        mDrawerLayout.openDrawer(mSliderLayout);
                    }
                }
            }
        };

        //if we got a toolbar set a toolbarNavigationListener
        if (mToolbar != null) {
            this.mToolbar.setNavigationOnClickListener(toolbarNavigationListener);
        }

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
                    if (!mAnimateActionBarDrawerToggle) {
                        super.onDrawerSlide(drawerView, 0);
                    } else {
                        super.onDrawerSlide(drawerView, slideOffset);
                    }
                }
            };
            this.mActionBarDrawerToggle.syncState();

        }

        //handle the ActionBarDrawerToggle
        if (mActionBarDrawerToggle != null) {
            mActionBarDrawerToggle.setToolbarNavigationClickListener(toolbarNavigationListener);
            mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        }

        //build the view which will be set to the drawer
        Result result = buildView();

        // add the slider to the drawer
        mDrawerLayout.addView(mSliderLayout, 1);

        return result;
    }

    public Result buildView() {
        // get the slider view
        mSliderLayout = (RelativeLayout) mActivity.getLayoutInflater().inflate(R.layout.material_drawer_slider, mDrawerLayout, false);
        mSliderLayout.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(mActivity, R.attr.material_drawer_background, R.color.material_drawer_background));
        // get the layout params
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mSliderLayout.getLayoutParams();
        if (params != null) {
            // if we've set a custom gravity set it
            if (mDrawerGravity != null) {
                params.gravity = mDrawerGravity;
            }
            // if this is a drawer from the right, change the margins :D
            params = processDrawerLayoutParams(params);
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

        //create the content
        createContent();

        //create the result object
        Result result = new Result(this);
        //set the drawer for the accountHeader if set
        if (mAccountHeader != null) {
            mAccountHeader.setDrawer(result);
        }

        //forget the reference to the activity
        mActivity = null;

        return result;
    }


    /**
     * Call this method to append a new Drawer to a existing Drawer.
     *
     * @param result the Drawer.Result of an existing Drawer
     * @return
     */
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
        mSliderLayout = (RelativeLayout) mActivity.getLayoutInflater().inflate(R.layout.material_drawer_slider, mDrawerLayout, false);
        mSliderLayout.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(mActivity, R.attr.material_drawer_background, R.color.material_drawer_background));
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

    /**
     * the helper method to create the content for the drawer
     */
    private void createContent() {
        // if we have an adapter (either by defining a custom one or the included one add a list :D
        if (mListView == null) {
            mListView = new ListView(mActivity);
            mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            mListView.setDivider(null);
            //only draw the selector on top if we are on a newer api than 10
            if (Build.VERSION.SDK_INT > 10) {
                mListView.setDrawSelectorOnTop(true);
            }
            mListView.setClipToPadding(false);

            int paddingTop = 0;
            if ((mTranslucentStatusBar && !mTranslucentActionBarCompatibility) || mFullscreen) {
                paddingTop = mActivity.getResources().getDimensionPixelSize(R.dimen.tool_bar_top_padding);
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

        //some extra stuff to beautify the whole thing ;)
        if ((!mTranslucentStatusBar || mTranslucentActionBarCompatibility) && !mFullscreen) {
            //disable the shadow if we don't use a translucent activity
            mSliderLayout.getChildAt(0).setVisibility(View.GONE);
        } else {
            //bring shadow bar to front again
            mSliderLayout.getChildAt(0).bringToFront();
        }


        // initialize list if there is an adapter or set items
        if (mDrawerItems != null && mAdapter == null) {
            mAdapter = new DrawerAdapter(mActivity, mDrawerItems);
        }

        //use the AccountHeader if set
        if (mAccountHeader != null) {
            if (mAccountHeaderSticky) {
                mStickyHeaderView = mAccountHeader.getView();
            } else {
                mHeaderView = mAccountHeader.getView();
            }
        }

        //sticky header view
        if (mStickyHeaderView != null) {
            //add the sticky footer view and align it to the bottom
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
            mStickyHeaderView.setId(R.id.sticky_header);
            mSliderLayout.addView(mStickyHeaderView, 0, layoutParams);

            //now align the listView above the stickyFooterView ;)
            RelativeLayout.LayoutParams layoutParamsListView = (RelativeLayout.LayoutParams) mListView.getLayoutParams();
            layoutParamsListView.addRule(RelativeLayout.BELOW, R.id.sticky_header);
            mListView.setLayoutParams(layoutParamsListView);

            //remove the padding of the listView again we have the header on top of it
            mListView.setPadding(0, 0, 0, 0);
        }

        //use the StickyDrawerItems if set
        if (mStickyDrawerItems != null && mStickyDrawerItems.size() > 0) {
            mStickyFooterView = buildStickyDrawerItemFooter();
        }

        //sticky footer view
        if (mStickyFooterView != null) {
            //add the sticky footer view and align it to the bottom
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
            mStickyFooterView.setId(R.id.sticky_footer);
            mSliderLayout.addView(mStickyFooterView, layoutParams);

            if ((mTranslucentNavigationBar || mFullscreen) && Build.VERSION.SDK_INT >= 19) {
                mStickyFooterView.setPadding(0, 0, 0, UIUtils.getNavigationBarHeight(mActivity));
            }

            //now align the listView above the stickyFooterView ;)
            RelativeLayout.LayoutParams layoutParamsListView = (RelativeLayout.LayoutParams) mListView.getLayoutParams();
            layoutParamsListView.addRule(RelativeLayout.ABOVE, R.id.sticky_footer);
            mListView.setLayoutParams(layoutParamsListView);

            //remove the padding of the listView again we have the header on top of it
            mListView.setPadding(mListView.getPaddingLeft(), mListView.getPaddingTop(), mListView.getPaddingRight(), mActivity.getResources().getDimensionPixelSize(R.dimen.material_drawer_padding));
        }

        // set the header (do this before the setAdapter because some devices will crash else
        if (mHeaderView != null) {
            if (mListView == null) {
                throw new RuntimeException("can't use a headerView without a listView");
            }

            if (mHeaderDivider) {
                LinearLayout headerContainer = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.material_drawer_item_header, mListView, false);
                headerContainer.addView(mHeaderView, 0);
                //set the color for the divider
                headerContainer.findViewById(R.id.divider).setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(mActivity, R.attr.material_drawer_divider, R.color.material_drawer_divider));
                //add the headerContainer to the list
                mListView.addHeaderView(headerContainer, null, mHeaderClickable);
                //link the view including the container to the headerView field
                mHeaderView = headerContainer;
            } else {
                mListView.addHeaderView(mHeaderView, null, mHeaderClickable);
            }
            //set the padding on the top to 0
            mListView.setPadding(mListView.getPaddingLeft(), 0, mListView.getPaddingRight(), mListView.getPaddingBottom());
        }

        // set the footer (do this before the setAdapter because some devices will crash else
        if (mFooterView != null) {
            if (mListView == null) {
                throw new RuntimeException("can't use a footerView without a listView");
            }

            if (mFooterDivider) {
                LinearLayout footerContainer = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.material_drawer_item_footer, mListView, false);
                footerContainer.addView(mFooterView, 1);
                //set the color for the divider
                footerContainer.findViewById(R.id.divider).setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(mActivity, R.attr.material_drawer_divider, R.color.material_drawer_divider));
                //add the footerContainer to the list
                mListView.addFooterView(footerContainer, null, mFooterClickable);
                //link the view including the container to the footerView field
                mFooterView = footerContainer;
            } else {
                mListView.addFooterView(mFooterView, null, mFooterClickable);
            }
        }

        //after adding the header do the setAdapter and set the selection
        if (mAdapter != null) {
            //set the adapter on the listView
            mListView.setAdapter(mAdapter);

            //predefine selection (should be the first element
            if (mListView != null && (mSelectedItem + mHeaderOffset) > -1) {
                resetStickyFooterSelection();
                mListView.setSelection(mSelectedItem + mHeaderOffset);
                mListView.setItemChecked(mSelectedItem + mHeaderOffset, true);
                mCurrentSelection = mSelectedItem;
            }
        }

        // add the onDrawerItemClickListener if set
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IDrawerItem i = getDrawerItem(position, true);

                //close the drawer after click
                closeDrawerDelayed();

                if (i != null && i instanceof Checkable && !((Checkable) i).isCheckable()) {
                    mListView.setSelection(mCurrentSelection + mHeaderOffset);
                    mListView.setItemChecked(mCurrentSelection + mHeaderOffset, true);
                } else {
                    resetStickyFooterSelection();
                    mCurrentSelection = position - mHeaderOffset;
                }

                if (mOnDrawerItemClickListener != null) {
                    mOnDrawerItemClickListener.onItemClick(parent, view, position - mHeaderOffset, id, i);
                }
            }
        });

        // add the onDrawerItemLongClickListener if set
        if (mOnDrawerItemLongClickListener != null) {
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    return mOnDrawerItemLongClickListener.onItemLongClick(parent, view, position - mHeaderOffset, id, getDrawerItem(position, true));
                }
            });
        }

        // add the onDrawerItemSelectedListener if set
        if (mOnDrawerItemSelectedListener != null) {
            mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mOnDrawerItemSelectedListener.onItemSelected(parent, view, position - mHeaderOffset, id, getDrawerItem(position, true));
                    mCurrentSelection = position - mHeaderOffset;
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
                if (mListView != null && (selection + mHeaderOffset) > -1) {
                    resetStickyFooterSelection();
                    mListView.setSelection(selection + mHeaderOffset);
                    mListView.setItemChecked(selection + mHeaderOffset, true);
                    mCurrentSelection = selection;
                }
            }
        }

        // call initial onClick event to allow the dev to init the first view
        if (mFireInitialOnClick && mOnDrawerItemClickListener != null) {
            mOnDrawerItemClickListener.onItemClick(null, null, mCurrentSelection, mCurrentSelection, getDrawerItem(mCurrentSelection, false));
        }
    }

    /**
     * helper method to close the drawer delayed
     */
    private void closeDrawerDelayed() {
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
    private IDrawerItem getDrawerItem(int position, boolean includeOffset) {
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
    private boolean checkDrawerItem(int position, boolean includeOffset) {
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
     * helper to extend the layoutParams of the drawer
     *
     * @param params
     * @return
     */
    private DrawerLayout.LayoutParams processDrawerLayoutParams(DrawerLayout.LayoutParams params) {
        if (params != null) {
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

            if (mTranslucentActionBarCompatibility) {
                TypedValue tv = new TypedValue();
                if (mActivity.getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                    int topMargin = TypedValue.complexToDimensionPixelSize(tv.data, mActivity.getResources().getDisplayMetrics());
                    if (mTranslucentStatusBar) {
                        topMargin = topMargin + mActivity.getResources().getDimensionPixelSize(R.dimen.tool_bar_top_padding);
                    }
                    params.topMargin = topMargin;
                }
            }

            if (mDrawerWidth > -1) {
                params.width = mDrawerWidth;
            } else {
                params.width = UIUtils.getOptimalDrawerWidth(mActivity);
            }
        }

        return params;
    }

    /**
     * build the sticky footer item view
     *
     * @return
     */
    private View buildStickyDrawerItemFooter() {
        //create the container view
        final LinearLayout linearLayout = new LinearLayout(mActivity);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        //create the divider
        if (mStickyFooterDivider) {
            LinearLayout divider = new LinearLayout(mActivity);
            LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dividerParams.bottomMargin = mActivity.getResources().getDimensionPixelSize(R.dimen.material_drawer_padding);
            divider.setMinimumHeight((int) UIUtils.convertDpToPixel(1, mActivity));
            divider.setOrientation(LinearLayout.VERTICAL);
            divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(mActivity, R.attr.material_drawer_divider, R.color.material_drawer_divider));
            linearLayout.addView(divider, dividerParams);
        }

        //get the inflater
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        int padding = mActivity.getResources().getDimensionPixelSize(R.dimen.material_drawer_vertical_padding);

        //add all drawer items
        for (IDrawerItem drawerItem : mStickyDrawerItems) {
            //get the selected_color
            int selected_color = UIUtils.getThemeColorFromAttrOrRes(mActivity, R.attr.material_drawer_selected, R.color.material_drawer_selected);
            if (drawerItem instanceof PrimaryDrawerItem) {
                if (selected_color == 0 && ((PrimaryDrawerItem) drawerItem).getSelectedColorRes() != -1) {
                    selected_color = mActivity.getResources().getColor(((PrimaryDrawerItem) drawerItem).getSelectedColorRes());
                } else if (((PrimaryDrawerItem) drawerItem).getSelectedColor() != 0) {
                    selected_color = ((PrimaryDrawerItem) drawerItem).getSelectedColor();
                }
            } else if (drawerItem instanceof SecondaryDrawerItem) {
                if (selected_color == 0 && ((SecondaryDrawerItem) drawerItem).getSelectedColorRes() != -1) {
                    selected_color = mActivity.getResources().getColor(((SecondaryDrawerItem) drawerItem).getSelectedColorRes());
                } else if (((SecondaryDrawerItem) drawerItem).getSelectedColor() != 0) {
                    selected_color = ((SecondaryDrawerItem) drawerItem).getSelectedColor();
                }
            }

            View view = drawerItem.convertView(layoutInflater, null, linearLayout);
            view.setTag(drawerItem);

            if (drawerItem.isEnabled()) {
                UIUtils.setBackground(view, UIUtils.getSelectableBackground(mActivity, selected_color));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetStickyFooterSelection();

                        IDrawerItem drawerItem = (IDrawerItem) v.getTag();
                        boolean notCheckable = drawerItem != null && drawerItem instanceof Checkable && !((Checkable) drawerItem).isCheckable();
                        boolean checkable = !notCheckable;
                        if (checkable) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                v.setActivated(true);
                            }
                            v.setSelected(true);

                            //remove the selection in the list
                            mListView.setSelection(-1);
                            mListView.setItemChecked(mCurrentSelection + mHeaderOffset, false);
                        }

                        //close the drawer after click
                        closeDrawerDelayed();

                        if (mOnDrawerItemClickListener != null) {
                            mOnDrawerItemClickListener.onItemClick(null, v, -1, -1, drawerItem);
                        }
                    }
                });
            }

            //don't ask my why but it forgets the padding from the original layout
            view.setPadding(padding, 0, padding, 0);
            linearLayout.addView(view);
        }
        //and really. don't ask about this. it won't set the padding if i don't set the padding for the container
        linearLayout.setPadding(0, 0, 0, 0);

        return linearLayout;
    }

    /**
     * simple helper method to reset the selection of the sticky footer
     */
    private void resetStickyFooterSelection() {
        if (mStickyFooterView instanceof LinearLayout) {
            for (int i = 1; i < ((LinearLayout) mStickyFooterView).getChildCount(); i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    ((LinearLayout) mStickyFooterView).getChildAt(i).setActivated(false);
                }
                ((LinearLayout) mStickyFooterView).getChildAt(i).setSelected(false);
            }
        }
    }

    /**
     * The result object used for the Drawer
     */
    public static class Result {
        private final Drawer mDrawer;
        private FrameLayout mContentView;
        private KeyboardUtil mKeyboardUtil = null;

        /**
         * the protected Constructor for the result
         *
         * @param drawer
         */
        protected Result(Drawer drawer) {
            this.mDrawer = drawer;
        }

        /**
         * Get the DrawerLayout of the current drawer
         *
         * @return
         */
        public DrawerLayout getDrawerLayout() {
            return this.mDrawer.mDrawerLayout;
        }

        /**
         * Open the drawer
         */
        public void openDrawer() {
            if (mDrawer.mDrawerLayout != null && mDrawer.mSliderLayout != null) {
                mDrawer.mDrawerLayout.openDrawer(mDrawer.mSliderLayout);
            }
        }

        /**
         * close the drawer
         */
        public void closeDrawer() {
            if (mDrawer.mDrawerLayout != null) {
                mDrawer.mDrawerLayout.closeDrawers();
            }
        }

        /**
         * Get the current state of the drawer.
         * True if the drawer is currently open.
         *
         * @return
         */
        public boolean isDrawerOpen() {
            if (mDrawer.mDrawerLayout != null && mDrawer.mSliderLayout != null) {
                return mDrawer.mDrawerLayout.isDrawerOpen(mDrawer.mSliderLayout);
            }
            return false;
        }


        /**
         * set the insetsFrameLayout to display the content in fullscreen
         * under the statusBar and navigationBar
         *
         * @param fullscreen
         */
        public void setFullscreen(boolean fullscreen) {
            if (mDrawer.mDrawerContentRoot != null) {
                mDrawer.mDrawerContentRoot.setEnabled(!fullscreen);
            }
        }

        /**
         * Set the color for the statusBar
         *
         * @param statusBarColor
         */
        public void setStatusBarColor(int statusBarColor) {
            if (mDrawer.mDrawerContentRoot != null) {
                mDrawer.mDrawerContentRoot.setInsetForeground(statusBarColor);
            }
        }


        /**
         * a helper method to enable the keyboardUtil for a specific activity
         * or disable it. note this will cause some frame drops because of the
         * listener.
         *
         * @param activity
         * @param enable
         */
        public void keyboardSupportEnabled(Activity activity, boolean enable) {
            if (getContent() != null && getContent().getChildCount() > 0) {
                if (mKeyboardUtil == null) {
                    mKeyboardUtil = new KeyboardUtil(activity, getContent().getChildAt(0));
                    mKeyboardUtil.disable();
                }

                if (enable) {
                    mKeyboardUtil.enable();
                } else {
                    mKeyboardUtil.disable();
                }
            }
        }


        /**
         * get the slider layout of the current drawer.
         * This is the layout containing the ListView
         *
         * @return
         */
        public RelativeLayout getSlider() {
            return mDrawer.mSliderLayout;
        }

        /**
         * get the container frameLayout of the current drawer
         *
         * @return
         */
        public FrameLayout getContent() {
            if (mContentView == null && this.mDrawer.mDrawerLayout != null) {
                mContentView = (FrameLayout) this.mDrawer.mDrawerLayout.findViewById(R.id.content_layout);
            }
            return mContentView;
        }

        /**
         * get the listView of the current drawer
         *
         * @return
         */
        public ListView getListView() {
            return mDrawer.mListView;
        }

        /**
         * get the BaseDrawerAdapter of the current drawer
         *
         * @return
         */
        public BaseDrawerAdapter getAdapter() {
            return mDrawer.mAdapter;
        }

        /**
         * get all drawerItems of the current drawer
         *
         * @return
         */
        public ArrayList<IDrawerItem> getDrawerItems() {
            return mDrawer.mDrawerItems;
        }

        /**
         * get the Header View if set else NULL
         *
         * @return
         */
        public View getHeader() {
            return mDrawer.mHeaderView;
        }

        /**
         * get the StickyHeader View if set else NULL
         *
         * @return
         */
        public View getStickyHeader() {
            return mDrawer.mStickyHeaderView;
        }

        /**
         * method to replace a previous set header
         *
         * @param view
         */
        public void setHeader(View view) {
            if (getListView() != null) {
                BaseDrawerAdapter adapter = getAdapter();
                getListView().setAdapter(null);
                if (getHeader() != null) {
                    getListView().removeHeaderView(getHeader());
                }
                getListView().addHeaderView(view);
                getListView().setAdapter(adapter);
            }
        }

        /**
         * get the Footer View if set else NULL
         *
         * @return
         */
        public View getFooter() {
            return mDrawer.mFooterView;
        }

        /**
         * get the StickyFooter View if set else NULL
         *
         * @return
         */
        public View getStickyFooter() {
            return mDrawer.mStickyFooterView;
        }

        /**
         * get the ActionBarDrawerToggle
         *
         * @return
         */
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
            if (identifier >= 0) {
                if (mDrawer.mDrawerItems != null) {
                    int position = 0;
                    for (IDrawerItem i : mDrawer.mDrawerItems) {
                        if (i.getIdentifier() == identifier) {
                            return position;
                        }
                        position = position + 1;
                    }
                }
            }

            return -1;
        }

        /**
         * get the current selection
         *
         * @return
         */
        public int getCurrentSelection() {
            return mDrawer.mCurrentSelection;
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
                mDrawer.resetStickyFooterSelection();
                mDrawer.mListView.setSelection(position + mDrawer.mHeaderOffset);
                mDrawer.mListView.setItemChecked(position + mDrawer.mHeaderOffset, true);

                if (fireOnClick && mDrawer.mOnDrawerItemClickListener != null) {
                    mDrawer.mOnDrawerItemClickListener.onItemClick(null, null, position, position, mDrawer.getDrawerItem(position, false));
                }

                mDrawer.mCurrentSelection = position;
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
         * Update a drawerItem at a specific position
         *
         * @param drawerItem
         * @param position
         */
        public void updateItem(IDrawerItem drawerItem, int position) {
            if (mDrawer.checkDrawerItem(position, false)) {
                mDrawer.mDrawerItems.set(position, drawerItem);
                mDrawer.mAdapter.dataUpdated();
            }
        }

        /**
         * Add a drawerItem at the end
         *
         * @param drawerItem
         */
        public void addItem(IDrawerItem drawerItem) {
            if (mDrawer.mDrawerItems != null) {
                mDrawer.mDrawerItems.add(drawerItem);
                mDrawer.mAdapter.dataUpdated();
            }
        }

        /**
         * Add a drawerItem at a specific position
         *
         * @param drawerItem
         * @param position
         */
        public void addItem(IDrawerItem drawerItem, int position) {
            if (mDrawer.mDrawerItems != null) {
                mDrawer.mDrawerItems.add(position, drawerItem);
                mDrawer.mAdapter.dataUpdated();
            }
        }

        /**
         * Set a drawerItem at a specific position
         *
         * @param drawerItem
         * @param position
         */
        public void setItem(IDrawerItem drawerItem, int position) {
            if (mDrawer.mDrawerItems != null) {
                mDrawer.mDrawerItems.set(position, drawerItem);
                mDrawer.mAdapter.dataUpdated();
            }
        }

        /**
         * Remove a drawerItem at a specific position
         *
         * @param position
         */
        public void removeItem(int position) {
            if (mDrawer.checkDrawerItem(position, false)) {
                mDrawer.mDrawerItems.remove(position);
                mDrawer.mAdapter.dataUpdated();
            }
        }

        /**
         * Removes all items from drawer
         */
        public void removeAllItems() {
            mDrawer.mDrawerItems.clear();
            mDrawer.mAdapter.dataUpdated();
        }

        /**
         * add new Items to the current DrawerItem List
         *
         * @param drawerItems
         */
        public void addItems(IDrawerItem... drawerItems) {
            if (mDrawer.mDrawerItems != null) {
                Collections.addAll(mDrawer.mDrawerItems, drawerItems);
                mDrawer.mAdapter.dataUpdated();
            }
        }

        /**
         * Replace the current DrawerItems with a new ArrayList of items
         *
         * @param drawerItems
         */
        public void setItems(ArrayList<IDrawerItem> drawerItems) {
            setItems(drawerItems, false);
        }

        /**
         * replace the current DrawerItems with the new ArrayList.
         *
         * @param drawerItems
         * @param switchedItems
         */
        private void setItems(ArrayList<IDrawerItem> drawerItems, boolean switchedItems) {
            mDrawer.mDrawerItems = drawerItems;

            //if we are currently at a switched list set the new reference
            if (originalDrawerItems != null && !switchedItems) {
                originalDrawerItems = drawerItems;
            } else {
                mDrawer.mAdapter.setDrawerItems(mDrawer.mDrawerItems);
            }

            mDrawer.mAdapter.dataUpdated();
        }

        /**
         * Update the name of a drawer item if its an instance of nameable
         *
         * @param nameRes
         * @param position
         */
        public void updateName(int nameRes, int position) {
            if (mDrawer.checkDrawerItem(position, false)) {
                IDrawerItem drawerItem = mDrawer.mDrawerItems.get(position);

                if (drawerItem instanceof Nameable) {
                    ((Nameable) drawerItem).setNameRes(nameRes);
                    ((Nameable) drawerItem).setName(null);
                }

                mDrawer.mDrawerItems.set(position, drawerItem);
                mDrawer.mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * Update the name of a drawer item if its an instance of nameable
         *
         * @param name
         * @param position
         */
        public void updateName(String name, int position) {
            if (mDrawer.checkDrawerItem(position, false)) {
                IDrawerItem drawerItem = mDrawer.mDrawerItems.get(position);

                if (drawerItem instanceof Nameable) {
                    ((Nameable) drawerItem).setName(name);
                    ((Nameable) drawerItem).setNameRes(-1);
                }

                mDrawer.mDrawerItems.set(position, drawerItem);
                mDrawer.mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * Update the badge of a drawer item if its an instance of badgeable
         *
         * @param badge
         * @param position
         */
        public void updateBadge(String badge, int position) {
            if (mDrawer.checkDrawerItem(position, false)) {
                IDrawerItem drawerItem = mDrawer.mDrawerItems.get(position);

                if (drawerItem instanceof Badgeable) {
                    ((Badgeable) drawerItem).setBadge(badge);
                }

                mDrawer.mDrawerItems.set(position, drawerItem);
                mDrawer.mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * Update the icon of a drawer item if its an instance of iconable
         *
         * @param icon
         * @param position
         */
        public void updateIcon(Drawable icon, int position) {
            if (mDrawer.checkDrawerItem(position, false)) {
                IDrawerItem drawerItem = mDrawer.mDrawerItems.get(position);

                if (drawerItem instanceof Iconable) {
                    ((Iconable) drawerItem).setIcon(icon);
                }

                mDrawer.mDrawerItems.set(position, drawerItem);
                mDrawer.mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * Update the icon of a drawer item from an iconRes
         *
         * @param iconRes
         * @param position
         */
        public void updateIcon(int iconRes, int position) {
            if (mDrawer.mRootView != null && mDrawer.checkDrawerItem(position, false)) {
                IDrawerItem drawerItem = mDrawer.mDrawerItems.get(position);

                if (drawerItem instanceof Iconable) {
                    ((Iconable) drawerItem).setIcon(UIUtils.getCompatDrawable(mDrawer.mRootView.getContext(), iconRes));
                }

                mDrawer.mDrawerItems.set(position, drawerItem);
                mDrawer.mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * Update the icon of a drawer item if its an instance of iconable
         *
         * @param icon
         * @param position
         */
        public void updateIcon(IIcon icon, int position) {
            if (mDrawer.checkDrawerItem(position, false)) {
                IDrawerItem drawerItem = mDrawer.mDrawerItems.get(position);

                if (drawerItem instanceof Iconable) {
                    ((Iconable) drawerItem).setIIcon(icon);
                }

                mDrawer.mDrawerItems.set(position, drawerItem);
                mDrawer.mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * setter for the OnDrawerItemClickListener
         *
         * @param onDrawerItemClickListener
         */
        public void setOnDrawerItemClickListener(OnDrawerItemClickListener onDrawerItemClickListener) {
            mDrawer.mOnDrawerItemClickListener = onDrawerItemClickListener;
        }

        /**
         * method to get the OnDrawerItemClickListener
         *
         * @return
         */
        public OnDrawerItemClickListener getOnDrawerItemClickListener() {
            return mDrawer.mOnDrawerItemClickListener;
        }

        /**
         * setter for the OnDrawerItemLongClickListener
         *
         * @param onDrawerItemLongClickListener
         */
        public void setOnDrawerItemLongClickListener(OnDrawerItemLongClickListener onDrawerItemLongClickListener) {
            mDrawer.mOnDrawerItemLongClickListener = onDrawerItemLongClickListener;
        }

        /**
         * method to get the OnDrawerItemLongClickListener
         *
         * @return
         */
        public OnDrawerItemLongClickListener getOnDrawerItemLongClickListener() {
            return mDrawer.mOnDrawerItemLongClickListener;
        }


        //variables to store and remember the original list of the drawer
        private Drawer.OnDrawerItemClickListener originalOnDrawerItemClickListener;
        private ArrayList<IDrawerItem> originalDrawerItems;
        private int originalDrawerSelection = -1;

        public boolean switchedDrawerContent() {
            return !(originalOnDrawerItemClickListener == null && originalDrawerItems == null && originalDrawerSelection == -1);
        }

        /**
         * method to switch the drawer content to new elements
         *
         * @param onDrawerItemClickListener
         * @param drawerItems
         * @param drawerSelection
         */
        public void switchDrawerContent(OnDrawerItemClickListener onDrawerItemClickListener, ArrayList<IDrawerItem> drawerItems, int drawerSelection) {
            //just allow a single switched drawer
            if (!switchedDrawerContent()) {
                //save out previous values
                originalOnDrawerItemClickListener = getOnDrawerItemClickListener();
                originalDrawerItems = getDrawerItems();
                originalDrawerSelection = getCurrentSelection();

                //set the new items
                setOnDrawerItemClickListener(onDrawerItemClickListener);
                setItems(drawerItems, true);
                setSelection(drawerSelection, false);
            }
        }

        /**
         * helper method to reset to the original drawerContent
         */
        public void resetDrawerContent() {
            if (switchedDrawerContent()) {
                //set the new items
                setOnDrawerItemClickListener(originalOnDrawerItemClickListener);
                setItems(originalDrawerItems, true);
                setSelection(originalDrawerSelection, false);
                //remove the references
                originalOnDrawerItemClickListener = null;
                originalDrawerItems = null;
                originalDrawerSelection = -1;
            }
        }

        /**
         * add the values to the bundle for saveInstanceState
         *
         * @param savedInstanceState
         * @return
         */
        public Bundle saveInstanceState(Bundle savedInstanceState) {
            if (savedInstanceState != null) {
                if (getListView() != null) {
                    savedInstanceState.putInt(BUNDLE_SELECTION, mDrawer.mCurrentSelection);
                }
            }
            return savedInstanceState;
        }
    }

    public interface OnDrawerNavigationListener {
        public boolean onNavigationClickListener(View clickedView);
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
