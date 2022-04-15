package com.mikepenz.materialdrawer.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.adapters.ModelAdapter
import com.mikepenz.fastadapter.expandable.ExpandableExtension
import com.mikepenz.fastadapter.expandable.ExpandableExtensionFactory
import com.mikepenz.fastadapter.extensions.ExtensionsFactories
import com.mikepenz.fastadapter.select.SelectExtension
import com.mikepenz.fastadapter.select.SelectExtensionFactory
import com.mikepenz.fastadapter.select.getSelectExtension
import com.mikepenz.fastadapter.utils.DefaultIdDistributor
import com.mikepenz.fastadapter.utils.DefaultIdDistributorImpl
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.DimenHolder
import com.mikepenz.materialdrawer.model.AbstractDrawerItem
import com.mikepenz.materialdrawer.model.ContainerDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.*

/**
 * This view is a simple drop in view for the [DrawerLayout] offering a convenient API to provide a nice and flexible slider view following
 * the material design guidelines v2.
 */
open class MaterialDrawerSliderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.materialDrawerStyle) : RelativeLayout(context, attrs, defStyleAttr) {

    /** Temporarily disable invalidation for optimizations */
    private var invalidationEnabled: Boolean = true
    private var invalidateContent: Boolean = false
    private var invalidateHeader: Boolean = false
    private var invalidateFooter: Boolean = false
    private var invalidateStickyFooter: Boolean = false

    /** Specify the foreground color for the insets */
    var insetForeground: Drawable? = null
        set(value) {
            field = value
            invalidateThis()
        }

    private var insets: Rect? = null
    private val tempRect = Rect()

    /** Fires when the insets get set */
    var onInsetsCallback: ((WindowInsetsCompat) -> Unit)? = null

    /** Specify if the statusbar should be tinted. */
    var tintStatusBar = false
        set(value) {
            field = value
            invalidateThis()
        }

    /** Specify if the navigationbar should be tinted. */
    var tintNavigationBar = true
        set(value) {
            field = value
            invalidateThis()
        }

    /** Specify if the systemUI should be visible. */
    var systemUIVisible = true
        set(value) {
            field = value
            invalidateThis()
        }

    /** Defines the current sticky footer selection */
    internal var currentStickyFooterSelection = -1

    /** Allow to specify a custom string key postfix for the savedInstanceKey (for multiple sliders). */
    var savedInstanceKey: String = ""

    /** Specify if the layoutManager for the recyclerView. */
    var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        set(value) {
            field = value
            createContent()
        }

    /** Specify if the [com.mikepenz.fastadapter.IIdDistributor] for the [FastAdapter] */
    val idDistributor: DefaultIdDistributor<IDrawerItem<*>> = DefaultIdDistributorImpl()

    /** Defines if we want an inner shadow (used in with the MiniDrawer). */
    var innerShadow = false
        set(value) {
            field = value
            createContent()
        }

    /** Defines the [AccountHeaderView] bound to this slider */
    var accountHeader: AccountHeaderView? = null
        set(value) {
            field = value
            if (field?.sliderView != this) {
                field?.attachToSliderView(this)
            }
        }

    /** Defines if the account header shall be displayed as a sticky header. */
    var accountHeaderSticky = false
        set(value) {
            field = value
            handleHeaderView()
        }

    /** Defines the [MiniDrawerSliderView] bound to this [MaterialDrawerSliderView] */
    var miniDrawer: MiniDrawerSliderView? = null
        set(value) {
            field = value
            if (field?.drawer != this) {
                field?.drawer = this
            }
        }

    /** Defines if the drawer should scroll to top after click. */
    var scrollToTopAfterClick = false

    /** Defines the header view to display in this [MaterialDrawerSliderView]. Note this is not possible when a [AccountHeaderView] is used. */
    var headerView: View? = null
        set(value) {
            field = value
            headerAdapter.clear()
            if (value != null) {
                if (headerPadding) {
                    headerAdapter.add(ContainerDrawerItem().withView(value).withDivider(headerDivider).withHeight(headerHeight).withViewPosition(ContainerDrawerItem.Position.TOP))
                } else {
                    headerAdapter.add(ContainerDrawerItem().withView(value).withDivider(headerDivider).withHeight(headerHeight).withViewPosition(ContainerDrawerItem.Position.NONE))
                }
                //we need to set the padding so the header starts on top
                recyclerView.setPadding(recyclerView.paddingLeft, 0, recyclerView.paddingRight, recyclerView.paddingBottom)
            }
        }
    internal var _headerDivider = true

