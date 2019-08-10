package com.mikepenz.materialdrawer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.view.SupportMenuInflater
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.IIdentifyable
import com.mikepenz.fastadapter.IItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.adapters.ModelAdapter
import com.mikepenz.fastadapter.expandable.ExpandableExtension
import com.mikepenz.fastadapter.expandable.ExpandableExtensionFactory
import com.mikepenz.fastadapter.extensions.ExtensionsFactories
import com.mikepenz.fastadapter.select.SelectExtension
import com.mikepenz.fastadapter.select.SelectExtensionFactory
import com.mikepenz.fastadapter.utils.DefaultIdDistributor
import com.mikepenz.fastadapter.utils.DefaultIdDistributorImpl
import com.mikepenz.materialdrawer.holder.DimenHolder
import com.mikepenz.materialdrawer.model.AbstractDrawerItem
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Selectable
import com.mikepenz.materialdrawer.util.DrawerUIUtils
import com.mikepenz.materialize.Materialize
import com.mikepenz.materialize.MaterializeBuilder
import com.mikepenz.materialize.util.UIUtils
import com.mikepenz.materialize.view.ScrimInsetsRelativeLayout
import java.util.*

/**
 * Created by mikepenz on 23.05.15.
 */
open class DrawerBuilder {

    // some internal vars
    // variable to check if a builder is only used once
    internal var mUsed = false
    internal var mCurrentStickyFooterSelection = -1
    internal var mAppended = false

    // the activity to use
    internal var mActivity: Activity? = null
    internal lateinit var mLayoutManager: RecyclerView.LayoutManager
    internal lateinit var mRootView: ViewGroup
    internal lateinit var mMaterialize: Materialize
    val idDistributor: DefaultIdDistributor<IIdentifyable> = DefaultIdDistributorImpl()

    // set non translucent statusBar mode
    internal var mTranslucentStatusBar = true

    // set if we want to display the specific Drawer below the statusBar
    internal var mDisplayBelowStatusBar: Boolean? = null

    //defines if we want a inner shadow (used in with the MiniDrawer)
    private var mInnerShadow = false

    // the toolbar of the activity
    internal var mToolbar: Toolbar? = null

    // set non translucent NavigationBar mode
    internal var mTranslucentNavigationBar = false

    // set to disable the translucent statusBar Programmatically
    internal var mTranslucentNavigationBarProgrammatically = false


    // set non translucent NavigationBar mode
    internal var mFullscreen = false

    // set to no systemUI visible mode
    internal var mSystemUIHidden = false


    // a custom view to be used instead of everything else
    internal var mCustomView: View? = null

    // the drawerLayout to use
    internal lateinit var mDrawerLayout: DrawerLayout
    internal lateinit var mSliderLayout: ScrimInsetsRelativeLayout

    //the background color for the slider
    internal var mSliderBackgroundColor = 0
    internal var mSliderBackgroundColorRes = -1
    internal var mSliderBackgroundDrawable: Drawable? = null
    internal var mSliderBackgroundDrawableRes = -1

    //the width of the drawer
    internal var mDrawerWidth = -1

    //the gravity of the drawer
    internal var mDrawerGravity: Int = GravityCompat.START

    //the account selection header to use
    internal var mAccountHeader: AccountHeader? = null
    internal var mAccountHeaderSticky = false

    // enable/disable the actionBarDrawerToggle animation
    internal var mAnimateActionBarDrawerToggle = false


    // enable the drawer toggle / if withActionBarDrawerToggle we will autoGenerate it
    internal var mActionBarDrawerToggleEnabled = true

    // drawer toggle
    internal var mActionBarDrawerToggle: ActionBarDrawerToggle? = null

    // defines if the drawer should scroll to top after click
    internal var mScrollToTopAfterClick = false


    // header view
    internal var mHeaderView: View? = null
    internal var mHeaderDivider = true
    internal var mHeaderPadding = true
    internal var mHeiderHeight: DimenHolder? = null

    // sticky view
    internal var mStickyHeaderView: View? = null
    // shadow shown on the top of the sticky header
    internal var mStickyHeaderShadow = true

    // footer view
    internal var mFooterView: View? = null
    internal var mFooterDivider = true
    internal var mFooterClickable = false

    // sticky view
    internal var mStickyFooterView: ViewGroup? = null
    // divider shown on top of the sticky footer
    internal var mStickyFooterDivider = false
    // sticky view
    internal var mStickyFooterShadowView: View? = null
    // shadow shown on the top of the sticky footer
    internal var mStickyFooterShadow = true

    // fire onClick after build
    internal var mFireInitialOnClick = false

    // if multiSelection is possible
    internal var mMultiSelect = false

    // item to select
    internal var mSelectedItemPosition = 0

    // item to select
    internal var mSelectedItemIdentifier: Long = 0

    // an RecyclerView to use within the drawer :D
    internal lateinit var mRecyclerView: RecyclerView

    // if the adapter should enable hasStableIds to improve performance and allow animations
    internal var mHasStableIds = false

    // an adapter to use for the list
    internal lateinit var _adapter: FastAdapter<IDrawerItem<*>>
    internal var mHeaderAdapter: ModelAdapter<IDrawerItem<*>, IDrawerItem<*>> = ItemAdapter()
    internal var mItemAdapter: ModelAdapter<IDrawerItem<*>, IDrawerItem<*>> = ItemAdapter()
    internal var mFooterAdapter: ModelAdapter<IDrawerItem<*>, IDrawerItem<*>> = ItemAdapter()
    internal lateinit var mExpandableExtension: ExpandableExtension<IDrawerItem<*>>
    internal lateinit var mSelectExtension: SelectExtension<IDrawerItem<*>>

    /**
     * get the adapter (null safe)
     *
     * @return the FastAdapter used with this drawer
     */
    internal var adapter: FastAdapter<IDrawerItem<*>>
        get() {
            if (!::_adapter.isInitialized) {
                _adapter = FastAdapter.with(Arrays.asList(mHeaderAdapter, mItemAdapter, mFooterAdapter))
                _adapter.setHasStableIds(mHasStableIds)
                initAdapter()
                mSelectExtension.isSelectable = true
                mSelectExtension.multiSelect = false
                mSelectExtension.allowDeselection = false
            }
            return _adapter
        }
        set(value) {
            _adapter = value
        }

    internal val selectExtension: SelectExtension<IDrawerItem<*>>
        get() {
            adapter
            return mSelectExtension
        }

    internal val itemAdapter: IItemAdapter<IDrawerItem<*>, IDrawerItem<*>>
        get() = mItemAdapter

    internal val headerAdapter: IItemAdapter<IDrawerItem<*>, IDrawerItem<*>>
        get() = mHeaderAdapter

    internal val footerAdapter: IItemAdapter<IDrawerItem<*>, IDrawerItem<*>>
        get() = mFooterAdapter

    // Defines a Adapter which wraps the main Adapter used in the RecyclerView to allow extended navigation and other stuff
    internal var adapterWrapper: RecyclerView.Adapter<*>? = null


    //defines the itemAnimator to be used in conjunction with the RecyclerView
    internal var mItemAnimator: RecyclerView.ItemAnimator = DefaultItemAnimator()

    // defines if we want to keep the sticky items visible, upon switching to the profiles
    internal var mKeepStickyItemsVisible = false

    // always visible list in drawer
    internal var mStickyDrawerItems: MutableList<IDrawerItem<*>> = ArrayList()

    // close drawer on click
    internal var mCloseOnClick = true

    // delay drawer close to prevent lag
    internal var mDelayOnDrawerClose = 50

