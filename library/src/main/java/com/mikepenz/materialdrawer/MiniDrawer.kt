package com.mikepenz.materialdrawer

import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.select.SelectExtension
import com.mikepenz.materialdrawer.interfaces.ICrossfader
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialize.util.UIUtils

/**
 * Created by mikepenz on 15.07.15.
 * Don't count this for real yet. it's just a quick try on creating a Gmail like panel
 */
open class MiniDrawer {

    private lateinit var mContainer: LinearLayout
    /**
     * get the RecyclerView of this MiniDrawer
     *
     * @return
     */
    lateinit var recyclerView: RecyclerView
        private set
    /**
     * get the FastAdapter of this MiniDrawer
     *
     * @return
     */
    lateinit var adapter: FastAdapter<IDrawerItem<*>>
        protected set
    /**
     * get the ItemAdapter of this MiniDrawer
     *
     * @return
     */
    lateinit var itemAdapter: ItemAdapter<IDrawerItem<*>>
        protected set

    lateinit var mSelectExtension: SelectExtension<IDrawerItem<*>>

    /**
     * get the Drawer used to fill this MiniDrawer
     *
     * @return
     */
    var drawer: Drawer? = null
        private set

    /**
     * get the AccountHeader used to fill the this MiniDrawer
     *
     * @return
     */
    var accountHeader: AccountHeader? = null
        private set

    /**
     * get the Crossfader used for this MiniDrawer
     *
     * @return
     */
    var crossFader: ICrossfader? = null
        private set

    private var mInnerShadow = false
    private var mInRTL = false
    private var mIncludeSecondaryDrawerItems = false
    private var mEnableSelectedMiniDrawerItemBackground = false
    private var mEnableProfileClick = true
    private var mOnMiniDrawerItemClickListener: OnMiniDrawerItemClickListener? = null
    private var mOnMiniDrawerItemOnClickListener: ((v: View?, adapter: IAdapter<IDrawerItem<*>>, item: IDrawerItem<*>, position: Int) -> Boolean)? = null
    private var mOnMiniDrawerItemLongClickListener: ((v: View, adapter: IAdapter<IDrawerItem<*>>, item: IDrawerItem<*>, position: Int) -> Boolean)? = null


    /**
     * the defined FastAdapter.OnClickListener which completely replaces the original behavior
     *
     * @return
     */
    val onMiniDrawerItemOnClickListener: ((v: View?, adapter: IAdapter<IDrawerItem<*>>, item: IDrawerItem<*>, position: Int) -> Boolean)?
        get() = mOnMiniDrawerItemOnClickListener

    /**
     * @return
     */
    val onMiniDrawerItemLongClickListener: ((v: View, adapter: IAdapter<IDrawerItem<*>>, item: IDrawerItem<*>, position: Int) -> Boolean)?
        get() = mOnMiniDrawerItemLongClickListener

    /**
     * returns always the original drawerItems and not the switched content
     *
     * @return
     */
    private val drawerItems: List<IDrawerItem<*>>
        get() = drawer?.originalDrawerItems ?: drawer?.drawerItems ?: ArrayList()

    /**
     * Provide the Drawer which will be used as dataSource for the drawerItems
     *
     * @param drawer
     * @return
     */
    fun withDrawer(drawer: Drawer): MiniDrawer {
        this.drawer = drawer
        return this
    }

    /**
     * Provide the AccountHeader which will be used as the dataSource for the profiles
     *
     * @param accountHeader
     * @return
     */
    fun withAccountHeader(accountHeader: AccountHeader?): MiniDrawer {
        this.accountHeader = accountHeader
        return this
    }

    /**
     * Provide the Crossfader implementation which is used with this MiniDrawer
     *
     * @param crossFader
     * @return
     */
    fun withCrossFader(crossFader: ICrossfader): MiniDrawer {
        this.crossFader = crossFader
        return this
    }

    /**
     * set to true if you want to show the innerShadow on the MiniDrawer
     *
     * @param innerShadow
     * @return
     */
    fun withInnerShadow(innerShadow: Boolean): MiniDrawer {
        this.mInnerShadow = innerShadow
        return this
    }

