package com.mikepenz.materialdrawer.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.select.SelectExtension
import com.mikepenz.materialdrawer.DrawerUtils
import com.mikepenz.materialdrawer.MiniDrawer
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.interfaces.ICrossfader
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

class MiniDrawerSliderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.materialDrawerStyle) : LinearLayout(context, attrs, defStyleAttr) {

    /**
     * get the RecyclerView of this MiniDrawer
     *
     * @return
     */
    var recyclerView: RecyclerView
        private set
    /**
     * get the FastAdapter of this MiniDrawer
     *
     * @return
     */
    var adapter: FastAdapter<IDrawerItem<*>>
        protected set
    /**
     * get the ItemAdapter of this MiniDrawer
     *
     * @return
     */
    var itemAdapter: ItemAdapter<IDrawerItem<*>>
        protected set

    var mSelectExtension: SelectExtension<IDrawerItem<*>>

    /**
     * get the Drawer used to fill this MiniDrawer
     *
     * @return
     */
    var drawer: MaterialDrawerSliderView? = null
        set(value) {
            field = value
            if (field?.miniDrawer != this) {
                field?.miniDrawer = this
            }
            createItems()
        }

    /**
     * get the AccountHeader used to fill the this MiniDrawer
     *
     * @return
     */
    val accountHeader: AccountHeaderView?
        get() = drawer?.accountHeader

    /**
     * get the Crossfader used for this MiniDrawer
     *
     * @return
     */
    var crossFader: ICrossfader? = null

    private var mInnerShadow = false
    private var mInRTL = false
    var includeSecondaryDrawerItems = false
    private var mEnableSelectedMiniDrawerItemBackground = false
    private var mEnableProfileClick = true
    private var mOnMiniDrawerItemClickListener: MiniDrawer.OnMiniDrawerItemClickListener? = null
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
        get() = drawer?.originalDrawerItems ?: drawer?.itemAdapter?.adapterItems ?: ArrayList()

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MaterialDrawerSliderView, defStyleAttr, R.style.Widget_MaterialDrawerStyle)
        background = a.getDrawable(R.styleable.MaterialDrawerSliderView_materialDrawerBackground)
        a.recycle()

        if (mInnerShadow) {
            if (!mInRTL) {
                setBackgroundResource(R.drawable.material_drawer_shadow_left)
            } else {
                setBackgroundResource(R.drawable.material_drawer_shadow_right)
            }
        }

        //create and append recyclerView
        recyclerView = RecyclerView(context)
        addView(recyclerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        //set the itemAnimator
        recyclerView.itemAnimator = DefaultItemAnimator()
        //some style improvements on older devices
        recyclerView.setFadingEdgeLength(0)
        //set the drawing cache background to the same color as the slider to improve performance
        recyclerView.clipToPadding = false
        //additional stuff
        recyclerView.layoutManager = LinearLayoutManager(context)
        //adapter
        itemAdapter = ItemAdapter()
        adapter = FastAdapter.with(itemAdapter)
        mSelectExtension = adapter.getOrCreateExtension(SelectExtension::class.java)!! // definitely not null
        mSelectExtension.isSelectable = true
        mSelectExtension.allowDeselection = false
        recyclerView.adapter = adapter

        // set the insets
        ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
            recyclerView.setPadding(recyclerView.paddingLeft, insets.systemWindowInsetTop, recyclerView.paddingRight, insets.systemWindowInsetBottom)
            insets
        }

        //set the adapter with the items
        createItems()
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
                                if (it.selectionListShown) {
                                    it.toggleSelectionList(it.context)
                                }
                            }
                            val drawerItem = drawer?.getDrawerItem(item.identifier)
                            if (drawerItem != null && !drawerItem.isSelected) {
                                //set the selection
                                drawer?.setSelection(item.identifier, true)
                            }
                        } else if (drawer?.onDrawerItemClickListener != null) {
                            DrawerUtils.getDrawerItem(drawerItems, item.identifier)?.let {
                                //get the original `DrawerItem` from the Drawer as this one will contain all information
                                drawer?.onDrawerItemClickListener?.invoke(v, it, position)
                            }
                        }
                    } else if (type == PROFILE) {
                        accountHeader?.let {
                            if (!it.selectionListShown) {
                                it.toggleSelectionList(it.context)
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

    /**
     * generates a MiniDrawerItem from a IDrawerItem
     *
     * @param drawerItem
     * @return
     */
    open fun generateMiniDrawerItem(drawerItem: IDrawerItem<*>): IDrawerItem<*>? {
        return when (drawerItem) {
            is SecondaryDrawerItem -> if (includeSecondaryDrawerItems) MiniDrawerItem(drawerItem).withEnableSelectedBackground(mEnableSelectedMiniDrawerItemBackground).withSelectedBackgroundAnimated(false) else null
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
            return MiniDrawer.PROFILE
        } else if (drawerItem is MiniDrawerItem) {
            return MiniDrawer.ITEM
        }
        return -1
    }

    companion object {
        val PROFILE = 1
        val ITEM = 2
    }
}