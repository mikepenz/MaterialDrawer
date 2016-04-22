package com.mikepenz.materialdrawer;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.FooterAdapter;
import com.mikepenz.fastadapter.adapters.HeaderAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.ContainerDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Iconable;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialize.Materialize;
import com.mikepenz.materialize.view.ScrimInsetsRelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mikepenz on 03.02.15.
 */
public class Drawer {
    /**
     * BUNDLE param to store the selection
     */
    protected static final String BUNDLE_SELECTION = "_selection";
    protected static final String BUNDLE_SELECTION_APPENDED = "_selection_appended";
    protected static final String BUNDLE_STICKY_FOOTER_SELECTION = "bundle_sticky_footer_selection";
    protected static final String BUNDLE_STICKY_FOOTER_SELECTION_APPENDED = "bundle_sticky_footer_selection_appended";
    protected static final String BUNDLE_DRAWER_CONTENT_SWITCHED = "bundle_drawer_content_switched";
    protected static final String BUNDLE_DRAWER_CONTENT_SWITCHED_APPENDED = "bundle_drawer_content_switched_appended";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    protected static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";


    protected final DrawerBuilder mDrawerBuilder;
    private FrameLayout mContentView;

    /**
     * the protected Constructor for the result
     *
     * @param drawerBuilder
     */
    protected Drawer(DrawerBuilder drawerBuilder) {
        this.mDrawerBuilder = drawerBuilder;
    }

    /**
     * the protected getter of the mDrawerBuilder
     * only used internally to prevent the default behavior of some public methods
     *
     * @return
     */
    protected DrawerBuilder getDrawerBuilder() {
        return this.mDrawerBuilder;
    }

    /**
     * Get the DrawerLayout of the current drawer
     *
     * @return
     */
    public DrawerLayout getDrawerLayout() {
        return this.mDrawerBuilder.mDrawerLayout;
    }

    /**
     * Sets the toolbar which should be used in combination with the drawer
     * This will handle the ActionBarDrawerToggle for you.
     * Do not set this if you are in a sub activity and want to handle the back arrow on your own
     *
     * @param activity
     * @param toolbar  the toolbar which is used in combination with the drawer
     */
    public void setToolbar(@NonNull Activity activity, @NonNull Toolbar toolbar) {
        setToolbar(activity, toolbar, false);
    }

    /**
     * Sets the toolbar which should be used in combination with the drawer
     * This will handle the ActionBarDrawerToggle for you.
     * Do not set this if you are in a sub activity and want to handle the back arrow on your own
     *
     * @param activity
     * @param toolbar                       the toolbar which is used in combination with the drawer
     * @param recreateActionBarDrawerToggle defines if the ActionBarDrawerToggle needs to be recreated with the new set Toolbar
     */
    public void setToolbar(@NonNull Activity activity, @NonNull Toolbar toolbar, boolean recreateActionBarDrawerToggle) {
        this.mDrawerBuilder.mToolbar = toolbar;
        this.mDrawerBuilder.handleDrawerNavigation(activity, recreateActionBarDrawerToggle);
    }

    /**
     * Add a custom ActionBarDrawerToggle which will be used in combination with this drawer.
     *
     * @param actionBarDrawerToggle
     */
    public void setActionBarDrawerToggle(@NonNull ActionBarDrawerToggle actionBarDrawerToggle) {
        this.mDrawerBuilder.mActionBarDrawerToggleEnabled = true;
        this.mDrawerBuilder.mActionBarDrawerToggle = actionBarDrawerToggle;
        this.mDrawerBuilder.handleDrawerNavigation(null, false);
    }

    /**
     * Open the drawer
     */
    public void openDrawer() {
        if (mDrawerBuilder.mDrawerLayout != null && mDrawerBuilder.mSliderLayout != null) {
            mDrawerBuilder.mDrawerLayout.openDrawer(mDrawerBuilder.mDrawerGravity);
        }
    }

    /**
     * close the drawer
     */
    public void closeDrawer() {
        if (mDrawerBuilder.mDrawerLayout != null) {
            mDrawerBuilder.mDrawerLayout.closeDrawer(mDrawerBuilder.mDrawerGravity);
        }
    }