    /**
     * set to true if you want the MiniDrawer in RTL mode
     *
     * @param inRTL
     * @return
     */
    fun withInRTL(inRTL: Boolean): MiniDrawer {
        this.mInRTL = inRTL
        return this
    }

    /**
     * set to true if you also want to display secondaryDrawerItems
     *
     * @param includeSecondaryDrawerItems
     * @return
     */
    fun withIncludeSecondaryDrawerItems(includeSecondaryDrawerItems: Boolean): MiniDrawer {
        this.mIncludeSecondaryDrawerItems = includeSecondaryDrawerItems
        return this
    }

    /**
     * set to true if you want to display the background for the miniDrawerItem
     *
     * @param enableSelectedMiniDrawerItemBackground
     * @return
     */
    fun withEnableSelectedMiniDrawerItemBackground(enableSelectedMiniDrawerItemBackground: Boolean): MiniDrawer {
        this.mEnableSelectedMiniDrawerItemBackground = enableSelectedMiniDrawerItemBackground
        return this
    }

    /**
     * set to false if you do not want the profile image to toggle to the normal drawers profile selection
     *
     * @param enableProfileClick
     * @return this
     */
    fun withEnableProfileClick(enableProfileClick: Boolean): MiniDrawer {
        this.mEnableProfileClick = enableProfileClick
        return this
    }

    /**
     * Define the onMiniDrawerItemClickListener called before any logic in the MiniDrawer is run, allows you to intercept the default behavior
     *
     * @param onMiniDrawerItemClickListener
     * @return this
     */
    fun withOnMiniDrawerItemClickListener(onMiniDrawerItemClickListener: OnMiniDrawerItemClickListener): MiniDrawer {
        this.mOnMiniDrawerItemClickListener = onMiniDrawerItemClickListener
        return this
    }

    /**
     * Define an onClickListener for the MiniDrawer item adapter. WARNING: this will completely overwrite the default behavior
     * You may want to check the `OnMiniDrawerItemClickListener` (withOnMiniDrawerItemClickListener) which just hooks into the default behavior
     *
     * @param onMiniDrawerItemOnClickListener
     * @return this
     */
    fun withOnMiniDrawerItemOnClickListener(onMiniDrawerItemOnClickListener: ((v: View?, adapter: IAdapter<IDrawerItem<*>>, item: IDrawerItem<*>, position: Int) -> Boolean)?): MiniDrawer {
        this.mOnMiniDrawerItemOnClickListener = onMiniDrawerItemOnClickListener
        return this
    }

    /**
     * Define an onLongClickListener for the MiniDrawer item adapter
     *
     * @param onMiniDrawerItemLongClickListener
     * @return
     */
    fun withOnMiniDrawerItemLongClickListener(onMiniDrawerItemLongClickListener: ((v: View, adapter: IAdapter<IDrawerItem<*>>, item: IDrawerItem<*>, position: Int) -> Boolean)?): MiniDrawer {
        this.mOnMiniDrawerItemLongClickListener = onMiniDrawerItemLongClickListener
        return this
    }


    /**
     * generates a MiniDrawerItem from a IDrawerItem
     *
     * @param drawerItem
     * @return
     */
    open fun generateMiniDrawerItem(drawerItem: IDrawerItem<*>): IDrawerItem<*>? {
        return when (drawerItem) {
            is SecondaryDrawerItem -> if (mIncludeSecondaryDrawerItems) MiniDrawerItem(drawerItem).withEnableSelectedBackground(mEnableSelectedMiniDrawerItemBackground).withSelectedBackgroundAnimated(false) else null
            is PrimaryDrawerItem -> MiniDrawerItem(drawerItem).withEnableSelectedBackground(mEnableSelectedMiniDrawerItemBackground).withSelectedBackgroundAnimated(false)
            is ProfileDrawerItem -> MiniProfileDrawerItem(drawerItem).apply { withEnabled(mEnableProfileClick) }
            else -> null
        }
    }