    // delay drawer click event to prevent lag (you should either choose DelayOnDrawerClose or this)
    internal var mDelayDrawerClickEvent = 0

    // onDrawerListener
    internal var mOnDrawerListener: Drawer.OnDrawerListener? = null

    // onDrawerItemClickListeners
    internal var mOnDrawerItemClickListener: Drawer.OnDrawerItemClickListener? = null

    // onDrawerItemClickListeners
    internal var mOnDrawerItemLongClickListener: Drawer.OnDrawerItemLongClickListener? = null

    // onDrawerListener
    internal var mOnDrawerNavigationListener: Drawer.OnDrawerNavigationListener? = null

    //show the drawer on the first launch to show the user its there
    internal var mShowDrawerOnFirstLaunch = false

    //show the drawer on launch to show the user its there, keep doing it until the user has dragged it open once
    internal var mShowDrawerUntilDraggedOpened = false

    //also generate the MiniDrawer for this Drawer
    internal var mGenerateMiniDrawer = false
    internal var mMiniDrawer: MiniDrawer? = null


    // savedInstance to restore state
    internal var mSavedInstance: Bundle? = null

    // shared preferences to use for integrated functions
    internal var mSharedPreferences: SharedPreferences? = null

    /**
     * default constructor
     */
    constructor() {
        adapter
    }

    /**
     * Construct a Drawer by passing the activity to use for the generation
     *
     * @param activity current activity which will contain the drawer
     */
    constructor(activity: Activity) {
        this.mRootView = activity.findViewById<ViewGroup>(android.R.id.content)
        this.mActivity = activity
        this.mLayoutManager = LinearLayoutManager(mActivity)
        adapter
    }

    /**
     * Sets the activity which will be generated for the generation
     * The activity is required and will be used to inflate the content in.
     * After generation it is set to null to prevent a memory leak.
     *
     * @param activity current activity which will contain the drawer
     */
    fun withActivity(activity: Activity): DrawerBuilder {
        this.mRootView = activity.findViewById<ViewGroup>(android.R.id.content)
        this.mActivity = activity
        this.mLayoutManager = LinearLayoutManager(mActivity)
        return this
    }

    /**
     * Sets the rootView which will host the DrawerLayout
     * The content of this view will be extracted and added as the new content inside the drawerLayout
     *
     * @param rootView a view which will get switched out by the DrawerLayout and added as its child
     */
    fun withRootView(rootView: ViewGroup): DrawerBuilder {
        this.mRootView = rootView

        //disable the translucent statusBar we don't need it
        withTranslucentStatusBar(false)

        return this
    }

    /**
     * Sets the rootView which will host the DrawerLayout
     * The content of this view will be extracted and added as the new content inside the drawerLayout
     *
     * @param rootViewRes the id of a view which will get switched out by the DrawerLayout and added as its child
     */
    fun withRootView(@IdRes rootViewRes: Int): DrawerBuilder {
        val mActivity = this.mActivity
                ?: throw RuntimeException("please pass an activity first to use this call")
        return withRootView(mActivity.findViewById<ViewGroup>(rootViewRes))
    }

    /**
     * Sets that the view which hosts the DrawerLayout should have a translucent statusBar
     * This is true by default, so it's possible to display the drawer under the statusBar
     *
     * @param translucentStatusBar sets whether the statusBar is transparent (and the drawer is displayed under it) or not
     */
    fun withTranslucentStatusBar(translucentStatusBar: Boolean): DrawerBuilder {
        this.mTranslucentStatusBar = translucentStatusBar
        return this
    }

    /**
     * Sets that the slider of this Drawer should be displayed below the statusBar even with a translucentStatusBar
     *
     * @param displayBelowStatusBar sets wheter the slider of the drawer is displayed below the statusBar or not
     */
    fun withDisplayBelowStatusBar(displayBelowStatusBar: Boolean): DrawerBuilder {
        this.mDisplayBelowStatusBar = displayBelowStatusBar
        return this
    }

    /**
     * sets if the drawer should show an inner shadow or not
     *
     * @param innerShadow sets wheter the drawer should display an inner shadow or not
     * @return
     */
    fun withInnerShadow(innerShadow: Boolean): DrawerBuilder {
        this.mInnerShadow = innerShadow
        return this
    }

    /**
     * Sets the toolbar which should be used in combination with the drawer
     * This will handle the ActionBarDrawerToggle for you.
     * Do not set this if you are in a sub activity and want to handle the back arrow on your own
     *
     * @param toolbar the toolbar which is used in combination with the drawer
     */
    fun withToolbar(toolbar: Toolbar): DrawerBuilder {
        this.mToolbar = toolbar
        return this
    }

    /**
     * Set to true if you use a translucent NavigationBar
     *
     * @param translucentNavigationBar
     * @return
     */
    fun withTranslucentNavigationBar(translucentNavigationBar: Boolean): DrawerBuilder {
        this.mTranslucentNavigationBar = translucentNavigationBar

        //if we disable the translucentNavigationBar it should be disabled at all
        if (!translucentNavigationBar) {
            this.mTranslucentNavigationBarProgrammatically = false
        }

        return this
    }

    /**
     * set this to true if you want a translucent navigation bar.
     *
     * @param translucentNavigationBarProgrammatically
     * @return
     */
    fun withTranslucentNavigationBarProgrammatically(translucentNavigationBarProgrammatically: Boolean): DrawerBuilder {
        this.mTranslucentNavigationBarProgrammatically = translucentNavigationBarProgrammatically
        //if we enable the programmatically translucent navigationBar we want also the normal navigationBar behavior
        if (translucentNavigationBarProgrammatically) {
            this.mTranslucentNavigationBar = true
        }
        return this
    }

    /**
     * Set to true if the used theme has a translucent statusBar
     * and navigationBar and you want to manage the padding on your own.
     *
     * @param fullscreen
     * @return
     */
    fun withFullscreen(fullscreen: Boolean): DrawerBuilder {
        this.mFullscreen = fullscreen

        if (fullscreen) {
            withTranslucentStatusBar(true)
            withTranslucentNavigationBar(false)
        }

        return this
    }

    /**
     * Set to true if you use your app in complete fullscreen mode
     * with hidden statusBar and navigationBar
     *
     * @param systemUIHidden
     * @return
     */
    fun withSystemUIHidden(systemUIHidden: Boolean): DrawerBuilder {
        this.mSystemUIHidden = systemUIHidden

        if (systemUIHidden) {
            withFullscreen(systemUIHidden)
        }

        return this
    }

    /**
     * Pass a custom view if you need a completely custom drawer
     * content
     *
     * @param customView
     * @return
     */
    fun withCustomView(customView: View): DrawerBuilder {
        this.mCustomView = customView
        return this
    }

    /**
     * Pass a custom DrawerLayout which will be used.
     * NOTE: This requires the same structure as the drawer.xml
     *
     * @param drawerLayout
     * @return
     */
    fun withDrawerLayout(drawerLayout: DrawerLayout): DrawerBuilder {
        this.mDrawerLayout = drawerLayout
        return this
    }

    /**
     * Pass a custom DrawerLayout Resource which will be used.
     * NOTE: This requires the same structure as the drawer.xml
     *
     * @param resLayout
     * @return
     */
    fun withDrawerLayout(@LayoutRes resLayout: Int): DrawerBuilder {
        val mActivity = this.mActivity
                ?: throw RuntimeException("please pass an activity first to use this call")

        if (resLayout != -1) {
            this.mDrawerLayout = mActivity.layoutInflater.inflate(resLayout, mRootView, false) as DrawerLayout
        } else {
            if (Build.VERSION.SDK_INT < 21) {
                this.mDrawerLayout = mActivity.layoutInflater.inflate(R.layout.material_drawer_fits_not, mRootView, false) as DrawerLayout
            } else {
                this.mDrawerLayout = mActivity.layoutInflater.inflate(R.layout.material_drawer, mRootView, false) as DrawerLayout
            }
        }

        return this
    }