    /**
     * Get the current state of the drawer.
     * True if the drawer is currently open.
     *
     * @return
     */
    public boolean isDrawerOpen() {
        if (mDrawerBuilder.mDrawerLayout != null && mDrawerBuilder.mSliderLayout != null) {
            return mDrawerBuilder.mDrawerLayout.isDrawerOpen(mDrawerBuilder.mDrawerGravity);
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
        if (mDrawerBuilder.mMaterialize != null) {
            mDrawerBuilder.mMaterialize.setFullscreen(fullscreen);
        }
    }

    /**
     * get the Materialize object used to beautify your activity
     *
     * @return
     */
    public Materialize getMaterialize() {
        return mDrawerBuilder.mMaterialize;
    }


    /**
     * gets the already generated MiniDrawer or creates a new one
     *
     * @return
     */
    public MiniDrawer getMiniDrawer() {
        if (mDrawerBuilder.mMiniDrawer == null) {
            mDrawerBuilder.mMiniDrawer = new MiniDrawer().withDrawer(this).withAccountHeader(mDrawerBuilder.mAccountHeader);
        }
        return mDrawerBuilder.mMiniDrawer;
    }

    /**
     * get the slider layout of the current drawer.
     * This is the layout containing the ListView
     *
     * @return
     */
    public ScrimInsetsRelativeLayout getSlider() {
        return mDrawerBuilder.mSliderLayout;
    }

    /**
     * get the container frameLayout of the current drawer
     *
     * @return
     */
    public FrameLayout getContent() {
        if (mContentView == null && this.mDrawerBuilder.mDrawerLayout != null) {
            mContentView = (FrameLayout) this.mDrawerBuilder.mDrawerLayout.findViewById(R.id.content_layout);
        }
        return mContentView;
    }

    /**
     * get the listView of the current drawer
     *
     * @return
     */
    public RecyclerView getRecyclerView() {
        return mDrawerBuilder.mRecyclerView;
    }

    /**
     * get the FastAdapter of the current drawer
     *
     * @return
     */
    public FastAdapter<IDrawerItem> getAdapter() {
        return mDrawerBuilder.mAdapter;
    }

    /**
     * get the HeaderAdapter of the current drawer
     *
     * @return
     */
    public HeaderAdapter<IDrawerItem> getHeaderAdapter() {
        return mDrawerBuilder.mHeaderAdapter;
    }

    /**
     * get the ItemAdapter of the current drawer
     *
     * @return
     */
    public ItemAdapter<IDrawerItem> getItemAdapter() {
        return mDrawerBuilder.mItemAdapter;
    }

    /**
     * get the FooterAdapter of the current drawer
     *
     * @return
     */
    public FooterAdapter<IDrawerItem> getFooterAdapter() {
        return mDrawerBuilder.mFooterAdapter;
    }

    /**
     * get all drawerItems of the current drawer
     *
     * @return
     */
    public List<IDrawerItem> getDrawerItems() {
        return mDrawerBuilder.getItemAdapter().getAdapterItems();
    }

    /**
     * get the Header View if set else NULL
     *
     * @return
     */
    public View getHeader() {
        return mDrawerBuilder.mHeaderView;
    }

    /**
     * get the StickyHeader View if set else NULL
     *
     * @return
     */
    public View getStickyHeader() {
        return mDrawerBuilder.mStickyHeaderView;
    }

    /**
     * method to replace a previous set header
     *
     * @param view
     */
    public void setHeader(@NonNull View view) {
        setHeader(view, true, true);
    }

    /**
     * method to replace a previous set header
     *
     * @param view
     * @param divider
     */
    public void setHeader(@NonNull View view, boolean divider) {
        setHeader(view, true, divider);
    }

    /**
     * method to replace a previous set header
     *
     * @param view
     * @param padding
     * @param divider
     */
    public void setHeader(@NonNull View view, boolean padding, boolean divider) {
        setHeader(view, padding, divider, null);
    }

    /**
     * method to replace a previous set header
     *
     * @param view
     * @param padding
     * @param divider
     * @param height
     */
    public void setHeader(@NonNull View view, boolean padding, boolean divider, DimenHolder height) {
        mDrawerBuilder.getHeaderAdapter().clear();
        if (padding) {
            mDrawerBuilder.getHeaderAdapter().add(new ContainerDrawerItem().withView(view).withDivider(divider).withHeight(height).withViewPosition(ContainerDrawerItem.Position.TOP));
        } else {
            mDrawerBuilder.getHeaderAdapter().add(new ContainerDrawerItem().withView(view).withDivider(divider).withHeight(height).withViewPosition(ContainerDrawerItem.Position.NONE));
        }
        //we need to set the padding so the header starts on top
        mDrawerBuilder.mRecyclerView.setPadding(mDrawerBuilder.mRecyclerView.getPaddingLeft(), 0, mDrawerBuilder.mRecyclerView.getPaddingRight(), mDrawerBuilder.mRecyclerView.getPaddingBottom());
    }

    /**
     * method to remove the header of the list
     */
    public void removeHeader() {
        mDrawerBuilder.getHeaderAdapter().clear();
        //possibly there should be also a reset of the padding so the first item starts below the toolbar
    }

    /**
     * get the Footer View if set else NULL
     *
     * @return
     */
    public View getFooter() {
        return mDrawerBuilder.mFooterView;
    }

    /**
     * get the StickyFooter View if set else NULL
     *
     * @return
     */
    public View getStickyFooter() {
        return mDrawerBuilder.mStickyFooterView;
    }

    /**
     * get the StickyFooter Shadow View if set else NULL
     *
     * @return
     */
    private View getStickyFooterShadow() {
        return mDrawerBuilder.mStickyFooterShadowView;
    }

    /**
     * get the ActionBarDrawerToggle
     *
     * @return
     */
    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        return mDrawerBuilder.mActionBarDrawerToggle;
    }