    /** Defines if there should be a divider below the header. */
    var headerDivider: Boolean
        get() = _headerDivider
        set(value) {
            _headerDivider = value
            headerView = headerView // udpate the header view
        }
    internal var _headerPadding = true

    /** Defines the apdding for the header divider. */
    var headerPadding: Boolean
        get() = _headerPadding
        set(value) {
            _headerPadding = value
            headerView = headerView // udpate the header view
        }

    /** Defines the height of the header. */
    var headerHeight: DimenHolder? = null
        set(value) {
            field = value
            handleHeaderView()
        }

    /** Defines the [View] to be displayed as sticky header. Note this is not possible if a sticky [AccountHeaderView] is uesed. */
    var stickyHeaderView: View? = null
        set(value) {
            field = value
            handleHeaderView()
        }

    /** Defines if a shadow shown on the top of the sticky header. */
    var stickyHeaderShadow = true
        set(value) {
            field = value
            handleHeaderView()
        }

    /** Defines the footer we want to display with the [MaterialDrawerSliderView]. */
    var footerView: View? = null
        set(value) {
            field = value
            // set the footer (do this before the setAdapter because some devices will crash else
            if (value != null) {
                if (footerDivider) {
                    footerAdapter.add(ContainerDrawerItem().apply { view = value; viewPosition = ContainerDrawerItem.Position.BOTTOM })
                } else {
                    footerAdapter.add(ContainerDrawerItem().apply { view = value; viewPosition = ContainerDrawerItem.Position.NONE })
                }
            }
        }

    /** Defines if the footer should show with a divider. */
    var footerDivider = true
        set(value) {
            field = value
            footerView = footerView // udpate the footer view
        }
    private val footerClickListener = OnClickListener { v ->
        val drawerItem = v.getTag(R.id.material_drawer_item) as IDrawerItem<*>
        onFooterDrawerItemClick(this, drawerItem, v, true)
    }

    internal var _stickyFooterView: ViewGroup? = null

    /** Defines the [ViewGroup] to display as sticky footer. */
    val stickyFooterView: ViewGroup?
        get() = _stickyFooterView

    /** Defines if the sticky footer should be displayed with a divider. */
    var stickyFooterDivider = false
        set(value) {
            field = value
            handleStickyFooterView()
        }

    /** Defines the shadow [View] to provide for the sticky footer. */
    var stickyFooterShadowView: View? = null
        set(value) {
            field = value
            handleStickyFooterView()
        }

    /** Defines if the sticky footer should display a shadow above. */
    var stickyFooterShadow = true
        set(value) {
            field = value
            handleFooterView()
        }

    /** Defines if multi select is enabled in this drawer. */
    var multiSelect
        set(value) {
            this.selectExtension.multiSelect = value
            this.selectExtension.allowDeselection = value
        }
        get() = this.selectExtension.multiSelect

    // item to select
    private var _selectedItemPosition: Int = 0

    /** Defines the currently selected item position. */
    var selectedItemPosition: Int
        get() = _selectedItemPosition
        set(value) {
            _selectedItemPosition = if (value == 0 && headerView != null) 1 else value
            this.selectExtension.deselect()
            this.selectExtension.select(_selectedItemPosition)
        }

    /** Defines the currently selected item identifier. Note this will not be updated if there are new items selected. */
    var selectedItemIdentifier: Long = 0
        set(value) {
            field = value
            selectedItemPosition = this.getPosition(selectedItemIdentifier)
        }

    // the _drawerLayout owning this slider
    internal var _drawerLayout: DrawerLayout? = null

    /** Defines the [DrawerLayout] bound to this [MaterialDrawerSliderView]. */
    val drawerLayout: DrawerLayout?
        get() = _drawerLayout

