package com.mikepenz.materialdrawer.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.select.SelectExtension
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.interfaces.ICrossfader
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.withEnabled
import com.mikepenz.materialdrawer.model.utils.hiddenInMiniDrawer
import com.mikepenz.materialdrawer.util.getDrawerItem

/**
 * This view is a simple drop in view for the [DrawerLayout] or as companion to a [MaterialDrawerSliderView] offering a convenient API to provide a nice and flexible mini slider view following
 * the material design guidelines v2.
 */
open class MiniDrawerSliderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.materialDrawerStyle) : LinearLayout(context, attrs, defStyleAttr) {

    /**
     * get the RecyclerView of this MiniDrawer
     *
     * @return
     */
    val recyclerView: RecyclerView

    /**
     * get the FastAdapter of this MiniDrawer
     *
     * @return
     */
    val adapter: FastAdapter<IDrawerItem<*>>

    /**
     * get the ItemAdapter of this MiniDrawer
     *
     * @return
     */
    val itemAdapter: ItemAdapter<IDrawerItem<*>>
    val selectExtension: SelectExtension<IDrawerItem<*>>

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

    var innerShadow = false
        set(value) {
            field = value
            updateInnerShadow()
        }

    var inRTL = false
        set(value) {
            field = value
            updateInnerShadow()
        }

    var includeSecondaryDrawerItems = false
        set(value) {
            field = value
            createItems()
        }

    var enableSelectedMiniDrawerItemBackground = false
        set(value) {
            field = value
            createItems()
        }

    var enableProfileClick = true
        set(value) {
            field = value
            createItems()
        }

    private var onMiniDrawerItemClickListener: ((view: View?, position: Int, drawerItem: IDrawerItem<*>, type: Int) -> Boolean)? = null
    private var onMiniDrawerItemOnClickListener: ((v: View?, adapter: IAdapter<IDrawerItem<*>>, item: IDrawerItem<*>, position: Int) -> Boolean)? = null
        set(value) {
            field = value
            if (value == null) {
                createItems()
            } else {
                adapter.onClickListener = value
            }
        }
    private var onMiniDrawerItemLongClickListener: ((v: View, adapter: IAdapter<IDrawerItem<*>>, item: IDrawerItem<*>, position: Int) -> Boolean)? = null
        set(value) {
            field = value
            adapter.onLongClickListener = value
        }

    /**
     * returns always the original drawerItems and not the switched content
     *
     * @return
     */
    private val drawerItems: List<IDrawerItem<*>>
        get() = drawer?.itemAdapter?.adapterItems ?: ArrayList()

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MaterialDrawerSliderView, defStyleAttr, R.style.Widget_MaterialDrawerStyle)
        background = a.getDrawable(R.styleable.MaterialDrawerSliderView_materialDrawerBackground)
        a.recycle()

        updateInnerShadow()

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
        selectExtension = adapter.getOrCreateExtension(SelectExtension::class.java)!! // definitely not null
        selectExtension.isSelectable = true
        selectExtension.allowDeselection = false
        recyclerView.adapter = adapter

        // set the insets
        ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
            recyclerView.setPadding(recyclerView.paddingLeft, insets.systemWindowInsetTop, recyclerView.paddingRight, insets.systemWindowInsetBottom)
            insets
        }

        //set the adapter with the items
        createItems()
    }

    private fun updateInnerShadow() {
        if (innerShadow) {
            if (!inRTL) {
                setBackgroundResource(R.drawable.material_drawer_shadow_left)
            } else {
                setBackgroundResource(R.drawable.material_drawer_shadow_right)
            }
        } else {
            background = null
        }
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
            // crossfade if we are cross faded
            crossFader?.let {
                if (drawer?.closeOnClick == true && it.isCrossfaded) {
                    it.crossfade()
                }
            }
            // update everything
            if (selectedDrawerItem.hiddenInMiniDrawer) {
                selectExtension.deselect()
            } else {
                setSelection(selectedDrawerItem.identifier)
            }

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
            selectExtension.deselect()
            return
        }
        val count = adapter.itemCount
        for (i in 0 until count) {
            val item = adapter.getItem(i)
            if (item?.identifier == identifier && !item.isSelected) {
                selectExtension.deselect()
                selectExtension.select(i)
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
            drawerItems.getDrawerItem(identifier)?.let { drawerItem ->
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
                selectExtension.select(select + profileOffset)
            }
        }

        //listener
        if (this.onMiniDrawerItemOnClickListener != null) {
            adapter.onClickListener = this.onMiniDrawerItemOnClickListener
        } else {
            adapter.onClickListener = { v: View?, _: IAdapter<IDrawerItem<*>>, item: IDrawerItem<*>, position: Int ->
                val type = getMiniDrawerType(item)

                //if a listener is defined and we consume the event return
                if (onMiniDrawerItemClickListener?.invoke(v, position, item, type) == true) {
                    false
                } else {
                    if (type == ITEM) {
                        //fire the onClickListener also if the specific drawerItem is not Selectable
                        if (item.isSelectable) {
                            //make sure we are on the original drawerItemList
                            accountHeader?.let {
                                if (it.selectionListShown) {
                                    it.toggleSelectionList()
                                }
                            }
                            val drawerItem = drawer?.getDrawerItem(item.identifier)
                            if (drawerItem != null && !drawerItem.isSelected) {
                                //set the selection
                                drawer?.selectExtension?.deselect()
                                drawer?.setSelection(item.identifier, true)
                            }
                        } else if (drawer?.onDrawerItemClickListener != null) {
                            drawerItems.getDrawerItem(item.identifier)?.let {
                                //get the original `DrawerItem` from the Drawer as this one will contain all information
                                drawer?.onDrawerItemClickListener?.invoke(v, it, position)
                            }
                        }
                    } else if (type == PROFILE) {
                        accountHeader?.let {
                            if (!it.selectionListShown) {
                                it.toggleSelectionList()
                            }
                        }

                        crossFader?.crossfade()
                    }
                    false
                }
            }
        }
        adapter.onLongClickListener = onMiniDrawerItemLongClickListener
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
            is SecondaryDrawerItem -> if (includeSecondaryDrawerItems && !drawerItem.hiddenInMiniDrawer) MiniDrawerItem(drawerItem).withEnableSelectedBackground(enableSelectedMiniDrawerItemBackground).withSelectedBackgroundAnimated(false) else null
            is PrimaryDrawerItem -> if (!drawerItem.hiddenInMiniDrawer) MiniDrawerItem(drawerItem).withEnableSelectedBackground(enableSelectedMiniDrawerItemBackground).withSelectedBackgroundAnimated(false) else null
            is ProfileDrawerItem -> MiniProfileDrawerItem(drawerItem).apply { withEnabled(enableProfileClick) }
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

    companion object {
        val PROFILE = 1
        val ITEM = 2
    }
}