    /**
     * calculates the position of an drawerItem. searching by it's identifier
     *
     * @param drawerItem
     * @return
     */
    public int getPosition(@NonNull IDrawerItem drawerItem) {
        return getPosition(drawerItem.getIdentifier());
    }

    /**
     * calculates the position of an drawerItem. searching by it's identifier
     *
     * @param identifier
     * @return
     */
    public int getPosition(long identifier) {
        return DrawerUtils.getPositionByIdentifier(mDrawerBuilder, identifier);
    }

    /**
     * returns the DrawerItem by the given identifier
     *
     * @param identifier
     * @return
     */
    public IDrawerItem getDrawerItem(long identifier) {
        return (IDrawerItem) getAdapter().getItem(getPosition(identifier));
    }

    /**
     * returns the found drawerItem by the given tag
     *
     * @param tag
     * @return
     */
    public IDrawerItem getDrawerItem(Object tag) {
        return DrawerUtils.getDrawerItem(getDrawerItems(), tag);
    }

    /**
     * calculates the position of an drawerItem. searching by it's identifier
     *
     * @param drawerItem
     * @return
     */
    public int getStickyFooterPosition(@NonNull IDrawerItem drawerItem) {
        return getStickyFooterPosition(drawerItem.getIdentifier());
    }

    /**
     * calculates the position of an drawerItem inside the footer. searching by it's identfier
     *
     * @param identifier
     * @return
     */
    public int getStickyFooterPosition(long identifier) {
        return DrawerUtils.getStickyFooterPositionByIdentifier(mDrawerBuilder, identifier);
    }

    /**
     * get the current position of the selected drawer element
     *
     * @return
     */
    public int getCurrentSelectedPosition() {
        return mDrawerBuilder.mAdapter.getSelections().size() == 0 ? -1 : mDrawerBuilder.mAdapter.getSelections().iterator().next();
    }

    /**
     * get the current selected item identifier
     *
     * @return
     */
    public long getCurrentSelection() {
        IDrawerItem drawerItem = mDrawerBuilder.getDrawerItem(getCurrentSelectedPosition());
        if (drawerItem != null) {
            return drawerItem.getIdentifier();
        }
        return -1;
    }

    /**
     * get the current position of the selected sticky footer element
     *
     * @return
     */
    public int getCurrentStickyFooterSelectedPosition() {
        return mDrawerBuilder.mCurrentStickyFooterSelection;
    }

    /**
     * deselects all selected items
     */
    public void deselect() {
        getAdapter().deselect();
    }