    /**
     * Set the background color for the Slider.
     * This is the view containing the list.
     *
     * @param sliderBackgroundColor
     * @return
     */
    fun withSliderBackgroundColor(@ColorInt sliderBackgroundColor: Int): DrawerBuilder {
        this.mSliderBackgroundColor = sliderBackgroundColor
        return this
    }

    /**
     * Set the background color for the Slider from a Resource.
     * This is the view containing the list.
     *
     * @param sliderBackgroundColorRes
     * @return
     */
    fun withSliderBackgroundColorRes(@ColorRes sliderBackgroundColorRes: Int): DrawerBuilder {
        this.mSliderBackgroundColorRes = sliderBackgroundColorRes
        return this
    }


    /**
     * Set the background drawable for the Slider.
     * This is the view containing the list.
     *
     * @param sliderBackgroundDrawable
     * @return
     */
    fun withSliderBackgroundDrawable(sliderBackgroundDrawable: Drawable): DrawerBuilder {
        this.mSliderBackgroundDrawable = sliderBackgroundDrawable
        return this
    }


    /**
     * Set the background drawable for the Slider from a Resource.
     * This is the view containing the list.
     *
     * @param sliderBackgroundDrawableRes
     * @return
     */
    fun withSliderBackgroundDrawableRes(@DrawableRes sliderBackgroundDrawableRes: Int): DrawerBuilder {
        this.mSliderBackgroundDrawableRes = sliderBackgroundDrawableRes
        return this
    }

    /**
     * Set the DrawerBuilder width with a pixel value
     *
     * @param drawerWidthPx
     * @return
     */
    fun withDrawerWidthPx(drawerWidthPx: Int): DrawerBuilder {
        this.mDrawerWidth = drawerWidthPx
        return this
    }

    /**
     * Set the DrawerBuilder width with a dp value
     *
     * @param drawerWidthDp
     * @return
     */
    fun withDrawerWidthDp(drawerWidthDp: Int): DrawerBuilder {
        val mActivity = this.mActivity
                ?: throw RuntimeException("please pass an activity first to use this call")
        this.mDrawerWidth = UIUtils.convertDpToPixel(drawerWidthDp.toFloat(), mActivity).toInt()
        return this
    }

    /**
     * Set the DrawerBuilder width with a dimension resource
     *
     * @param drawerWidthRes
     * @return
     */
    fun withDrawerWidthRes(@DimenRes drawerWidthRes: Int): DrawerBuilder {
        val mActivity = this.mActivity
                ?: throw RuntimeException("please pass an activity first to use this call")
        this.mDrawerWidth = mActivity.resources.getDimensionPixelSize(drawerWidthRes)
        return this
    }

    /**
     * Set the gravity for the drawer. START, LEFT | RIGHT, END
     *
     * @param gravity
     * @return
     */
    fun withDrawerGravity(gravity: Int): DrawerBuilder {
        this.mDrawerGravity = gravity
        return this
    }

    /**
     * Add a AccountSwitcherHeader which will be used in this drawer instance. Pass true if it should be sticky
     * NOTE: This will overwrite any set headerView or stickyHeaderView (depends on the boolean).
     *
     * @param accountHeader
     * @param accountHeaderSticky
     * @return
     */
    @JvmOverloads
    fun withAccountHeader(accountHeader: AccountHeader, accountHeaderSticky: Boolean = false): DrawerBuilder {
        this.mAccountHeader = accountHeader
        this.mAccountHeaderSticky = accountHeaderSticky
        return this
    }

    /**
     * Set this to true if you want the ActionBarDrawerToggle to be animated.
     * NOTE: This will only work if the built in ActionBarDrawerToggle is used.
     * Enable it by setting withActionBarDrawerToggle to true
     *
     * @param actionBarDrawerToggleAnimated
     * @return
     */
    fun withActionBarDrawerToggleAnimated(actionBarDrawerToggleAnimated: Boolean): DrawerBuilder {
        this.mAnimateActionBarDrawerToggle = actionBarDrawerToggleAnimated
        return this
    }

    /**
     * Set this to false if you don't need the included ActionBarDrawerToggle
     *
     * @param actionBarDrawerToggleEnabled
     * @return
     */
    fun withActionBarDrawerToggle(actionBarDrawerToggleEnabled: Boolean): DrawerBuilder {
        this.mActionBarDrawerToggleEnabled = actionBarDrawerToggleEnabled
        return this
    }

    /**
     * Add a custom ActionBarDrawerToggle which will be used in combination with this drawer.
     *
     * @param actionBarDrawerToggle
     * @return
     */
    fun withActionBarDrawerToggle(actionBarDrawerToggle: ActionBarDrawerToggle): DrawerBuilder {
        this.mActionBarDrawerToggleEnabled = true
        this.mActionBarDrawerToggle = actionBarDrawerToggle
        return this
    }

    /**
     * defines if the drawer should scroll to top after click
     *
     * @param scrollToTopAfterClick
     * @return
     */
    fun withScrollToTopAfterClick(scrollToTopAfterClick: Boolean): DrawerBuilder {
        this.mScrollToTopAfterClick = scrollToTopAfterClick
        return this
    }

    /**
     * Add a header to the DrawerBuilder ListView. This can be any view
     *
     * @param headerView
     * @return
     */
    fun withHeader(headerView: View): DrawerBuilder {
        this.mHeaderView = headerView
        return this
    }

    /**
     * Add a header to the DrawerBuilder ListView defined by a resource.
     *
     * @param headerViewRes
     * @return
     */
    fun withHeader(@LayoutRes headerViewRes: Int): DrawerBuilder {
        val mActivity = this.mActivity
                ?: throw RuntimeException("please pass an activity first to use this call")
        if (headerViewRes != -1) {
            //i know there should be a root, bit i got none here
            this.mHeaderView = mActivity.layoutInflater.inflate(headerViewRes, null, false)
        }

        return this
    }

    /**
     * Set this to false if you don't need the divider below the header
     *
     * @param headerDivider
     * @return
     */
    fun withHeaderDivider(headerDivider: Boolean): DrawerBuilder {
        this.mHeaderDivider = headerDivider
        return this
    }

    /**
     * Set this to false if you don't need the padding below the header
     *
     * @param headerPadding
     * @return
     */
    fun withHeaderPadding(headerPadding: Boolean): DrawerBuilder {
        this.mHeaderPadding = headerPadding
        return this
    }

    /**
     * Sets the header height for the header provided via `withHeader()`
     *
     * @param headerHeight the DimenHolder with the height we want to set for the header
     * @return
     */
    fun withHeaderHeight(headerHeight: DimenHolder): DrawerBuilder {
        this.mHeiderHeight = headerHeight
        return this
    }

    /**
     * Add a sticky header below the DrawerBuilder ListView. This can be any view
     *
     * @param stickyHeader
     * @return
     */
    fun withStickyHeader(stickyHeader: View): DrawerBuilder {
        this.mStickyHeaderView = stickyHeader
        return this
    }

    /**
     * Add a sticky header below the DrawerBuilder ListView defined by a resource.
     *
     * @param stickyHeaderRes
     * @return
     */
    fun withStickyHeader(@LayoutRes stickyHeaderRes: Int): DrawerBuilder {
        val mActivity = this.mActivity
                ?: throw RuntimeException("please pass an activity first to use this call")
        if (stickyHeaderRes != -1) {
            //i know there should be a root, bit i got none here
            this.mStickyHeaderView = mActivity.layoutInflater.inflate(stickyHeaderRes, null, false)
        }

        return this
    }