    /**
     * gets the type of a IDrawerItem
     *
     * @param drawerItem
     * @return
     */
    open fun getMiniDrawerType(drawerItem: IDrawerItem<*>): Int {
        if (drawerItem is MiniProfileDrawerItem) {
            return PROFILE
        } else if (drawerItem is MiniDrawerItem) {
            return ITEM
        }
        return -1
    }

    /**
     * build the MiniDrawer
     *
     * @param ctx
     * @return
     */
    open fun build(ctx: Context): View {
        mContainer = LinearLayout(ctx)
        if (mInnerShadow) {
            if (!mInRTL) {
                mContainer.setBackgroundResource(R.drawable.material_drawer_shadow_left)
            } else {
                mContainer.setBackgroundResource(R.drawable.material_drawer_shadow_right)
            }
        }

        //create and append recyclerView
        recyclerView = RecyclerView(ctx)
        mContainer.addView(recyclerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        //set the itemAnimator
        recyclerView.itemAnimator = DefaultItemAnimator()
        //some style improvements on older devices
        recyclerView.setFadingEdgeLength(0)
        //set the drawing cache background to the same color as the slider to improve performance
        //mRecyclerView.setDrawingCacheBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(mActivity, R.attr.material_drawer_background, R.color.material_drawer_background));
        recyclerView.clipToPadding = false
        //additional stuff
        recyclerView.layoutManager = LinearLayoutManager(ctx)
        //adapter
        itemAdapter = ItemAdapter()
        adapter = FastAdapter.with(itemAdapter)
        mSelectExtension = adapter.getOrCreateExtension(SelectExtension::class.java)!! // definitely not null
        mSelectExtension.isSelectable = true
        mSelectExtension.allowDeselection = false
        recyclerView.adapter = adapter

        //if the activity with the drawer should be fullscreen add the padding for the statusbar
        drawer?.drawerBuilder?.let { builder ->
            if ((builder.mFullscreen || builder.mTranslucentStatusBar)) {
                recyclerView.setPadding(recyclerView.paddingLeft, UIUtils.getStatusBarHeight(ctx), recyclerView.paddingRight, recyclerView.paddingBottom)
            }

            //if the activity with the drawer should be fullscreen add the padding for the navigationBar
            if ((builder.mFullscreen || builder.mTranslucentNavigationBar) && ctx.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                recyclerView.setPadding(recyclerView.paddingLeft, recyclerView.paddingTop, recyclerView.paddingRight, UIUtils.getNavigationBarHeight(ctx))
            }
        }

        //set the adapter with the items
        createItems()

        return mContainer
    }

    /**
     * call this method to trigger the onProfileClick on the MiniDrawer
     */
    fun onProfileClick() {
        //crossfade if we are cross faded
        crossFader?.let {
            if (it.isCrossfaded) {
                it.crossfade()
            }
        }

        //update the current profile
        accountHeader?.let {
            val profile = it.activeProfile
            if (profile is IDrawerItem<*>) {
                generateMiniDrawerItem(profile as IDrawerItem<*>)?.let { item ->
                    itemAdapter.set(0, item)
                }
            }
        }
    }

    /**
     * call this method to trigger the onItemClick on the MiniDrawer
     *
     * @param selectedDrawerItem
     * @return
     */
    fun onItemClick(selectedDrawerItem: IDrawerItem<*>): Boolean {
        //We only need to clear if the new item is selectable
        return if (selectedDrawerItem.isSelectable) {
            //crossfade if we are cross faded
            crossFader?.let {
                if (it.isCrossfaded) {
                    it.crossfade()
                }
            }
            //update everything
            setSelection(selectedDrawerItem.identifier)

            false
        } else {
            true
        }
    }

    /**
     * set the selection of the MiniDrawer
     *
     * @param identifier the identifier of the item which should be selected (-1 for none)
     */
    fun setSelection(identifier: Long) {
        if (identifier == -1L) {
            mSelectExtension.deselect()
        }
        val count = adapter.itemCount
        for (i in 0 until count) {
            val item = adapter.getItem(i)
            if (item?.identifier == identifier && !item.isSelected) {
                mSelectExtension.deselect()
                mSelectExtension.select(i)
            }
        }
    }

    /**
     * update a MiniDrawerItem (after updating the main Drawer) via its identifier
     *
     * @param identifier the identifier of the item which was updated
     */
    fun updateItem(identifier: Long) {
        if (drawer != null && identifier != -1L) {
            DrawerUtils.getDrawerItem(drawerItems, identifier)?.let { drawerItem ->
                for (i in 0 until itemAdapter.adapterItems.size) {
                    if (itemAdapter.adapterItems[i].identifier == drawerItem.identifier) {
                        val miniDrawerItem = generateMiniDrawerItem(drawerItem)
                        if (miniDrawerItem != null) {
                            itemAdapter[i] = miniDrawerItem
                        }
                    }
                }
            }
        }
    }

    /**
     * creates the items for the MiniDrawer
     */
    open fun createItems() {
        itemAdapter.clear()

        var profileOffset = 0
        accountHeader?.let { accountHeader ->
            val profile = accountHeader.activeProfile
            if (profile is IDrawerItem<*>) {
                generateMiniDrawerItem(profile as IDrawerItem<*>)?.let {
                    itemAdapter.add(it)
                }
                profileOffset = 1
            }
        }

        var select = -1
        if (drawer != null) {
            //migrate to miniDrawerItems
            val length = drawerItems.size

            var position = 0
            for (i in 0 until length) {
                val miniDrawerItem = generateMiniDrawerItem(drawerItems[i])
                if (miniDrawerItem != null) {
                    if (miniDrawerItem.isSelected) {
                        select = position
                    }
                    itemAdapter.add(miniDrawerItem)
                    position += 1
                }
            }

            if (select >= 0) {
                //+1 because of the profile
                mSelectExtension.select(select + profileOffset)
            }
        }

        //listener
        if (mOnMiniDrawerItemOnClickListener != null) {
            adapter.onClickListener = mOnMiniDrawerItemOnClickListener
        } else {
            adapter.onClickListener = { v: View?, _: IAdapter<IDrawerItem<*>>, item: IDrawerItem<*>, position: Int ->
                val type = getMiniDrawerType(item)

                //if a listener is defined and we consume the event return
                if (mOnMiniDrawerItemClickListener?.onItemClick(v, position, item, type) == true) {
                    false
                } else {
                    if (type == ITEM) {
                        //fire the onClickListener also if the specific drawerItem is not Selectable
                        if (item.isSelectable) {
                            //make sure we are on the original drawerItemList
                            accountHeader?.let {
                                if (it.isSelectionListShown) {
                                    it.toggleSelectionList(it.view.context)
                                }
                            }
                            val drawerItem = drawer?.getDrawerItem(item.identifier)
                            if (drawerItem != null && !drawerItem.isSelected) {
                                //set the selection
                                drawer?.setSelection(item, true)
                            }
                        } else if (drawer?.onDrawerItemClickListener != null) {
                            DrawerUtils.getDrawerItem(drawerItems, item.identifier)?.let {
                                //get the original `DrawerItem` from the Drawer as this one will contain all information
                                drawer?.onDrawerItemClickListener?.onItemClick(v, position, it)
                            }
                        }
                    } else if (type == PROFILE) {
                        accountHeader?.let {
                            if (!it.isSelectionListShown) {
                                it.toggleSelectionList(it.view.context)
                            }
                        }

                        crossFader?.crossfade()
                    }
                    false
                }
            }
        }
        adapter.onLongClickListener = mOnMiniDrawerItemLongClickListener
        recyclerView.scrollToPosition(0)
    }


    interface OnMiniDrawerItemClickListener {
        /**
         * @param view
         * @param position
         * @param drawerItem
         * @param type       either MiniDrawer.PROFILE or MiniDrawer.ITEM
         * @return true if the event was consumed
         */
        fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>, type: Int): Boolean
    }

    companion object {
        val PROFILE = 1
        val ITEM = 2
    }
}
