package com.mikepenz.materialdrawer;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.mikepenz.materialdrawer.adapter.BaseDrawerAdapter;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.ContainerDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Iconable;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.IdDistributor;
import com.mikepenz.materialdrawer.util.KeyboardUtil;
import com.mikepenz.materialize.Materialize;
import com.mikepenz.materialize.view.IScrimInsetsLayout;

import java.util.ArrayList;

/**
 * Created by mikepenz on 03.02.15.
 */
public class Drawer {
    /**
     * BUNDLE param to store the selection
     */
    protected static final String BUNDLE_SELECTION = "bundle_selection";
    protected static final String BUNDLE_SELECTION_APPENDED = "bundle_selection_appended";
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
    private KeyboardUtil mKeyboardUtil = null;

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
     * Set the color for the statusBar
     *
     * @param statusBarColor
     */
    public void setStatusBarColor(@ColorInt int statusBarColor) {
        if (mDrawerBuilder.mMaterialize != null) {
            mDrawerBuilder.mMaterialize.setStatusBarColor(statusBarColor);
            mDrawerBuilder.mMaterialize.getScrimInsetsFrameLayout().getView().invalidate();
        }
    }

    /**
     * get the drawerContentRoot Layout (ScrimInsetsFrameLayout)
     *
     * @return
     */
    public IScrimInsetsLayout getScrimInsetsFrameLayout() {
        if (mDrawerBuilder.mMaterialize != null) {
            return mDrawerBuilder.mMaterialize.getScrimInsetsFrameLayout();
        }
        return null;
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
     * a helper method to enable the keyboardUtil for a specific activity
     * or disable it. note this will cause some frame drops because of the
     * listener.
     *
     * @param activity
     * @param enable
     */
    public void keyboardSupportEnabled(@NonNull Activity activity, boolean enable) {
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
     * get the BaseDrawerAdapter of the current drawer
     *
     * @return
     */
    public BaseDrawerAdapter getAdapter() {
        return mDrawerBuilder.mAdapter;
    }

    /**
     * get all drawerItems of the current drawer
     *
     * @return
     */
    public ArrayList<IDrawerItem> getDrawerItems() {
        return mDrawerBuilder.getAdapter().getDrawerItems();
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
        getAdapter().clearHeaderItems();
        if (padding) {
            getAdapter().addHeaderDrawerItems(new ContainerDrawerItem().withView(view).withDivider(divider).withViewPosition(ContainerDrawerItem.Position.TOP));
        } else {
            getAdapter().addHeaderDrawerItems(new ContainerDrawerItem().withView(view).withDivider(divider).withViewPosition(ContainerDrawerItem.Position.NONE));
        }
    }

    /**
     * method to remove the header of the list
     */
    public void removeHeader() {
        getAdapter().clearHeaderItems();
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
    public int getPosition(int identifier) {
        return DrawerUtils.getPositionByIdentifier(mDrawerBuilder, identifier);
    }

    /**
     * returns the DrawerItem by the given identifier
     *
     * @param identifier
     * @return
     */
    public IDrawerItem getDrawerItem(int identifier) {
        return getAdapter().getItem(getPosition(identifier));
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
    public int getStickyFooterPosition(int identifier) {
        return DrawerUtils.getStickyFooterPositionByIdentifier(mDrawerBuilder, identifier);
    }

    /**
     * get the current position of the selected drawer element
     *
     * @return
     */
    public int getCurrentSelectedPosition() {
        return mDrawerBuilder.mCurrentSelection;
    }

    /**
     * get the current selected item identifier
     *
     * @return
     */
    public int getCurrentSelection() {
        IDrawerItem drawerItem = mDrawerBuilder.getDrawerItem(mDrawerBuilder.mCurrentSelection);
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
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view!
     *
     * @param identifier
     */
    public boolean setSelection(int identifier) {
        return setSelectionAtPosition(getPosition(identifier), true);
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param identifier
     * @param fireOnClick
     */
    public boolean setSelection(int identifier, boolean fireOnClick) {
        return setSelectionAtPosition(getPosition(identifier), fireOnClick);
    }

    /**
     * set the current selection in the footer of the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param identifier
     * @param fireOnClick
     */
    public void setStickyFooterSelection(int identifier, boolean fireOnClick) {
        setStickyFooterSelectionAtPosition(getStickyFooterPosition(identifier), fireOnClick);
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view!
     *
     * @param drawerItem
     */
    public boolean setSelection(@NonNull IDrawerItem drawerItem) {
        return setSelectionAtPosition(getPosition(drawerItem), true);
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param drawerItem
     * @param fireOnClick
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
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param position
     * @param fireOnClick
     * @return true if the event was consumed
     */
    public boolean setSelectionAtPosition(int position, boolean fireOnClick) {
        if (mDrawerBuilder.mRecyclerView != null) {
            return DrawerUtils.setRecyclerViewSelection(mDrawerBuilder, position, fireOnClick, mDrawerBuilder.getDrawerItem(position));
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
    public void updateBadge(int identifier, StringHolder badge) {
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
    public void updateName(int identifier, StringHolder name) {
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
    public void updateIcon(int identifier, ImageHolder image) {
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
            mDrawerBuilder.getAdapter().setDrawerItem(position, drawerItem);
        }
    }

    /**
     * Add a drawerItem at the end
     *
     * @param drawerItem
     */
    public void addItem(@NonNull IDrawerItem drawerItem) {
        mDrawerBuilder.getAdapter().addDrawerItem(IdDistributor.checkId(drawerItem));
    }

    /**
     * Add a drawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void addItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        mDrawerBuilder.getAdapter().addDrawerItem(position, IdDistributor.checkId(drawerItem));
        if (position < mDrawerBuilder.mCurrentSelection) {
            mDrawerBuilder.mCurrentSelection = mDrawerBuilder.mCurrentSelection + 1;
        }
    }

    /**
     * Set a drawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void setItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        mDrawerBuilder.getAdapter().addDrawerItem(position, IdDistributor.checkId(drawerItem));
    }

    /**
     * Remove a drawerItem at a specific position
     *
     * @param position
     */
    public void removeItemByPosition(int position) {
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            mDrawerBuilder.getAdapter().removeDrawerItem(position);
            if (position < mDrawerBuilder.mCurrentSelection) {
                mDrawerBuilder.mCurrentSelection = mDrawerBuilder.mCurrentSelection - 1;
            }
        }
    }

    /**
     * Remove a drawerItem by the identifier
     *
     * @param identifier
     */
    public void removeItem(int identifier) {
        int position = getPosition(identifier);
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            mDrawerBuilder.getAdapter().removeDrawerItem(position);
            if (position < mDrawerBuilder.mCurrentSelection) {
                mDrawerBuilder.mCurrentSelection = mDrawerBuilder.mCurrentSelection - 1;
            }
        }
    }

    /**
     * remove a list of drawerItems by ther identifiers
     *
     * @param identifiers
     */
    public void removeItems(int... identifiers) {
        if (identifiers != null) {
            for (int identifier : identifiers) {
                removeItem(identifier);
            }
        }
    }

    /**
     * Removes all items from drawer
     */
    public void removeAllItems() {
        mDrawerBuilder.getAdapter().clearDrawerItems();
        mDrawerBuilder.mCurrentSelection = -1;
    }

    /**
     * add new Items to the current DrawerItem List
     *
     * @param drawerItems
     */
    public void addItems(@NonNull IDrawerItem... drawerItems) {
        mDrawerBuilder.getAdapter().addDrawerItems(IdDistributor.checkIds(drawerItems));
    }

    /**
     * add new items to the current DrawerItem list at a specific position
     *
     * @param position
     * @param drawerItems
     */
    public void addItemsAtPosition(int position, @NonNull IDrawerItem... drawerItems) {
        mDrawerBuilder.getAdapter().addDrawerItems(position, IdDistributor.checkIds(drawerItems));
    }

    /**
     * Replace the current DrawerItems with a new ArrayList of items
     *
     * @param drawerItems
     */
    public void setItems(@NonNull ArrayList<IDrawerItem> drawerItems) {
        setItems(IdDistributor.checkIds(drawerItems), false);
    }

    /**
     * replace the current DrawerItems with the new ArrayList.
     *
     * @param drawerItems
     * @param switchedItems
     */
    private void setItems(@NonNull ArrayList<IDrawerItem> drawerItems, boolean switchedItems) {
        //if we are currently at a switched list set the new reference
        if (originalDrawerItems != null && !switchedItems) {
            originalDrawerItems = drawerItems;
            mDrawerBuilder.mCurrentSelection = -1;
        } else {
            mDrawerBuilder.getAdapter().setDrawerItems(drawerItems);
        }

        mDrawerBuilder.mAdapter.notifyDataSetChanged();
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
        mDrawerBuilder.mStickyDrawerItems.add(IdDistributor.checkId(drawerItem));

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
        mDrawerBuilder.mStickyDrawerItems.add(position, IdDistributor.checkId(drawerItem));

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
            mDrawerBuilder.mStickyDrawerItems.set(position, IdDistributor.checkId(drawerItem));
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

    /**
     * method to get the OnDrawerItemClickListener
     *
     * @return
     */
    public OnDrawerItemClickListener getOnDrawerItemClickListener() {
        return mDrawerBuilder.mOnDrawerItemClickListener;
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

    /**
     * Sets the {@link OnDrawerNavigationListener}.
     * @param onDrawerNavigationListener the OnDrawerNavigationListener
     */
    public void setOnDrawerNavigationListener(OnDrawerNavigationListener onDrawerNavigationListener) {
        mDrawerBuilder.mOnDrawerNavigationListener = onDrawerNavigationListener;
    }

    /**
     * Gets the {@link OnDrawerNavigationListener}.
     * @return the OnDrawerNavigationListener
     */
    public OnDrawerNavigationListener getOnDrawerNavigationListener() {
        return mDrawerBuilder.mOnDrawerNavigationListener;
    }

    //variables to store and remember the original list of the drawer
    private Drawer.OnDrawerItemClickListener originalOnDrawerItemClickListener;
    private Drawer.OnDrawerItemLongClickListener originalOnDrawerItemLongClickListener;
    private ArrayList<IDrawerItem> originalDrawerItems;
    private int originalDrawerSelection = -1;

    /**
     * information if the current drawer content is switched by alternative content (profileItems)
     *
     * @return
     */
    public boolean switchedDrawerContent() {
        return !(originalOnDrawerItemClickListener == null && originalDrawerItems == null && originalDrawerSelection == -1);
    }

    /**
     * get the original list of drawerItems
     *
     * @return
     */
    public ArrayList<IDrawerItem> getOriginalDrawerItems() {
        return originalDrawerItems;
    }

    /**
     * method to switch the drawer content to new elements
     *
     * @param onDrawerItemClickListener
     * @param drawerItems
     * @param drawerSelection
     */
    public void switchDrawerContent(@NonNull OnDrawerItemClickListener onDrawerItemClickListener, OnDrawerItemLongClickListener onDrawerItemLongClickListener, @NonNull ArrayList<IDrawerItem> drawerItems, int drawerSelection) {
        //just allow a single switched drawer
        if (!switchedDrawerContent()) {
            //save out previous values
            originalOnDrawerItemClickListener = getOnDrawerItemClickListener();
            originalOnDrawerItemLongClickListener = getOnDrawerItemLongClickListener();
            originalDrawerItems = getDrawerItems();
            originalDrawerSelection = getCurrentSelectedPosition();
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
            setSelectionAtPosition(originalDrawerSelection, false);
            //remove the references
            originalOnDrawerItemClickListener = null;
            originalOnDrawerItemLongClickListener = null;
            originalDrawerItems = null;
            originalDrawerSelection = -1;

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
                savedInstanceState.putInt(BUNDLE_SELECTION, switchedDrawerContent() ? originalDrawerSelection : mDrawerBuilder.mCurrentSelection);
                savedInstanceState.putInt(BUNDLE_STICKY_FOOTER_SELECTION, mDrawerBuilder.mCurrentStickyFooterSelection);
                savedInstanceState.putBoolean(BUNDLE_DRAWER_CONTENT_SWITCHED, switchedDrawerContent());
            } else {
                savedInstanceState.putInt(BUNDLE_SELECTION_APPENDED, switchedDrawerContent() ? originalDrawerSelection : mDrawerBuilder.mCurrentSelection);
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