    /**
     * Set this to false if you don't want the shadow below the sticky header
     *
     * @param stickyHeaderShadow
     * @return
     */
    fun withStickyHeaderShadow(stickyHeaderShadow: Boolean): DrawerBuilder {
        this.mStickyHeaderShadow = stickyHeaderShadow
        return this
    }

    /**
     * Add a footer to the DrawerBuilder ListView. This can be any view
     *
     * @param footerView
     * @return
     */
    fun withFooter(footerView: View): DrawerBuilder {
        this.mFooterView = footerView
        return this
    }

    /**
     * Add a footer to the DrawerBuilder ListView defined by a resource.
     *
     * @param footerViewRes
     * @return
     */
    fun withFooter(@LayoutRes footerViewRes: Int): DrawerBuilder {
        val mActivity = this.mActivity
                ?: throw RuntimeException("please pass an activity first to use this call")
        if (footerViewRes != -1) {
            //i know there should be a root, bit i got none here
            this.mFooterView = mActivity.layoutInflater.inflate(footerViewRes, null, false)
        }

        return this
    }

    /**
     * Set this to true if you want the footer to be clickable
     *
     * @param footerClickable
     * @return
     */
    fun withFooterClickable(footerClickable: Boolean): DrawerBuilder {
        this.mFooterClickable = footerClickable
        return this
    }

    /**
     * Set this to false if you don't need the divider above the footer
     *
     * @param footerDivider
     * @return
     */
    fun withFooterDivider(footerDivider: Boolean): DrawerBuilder {
        this.mFooterDivider = footerDivider
        return this
    }

    /**
     * Add a sticky footer below the DrawerBuilder ListView. This can be any view
     *
     * @param stickyFooter
     * @return
     */
    fun withStickyFooter(stickyFooter: ViewGroup): DrawerBuilder {
        this.mStickyFooterView = stickyFooter
        return this
    }

    /**
     * Add a sticky footer below the DrawerBuilder ListView defined by a resource.
     *
     * @param stickyFooterRes
     * @return
     */
    fun withStickyFooter(@LayoutRes stickyFooterRes: Int): DrawerBuilder {
        val mActivity = this.mActivity
                ?: throw RuntimeException("please pass an activity first to use this call")
        if (stickyFooterRes != -1) {
            //i know there should be a root, bit i got none here
            this.mStickyFooterView = mActivity.layoutInflater.inflate(stickyFooterRes, null, false) as ViewGroup
        }

        return this
    }

    /**
     * Set this to true if you want the divider above the sticky footer
     *
     * @param stickyFooterDivider
     * @return
     */
    fun withStickyFooterDivider(stickyFooterDivider: Boolean): DrawerBuilder {
        this.mStickyFooterDivider = stickyFooterDivider
        return this
    }

    /**
     * Set this to false if you don't want the shadow on top of the sticky footer
     *
     * @param stickyFooterShadow
     * @return
     */
    fun withStickyFooterShadow(stickyFooterShadow: Boolean): DrawerBuilder {
        this.mStickyFooterShadow = stickyFooterShadow
        return this
    }

    /**
     * Set this to true if you love to get an initial onClick event after the build method is called
     *
     * @param fireOnInitialOnClick
     * @return
     */
    fun withFireOnInitialOnClick(fireOnInitialOnClick: Boolean): DrawerBuilder {
        this.mFireInitialOnClick = fireOnInitialOnClick
        return this
    }

    /**
     * set this to true if you want to enable multiSelect mode inside the drawer. Note
     * you will have to programmatically deselect if you want to remove all selections!
     * You can disable this at a later time via .getAdapter().withMultiSelect(false)
     * You can also modify all other settings of the FastAdapter via this method
     *
     * @param multiSelect true if multiSelect is enabled (default: false)
     * @return this
     */
    fun withMultiSelect(multiSelect: Boolean): DrawerBuilder {
        this.mMultiSelect = multiSelect
        return this
    }

    /**
     * Set this to the index of the item, you would love to select upon start
     *
     * @param selectedItemPosition
     * @return
     */
    fun withSelectedItemByPosition(selectedItemPosition: Int): DrawerBuilder {
        this.mSelectedItemPosition = selectedItemPosition
        return this
    }

    /**
     * Set this to the identifier of the item, you would love to select upon start
     *
     * @param selectedItemIdentifier
     * @return
     */
    fun withSelectedItem(selectedItemIdentifier: Long): DrawerBuilder {
        this.mSelectedItemIdentifier = selectedItemIdentifier
        return this
    }

    /**
     * Define a custom RecyclerView which will be used in the drawer
     * NOTE: this is not recommended
     *
     * @param recyclerView
     * @return
     */
    fun withRecyclerView(recyclerView: RecyclerView): DrawerBuilder {
        this.mRecyclerView = recyclerView
        return this
    }

    /**
     * define this if you want enable hasStableIds for the adapter which is generated.
     * WARNING: only use this if you have set an identifer for all of your items else this could cause
     * many weird things
     *
     * @param hasStableIds
     * @return
     */
    fun withHasStableIds(hasStableIds: Boolean): DrawerBuilder {
        this.mHasStableIds = hasStableIds
        if (adapter != null) {
            adapter.setHasStableIds(hasStableIds)
        }
        return this
    }

    /**
     * Define a custom Adapter which will be used in the drawer
     * NOTE: this is not recommender
     * WARNING: if you do this after adding items you will loose those!
     *
     * @param adapter the FastAdapter to use with this drawer
     * @return this
     */
    fun withAdapter(adaptr: FastAdapter<IDrawerItem<*>>): DrawerBuilder {
        this.adapter = adaptr
        mSelectExtension = adapter.getOrCreateExtension(SelectExtension::class.java)!! // is definitely not null
        //we have to rewrap as a different FastAdapter was provided
        adapter.addAdapter(0, mHeaderAdapter)
        adapter.addAdapter(1, mItemAdapter)
        adapter.addAdapter(2, mFooterAdapter)
        initAdapter()
        return this
    }

    private fun initAdapter() {
        ExtensionsFactories.register(SelectExtensionFactory())
        ExtensionsFactories.register(ExpandableExtensionFactory())

        mSelectExtension = adapter.getOrCreateExtension(SelectExtension::class.java)!! // is definitely not null
        // TODO mHeaderAdapter.idDistributor = idDistributor TODO
        // TODO mItemAdapter.idDistributor = idDistributor TODO
        // TODO mFooterAdapter.idDistributor = idDistributor TODO
        mExpandableExtension = adapter.getOrCreateExtension(ExpandableExtension::class.java)!! // is definitely not null
    }

    /**
     * Defines a Adapter which wraps the main Adapter used in the RecyclerView to allow extended navigation and other stuff
     *
     * @param adapterWrapper
     * @return
     */
    fun withAdapterWrapper(adapterWrapper: RecyclerView.Adapter<*>): DrawerBuilder {
        if (!::_adapter.isInitialized) {
            throw RuntimeException("this adapter has to be set in conjunction to a normal adapter which is used inside this wrapper adapter")
        }
        this.adapterWrapper = adapterWrapper
        return this
    }

    /**
     * defines the itemAnimator to be used in conjunction with the RecyclerView
     *
     * @param itemAnimator
     * @return
     */
    fun withItemAnimator(itemAnimator: RecyclerView.ItemAnimator): DrawerBuilder {
        mItemAnimator = itemAnimator
        return this
    }