    /** Defines if we want to use a custom width with this [MaterialDrawerSliderView]. */
    var customWidth: Int? = null
        set(value) {
            field = value
            onAttachedToWindow()
        }

    /** Defines the [RecyclerView] used in this [MaterialDrawerSliderView]. */
    lateinit var recyclerView: RecyclerView

    /** Defines if the adapter should enable hasStableIds to improve performance and allow animations. */
    var hasStableIds = true
        set(value) {
            field = value
            recyclerView.adapter = null // disconnect the RV
            adapter.setHasStableIds(hasStableIds)
            attachAdapter() // reattach the RV
        }

    // an adapter to use for the list
    internal lateinit var _adapter: FastAdapter<IDrawerItem<*>>

    /** Defines the adapter for the header items */
    var headerAdapter: ModelAdapter<IDrawerItem<*>, IDrawerItem<*>> = ItemAdapter()
        internal set

    /** Defines the adapter for the normal items */
    var itemAdapter: ModelAdapter<IDrawerItem<*>, IDrawerItem<*>> = ItemAdapter()
        internal set

    /** Defines the adapter for the account items (if we switch the set of elements) */
    var secondaryItemAdapter: ModelAdapter<IDrawerItem<*>, IDrawerItem<*>> = ItemAdapter()
        internal set

    /** Defines the adapter for the footer items */
    var footerAdapter: ModelAdapter<IDrawerItem<*>, IDrawerItem<*>> = ItemAdapter()
        internal set
    lateinit var expandableExtension: ExpandableExtension<IDrawerItem<*>>
    lateinit var selectExtension: SelectExtension<IDrawerItem<*>>

    /**
     * Defines the [FastAdapter] used to display elements in the [MaterialDrawerSliderView]
     */
    var adapter: FastAdapter<IDrawerItem<*>>
        get() {
            if (!::_adapter.isInitialized) {
                secondaryItemAdapter.active = false
                _adapter = FastAdapter.with(listOf(headerAdapter, itemAdapter, secondaryItemAdapter, footerAdapter))
                _adapter.setHasStableIds(hasStableIds)
                initAdapter()
                this.selectExtension.isSelectable = true
                this.selectExtension.multiSelect = false
                this.selectExtension.allowDeselection = false
            }
            return _adapter
        }
        set(value) {
            secondaryItemAdapter.active = false
            _adapter = value
            this.selectExtension = _adapter.getOrCreateExtension(SelectExtension::class.java)!! // is definitely not null
            //we have to rewrap as a different FastAdapter was provided
            _adapter.addAdapter(0, headerAdapter)
            _adapter.addAdapter(1, itemAdapter)
            _adapter.addAdapter(2, secondaryItemAdapter)
            _adapter.addAdapter(3, footerAdapter)
            initAdapter()
        }

    /** Defines an Adapter which wraps the main Adapter used in the RecyclerView to allow extended navigation and other stuff */
    var adapterWrapper: RecyclerView.Adapter<*>? = null
        set(value) {
            if (!::_adapter.isInitialized) {
                throw RuntimeException("this adapter has to be set in conjunction to a normal adapter which is used inside this wrapper adapter")
            }
            field = value
            createContent()
        }

    /** defines the itemAnimator to be used in conjunction with the RecyclerView */
    var itemAnimator: RecyclerView.ItemAnimator = DefaultItemAnimator()
        set(value) {
            field = value
            createContent()
        }

    /** Defines if the drawer should be closed on a click */
    var closeOnClick = true

    /** Defines the delay before the drawer gets closed after a click. This is meant to prevent lag */
    var delayOnDrawerClose = 50

    /** delay drawer click event to prevent lag (you should either choose DelayOnDrawerClose or this) */
    var delayDrawerClickEvent = 0

    /** defines if we want to keep the sticky items visible, upon switching to the profiles */
    var keepStickyItemsVisible = false

    /** Defines the set of stickyDrawerItems to show */
    var stickyDrawerItems: MutableList<IDrawerItem<*>> = ArrayList()

    /** Defines the click listener to listen for clicks on drawer items */
    var onDrawerItemClickListener: ((v: View?, item: IDrawerItem<*>, position: Int) -> Boolean)? = null

