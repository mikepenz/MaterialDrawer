package com.mikepenz.materialdrawer

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ModelAdapter
import com.mikepenz.fastadapter.expandable.ExpandableExtension
import com.mikepenz.fastadapter.select.SelectExtension
import com.mikepenz.fastadapter.select.getSelectExtension
import com.mikepenz.materialdrawer.holder.DimenHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.AbstractDrawerItem
import com.mikepenz.materialdrawer.model.ContainerDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Badgeable
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Iconable
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialize.view.IScrimInsetsLayout

/**
 * Created by mikepenz on 03.02.15.
 */
open class Drawer(internal val drawerBuilder: DrawerBuilder) {
    private var mContentView: FrameLayout? = null

    /**
     * Get the DrawerLayout of the current drawer
     *
     * @return
     */
    val drawerLayout: DrawerLayout
        get() = this.drawerBuilder.mDrawerLayout

    /**
     * Get the current state of the drawer.
     * True if the drawer is currently open.
     *
     * @return
     */
    val isDrawerOpen: Boolean
        get() = drawerBuilder.mDrawerLayout.isDrawerOpen(drawerBuilder.mDrawerGravity)


    /**
     * gets the already generated MiniDrawer or creates a new one
     *
     * @return
     */
    val miniDrawer: MiniDrawer?
        get() {
            if (drawerBuilder.mMiniDrawer == null) {
                drawerBuilder.mMiniDrawer = MiniDrawer().withDrawer(this).withAccountHeader(drawerBuilder.mAccountHeader)
            }
            return drawerBuilder.mMiniDrawer
        }

    /**
     * get the slider layout of the current drawer.
     * This is the layout containing the ListView
     *
     * @return
     */
    val slider: IScrimInsetsLayout
        get() = drawerBuilder.mSliderLayout

    /**
     * get the container frameLayout of the current drawer
     *
     * @return
     */
    val content: FrameLayout?
        get() {
            if (mContentView == null) {
                mContentView = this.drawerBuilder.mDrawerLayout.findViewById<FrameLayout>(R.id.content_layout)
            }
            return mContentView
        }

    /**
     * get the listView of the current drawer
     *
     * @return
     */
    val recyclerView: RecyclerView
        get() = drawerBuilder.mRecyclerView

    /**
     * get the FastAdapter of the current drawer
     *
     * @return
     */
    val adapter: FastAdapter<IDrawerItem<*>>
        get() = drawerBuilder.adapter
    /**
     *
     * @return
     */
    val selectExtension: SelectExtension<IDrawerItem<*>>
        get() = drawerBuilder.selectExtension

    /**
     * get the HeaderAdapter of the current drawer
     *
     * @return
     */
    val headerAdapter: ModelAdapter<IDrawerItem<*>, IDrawerItem<*>>
        get() = drawerBuilder.mHeaderAdapter

    /**
     * get the ItemAdapter of the current drawer
     *
     * @return
     */
    val itemAdapter: ModelAdapter<IDrawerItem<*>, IDrawerItem<*>>
        get() = drawerBuilder.mItemAdapter

    /**
     * get the FooterAdapter of the current drawer
     *
     * @return
     */
    val footerAdapter: ModelAdapter<IDrawerItem<*>, IDrawerItem<*>>
        get() = drawerBuilder.mFooterAdapter

    /**
     * get the ExpandableExtension of the current drawer
     *
     * @return
     */
    val expandableExtension: ExpandableExtension<IDrawerItem<*>>
        get() = drawerBuilder.mExpandableExtension

    /**
     * get all drawerItems of the current drawer
     *
     * @return
     */
    val drawerItems: List<IDrawerItem<*>>
        get() = drawerBuilder.itemAdapter.adapterItems

    /**
     * get the Header View if set else NULL
     *
     * @return
     */
    /**
     * method to replace a previous set header
     *
     * @param view
     */
    var header: View?
        get() = drawerBuilder.mHeaderView
        set(view) = setHeader(view, true, true)