    /**
     * Set the initial List of IDrawerItems for the Drawer
     *
     * @param drawerItems
     * @return
     */
    fun withDrawerItems(drawerItems: List<IDrawerItem<*>>): DrawerBuilder {
        this.itemAdapter.set(drawerItems)
        return this
    }

    /**
     * Add a initial DrawerItem or a DrawerItem Array  for the Drawer
     *
     * @param drawerItems
     * @return
     */
    fun addDrawerItems(vararg drawerItems: IDrawerItem<*>): DrawerBuilder {
        this.itemAdapter.add(*drawerItems)
        return this
    }

    /**
     * Toggles if the sticky footer should stay visible upon switching to the profile list
     * **WARNING** using this with stickyDrawerItems can lead to the selection not being updated correctly. Use with care
     *
     * @param keepStickyItemsVisible true if the sticky footer should stay visible
     * @return this
     */
    fun withKeepStickyItemsVisible(keepStickyItemsVisible: Boolean): DrawerBuilder {
        this.mKeepStickyItemsVisible = keepStickyItemsVisible
        return this
    }

    /**
     * Set the initial List of IDrawerItems for the StickyDrawerFooter
     *
     * @param stickyDrawerItems
     * @return
     */
    fun withStickyDrawerItems(stickyDrawerItems: MutableList<IDrawerItem<*>>): DrawerBuilder {
        this.mStickyDrawerItems = stickyDrawerItems
        return this
    }

    /**
     * Add a initial DrawerItem or a DrawerItem Array for the StickyDrawerFooter
     *
     * @param stickyDrawerItems
     * @return
     */
    fun addStickyDrawerItems(vararg stickyDrawerItems: IDrawerItem<*>): DrawerBuilder {
        Collections.addAll(this.mStickyDrawerItems, *stickyDrawerItems)
        return this
    }

    /**
     * Inflates the DrawerItems from a menu.xml
     *
     * @param menuRes
     * @return
     */
    @SuppressLint("RestrictedApi")
    fun inflateMenu(@MenuRes menuRes: Int): DrawerBuilder {
        val menuInflater = SupportMenuInflater(mActivity)
        val mMenu = MenuBuilder(mActivity)

        menuInflater.inflate(menuRes, mMenu)

        addMenuItems(mMenu, false)

        return this
    }

    /**
     * helper method to init the drawerItems from a menu
     *
     * @param mMenu
     * @param subMenu
     */
    private fun addMenuItems(mMenu: Menu, subMenu: Boolean) {
        var groupId = R.id.material_drawer_menu_default_group
        for (i in 0 until mMenu.size()) {
            val mMenuItem = mMenu.getItem(i)
            var iDrawerItem: IDrawerItem<*>
            if (!subMenu && mMenuItem.groupId != groupId && mMenuItem.groupId != 0) {
                groupId = mMenuItem.groupId
                iDrawerItem = DividerDrawerItem()
                itemAdapter.add(iDrawerItem)
            }
            if (mMenuItem.hasSubMenu()) {
                iDrawerItem = PrimaryDrawerItem()
                        .withName(mMenuItem.title.toString())
                        .withIcon(mMenuItem.icon)
                        .withIdentifier(mMenuItem.itemId.toLong())
                        .withEnabled(mMenuItem.isEnabled)
                        .withSelectable(false)
                itemAdapter.add(iDrawerItem)
                addMenuItems(mMenuItem.subMenu, true)
            } else if (mMenuItem.groupId != 0 || subMenu) {
                iDrawerItem = SecondaryDrawerItem()
                        .withName(mMenuItem.title.toString())
                        .withIcon(mMenuItem.icon)
                        .withIdentifier(mMenuItem.itemId.toLong())
                        .withEnabled(mMenuItem.isEnabled)
                itemAdapter.add(iDrawerItem)
            } else {
                iDrawerItem = PrimaryDrawerItem()
                        .withName(mMenuItem.title.toString())
                        .withIcon(mMenuItem.icon)
                        .withIdentifier(mMenuItem.itemId.toLong())
                        .withEnabled(mMenuItem.isEnabled)
                itemAdapter.add(iDrawerItem)
            }
        }
    }

    /**
     * Set this to false if the drawer should stay opened after an item was clicked
     *
     * @param closeOnClick
     * @return this
     */
    fun withCloseOnClick(closeOnClick: Boolean): DrawerBuilder {
        this.mCloseOnClick = closeOnClick
        return this
    }

    /**
     * Define the delay for the drawer close operation after a click.
     * This is a small trick to improve the speed (and remove lag) if you open a new activity after a DrawerItem
     * was selected.
     * NOTE: Disable this by passing -1
     *
     * @param delayOnDrawerClose the delay in MS (-1 to disable)
     * @return this
     */
    fun withDelayOnDrawerClose(delayOnDrawerClose: Int): DrawerBuilder {
        this.mDelayOnDrawerClose = delayOnDrawerClose
        return this
    }

    /**
     * Define the delay for the drawer click event after a click.
     * This can be used to improve performance and prevent lag, especially when you switch fragments inside the listener.
     * This will ignore the boolean value you can return in the listener, as the listener is called after the drawer was closed.
     * NOTE: Disable this to pass -1
     *
     * @param delayDrawerClickEvent -1 to disable
     * @return this
     */
    fun withDelayDrawerClickEvent(delayDrawerClickEvent: Int): DrawerBuilder {
        this.mDelayDrawerClickEvent = delayDrawerClickEvent
        return this
    }

    /**
     * Define a OnDrawerListener for this Drawer
     *
     * @param onDrawerListener
     * @return this
     */
    fun withOnDrawerListener(onDrawerListener: Drawer.OnDrawerListener): DrawerBuilder {
        this.mOnDrawerListener = onDrawerListener
        return this
    }

    /**
     * Define a OnDrawerItemClickListener for this Drawer
     *
     * @param onDrawerItemClickListener
     * @return
     */
    fun withOnDrawerItemClickListener(onDrawerItemClickListener: Drawer.OnDrawerItemClickListener): DrawerBuilder {
        this.mOnDrawerItemClickListener = onDrawerItemClickListener
        return this
    }

    /**
     * Define a OnDrawerItemLongClickListener for this Drawer
     *
     * @param onDrawerItemLongClickListener
     * @return
     */
    fun withOnDrawerItemLongClickListener(onDrawerItemLongClickListener: Drawer.OnDrawerItemLongClickListener): DrawerBuilder {
        this.mOnDrawerItemLongClickListener = onDrawerItemLongClickListener
        return this
    }

    /**
     * Define a OnDrawerNavigationListener for this Drawer
     *
     * @param onDrawerNavigationListener
     * @return this
     */
    fun withOnDrawerNavigationListener(onDrawerNavigationListener: Drawer.OnDrawerNavigationListener): DrawerBuilder {
        this.mOnDrawerNavigationListener = onDrawerNavigationListener
        return this
    }

    /**
     * define if the DrawerBuilder is shown on the first launch
     *
     * @param showDrawerOnFirstLaunch
     * @return
     */
    fun withShowDrawerOnFirstLaunch(showDrawerOnFirstLaunch: Boolean): DrawerBuilder {
        this.mShowDrawerOnFirstLaunch = showDrawerOnFirstLaunch
        return this
    }

    /**
     * define if the DrawerBuilder is shown until the user has dragged it open once
     *
     * @param showDrawerUntilDraggedOpened
     * @return DrawerBuilder
     */
    fun withShowDrawerUntilDraggedOpened(showDrawerUntilDraggedOpened: Boolean): DrawerBuilder {
        mShowDrawerUntilDraggedOpened = showDrawerUntilDraggedOpened
        return this
    }