    /** Defines the long click listener to listen for long clicks on drawer items */
    var onDrawerItemLongClickListener: ((v: View?, item: IDrawerItem<*>, position: Int) -> Boolean)? = null

    //variables to store and remember the original list of the drawer
    private var originalOnDrawerItemClickListener: ((v: View?, item: IDrawerItem<*>, position: Int) -> Boolean)? = null
    private var originalOnDrawerItemLongClickListener: ((v: View?, item: IDrawerItem<*>, position: Int) -> Boolean)? = null
    private var originalDrawerState: Bundle? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MaterialDrawerSliderView, defStyleAttr, R.style.Widget_MaterialDrawerStyle)
        insetForeground = a.getDrawable(R.styleable.MaterialDrawerSliderView_materialDrawerInsetForeground)
        background = a.getDrawable(R.styleable.MaterialDrawerSliderView_materialDrawerBackground)
        a.recycle()
        setWillNotDraw(true) // No need to draw until the insets are adjusted

        adapter // call getter to setup
        createContent()

        ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
            if (null == this.insets) {
                this.insets = Rect()
            }

            this.insets?.set(insets.systemWindowInsetLeft, insets.systemWindowInsetTop, insets.systemWindowInsetRight, insets.systemWindowInsetBottom)

            if (headerView == null && accountHeader == null) {
                if (stickyHeaderView == null) {
                    recyclerView.updatePadding(top = insets.systemWindowInsetTop + context.resources.getDimensionPixelSize(R.dimen.material_drawer_padding_top_bottom))
                }
                if (stickyFooterView == null) {
                    recyclerView.updatePadding(bottom = insets.systemWindowInsetBottom + context.resources.getDimensionPixelSize(R.dimen.material_drawer_padding_top_bottom))
                }
            }

            setWillNotDraw(insetForeground == null)
            ViewCompat.postInvalidateOnAnimation(this@MaterialDrawerSliderView)
            onInsetsCallback?.invoke(insets)
            insets
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        val width = width
        val height = height
        val insets = insets
        val insetForeground = insetForeground
        if (insets != null && insetForeground != null) {
            val sc = canvas.save()
            canvas.translate(scrollX.toFloat(), scrollY.toFloat())

            if (!systemUIVisible) {
                insets.top = 0
                insets.right = 0
                insets.bottom = 0
                insets.left = 0
            }

            // Top
            if (tintStatusBar) {
                tempRect.set(0, 0, width, insets.top)
                insetForeground.bounds = tempRect
                insetForeground.draw(canvas)
            }

            // Bottom
            if (tintNavigationBar) {
                tempRect.set(0, height - insets.bottom, width, height)
                insetForeground.bounds = tempRect
                insetForeground.draw(canvas)
            }
            // Left
            if (tintNavigationBar) {
                tempRect.set(0, insets.top, insets.left, height - insets.bottom)
                insetForeground.bounds = tempRect
                insetForeground.draw(canvas)
            }

            // Right
            if (tintNavigationBar) {
                tempRect.set(width - insets.right, insets.top, width, height - insets.bottom)
                insetForeground.bounds = tempRect
                insetForeground.draw(canvas)
            }

            canvas.restoreToCount(sc)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        insetForeground?.callback = this

        if (parent != null) {
            _drawerLayout = parent as? DrawerLayout ?: parent.parent as? DrawerLayout
                    ?: parent.parent.parent as? DrawerLayout // give it 3 parents chance to find the parent
            layoutParams?.also {
                // if this is a drawer from the right, change the margins :D &  set the new params
                it.width = customWidth ?: getOptimalDrawerWidth(context)
                layoutParams = it
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        insetForeground?.callback = null
    }


    /**
     * Set the Bundle (savedInstance) which is passed by the activity.
     * No need to null-check everything is handled automatically
     */
    @Deprecated("Replaced with setSavedInstance", ReplaceWith("setSavedInstance(savedInstance)"))
    fun withSavedInstance(savedInstance: Bundle?) {
        setSavedInstance(savedInstance)
    }

    /**
     * Set the Bundle (savedInstance) which is passed by the activity.
     * No need to null-check everything is handled automatically
     */
    fun setSavedInstance(savedInstance: Bundle?) {
        savedInstance ?: return
        // try to restore all saved values again
        this.selectExtension.deselect()
        adapter.withSavedInstanceState(savedInstance, BUNDLE_SELECTION + savedInstanceKey)
        this.setStickyFooterSelection(savedInstance.getInt(BUNDLE_STICKY_FOOTER_SELECTION + savedInstanceKey, -1), null)

        //toggle selection list if we were previously on the account list
        if (savedInstance.getBoolean(BUNDLE_DRAWER_CONTENT_SWITCHED + savedInstanceKey, false)) {
            accountHeader?.toggleSelectionList()
        }
    }

    private fun initAdapter() {
        ExtensionsFactories.register(SelectExtensionFactory())
        ExtensionsFactories.register(ExpandableExtensionFactory())

        this.selectExtension = adapter.getOrCreateExtension(SelectExtension::class.java)!! // is definitely not null
        headerAdapter.idDistributor = idDistributor
        itemAdapter.idDistributor = idDistributor
        footerAdapter.idDistributor = idDistributor
        expandableExtension = adapter.getOrCreateExtension(ExpandableExtension::class.java)!! // is definitely not null
    }

    /**
     * the helper method to create the content for the drawer
     */
    private fun createContent() {
        if (!invalidationEnabled) {
            invalidateContent = true
            return
        }
        invalidateContent = false

        // if we have an adapter (either by defining a custom one or the included one add a list :D
        val contentView: View
        if (!::recyclerView.isInitialized) {
            contentView = LayoutInflater.from(context).inflate(R.layout.material_drawer_recycler_view, this, false)
            recyclerView = contentView.findViewById(R.id.material_drawer_recycler_view)
            //some style improvements on older devices
            recyclerView.setFadingEdgeLength(0)
            recyclerView.clipToPadding = false
        } else {
            contentView = recyclerView
        }

        //set the itemAnimator
        recyclerView.itemAnimator = itemAnimator
        //additional stuff
        recyclerView.layoutManager = layoutManager

        val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )
        params.weight = 1f

        this.removeView(contentView) // ensure the view is not already part
        this.addView(contentView, params)

        if (innerShadow) {
            var innerShadow = this.findViewById<View?>(R.id.material_drawer_inner_shadow)
            if (innerShadow == null) {
                innerShadow = LayoutInflater.from(context).inflate(R.layout.material_drawer_inner_shadow, this, false)!!
                this.addView(innerShadow)
            }

            innerShadow.visibility = View.VISIBLE
            innerShadow.bringToFront()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && gravity == GravityCompat.END) {
                innerShadow.setBackgroundResource(R.drawable.material_drawer_shadow_right)
            } else {
                innerShadow.setBackgroundResource(R.drawable.material_drawer_shadow_left)
            }
        } else {
            removeView(this.findViewById(R.id.material_drawer_inner_shadow))
        }

        //handle the header
        handleHeaderView()

        //handle the footer
        handleFooterView()

        //set the adapter on the listView
        attachAdapter()

        //predefine selection (should be the first element)
        selectedItemPosition = _selectedItemPosition

        // add the onDrawerItemClickListener if set
        adapter.onClickListener = { v: View?, _: IAdapter<IDrawerItem<*>>, item: IDrawerItem<*>, position: Int ->
            if (item.isSelectable) {
                resetStickyFooterSelection()
                currentStickyFooterSelection = -1
            }

            //call the listener
            var consumed = false

            //call the item specific listener
            if (item is AbstractDrawerItem<*, *>) {
                consumed = item.onDrawerItemClickListener?.invoke(v, item, position) ?: false
            }

            //we have to notify the miniDrawer if existing, and if the event was not consumed yet
            if (!consumed) {
                consumed = miniDrawer?.onItemClick(item) ?: false
            }

            //call the drawer listener
            onDrawerItemClickListener?.let { mOnDrawerItemClickListener ->
                if (delayDrawerClickEvent > 0) {
                    Handler().postDelayed({ mOnDrawerItemClickListener.invoke(v, item, position) }, delayDrawerClickEvent.toLong())
                } else {
                    consumed = mOnDrawerItemClickListener.invoke(v, item, position)
                }
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
            onDrawerItemLongClickListener?.invoke(v, item, position) ?: false
        }

        recyclerView.scrollToPosition(0)
    }

    /**
     * Attaches the adapter to the recyclerView
     */
    private fun attachAdapter() {
        //set the adapter on the listView
        if (adapterWrapper == null) {
            recyclerView.adapter = adapter
        } else {
            recyclerView.adapter = adapterWrapper
        }
    }

    /**
     * simple helper method to reset the selection of the sticky footer
     */
    internal fun resetStickyFooterSelection() {
        val stickyFooterView = stickyFooterView ?: return
        if (stickyFooterView is LinearLayout) {
            for (i in 0 until stickyFooterView.childCount) {
                stickyFooterView.getChildAt(i).isActivated = false
                stickyFooterView.getChildAt(i).isSelected = false
            }
        }
    }

    /**
     * helper method to close the drawer delayed
     */
    internal fun closeDrawerDelayed() {
        if (closeOnClick && _drawerLayout != null) {
            if (delayOnDrawerClose > -1) {
                Handler().postDelayed({
                    _drawerLayout?.closeDrawers()

                    if (scrollToTopAfterClick) {
                        recyclerView.smoothScrollToPosition(0)
                    }
                }, delayOnDrawerClose.toLong())
            } else {
                _drawerLayout?.closeDrawers()
            }
        }
    }

    /**
     * information if the current drawer content is switched by alternative content (profileItems)
     */
    fun switchedDrawerContent(): Boolean {
        return !(originalOnDrawerItemClickListener == null && originalDrawerState == null)
    }

    /**
     * method to switch the drawer content to new elements
     */
    fun switchDrawerContent(onDrawerItemClickListenerInner: ((v: View?, item: IDrawerItem<*>, position: Int) -> Boolean)?, onDrawerItemLongClickListenerInner: ((v: View?, item: IDrawerItem<*>, position: Int) -> Boolean)?, drawerItemsInner: List<IDrawerItem<*>>, drawerSelection: Int) {
        //just allow a single switched drawer
        if (!switchedDrawerContent()) {
            //save out previous values
            originalOnDrawerItemClickListener = onDrawerItemClickListener
            originalOnDrawerItemLongClickListener = onDrawerItemLongClickListener
            originalDrawerState = adapter.saveInstanceState(Bundle())
            expandableExtension.collapse(false)

            secondaryItemAdapter.active = true
            itemAdapter.active = false
        }

        //set the new items
        onDrawerItemClickListener = onDrawerItemClickListenerInner
        onDrawerItemLongClickListener = onDrawerItemLongClickListenerInner
        secondaryItemAdapter.set(drawerItemsInner)
        setSelectionAtPosition(drawerSelection, false)

        if (!keepStickyItemsVisible) {
            //hide stickyFooter and it's shadow
            stickyFooterView?.visibility = View.GONE
            stickyFooterShadowView?.visibility = View.GONE
        }
    }

    /**
     * helper method to reset to the original drawerContent
     */
    fun resetDrawerContent() {
        if (switchedDrawerContent()) {
            //set the new items
            onDrawerItemClickListener = originalOnDrawerItemClickListener
            onDrawerItemLongClickListener = originalOnDrawerItemLongClickListener
            adapter.withSavedInstanceState(originalDrawerState)
            //remove the references
            originalOnDrawerItemClickListener = null
            originalOnDrawerItemLongClickListener = null
            originalDrawerState = null

            secondaryItemAdapter.active = false
            itemAdapter.active = true

            //if we switch back scroll back to the top
            recyclerView.smoothScrollToPosition(0)

            //show the stickyFooter and it's shadow again
            stickyFooterView?.visibility = View.VISIBLE
            stickyFooterShadowView?.visibility = View.VISIBLE

            //if we currently show the accountHeader selection list make sure to reset this attr
            accountHeader?._selectionListShown = false
        }
    }

    /**
     * set the current selection in the drawer
     * NOTE: this also deselects all other selections. if you do not want this. use the direct api of the adater .getAdapter().select(position, fireOnClick)
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     */
    @JvmOverloads
    fun setSelectionAtPosition(position: Int, fireOnClick: Boolean = true): Boolean {
        selectExtension.deselect()
        if (position >= 0) {
            selectExtension.select(position, false)
            notifySelect(position, fireOnClick)
        }
        return false
    }

    private fun notifySelect(position: Int, fireOnClick: Boolean) {
        _selectedItemPosition = position
        if (fireOnClick && position >= 0) {
            adapter.getItem(position)?.let { item ->
                if (item is AbstractDrawerItem<*, *>) {
                    item.onDrawerItemClickListener?.invoke(null, item, position)
                }
                onDrawerItemClickListener?.invoke(null, item, position)
            }
        }

        //we set the selection on a normal item in the drawer so we have to deselect the items in the StickyDrawer
        resetStickyFooterSelection()
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param identifier  the identifier to search for
     * @param fireOnClick true if the click listener should be called
     */
    @JvmOverloads
    fun setSelection(identifier: Long, fireOnClick: Boolean = true) {
        val select = adapter.getSelectExtension()
        select.selectByIdentifier(identifier, false, true)

        //we also have to call the general notify
        val res = adapter.getItemById(identifier)
        if (res != null) {
            val position = res.second
            notifySelect(position ?: -1, fireOnClick)
        }
    }

    /**
     * add the values to the bundle for saveInstanceState
     */
    fun saveInstanceState(_savedInstanceState: Bundle): Bundle {
        adapter.saveInstanceState(_savedInstanceState, BUNDLE_SELECTION + savedInstanceKey).apply {
            putInt(BUNDLE_STICKY_FOOTER_SELECTION + savedInstanceKey, currentStickyFooterSelection)
            putBoolean(BUNDLE_DRAWER_CONTENT_SWITCHED + savedInstanceKey, switchedDrawerContent())
        }
        return _savedInstanceState
    }


    /**
     * Invalidates the `IconicsDrawable` if invalidation is currently enabled
     */
    private fun invalidateThis() {
        if (invalidationEnabled) {
            invalidate()
        }
    }

    /**
     * Invalidates the header view in an optimized form
     */
    private fun handleHeaderView() {
        if (!invalidationEnabled) {
            invalidateHeader = true
            return
        }
        invalidateHeader = false
        handleHeaderView(this)
    }

    /**
     * Invalidates the footer view in an optimized form
     */
    private fun handleFooterView() {
        if (!invalidationEnabled) {
            invalidateFooter = true
            return
        }
        invalidateFooter = false
        handleFooterView(this, footerClickListener)
    }

    /**
     * Invalidates the sticky footer view in an optimized form
     */
    internal fun handleStickyFooterView() {
        if (!invalidationEnabled) {
            invalidateStickyFooter = true
            return
        }
        invalidateStickyFooter = false
        rebuildStickyFooterView(this)
    }

    /** Applies properties in an optimized form. Will disable invalidation of the MaterialDrawerSliderView for the inner property set operations */
    fun apply(block: MaterialDrawerSliderView.() -> Unit): MaterialDrawerSliderView {
        invalidationEnabled = false
        block()
        invalidationEnabled = true
        if (invalidateContent) {
            createContent()
        }
        if (invalidateHeader) {
            handleHeaderView()
        }
        if (invalidateFooter) {
            handleFooterView()
        }
        if (invalidateStickyFooter) {
            handleStickyFooterView()
        }
        invalidate()
        return this
    }

    companion object {
        /**
         * BUNDLE param to store the selection
         */
        const val BUNDLE_SELECTION = "_selection"
        const val BUNDLE_STICKY_FOOTER_SELECTION = "bundle_sticky_footer_selection"
        const val BUNDLE_DRAWER_CONTENT_SWITCHED = "bundle_drawer_content_switched"

        /**
         * Defines globally if we should animat selection state changes of the background
         */
        var DEFAULT_SELECTED_BACKGROUND_ANIMATED = true
    }
}