    /**
     * get the StickyHeader View if set else NULL
     *
     * @return
     */
    val stickyHeader: View?
        get() = drawerBuilder.mStickyHeaderView

    /**
     * get the Footer View if set else NULL
     *
     * @return
     */
    val footer: View?
        get() = drawerBuilder.mFooterView

    /**
     * get the StickyFooter View if set else NULL
     *
     * @return
     */
    val stickyFooter: View?
        get() = drawerBuilder.mStickyFooterView

    /**
     * get the StickyFooter Shadow View if set else NULL
     *
     * @return
     */
    private val stickyFooterShadow: View?
        get() = drawerBuilder.mStickyFooterShadowView

    /**
     * get the ActionBarDrawerToggle
     *
     * @return
     */
    /**
     * Add a custom ActionBarDrawerToggle which will be used in combination with this drawer.
     *
     * @param actionBarDrawerToggle
     */
    var actionBarDrawerToggle: ActionBarDrawerToggle?
        get() = drawerBuilder.mActionBarDrawerToggle
        set(actionBarDrawerToggle) {
            this.drawerBuilder.mActionBarDrawerToggleEnabled = true
            this.drawerBuilder.mActionBarDrawerToggle = actionBarDrawerToggle
            this.drawerBuilder.handleDrawerNavigation(null, false)
        }

    /**
     * get the current position of the selected drawer element
     *
     * @return
     */
    val currentSelectedPosition: Int
        get() = if (drawerBuilder.selectExtension.selections.isEmpty()) -1 else drawerBuilder.selectExtension.selections.iterator().next()

    /**
     * get the current selected item identifier
     *
     * @return
     */
    val currentSelection: Long
        get() {
            val drawerItem = drawerBuilder.getDrawerItem(currentSelectedPosition)
            return drawerItem?.identifier ?: -1
        }

    /**
     * get the current position of the selected sticky footer element
     *
     * @return
     */
    val currentStickyFooterSelectedPosition: Int
        get() = drawerBuilder.mCurrentStickyFooterSelection

    /**
     * method to get the OnDrawerItemClickListener
     *
     * @return
     */
    /**
     * setter for the OnDrawerItemClickListener
     *
     * @param onDrawerItemClickListener
     */
    var onDrawerItemClickListener: OnDrawerItemClickListener?
        get() = drawerBuilder.mOnDrawerItemClickListener
        set(onDrawerItemClickListener) {
            drawerBuilder.mOnDrawerItemClickListener = onDrawerItemClickListener
        }

    /**
     * method to get the OnDrawerNavigationListener
     *
     * @return
     */
    //WBE
    //WBE
    var onDrawerNavigationListener: OnDrawerNavigationListener?
        get() = drawerBuilder.mOnDrawerNavigationListener
        set(onDrawerNavigationListener) {
            drawerBuilder.mOnDrawerNavigationListener = onDrawerNavigationListener
        }

    /**
     * method to get the OnDrawerItemLongClickListener
     *
     * @return
     */
    /**
     * setter for the OnDrawerItemLongClickListener
     *
     * @param onDrawerItemLongClickListener
     */
    var onDrawerItemLongClickListener: OnDrawerItemLongClickListener?
        get() = drawerBuilder.mOnDrawerItemLongClickListener
        set(onDrawerItemLongClickListener) {
            drawerBuilder.mOnDrawerItemLongClickListener = onDrawerItemLongClickListener
        }

    //variables to store and remember the original list of the drawer
    private var originalOnDrawerItemClickListener: Drawer.OnDrawerItemClickListener? = null
    private var originalOnDrawerItemLongClickListener: Drawer.OnDrawerItemLongClickListener? = null
    /**
     * get the original list of drawerItems
     *
     * @return
     */
    var originalDrawerItems: List<IDrawerItem<*>>? = null
        private set
    private var originalDrawerState: Bundle? = null