    /**
     * define if the DrawerBuilder should also generate a MiniDrawer for th
     *
     * @param generateMiniDrawer
     * @return
     */
    fun withGenerateMiniDrawer(generateMiniDrawer: Boolean): DrawerBuilder {
        this.mGenerateMiniDrawer = generateMiniDrawer
        return this
    }

    /**
     * Set the Bundle (savedInstance) which is passed by the activity.
     * No need to null-check everything is handled automatically
     *
     * @param savedInstance
     * @return
     */
    fun withSavedInstance(savedInstance: Bundle?): DrawerBuilder {
        this.mSavedInstance = savedInstance
        return this
    }

    /**
     * Set the [SharedPreferences] to use for the `showDrawerOnFirstLaunch` or the `ShowDrawerUntilDraggedOpened`
     *
     * @param sharedPreferences SharedPreference to use
     * @return this
     */
    fun withSharedPreferences(sharedPreferences: SharedPreferences): DrawerBuilder {
        this.mSharedPreferences = sharedPreferences
        return this
    }

    /**
     * helper method to handle when the drawer should be shown on launch
     */
    private fun handleShowOnLaunch() {
        //check if it should be shown on launch (and we have a drawerLayout)
        if (mActivity != null) {
            if (mShowDrawerOnFirstLaunch || mShowDrawerUntilDraggedOpened) {
                val preferences = if (mSharedPreferences != null) mSharedPreferences else PreferenceManager.getDefaultSharedPreferences(mActivity)

                preferences?.let { preferences ->
                    if (mShowDrawerOnFirstLaunch && !preferences.getBoolean(Drawer.PREF_USER_LEARNED_DRAWER, false)) {
                        //if it was not shown yet
                        //open the drawer
                        mDrawerLayout.openDrawer(mSliderLayout)

                        //save that it showed up once ;)
                        val editor = preferences.edit()
                        editor.putBoolean(Drawer.PREF_USER_LEARNED_DRAWER, true)
                        editor.apply()

                    } else if (mShowDrawerUntilDraggedOpened && !preferences.getBoolean(Drawer.PREF_USER_OPENED_DRAWER_BY_DRAGGING, false)) {
                        // open the drawer since the user has not dragged it open yet
                        mDrawerLayout.openDrawer(mSliderLayout)

                        // add a listener to detect dragging
                        mDrawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
                            var hasBeenDragged = false

                            override fun onDrawerStateChanged(newState: Int) {
                                if (newState == DrawerLayout.STATE_DRAGGING) {
                                    // save that the user was dragging
                                    hasBeenDragged = true

                                } else if (newState == DrawerLayout.STATE_IDLE) {
                                    // check if the user was dragging and if that resulted in an open drawer
                                    if (hasBeenDragged && mDrawerLayout.isDrawerOpen(mDrawerGravity)) {
                                        // Save that the user has dragged it open
                                        val editor = preferences.edit()
                                        editor.putBoolean(Drawer.PREF_USER_OPENED_DRAWER_BY_DRAGGING, true)
                                        editor.apply()
                                    } else {
                                        // reset the drag boolean
                                        hasBeenDragged = false
                                    }
                                }
                            }
                        })
                    }
                }

            }
        }
    }

    /**
     * Build and add the DrawerBuilder to your activity
     *
     * @return
     */
    open fun build(): Drawer {
        if (mUsed) {
            throw RuntimeException("you must not reuse a DrawerBuilder builder")
        }

        val mActivity = this.mActivity
                ?: throw RuntimeException("please pass an activity first to use this call")

        //set that this builder was used. now you have to create a new one
        mUsed = true

        // if the user has not set a drawerLayout use the default one :D
        if (!::mDrawerLayout.isInitialized) {
            withDrawerLayout(-1)
        }

        //some new Materialize magic ;)
        mMaterialize = MaterializeBuilder()
                .withActivity(mActivity)
                .withRootView(mRootView)
                .withFullscreen(mFullscreen)
                .withSystemUIHidden(mSystemUIHidden)
                .withUseScrimInsetsLayout(false)
                .withTransparentStatusBar(mTranslucentStatusBar)
                .withTranslucentNavigationBarProgrammatically(mTranslucentNavigationBarProgrammatically)
                .withContainer(mDrawerLayout)
                .build()

        //handle the navigation stuff of the ActionBarDrawerToggle and the drawer in general
        handleDrawerNavigation(mActivity, false)

        //build the view which will be set to the drawer
        val result = buildView()

        //define id for the sliderLayout
        mSliderLayout.id = R.id.material_drawer_slider_layout
        // add the slider to the drawer
        mDrawerLayout.addView(mSliderLayout, 1)

        return result
    }

    /**
     * Build and add the DrawerBuilder to your activity
     *
     * @return
     */
    open fun buildForFragment(): Drawer {
        if (mUsed) {
            throw RuntimeException("you must not reuse a DrawerBuilder builder")
        }
        if (!::mRootView.isInitialized) {
            throw RuntimeException("please pass the view which should host the DrawerLayout")
        }
        val mActivity = this.mActivity
                ?: throw RuntimeException("please pass an activity first to use this call")

        //set that this builder was used. now you have to create a new one
        mUsed = true

        // if the user has not set a drawerLayout use the default one :D
        if (!::mDrawerLayout.isInitialized) {
            withDrawerLayout(-1)
        }

        //set the drawer here...

        val originalContentView = mRootView.getChildAt(0)

        val alreadyInflated = originalContentView.id == R.id.materialize_root

        //only add the new layout if it wasn't done before
        if (!alreadyInflated) {
            // remove the contentView
            mRootView.removeView(originalContentView)
        } else {
            //if it was already inflated we have to clean up again
            mRootView.removeAllViews()
        }

        //create the layoutParams to use for the contentView
        val layoutParamsContentView = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )

        //add the drawer
        mRootView.addView(mDrawerLayout, layoutParamsContentView)

        //set the id so we can check if it was already inflated
        mDrawerLayout.id = R.id.materialize_root

        //handle the navigation stuff of the ActionBarDrawerToggle and the drawer in general
        handleDrawerNavigation(mActivity, false)

        //build the view which will be set to the drawer
        val result = buildView()

        // add the slider to the drawer
        mDrawerLayout.addView(originalContentView, 0)

        //define id for the sliderLayout
        mSliderLayout.id = R.id.material_drawer_slider_layout
        // add the slider to the drawer
        mDrawerLayout.addView(mSliderLayout, 1)

        return result
    }

    /**
     * handles the different logics for the Drawer Navigation Listeners / Indications (ActionBarDrawertoggle)
     */
    internal fun handleDrawerNavigation(activity: Activity?, recreateActionBarDrawerToggle: Boolean) {
        //set the navigationOnClickListener
        val toolbarNavigationListener = View.OnClickListener { v ->
            var handled = false

            if (mActionBarDrawerToggle?.isDrawerIndicatorEnabled == false) {
                handled = mOnDrawerNavigationListener?.onNavigationClickListener(v) ?: false
            }

            if (!handled) {
                if (mDrawerLayout.isDrawerOpen(mDrawerGravity)) {
                    mDrawerLayout.closeDrawer(mDrawerGravity)
                } else {
                    mDrawerLayout.openDrawer(mDrawerGravity)
                }
            }
        }

        if (recreateActionBarDrawerToggle) {
            mActionBarDrawerToggle = null
        }

        // create the ActionBarDrawerToggle if not set and enabled and if we have a toolbar
        if (mActionBarDrawerToggleEnabled && mActionBarDrawerToggle == null && mToolbar != null) {
            this.mActionBarDrawerToggle = object : ActionBarDrawerToggle(activity, mDrawerLayout, mToolbar, R.string.material_drawer_open, R.string.material_drawer_close) {

                override fun onDrawerOpened(drawerView: View) {
                    mOnDrawerListener?.onDrawerOpened(drawerView)
                    super.onDrawerOpened(drawerView)
                }

                override fun onDrawerClosed(drawerView: View) {
                    mOnDrawerListener?.onDrawerClosed(drawerView)
                    super.onDrawerClosed(drawerView)
                }

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    mOnDrawerListener?.onDrawerSlide(drawerView, slideOffset)

                    if (!mAnimateActionBarDrawerToggle) {
                        super.onDrawerSlide(drawerView, 0f)
                    } else {
                        super.onDrawerSlide(drawerView, slideOffset)
                    }
                }
            }
            this.mActionBarDrawerToggle?.syncState()
        }

        //if we got a toolbar set a toolbarNavigationListener
        //we also have to do this after setting the ActionBarDrawerToggle as this will overwrite this
        this.mToolbar?.setNavigationOnClickListener(toolbarNavigationListener)

        //handle the ActionBarDrawerToggle
        mActionBarDrawerToggle?.let {
            it.toolbarNavigationClickListener = toolbarNavigationListener
            mDrawerLayout.addDrawerListener(it)
        } ?: run {
            mDrawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    mOnDrawerListener?.onDrawerSlide(drawerView, slideOffset)
                }

                override fun onDrawerOpened(drawerView: View) {
                    mOnDrawerListener?.onDrawerOpened(drawerView)
                }

                override fun onDrawerClosed(drawerView: View) {
                    mOnDrawerListener?.onDrawerClosed(drawerView)
                }

                override fun onDrawerStateChanged(newState: Int) {

                }
            })
        }
    }

    /**
     * build the drawers content only. This will still return a Result object, but only with the content set. No inflating of a DrawerLayout.
     *
     * @return Result object with only the content set
     */
    open fun buildView(): Drawer {
        val mActivity = this.mActivity
                ?: throw RuntimeException("please pass an activity first to use this call")

        // if the user has not set a drawerLayout use the default one :D
        if (!::mDrawerLayout.isInitialized) {
            withDrawerLayout(-1)
        }

        // get the slider view
        mSliderLayout = mActivity.layoutInflater.inflate(R.layout.material_drawer_slider, mDrawerLayout, false) as ScrimInsetsRelativeLayout
        mSliderLayout.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(mActivity, R.attr.material_drawer_background, R.color.material_drawer_background))
        // get the layout params
        var params: DrawerLayout.LayoutParams? = mSliderLayout.layoutParams as DrawerLayout.LayoutParams?
        if (params != null) {
            // if we've set a custom gravity set it
            params.gravity = mDrawerGravity
            // if this is a drawer from the right, change the margins :D
            params = DrawerUtils.processDrawerLayoutParams(this, params)
            // set the new layout params
            mSliderLayout.layoutParams = params
        }

        //create the content
        createContent()

        //create the result object
        val result = Drawer(this)
        //set the drawer for the accountHeader if set
        mAccountHeader?.setDrawer(result)

        //toggle selection list if we were previously on the account list
        if (mSavedInstance?.getBoolean(Drawer.BUNDLE_DRAWER_CONTENT_SWITCHED, false) == true) {
            mAccountHeader?.toggleSelectionList(mActivity)
        }

        //handle if the drawer should be shown on launch
        handleShowOnLaunch()

        //we only want to hook a Drawer to the MiniDrawer if it is the main drawer, not the appended one
        if (!mAppended && mGenerateMiniDrawer) {
            // if we should create a MiniDrawer we have to do this now
            mMiniDrawer = MiniDrawer().withDrawer(result).withAccountHeader(mAccountHeader)
        }

        //forget the reference to the activity
        this.mActivity = null

        return result
    }

    /**
     * Call this method to append a new DrawerBuilder to a existing Drawer.
     *
     * @param result the Drawer.Result of an existing Drawer
     * @return
     */
    open fun append(result: Drawer): Drawer {
        if (mUsed) {
            throw RuntimeException("you must not reuse a DrawerBuilder builder")
        }
        val mActivity = this.mActivity
                ?: throw RuntimeException("please pass an activity first to use this call")

        //set that this builder was used. now you have to create a new one
        mUsed = true
        mAppended = true

        //get the drawer layout from the previous drawer
        mDrawerLayout = result.drawerLayout

        // get the slider view
        mSliderLayout = mActivity.layoutInflater.inflate(R.layout.material_drawer_slider, mDrawerLayout, false) as ScrimInsetsRelativeLayout
        mSliderLayout.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(mActivity, R.attr.material_drawer_background, R.color.material_drawer_background))
        // get the layout params
        (mSliderLayout.layoutParams as DrawerLayout.LayoutParams).also {
            // set the gravity of this drawerGravity
            it.gravity = mDrawerGravity
            // if this is a drawer from the right, change the margins :D &  set the new params
            mSliderLayout.layoutParams = DrawerUtils.processDrawerLayoutParams(this, it)
        }
        //define id for the sliderLayout
        mSliderLayout.id = R.id.material_drawer_slider_layout
        // add the slider to the drawer
        mDrawerLayout.addView(mSliderLayout, 1)

        //create the content
        createContent()

        //create the result object
        val appendedResult = Drawer(this)

        //toggle selection list if we were previously on the account list
        if (mSavedInstance?.getBoolean(Drawer.BUNDLE_DRAWER_CONTENT_SWITCHED_APPENDED, false) == true) {
            mAccountHeader?.toggleSelectionList(mActivity)
        }

        //forget the reference to the activity
        this.mActivity = null

        return appendedResult
    }

    /**
     * the helper method to create the content for the drawer
     */
    private fun createContent() {
        val mActivity = this.mActivity
                ?: throw RuntimeException("please pass an activity first to use this call")

        //if we have a customView use this
        if (mCustomView != null) {
            val contentParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            )
            contentParams.weight = 1f
            mSliderLayout.addView(mCustomView, contentParams)
            return
        }

        //set the shadow for the drawer
        if (Build.VERSION.SDK_INT < 21) {
            if (ViewCompat.getLayoutDirection(mRootView) == ViewCompat.LAYOUT_DIRECTION_LTR) {
                mDrawerLayout.setDrawerShadow(if (mDrawerGravity == GravityCompat.START) R.drawable.material_drawer_shadow_right else R.drawable.material_drawer_shadow_left, mDrawerGravity)
            } else {
                mDrawerLayout.setDrawerShadow(if (mDrawerGravity == GravityCompat.START) R.drawable.material_drawer_shadow_left else R.drawable.material_drawer_shadow_right, mDrawerGravity)
            }
        }

        // if we have an adapter (either by defining a custom one or the included one add a list :D
        val contentView: View
        if (!::mRecyclerView.isInitialized) {
            contentView = LayoutInflater.from(mActivity).inflate(R.layout.material_drawer_recycler_view, mSliderLayout, false)
            mRecyclerView = contentView.findViewById<RecyclerView>(R.id.material_drawer_recycler_view)
            //set the itemAnimator
            mRecyclerView.itemAnimator = mItemAnimator
            //some style improvements on older devices
            mRecyclerView.setFadingEdgeLength(0)

            //set the drawing cache background to the same color as the slider to improve performance
            //mRecyclerView.setDrawingCacheBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(mActivity, R.attr.material_drawer_background, R.color.material_drawer_background));
            mRecyclerView.clipToPadding = false
            //additional stuff
            mRecyclerView.layoutManager = mLayoutManager

            var paddingTop = 0
            if ((mDisplayBelowStatusBar == null || mDisplayBelowStatusBar == true) && !mSystemUIHidden) {
                paddingTop = UIUtils.getStatusBarHeight(mActivity)
            }
            var paddingBottom = 0
            val orientation = mActivity.resources.configuration.orientation
            if ((mTranslucentNavigationBar || mFullscreen) && Build.VERSION.SDK_INT >= 21 && !mSystemUIHidden
                    && (orientation == Configuration.ORIENTATION_PORTRAIT || orientation == Configuration.ORIENTATION_LANDSCAPE && DrawerUIUtils.isSystemBarOnBottom(mActivity))) {
                paddingBottom = UIUtils.getNavigationBarHeight(mActivity)
            }

            mRecyclerView.setPadding(0, paddingTop, 0, paddingBottom)
        } else {
            contentView = mRecyclerView
        }

        val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )
        params.weight = 1f
        mSliderLayout.addView(contentView, params)

        if (mInnerShadow) {
            val innerShadow = mSliderLayout.findViewById<View>(R.id.material_drawer_inner_shadow)
            innerShadow.visibility = View.VISIBLE
            innerShadow.bringToFront()
            if (mDrawerGravity == GravityCompat.START) {
                innerShadow.setBackgroundResource(R.drawable.material_drawer_shadow_left)
            } else {
                innerShadow.setBackgroundResource(R.drawable.material_drawer_shadow_right)
            }
        }

        // set the background
        when {
            mSliderBackgroundColor != 0 -> mSliderLayout.setBackgroundColor(mSliderBackgroundColor)
            mSliderBackgroundColorRes != -1 -> mSliderLayout.setBackgroundColor(ContextCompat.getColor(mActivity, mSliderBackgroundColorRes))
            mSliderBackgroundDrawable != null -> UIUtils.setBackground(mSliderLayout, mSliderBackgroundDrawable)
            mSliderBackgroundDrawableRes != -1 -> UIUtils.setBackground(mSliderLayout, mSliderBackgroundDrawableRes)
        }

        //handle the header
        DrawerUtils.handleHeaderView(this)

        //handle the footer
        DrawerUtils.handleFooterView(this, View.OnClickListener { v ->
            val drawerItem = v.getTag(R.id.material_drawer_item) as IDrawerItem<*>
            DrawerUtils.onFooterDrawerItemClick(this@DrawerBuilder, drawerItem, v, true)
        })

        //if MultiSelect is possible
        mSelectExtension.multiSelect = mMultiSelect
        if (mMultiSelect) {
            mSelectExtension.selectOnLongClick = false
            mSelectExtension.allowDeselection = true
        }

        //set the adapter on the listView
        if (adapterWrapper == null) {
            mRecyclerView.adapter = adapter
        } else {
            mRecyclerView.adapter = adapterWrapper
        }

        //predefine selection (should be the first element
        if (mSelectedItemPosition == 0 && mSelectedItemIdentifier != 0L) {
            mSelectedItemPosition = DrawerUtils.getPositionByIdentifier(this, mSelectedItemIdentifier)
        }
        if (mHeaderView != null && mSelectedItemPosition == 0) {
            mSelectedItemPosition = 1
        }
        mSelectExtension.deselect()
        mSelectExtension.select(mSelectedItemPosition)

        // add the onDrawerItemClickListener if set

        adapter.onClickListener = { v: View?, _: IAdapter<IDrawerItem<*>>, item: IDrawerItem<*>, position: Int ->
            if (!(item is Selectable<*> && !item.isSelectable)) {
                resetStickyFooterSelection()
                mCurrentStickyFooterSelection = -1
            }

            //call the listener
            var consumed = false

            //call the item specific listener
            if (item is AbstractDrawerItem<*, *>) {
                consumed = item.onDrawerItemClickListener?.onItemClick(v, position, item)
                        ?: false
            }

            //call the drawer listener
            mOnDrawerItemClickListener?.let { mOnDrawerItemClickListener ->
                if (mDelayDrawerClickEvent > 0) {
                    Handler().postDelayed({ mOnDrawerItemClickListener.onItemClick(v, position, item) }, mDelayDrawerClickEvent.toLong())
                } else {
                    consumed = mOnDrawerItemClickListener.onItemClick(v, position, item)
                }
            }

            //we have to notify the miniDrawer if existing, and if the event was not consumed yet
            if (!consumed) {
                consumed = mMiniDrawer?.onItemClick(item) ?: false
            }

            //if we were a expandable item we consume the event closing makes no sense
            if (item.subItems.isNotEmpty()) {
                //we consume the event and want no further handling
                true
            } else {
                if (!consumed) {
                    //close the drawer after click
                    closeDrawerDelayed()
                }
                consumed
            }
        }
        // add the onDrawerItemLongClickListener if set
        adapter.onLongClickListener = { v: View, _: IAdapter<IDrawerItem<*>>, item: IDrawerItem<*>, position: Int ->
            mOnDrawerItemLongClickListener?.onItemLongClick(v, position, item) ?: false
        }

        mRecyclerView.scrollToPosition(0)

        // try to restore all saved values again
        mSavedInstance?.let { mSavedInstance ->
            if (!mAppended) {
                mSelectExtension.deselect()
                adapter.withSavedInstanceState(mSavedInstance, Drawer.BUNDLE_SELECTION)
                DrawerUtils.setStickyFooterSelection(this, mSavedInstance.getInt(Drawer.BUNDLE_STICKY_FOOTER_SELECTION, -1), null)
            } else {
                mSelectExtension.deselect()
                adapter.withSavedInstanceState(mSavedInstance, Drawer.BUNDLE_SELECTION_APPENDED)
                DrawerUtils.setStickyFooterSelection(this, mSavedInstance.getInt(Drawer.BUNDLE_STICKY_FOOTER_SELECTION_APPENDED, -1), null)
            }
        }

        // call initial onClick event to allow the dev to init the first view
        if (mFireInitialOnClick && mOnDrawerItemClickListener != null) {
            val selection = if (mSelectExtension.selections.isEmpty()) -1 else mSelectExtension.selections.iterator().next()
            getDrawerItem(selection)?.let {
                mOnDrawerItemClickListener?.onItemClick(null, selection, it)
            }

        }
    }

    /**
     * helper method to close the drawer delayed
     */
    internal fun closeDrawerDelayed() {
        if (mCloseOnClick) {
            if (mDelayOnDrawerClose > -1) {
                Handler().postDelayed({
                    mDrawerLayout.closeDrawers()

                    if (mScrollToTopAfterClick) {
                        mRecyclerView.smoothScrollToPosition(0)
                    }
                }, mDelayOnDrawerClose.toLong())
            } else {
                mDrawerLayout.closeDrawers()
            }
        }
    }

    /**
     * get the drawerItem at a specific position
     *
     * @param position
     * @return
     */
    internal fun getDrawerItem(position: Int): IDrawerItem<*>? {
        return adapter.getItem(position)
    }

    /**
     * check if the item is within the bounds of the list
     *
     * @param position
     * @param includeOffset
     * @return
     */
    internal fun checkDrawerItem(position: Int, includeOffset: Boolean): Boolean {
        return adapter.getItem(position) != null
    }

    /**
     * simple helper method to reset the selection of the sticky footer
     */
    internal fun resetStickyFooterSelection() {
        mStickyFooterView?.let {
            if (it is LinearLayout) {
                for (i in 0 until it.childCount) {
                    it.getChildAt(i).isActivated = false
                    it.getChildAt(i).isSelected = false
                }
            }
        }
    }
}