    /**
     * deselects the item with the given identifier
     *
     * @param identifier the identifier to search for
     */
    public void deselect(long identifier) {
        getAdapter().deselect(getPosition(identifier));
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view!
     *
     * @param identifier the identifier to search for
     */
    public boolean setSelection(long identifier) {
        return setSelectionAtPosition(getPosition(identifier), true);
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param identifier  the identifier to search for
     * @param fireOnClick true if the click listener should be called
     */
    public boolean setSelection(long identifier, boolean fireOnClick) {
        return setSelectionAtPosition(getPosition(identifier), fireOnClick);
    }

    /**
     * set the current selection in the footer of the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param identifier  the identifier to search for
     * @param fireOnClick true if the click listener should be called
     */
    public void setStickyFooterSelection(long identifier, boolean fireOnClick) {
        setStickyFooterSelectionAtPosition(getStickyFooterPosition(identifier), fireOnClick);
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view!
     *
     * @param drawerItem the drawerItem to select (this requires a set identifier)
     */
    public boolean setSelection(@NonNull IDrawerItem drawerItem) {
        return setSelectionAtPosition(getPosition(drawerItem), true);
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param drawerItem  the drawerItem to select (this requires a set identifier)
     * @param fireOnClick true if the click listener should be called
     */
    public boolean setSelection(@NonNull IDrawerItem drawerItem, boolean fireOnClick) {
        return setSelectionAtPosition(getPosition(drawerItem), fireOnClick);
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view!
     *
     * @param position the position to select
     */
    public boolean setSelectionAtPosition(int position) {
        return setSelectionAtPosition(position, true);
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
    public boolean setSelectionAtPosition(int position, boolean fireOnClick) {
        if (mDrawerBuilder.mRecyclerView != null) {
            mDrawerBuilder.mAdapter.deselect();
            mDrawerBuilder.mAdapter.select(position, false);
            if (mDrawerBuilder.mOnDrawerItemClickListener != null && fireOnClick && position >= 0) {
                mDrawerBuilder.mOnDrawerItemClickListener.onItemClick(null, position, mDrawerBuilder.mAdapter.getItem(position));
            }

            //we set the selection on a normal item in the drawer so we have to deselect the items in the StickyDrawer
            mDrawerBuilder.resetStickyFooterSelection();
        }
        return false;
    }

    /**
     * set the current selection in the footer of the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view!
     *
     * @param position the position to select
     */
    public void setStickyFooterSelectionAtPosition(int position) {
        setStickyFooterSelectionAtPosition(position, true);
    }

    /**
     * set the current selection in the footer of the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param position
     * @param fireOnClick
     */
    public void setStickyFooterSelectionAtPosition(int position, boolean fireOnClick) {
        DrawerUtils.setStickyFooterSelection(mDrawerBuilder, position, fireOnClick);
    }

    /**
     * update a specific drawer item :D
     * automatically identified by its id
     *
     * @param drawerItem
     */
    public void updateItem(@NonNull IDrawerItem drawerItem) {
        updateItemAtPosition(drawerItem, getPosition(drawerItem));
    }

    /**
     * update the badge for a specific drawerItem
     * identified by its id
     *
     * @param identifier
     * @param badge
     */
    public void updateBadge(long identifier, StringHolder badge) {
        IDrawerItem drawerItem = getDrawerItem(identifier);
        if (drawerItem instanceof Badgeable) {
            Badgeable badgeable = (Badgeable) drawerItem;
            badgeable.withBadge(badge);
            updateItem((IDrawerItem) badgeable);
        }
    }

    /**
     * update the name for a specific drawerItem
     * identified by its id
     *
     * @param identifier
     * @param name
     */
    public void updateName(long identifier, StringHolder name) {
        IDrawerItem drawerItem = getDrawerItem(identifier);
        if (drawerItem instanceof Nameable) {
            Nameable pdi = (Nameable) drawerItem;
            pdi.withName(name);
            updateItem((IDrawerItem) pdi);
        }
    }

    /**
     * update the name for a specific drawerItem
     * identified by its id
     *
     * @param identifier
     * @param image
     */
    public void updateIcon(long identifier, ImageHolder image) {
        IDrawerItem drawerItem = getDrawerItem(identifier);
        if (drawerItem instanceof Iconable) {
            Iconable pdi = (Iconable) drawerItem;
            pdi.withIcon(image);
            updateItem((IDrawerItem) pdi);
        }
    }

    /**
     * Update a drawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void updateItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            mDrawerBuilder.getItemAdapter().set(position, drawerItem);
        }
    }

    /**
     * Add a drawerItem at the end
     *
     * @param drawerItem
     */
    public void addItem(@NonNull IDrawerItem drawerItem) {
        mDrawerBuilder.getItemAdapter().add(drawerItem);
    }

    /**
     * Add a drawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void addItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        mDrawerBuilder.getItemAdapter().add(position, drawerItem);
    }

    /**
     * Set a drawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void setItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        mDrawerBuilder.getItemAdapter().add(position, drawerItem);
    }

    /**
     * Remove a drawerItem at a specific position
     *
     * @param position
     */
    public void removeItemByPosition(int position) {
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            mDrawerBuilder.getItemAdapter().remove(position);
        }
    }

    /**
     * Remove a drawerItem by the identifier
     *
     * @param identifier
     */
    public void removeItem(long identifier) {
        int position = getPosition(identifier);
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            mDrawerBuilder.getItemAdapter().remove(position);
        }
    }

    /**
     * remove a list of drawerItems by ther identifiers
     *
     * @param identifiers
     */
    public void removeItems(long... identifiers) {
        if (identifiers != null) {
            for (long identifier : identifiers) {
                removeItem(identifier);
            }
        }
    }

    /**
     * Removes all items from drawer
     */
    public void removeAllItems() {
        mDrawerBuilder.getItemAdapter().clear();
    }

    /**
     * add new Items to the current DrawerItem List
     *
     * @param drawerItems
     */
    public void addItems(@NonNull IDrawerItem... drawerItems) {
        mDrawerBuilder.getItemAdapter().add(drawerItems);
    }

    /**
     * add new items to the current DrawerItem list at a specific position
     *
     * @param position
     * @param drawerItems
     */
    public void addItemsAtPosition(int position, @NonNull IDrawerItem... drawerItems) {
        mDrawerBuilder.getItemAdapter().add(position, drawerItems);
    }

    /**
     * Replace the current DrawerItems with a new ArrayList of items
     *
     * @param drawerItems
     */
    public void setItems(@NonNull List<IDrawerItem> drawerItems) {
        setItems(drawerItems, false);
    }

    /**
     * replace the current DrawerItems with the new ArrayList.
     *
     * @param drawerItems
     * @param switchedItems
     */
    private void setItems(@NonNull List<IDrawerItem> drawerItems, boolean switchedItems) {
        //if we are currently at a switched list set the new reference
        if (originalDrawerItems != null && !switchedItems) {
            originalDrawerItems = drawerItems;
        }
        mDrawerBuilder.getItemAdapter().setNewList(drawerItems);
    }

    /**
     * update a specific footerDrawerItem :D
     * automatically identified by it's id
     *
     * @param drawerItem
     */
    public void updateStickyFooterItem(@NonNull IDrawerItem drawerItem) {
        updateStickyFooterItemAtPosition(drawerItem, getStickyFooterPosition(drawerItem));
    }

    /**
     * update a footerDrawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void updateStickyFooterItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        if (mDrawerBuilder.mStickyDrawerItems != null && mDrawerBuilder.mStickyDrawerItems.size() > position) {
            mDrawerBuilder.mStickyDrawerItems.set(position, drawerItem);
        }

        DrawerUtils.rebuildStickyFooterView(mDrawerBuilder);
    }


    /**
     * Add a footerDrawerItem at the end
     *
     * @param drawerItem
     */
    public void addStickyFooterItem(@NonNull IDrawerItem drawerItem) {
        if (mDrawerBuilder.mStickyDrawerItems == null) {
            mDrawerBuilder.mStickyDrawerItems = new ArrayList<>();
        }
        mDrawerBuilder.mStickyDrawerItems.add(drawerItem);

        DrawerUtils.rebuildStickyFooterView(mDrawerBuilder);
    }

    /**
     * Add a footerDrawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void addStickyFooterItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        if (mDrawerBuilder.mStickyDrawerItems == null) {
            mDrawerBuilder.mStickyDrawerItems = new ArrayList<>();
        }
        mDrawerBuilder.mStickyDrawerItems.add(position, drawerItem);

        DrawerUtils.rebuildStickyFooterView(mDrawerBuilder);
    }

    /**
     * Set a footerDrawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void setStickyFooterItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        if (mDrawerBuilder.mStickyDrawerItems != null && mDrawerBuilder.mStickyDrawerItems.size() > position) {
            mDrawerBuilder.mStickyDrawerItems.set(position, drawerItem);
        }

        DrawerUtils.rebuildStickyFooterView(mDrawerBuilder);
    }


    /**
     * Remove a footerDrawerItem at a specific position
     *
     * @param position
     */
    public void removeStickyFooterItemAtPosition(int position) {
        if (mDrawerBuilder.mStickyDrawerItems != null && mDrawerBuilder.mStickyDrawerItems.size() > position) {
            mDrawerBuilder.mStickyDrawerItems.remove(position);
        }

        DrawerUtils.rebuildStickyFooterView(mDrawerBuilder);
    }

    /**
     * Removes all footerItems from drawer
     */
    public void removeAllStickyFooterItems() {
        if (mDrawerBuilder.mStickyDrawerItems != null) {
            mDrawerBuilder.mStickyDrawerItems.clear();
        }
        if (mDrawerBuilder.mStickyFooterView != null) {
            mDrawerBuilder.mStickyFooterView.setVisibility(View.GONE);
        }
    }

    /**
     * setter for the OnDrawerItemClickListener
     *
     * @param onDrawerItemClickListener
     */
    public void setOnDrawerItemClickListener(OnDrawerItemClickListener onDrawerItemClickListener) {
        mDrawerBuilder.mOnDrawerItemClickListener = onDrawerItemClickListener;
    }

    public void setOnDrawerNavigationListener(OnDrawerNavigationListener onDrawerNavigationListener) { //WBE
        mDrawerBuilder.mOnDrawerNavigationListener = onDrawerNavigationListener;
    }

    /**
     * method to get the OnDrawerItemClickListener
     *
     * @return
     */
    public OnDrawerItemClickListener getOnDrawerItemClickListener() {
        return mDrawerBuilder.mOnDrawerItemClickListener;
    }

    /**
     * method to get the OnDrawerNavigationListener
     *
     * @return
     */
    public OnDrawerNavigationListener getOnDrawerNavigationListener() {  //WBE
        return mDrawerBuilder.mOnDrawerNavigationListener;
    }

    /**
     * setter for the OnDrawerItemLongClickListener
     *
     * @param onDrawerItemLongClickListener
     */
    public void setOnDrawerItemLongClickListener(OnDrawerItemLongClickListener onDrawerItemLongClickListener) {
        mDrawerBuilder.mOnDrawerItemLongClickListener = onDrawerItemLongClickListener;
    }

    /**
     * method to get the OnDrawerItemLongClickListener
     *
     * @return
     */
    public OnDrawerItemLongClickListener getOnDrawerItemLongClickListener() {
        return mDrawerBuilder.mOnDrawerItemLongClickListener;
    }

    //variables to store and remember the original list of the drawer
    private Drawer.OnDrawerItemClickListener originalOnDrawerItemClickListener;
    private Drawer.OnDrawerItemLongClickListener originalOnDrawerItemLongClickListener;
    private List<IDrawerItem> originalDrawerItems;
    private Bundle originalDrawerState;

    /**
     * information if the current drawer content is switched by alternative content (profileItems)
     *
     * @return
     */
    public boolean switchedDrawerContent() {
        return !(originalOnDrawerItemClickListener == null && originalDrawerItems == null && originalDrawerState == null);
    }

    /**
     * get the original list of drawerItems
     *
     * @return
     */
    public List<IDrawerItem> getOriginalDrawerItems() {
        return originalDrawerItems;
    }

    /**
     * method to switch the drawer content to new elements
     *
     * @param onDrawerItemClickListener
     * @param drawerItems
     * @param drawerSelection
     */
    public void switchDrawerContent(@NonNull OnDrawerItemClickListener onDrawerItemClickListener, OnDrawerItemLongClickListener onDrawerItemLongClickListener, @NonNull List<IDrawerItem> drawerItems, int drawerSelection) {
        //just allow a single switched drawer
        if (!switchedDrawerContent()) {
            //save out previous values
            originalOnDrawerItemClickListener = getOnDrawerItemClickListener();
            originalOnDrawerItemLongClickListener = getOnDrawerItemLongClickListener();
            originalDrawerState = getAdapter().saveInstanceState(new Bundle());
            getAdapter().collapse(false);
            originalDrawerItems = getDrawerItems();
        }

        //set the new items
        setOnDrawerItemClickListener(onDrawerItemClickListener);
        setOnDrawerItemLongClickListener(onDrawerItemLongClickListener);
        setItems(drawerItems, true);
        setSelectionAtPosition(drawerSelection, false);

        //hide stickyFooter and it's shadow
        if (getStickyFooter() != null) {
            getStickyFooter().setVisibility(View.GONE);
        }
        if (getStickyFooterShadow() != null) {
            getStickyFooterShadow().setVisibility(View.GONE);
        }
    }

    /**
     * helper method to reset to the original drawerContent
     */
    public void resetDrawerContent() {
        if (switchedDrawerContent()) {
            //set the new items
            setOnDrawerItemClickListener(originalOnDrawerItemClickListener);
            setOnDrawerItemLongClickListener(originalOnDrawerItemLongClickListener);
            setItems(originalDrawerItems, true);
            getAdapter().withSavedInstanceState(originalDrawerState);
            //remove the references
            originalOnDrawerItemClickListener = null;
            originalOnDrawerItemLongClickListener = null;
            originalDrawerItems = null;
            originalDrawerState = null;

            //if we switch back scroll back to the top
            mDrawerBuilder.mRecyclerView.smoothScrollToPosition(0);

            //show the stickyFooter and it's shadow again
            if (getStickyFooter() != null) {
                getStickyFooter().setVisibility(View.VISIBLE);
            }
            if (getStickyFooterShadow() != null) {
                getStickyFooterShadow().setVisibility(View.VISIBLE);
            }

            //if we currently show the accountHeader selection list make sure to reset this attr
            if (mDrawerBuilder.mAccountHeader != null && mDrawerBuilder.mAccountHeader.mAccountHeaderBuilder != null) {
                mDrawerBuilder.mAccountHeader.mAccountHeaderBuilder.mSelectionListShown = false;
            }
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
            if (!mDrawerBuilder.mAppended) {
                savedInstanceState = mDrawerBuilder.mAdapter.saveInstanceState(savedInstanceState, BUNDLE_SELECTION);
                savedInstanceState.putInt(BUNDLE_STICKY_FOOTER_SELECTION, mDrawerBuilder.mCurrentStickyFooterSelection);
                savedInstanceState.putBoolean(BUNDLE_DRAWER_CONTENT_SWITCHED, switchedDrawerContent());
            } else {
                savedInstanceState = mDrawerBuilder.mAdapter.saveInstanceState(savedInstanceState, BUNDLE_SELECTION_APPENDED);
                savedInstanceState.putInt(BUNDLE_STICKY_FOOTER_SELECTION_APPENDED, mDrawerBuilder.mCurrentStickyFooterSelection);
                savedInstanceState.putBoolean(BUNDLE_DRAWER_CONTENT_SWITCHED_APPENDED, switchedDrawerContent());
            }
        }
        return savedInstanceState;
    }


    public interface OnDrawerNavigationListener {
        /**
         * @param clickedView
         * @return true if the event was consumed
         */
        boolean onNavigationClickListener(View clickedView);
    }

    public interface OnDrawerItemClickListener {
        /**
         * @param view
         * @param position
         * @param drawerItem
         * @return true if the event was consumed
         */
        boolean onItemClick(View view, int position, IDrawerItem drawerItem);
    }

    public interface OnDrawerItemLongClickListener {
        /**
         * @param view
         * @param position
         * @param drawerItem
         * @return true if the event was consumed
         */
        boolean onItemLongClick(View view, int position, IDrawerItem drawerItem);
    }

    public interface OnDrawerListener {
        /**
         * @param drawerView
         */
        void onDrawerOpened(View drawerView);

        /**
         * @param drawerView
         */
        void onDrawerClosed(View drawerView);

        /**
         * @param drawerView
         * @param slideOffset
         */
        void onDrawerSlide(View drawerView, float slideOffset);
    }

    public interface OnDrawerItemSelectedListener {
        /**
         * @param parent
         * @param view
         * @param position
         * @param id
         * @param drawerItem
         */
        void onItemSelected(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem);

        /**
         * @param parent
         */
        void onNothingSelected(AdapterView<?> parent);
    }
}