    /**
     * Sets the toolbar which should be used in combination with the drawer
     * This will handle the ActionBarDrawerToggle for you.
     * Do not set this if you are in a sub activity and want to handle the back arrow on your own
     *
     * @param activity
     * @param toolbar                       the toolbar which is used in combination with the drawer
     * @param recreateActionBarDrawerToggle defines if the ActionBarDrawerToggle needs to be recreated with the new set Toolbar
     */
    @JvmOverloads
    fun setToolbar(activity: Activity, toolbar: Toolbar, recreateActionBarDrawerToggle: Boolean = false) {
        this.drawerBuilder.mToolbar = toolbar
        this.drawerBuilder.handleDrawerNavigation(activity, recreateActionBarDrawerToggle)
    }

    /**
     * Open the drawer
     */
    fun openDrawer() {
        drawerBuilder.mDrawerLayout.openDrawer(drawerBuilder.mDrawerGravity)
    }

    /**
     * close the drawer
     */
    fun closeDrawer() {
        drawerBuilder.mDrawerLayout.closeDrawer(drawerBuilder.mDrawerGravity)
    }

    /**
     * method to replace a previous set header
     *
     * @param view
     * @param divider
     */
    fun setHeader(view: View, divider: Boolean) {
        setHeader(view, true, divider)
    }

    /**
     * method to replace a previous set header
     *
     * @param view
     * @param padding
     * @param divider
     * @param height
     */
    @JvmOverloads
    fun setHeader(view: View?, padding: Boolean, divider: Boolean, height: DimenHolder? = null) {
        drawerBuilder.headerAdapter.clear()
        view?.let {
            if (padding) {
                drawerBuilder.headerAdapter.add(ContainerDrawerItem().withView(view).withDivider(divider).withHeight(height).withViewPosition(ContainerDrawerItem.Position.TOP))
            } else {
                drawerBuilder.headerAdapter.add(ContainerDrawerItem().withView(view).withDivider(divider).withHeight(height).withViewPosition(ContainerDrawerItem.Position.NONE))
            }
        }
        //we need to set the padding so the header starts on top
        drawerBuilder.mRecyclerView.setPadding(drawerBuilder.mRecyclerView.paddingLeft, 0, drawerBuilder.mRecyclerView.paddingRight, drawerBuilder.mRecyclerView.paddingBottom)
    }

    /**
     * method to remove the header of the list
     */
    fun removeHeader() {
        drawerBuilder.headerAdapter.clear()
        //possibly there should be also a reset of the padding so the first item starts below the toolbar
    }

    /**
     * sets the gravity for this drawer.
     *
     * @param gravity the gravity which is defined for the drawer
     */
    fun setGravity(gravity: Int) {
        val params = (slider as View).layoutParams as DrawerLayout.LayoutParams
        params.gravity = gravity
        (slider as View).layoutParams = params
        drawerBuilder.mDrawerGravity = gravity
    }

    /**
     * calculates the position of an drawerItem. searching by it's identifier
     *
     * @param drawerItem
     * @return
     */
    fun getPosition(drawerItem: IDrawerItem<*>): Int {
        return getPosition(drawerItem.identifier)
    }

    /**
     * calculates the position of an drawerItem. searching by it's identifier
     *
     * @param identifier
     * @return
     */
    fun getPosition(identifier: Long): Int {
        return 0 //DrawerUtils.getPositionByIdentifier(drawerBuilder, identifier)
    }

    /**
     * returns the DrawerItem by the given identifier
     *
     * @param identifier
     * @return
     */
    fun getDrawerItem(identifier: Long): IDrawerItem<*>? {
        val res = adapter.getItemById(identifier)
        return res?.first
    }

    /**
     * returns the found drawerItem by the given tag
     *
     * @param tag
     * @return
     */
    fun getDrawerItem(tag: Any): IDrawerItem<*>? {
        return DrawerUtils.getDrawerItem(drawerItems, tag)
    }

    /**
     * calculates the position of an drawerItem. searching by it's identifier
     *
     * @param drawerItem
     * @return
     */
    fun getStickyFooterPosition(drawerItem: IDrawerItem<*>): Int {
        return getStickyFooterPosition(drawerItem.identifier)
    }

