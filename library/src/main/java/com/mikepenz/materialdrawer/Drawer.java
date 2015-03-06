package com.mikepenz.materialdrawer;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.utils.Utils;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.adapter.BaseDrawerAdapter;
import com.mikepenz.materialdrawer.adapter.DrawerAdapter;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.Checkable;
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
    protected boolean mTranslucentActionBarCompatibility = false;

    /**
     * Just use this parameter if you really want to use a translucent statusbar with an
     * actionbar
     *
     * @param translucentActionBarCompatibility
     * @return
     */
    public Drawer withTranslucentActionBarCompatibility(boolean translucentActionBarCompatibility) {
        this.mTranslucentActionBarCompatibility = translucentActionBarCompatibility;
        return this;
    }

    // set non translucent statusbar mode
    protected boolean mTranslucentStatusBar = true;

    /**
     * Set or disable this if you use a translucent statusbar
     *
     * @param translucentStatusBar
     * @return
     */
    public Drawer withTranslucentStatusBar(boolean translucentStatusBar) {
        this.mTranslucentStatusBar = translucentStatusBar;
        return this;
    }

    /**
     * Set or disable this if you want to show the drawer below the toolbar.
     * Note this will add a margin above the drawer
     *
     * @param displayBelowToolbar
     * @return
     */
    public Drawer withDisplayBelowToolbar(boolean displayBelowToolbar) {
        this.mTranslucentStatusBar = displayBelowToolbar;
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
            this.mDrawerLayout = (DrawerLayout) mActivity.getLayoutInflater().inflate(R.layout.material_drawer, mRootView, false);
        }

        return this;
    }

    //the background color for the slider
    protected int mSliderBackgroundColor = -1;
    protected int mSliderBackgroundColorRes = -1;

    /**
     * set the background for the slider as color
     *
     * @param sliderBackgroundColor
     * @return
     */
    public Drawer withSliderBackgroundColor(int sliderBackgroundColor) {
        this.mSliderBackgroundColor = sliderBackgroundColor;
        return this;
    }

    /**
     * set the background for the slider as resource
     *
     * @param sliderBackgroundColorRes
     * @return
     */
    public Drawer withSliderBackgroundColorRes(int sliderBackgroundColorRes) {
        this.mSliderBackgroundColorRes = sliderBackgroundColorRes;
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

    //the account selection header to use
    protected AccountHeader.Result mAccountHeader;

    /**
     * set the accountHeader to use for this drawer instance
     * not this will overwrite the mHeaderView if set
     *
     * @param accountHeader
     * @return
     */
    public Drawer withAccountHeader(AccountHeader.Result accountHeader) {
        this.mAccountHeader = accountHeader;
        //set the header offset
        mHeaderOffset = 1;
        return this;
    }

    // enable/disable the actionBarDrawerToggle animation
    protected boolean mAnimateActionBarDrawerToggle = false;

    /**
     * set this to enable/disable the actionBarDrawerToggle animation
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
    protected boolean mHeaderClickable = false;

    /**
     * add a header layout from view
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
     * add a header layout from res
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
     * set if the header is clickable
     *
     * @param headerClickable
     * @return
     */
    public Drawer withHeaderClickable(boolean headerClickable) {
        this.mHeaderClickable = headerClickable;
        return this;
    }

    /**
     * this method allows you to disable the divider on the bottom of the header
     *
     * @param headerDivider
     * @return
     */
    public Drawer withHeaderDivider(boolean headerDivider) {
        this.mHeaderDivider = headerDivider;
        return this;
    }

    // footer view
    protected View mFooterView;
    protected boolean mFooterDivider = true;
    protected boolean mFooterClickable = false;

    /**
     * add a footer layout from view
     *
     * @param footerView
     * @return
     */
    public Drawer withFooter(View footerView) {
        this.mFooterView = footerView;
        return this;
    }

    /**
     * add a footer layout from res
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
     * set if the footer is clickable
     *
     * @param footerClickable
     * @return
     */
    public Drawer withFooterClickable(boolean footerClickable) {
        this.mFooterClickable = footerClickable;
        return this;
    }

    /**
     * this method allows you to disable the divider on top of the footer
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

    /**
     * add a sticky footer layout from view
     * this view will be always visible on the bottom
     *
     * @param stickyFooter
     * @return
     */
    public Drawer withStickyFooter(View stickyFooter) {
        this.mStickyFooterView = stickyFooter;
        return this;
    }

    /**
     * add a sticky footer layout from res
     * this view will be always visible on the bottom
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

    // fire onClick after build
    protected boolean mFireInitialOnClick = false;

    /**
     * enable this if you would love to receive a onClick event after the build method is called
     * to be able to show the initial layout.
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
    protected BaseDrawerAdapter mAdapter;

    /**
     * Set the adapter to be used with the list
     *
     * @param adapter
     * @return
     */
    public Drawer withAdapter(BaseDrawerAdapter adapter) {
        this.mAdapter = adapter;
        return this;
    }

    // list in drawer
    protected ArrayList<IDrawerItem> mDrawerItems;

    /**
     * set the arrayList of DrawerItems for the drawer
     *
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
            this.mDrawerItems = new ArrayList<>();
        }

        if (drawerItems != null) {
            Collections.addAll(this.mDrawerItems, drawerItems);
        }
        return this;
    }

    // close drawer on click
    protected boolean mCloseOnClick = true;

    /**
     * set if the drawer should autoClose if an item is clicked
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
     * set the delay for the drawer close operation
     * this is a small hack to improve the responsivness if you open a new activity within the drawer onClick
     * else you will see some lag
     * you can disable this by passing -1
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
     * set the drawerListener
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
     * set the DrawerItemClickListener
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
     * set the DrawerItemLongClickListener
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
     * set the ItemSelectedListener
     *
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
     * create the drawer with the values of a savedInstance
     *
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

        //set the navigationOnClickListener
        if (mToolbar != null) {
            this.mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDrawerLayout.isDrawerOpen(mSliderLayout)) {
                        mDrawerLayout.closeDrawer(mSliderLayout);
                    } else {
                        mDrawerLayout.openDrawer(mSliderLayout);
                    }
                }
            });
        }

        // create the ActionBarDrawerToggle if not set and enabled and if we have a toolbar
        if (mActionBarDrawerToggleEnabled && mActionBarDrawerToggle == null) {
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
            mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        }

        // get the slider view
        mSliderLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.material_drawer_slider, mDrawerLayout, false);
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

        // set the background
        if (mSliderBackgroundColor != -1) {
            mSliderLayout.setBackgroundColor(mSliderBackgroundColor);
        } else if (mSliderBackgroundColorRes != -1) {
            mSliderLayout.setBackgroundColor(mActivity.getResources().getColor(mSliderBackgroundColorRes));
        }

        // add the slider to the drawer
        mDrawerLayout.addView(mSliderLayout, 1);

        //create the content
        createContent();

        //forget the reference to the activity
        mActivity = null;

        //create the result object
        Result result = new Result(this);
        //set the drawer for the accountHeader if set
        if (mAccountHeader != null) {
            mAccountHeader.setDrawer(result);
        }

        return result;
    }


    /**
     * the builder method to append a new drawer to an existing Drawer
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
        mSliderLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.material_drawer_slider, mDrawerLayout, false);
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
            mListView.setDrawSelectorOnTop(true);
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

        //use the AccountHeader if set
        if (mAccountHeader != null) {
            mHeaderView = mAccountHeader.getView();
        }

        // set the header (do this before the setAdapter because some devices will crash else
        if (mHeaderView != null) {
            if (mListView == null) {
                throw new RuntimeException("can't use a headerView without a listView");
            }

            if (mHeaderDivider) {
                LinearLayout headerContainer = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.material_drawer_item_header, mListView, false);
                headerContainer.addView(mHeaderView, 0);
                mListView.addHeaderView(headerContainer, null, mHeaderClickable);
                mListView.setPadding(0, 0, 0, 0);
                //link the view including the container to the headerView field
                mHeaderView = headerContainer;
            } else {
                mListView.addHeaderView(mHeaderView, null, mHeaderClickable);
                mListView.setPadding(0, 0, 0, 0);
            }
        }

        // set the footer (do this before the setAdapter because some devices will crash else
        if (mFooterView != null) {
            if (mListView == null) {
                throw new RuntimeException("can't use a footerView without a listView");
            }

            if (mFooterDivider) {
                LinearLayout footerContainer = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.material_drawer_item_footer, mListView, false);
                footerContainer.addView(mFooterView, 1);
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

                if (mCloseOnClick) {
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

                if (i != null && i instanceof Checkable && !((Checkable) i).isCheckable()) {
                    mListView.setSelection(mCurrentSelection + mHeaderOffset);
                    mListView.setItemChecked(mCurrentSelection + mHeaderOffset, true);
                } else {
                    mCurrentSelection = position - mHeaderOffset;
                }


                if (mOnDrawerItemClickListener != null) {
                    mOnDrawerItemClickListener.onItemClick(parent, view, position, id, i);
                }
            }
        });

        // add the onDrawerItemLongClickListener if set
        if (mOnDrawerItemLongClickListener != null) {
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    return mOnDrawerItemLongClickListener.onItemLongClick(parent, view, position, id, getDrawerItem(position, true));
                }
            });
        }

        // add the onDrawerItemSelectedListener if set
        if (mOnDrawerItemSelectedListener != null) {
            mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mOnDrawerItemSelectedListener.onItemSelected(parent, view, position, id, getDrawerItem(position, true));
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
                if (mListView != null && (selection) > -1) {
                    mListView.setSelection(selection);
                    mListView.setItemChecked(selection, true);
                    mCurrentSelection = selection - mHeaderOffset;
                }
            }
        }

        // call initial onClick event to allow the dev to init the first view
        if (mFireInitialOnClick && mOnDrawerItemClickListener != null) {
            mOnDrawerItemClickListener.onItemClick(null, null, mCurrentSelection, mCurrentSelection, getDrawerItem(mCurrentSelection, false));
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
        private final Drawer mDrawer;
        private FrameLayout mContentView;

        /**
         * the protected Constructor for the result
         *
         * @param drawer
         */
        protected Result(Drawer drawer) {
            this.mDrawer = drawer;
        }

        /**
         * get the drawerLayout of the current drawer
         *
         * @return
         */
        public DrawerLayout getDrawerLayout() {
            return this.mDrawer.mDrawerLayout;
        }

        /**
         * open the drawer
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
         * get the current state if the drawer is open
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
         * get the slider layout of the current drawer
         *
         * @return
         */
        public LinearLayout getSlider() {
            return mDrawer.mSliderLayout;
        }

        /**
         * get the cootainer frameLayout of the current drawer
         *
         * @return
         */
        public FrameLayout getContent() {
            if (mContentView == null) {
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
            } else {
                throw new RuntimeException("the item requires a unique identifier to use this method");
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
        public void setItems(ArrayList<IDrawerItem> drawerItems, boolean switchedItems) {
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
                    ((Iconable) drawerItem).setIcon(mDrawer.mRootView.getContext().getResources().getDrawable(iconRes));
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