    /**
     * calculates the position of an drawerItem inside the footer. searching by it's identfier
     *
     * @param identifier
     * @return
     */
    fun getStickyFooterPosition(identifier: Long): Int {
        return DrawerUtils.getStickyFooterPositionByIdentifier(drawerBuilder, identifier)
    }

    /**
     * deselects all selected items
     */
    fun deselect() {
        selectExtension.deselect()
    }

    /**
     * deselects the item with the given identifier
     *
     * @param identifier the identifier to search for
     */
    fun deselect(identifier: Long) {
        selectExtension.deselect(getPosition(identifier))

        // we want to deselect all items, also the sticky drawer
        drawerBuilder.resetStickyFooterSelection()
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
        if (select != null) {
            select.deselect()
            select.selectByIdentifier(identifier, false, true)

            //we also have to call the general notify
            val res = adapter.getItemById(identifier)
            if (res != null) {
                val position = res.second
                notifySelect(position ?: -1, fireOnClick)
            }
        }
    }

    /**
     * set the current selection in the footer of the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param identifier  the identifier to search for
     * @param fireOnClick true if the click listener should be called
     */
    fun setStickyFooterSelection(identifier: Long, fireOnClick: Boolean) {
        setStickyFooterSelectionAtPosition(getStickyFooterPosition(identifier), fireOnClick)
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view!
     *
     * @param drawerItem the drawerItem to select (this requires a set identifier)
     */
    fun setSelection(drawerItem: IDrawerItem<*>) {
        setSelection(drawerItem.identifier, true)
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param drawerItem  the drawerItem to select (this requires a set identifier)
     * @param fireOnClick true if the click listener should be called
     */
    fun setSelection(drawerItem: IDrawerItem<*>, fireOnClick: Boolean) {
        setSelection(drawerItem.identifier, fireOnClick)
    }

    /*
     * set the current selection in the drawer
     * NOTE: this also deselects all other selections. if you do not want this. use the direct api of the adater .getAdapter().select(position, fireOnClick)
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param position
     * @param fireOnClick
     * @return true if the event was consumed
     */
    @JvmOverloads
    fun setSelectionAtPosition(position: Int, fireOnClick: Boolean = true): Boolean {
        drawerBuilder.selectExtension.deselect()
        drawerBuilder.selectExtension.select(position, false)
        notifySelect(position, fireOnClick)
        return false
    }

    private fun notifySelect(position: Int, fireOnClick: Boolean) {
        if (fireOnClick && position >= 0) {
            drawerBuilder.adapter.getItem(position)?.let { item ->
                if (item is AbstractDrawerItem<*, *>) {
                    item.onDrawerItemClickListener?.onItemClick(null, position, item)
                }
                drawerBuilder.mOnDrawerItemClickListener?.onItemClick(null, position, item)
            }
        }

        //we set the selection on a normal item in the drawer so we have to deselect the items in the StickyDrawer
        drawerBuilder.resetStickyFooterSelection()
    }

    /**
     * set the current selection in the footer of the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param position
     * @param fireOnClick
     */
    @JvmOverloads
    fun setStickyFooterSelectionAtPosition(position: Int, fireOnClick: Boolean = true) {
        //DrawerUtils.setStickyFooterSelection(drawerBuilder, position, fireOnClick)
    }

    /**
     * update a specific drawer item :D
     * automatically identified by its id
     *
     * @param drawerItem
     */
    fun updateItem(drawerItem: IDrawerItem<*>) {
        updateItemAtPosition(drawerItem, getPosition(drawerItem))
    }

    /**
     * update the badge for a specific drawerItem
     * identified by its id
     *
     * @param identifier
     * @param badge
     */
    fun updateBadge(identifier: Long, badge: StringHolder) {
        val drawerItem = getDrawerItem(identifier)
        if (drawerItem is Badgeable<*>) {
            drawerItem.withBadge(badge)
            updateItem(drawerItem)
        }
    }

    /**
     * update the name for a specific drawerItem
     * identified by its id
     *
     * @param identifier
     * @param name
     */
    fun updateName(identifier: Long, name: StringHolder) {
        val drawerItem = getDrawerItem(identifier)
        if (drawerItem is Nameable<*>) {
            drawerItem.withName(name)
            updateItem(drawerItem)
        }
    }

    /**
     * update the name for a specific drawerItem
     * identified by its id
     *
     * @param identifier
     * @param image
     */
    fun updateIcon(identifier: Long, image: ImageHolder) {
        val drawerItem = getDrawerItem(identifier)
        if (drawerItem is Iconable<*>) {
            drawerItem.withIcon(image)
            updateItem(drawerItem)
        }
    }

    /**
     * Update a drawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    fun updateItemAtPosition(drawerItem: IDrawerItem<*>, position: Int) {
        if (drawerBuilder.checkDrawerItem(position, false)) {
            drawerBuilder.itemAdapter[position] = drawerItem
        }
    }

    /**
     * Add a drawerItem at the end
     *
     * @param drawerItem
     */
    fun addItem(drawerItem: IDrawerItem<*>) {
        drawerBuilder.itemAdapter.add(drawerItem)
    }

    /**
     * Add a drawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    fun addItemAtPosition(drawerItem: IDrawerItem<*>, position: Int) {
        drawerBuilder.itemAdapter.add(position, drawerItem)
    }

    /**
     * Set a drawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    fun setItemAtPosition(drawerItem: IDrawerItem<*>, position: Int) {
        drawerBuilder.itemAdapter.add(position, drawerItem)
    }

    /**
     * Remove a drawerItem at a specific position
     *
     * @param position
     */
    fun removeItemByPosition(position: Int) {
        if (drawerBuilder.checkDrawerItem(position, false)) {
            drawerBuilder.itemAdapter.remove(position)
        }
    }

    /**
     * Remove a drawerItem by the identifier
     *
     * @param identifier
     */
    fun removeItem(identifier: Long) {
        itemAdapter.removeByIdentifier(identifier)
    }

    /**
     * remove a list of drawerItems by ther identifiers
     *
     * @param identifiers
     */
    fun removeItems(vararg identifiers: Long) {
        if (identifiers != null) {
            for (identifier in identifiers) {
                removeItem(identifier)
            }
        }
    }

    /**
     * Removes all items from drawer
     */
    fun removeAllItems() {
        drawerBuilder.itemAdapter.clear()
    }

    /**
     * add new Items to the current DrawerItem List
     *
     * @param drawerItems
     */
    fun addItems(vararg drawerItems: IDrawerItem<*>) {
        drawerBuilder.itemAdapter.add(*drawerItems)
    }

    /**
     * add new items to the current DrawerItem list at a specific position
     *
     * @param position
     * @param drawerItems
     */
    fun addItemsAtPosition(position: Int, vararg drawerItems: IDrawerItem<*>) {
        drawerBuilder.itemAdapter.add(position, *drawerItems)
    }

    /**
     * Replace the current DrawerItems with a new ArrayList of items
     *
     * @param drawerItems
     */
    fun setItems(drawerItems: List<IDrawerItem<*>>) {
        setItems(drawerItems, false)
    }

    /**
     * replace the current DrawerItems with the new ArrayList.
     *
     * @param drawerItems
     * @param switchedItems
     */
    private fun setItems(drawerItems: List<IDrawerItem<*>>?, switchedItems: Boolean) {
        //if we are currently at a switched list set the new reference
        if (originalDrawerItems != null && !switchedItems) {
            originalDrawerItems = drawerItems
        }
        drawerBuilder.itemAdapter.setNewList(drawerItems ?: ArrayList())
    }

    /**
     * update a specific footerDrawerItem :D
     * automatically identified by it's id
     *
     * @param drawerItem
     */
    fun updateStickyFooterItem(drawerItem: IDrawerItem<*>) {
        updateStickyFooterItemAtPosition(drawerItem, getStickyFooterPosition(drawerItem))
    }

    /**
     * update a footerDrawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    fun updateStickyFooterItemAtPosition(drawerItem: IDrawerItem<*>, position: Int) {
        if (drawerBuilder.mStickyDrawerItems.size > position) {
            drawerBuilder.mStickyDrawerItems[position] = drawerItem
        }

        //DrawerUtils.rebuildStickyFooterView(drawerBuilder)
    }


    /**
     * Add a footerDrawerItem at the end
     *
     * @param drawerItem
     */
    fun addStickyFooterItem(drawerItem: IDrawerItem<*>) {
        drawerBuilder.mStickyDrawerItems.add(drawerItem)
        //DrawerUtils.rebuildStickyFooterView(drawerBuilder)
    }

    /**
     * Add a footerDrawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    fun addStickyFooterItemAtPosition(drawerItem: IDrawerItem<*>, position: Int) {
        drawerBuilder.mStickyDrawerItems.add(position, drawerItem)
        //DrawerUtils.rebuildStickyFooterView(drawerBuilder)
    }

    /**
     * Set a footerDrawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    fun setStickyFooterItemAtPosition(drawerItem: IDrawerItem<*>, position: Int) {
        if (drawerBuilder.mStickyDrawerItems.size > position) {
            drawerBuilder.mStickyDrawerItems[position] = drawerItem
        }
        //DrawerUtils.rebuildStickyFooterView(drawerBuilder)
    }


    /**
     * Remove a footerDrawerItem at a specific position
     *
     * @param position
     */
    fun removeStickyFooterItemAtPosition(position: Int) {
        if (drawerBuilder.mStickyDrawerItems.size > position) {
            drawerBuilder.mStickyDrawerItems.removeAt(position)
        }
        //DrawerUtils.rebuildStickyFooterView(drawerBuilder)
    }

    /**
     * Removes all footerItems from drawer
     */
    fun removeAllStickyFooterItems() {
        drawerBuilder.mStickyDrawerItems.clear()
        drawerBuilder.mStickyFooterView?.visibility = View.GONE
    }

    /**
     * information if the current drawer content is switched by alternative content (profileItems)
     *
     * @return
     */
    fun switchedDrawerContent(): Boolean {
        return !(originalOnDrawerItemClickListener == null && originalDrawerItems == null && originalDrawerState == null)
    }

    /**
     * method to switch the drawer content to new elements
     *
     * @param onDrawerItemClickListener
     * @param drawerItems
     * @param drawerSelection
     */
    fun switchDrawerContent(onDrawerItemClickListenerInner: OnDrawerItemClickListener, onDrawerItemLongClickListenerInner: OnDrawerItemLongClickListener, drawerItemsInner: List<IDrawerItem<*>>, drawerSelection: Int) {
        //just allow a single switched drawer
        if (!switchedDrawerContent()) {
            //save out previous values
            originalOnDrawerItemClickListener = onDrawerItemClickListener
            originalOnDrawerItemLongClickListener = onDrawerItemLongClickListener
            originalDrawerState = adapter.saveInstanceState(Bundle())
            drawerBuilder.mExpandableExtension.collapse(false)
            originalDrawerItems = drawerItems
        }

        //set the new items
        onDrawerItemClickListener = onDrawerItemClickListenerInner
        onDrawerItemLongClickListener = onDrawerItemLongClickListenerInner
        setItems(drawerItemsInner, true)
        setSelectionAtPosition(drawerSelection, false)

        if (!drawerBuilder.mKeepStickyItemsVisible) {
            //hide stickyFooter and it's shadow
            stickyFooter?.visibility = View.GONE
            stickyFooterShadow?.visibility = View.GONE
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
            setItems(originalDrawerItems, true)
            adapter.withSavedInstanceState(originalDrawerState)
            //remove the references
            originalOnDrawerItemClickListener = null
            originalOnDrawerItemLongClickListener = null
            originalDrawerItems = null
            originalDrawerState = null

            //if we switch back scroll back to the top
            drawerBuilder.mRecyclerView.smoothScrollToPosition(0)

            //show the stickyFooter and it's shadow again
            stickyFooter?.visibility = View.VISIBLE
            stickyFooterShadow?.visibility = View.VISIBLE

            //if we currently show the accountHeader selection list make sure to reset this attr
            drawerBuilder.mAccountHeader?.accountHeaderBuilder?.selectionListShown = false
        }
    }

    /**
     * add the values to the bundle for saveInstanceState
     *
     * @param savedInstanceState
     * @return
     */
    fun saveInstanceState(_savedInstanceState: Bundle): Bundle {
        if (!drawerBuilder.mAppended) {
            drawerBuilder.adapter.saveInstanceState(_savedInstanceState, BUNDLE_SELECTION)?.apply {
                putInt(BUNDLE_STICKY_FOOTER_SELECTION, drawerBuilder.mCurrentStickyFooterSelection)
                putBoolean(BUNDLE_DRAWER_CONTENT_SWITCHED, switchedDrawerContent())
            }
        } else {
            drawerBuilder.adapter.saveInstanceState(_savedInstanceState, BUNDLE_SELECTION_APPENDED)?.apply {
                putInt(BUNDLE_STICKY_FOOTER_SELECTION_APPENDED, drawerBuilder.mCurrentStickyFooterSelection)
                putBoolean(BUNDLE_DRAWER_CONTENT_SWITCHED_APPENDED, switchedDrawerContent())
            }
        }
        return _savedInstanceState
    }

    /**
     * Call this helper method on the onBackPressed method of the activity.
     * Returns true if the drawer was open and it closed with the call.
     * Returns false if the drawer was already closed and can continue with the default activity behavior.
     *
     * @return true if acted, false if not acted
     */
    fun onBackPressed(): Boolean {
        if (isDrawerOpen) {
            closeDrawer()
            return true
        }
        return false
    }

    interface OnDrawerNavigationListener {
        /**
         * @param clickedView
         * @return true if the event was consumed
         */
        fun onNavigationClickListener(clickedView: View): Boolean
    }

    interface OnDrawerItemClickListener {
        /**
         * @param view
         * @param position
         * @param drawerItem
         * @return true if the event was consumed
         */
        fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean
    }

    interface OnDrawerItemLongClickListener {
        /**
         * @param view
         * @param position
         * @param drawerItem
         * @return true if the event was consumed
         */
        fun onItemLongClick(view: View, position: Int, drawerItem: IDrawerItem<*>): Boolean
    }

    interface OnDrawerListener {
        /**
         * @param drawerView
         */
        fun onDrawerOpened(drawerView: View)

        /**
         * @param drawerView
         */
        fun onDrawerClosed(drawerView: View)

        /**
         * @param drawerView
         * @param slideOffset
         */
        fun onDrawerSlide(drawerView: View, slideOffset: Float)
    }

    interface OnDrawerItemSelectedListener {
        /**
         * @param parent
         * @param view
         * @param position
         * @param id
         * @param drawerItem
         */
        fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long, drawerItem: IDrawerItem<*>)

        /**
         * @param parent
         */
        fun onNothingSelected(parent: AdapterView<*>)
    }

    companion object {
        /**
         * BUNDLE param to store the selection
         */
        const val BUNDLE_SELECTION = "_selection"
        const val BUNDLE_SELECTION_APPENDED = "_selection_appended"
        const val BUNDLE_STICKY_FOOTER_SELECTION = "bundle_sticky_footer_selection"
        const val BUNDLE_STICKY_FOOTER_SELECTION_APPENDED = "bundle_sticky_footer_selection_appended"
        const val BUNDLE_DRAWER_CONTENT_SWITCHED = "bundle_drawer_content_switched"
        const val BUNDLE_DRAWER_CONTENT_SWITCHED_APPENDED = "bundle_drawer_content_switched_appended"

        /**
         * Per the design guidelines, you should show the drawer on launch until the user manually
         * expands it. This shared preference tracks this.
         */
        const val PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned"

        /**
         * Per the design guidelines, you should show the drawer on launch until the user manually
         * expands it. This shared preference tracks this.
         */
        const val PREF_USER_OPENED_DRAWER_BY_DRAGGING = "navigation_drawer_dragged_open"
    }
}
