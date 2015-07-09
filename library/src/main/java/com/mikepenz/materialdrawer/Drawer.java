package com.mikepenz.materialdrawer;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.adapter.BaseDrawerAdapter;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
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
    /**
     * BUNDLE param to store the selection
     */
    protected static final String BUNDLE_SELECTION = "bundle_selection";
    protected static final String BUNDLE_FOOTER_SELECTION = "bundle_footer_selection";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    protected static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";


    private final DrawerBuilder mDrawerBuilder;
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
     * Get the DrawerLayout of the current drawer
     *
     * @return
     */
    public DrawerLayout getDrawerLayout() {
        return this.mDrawerBuilder.mDrawerLayout;
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
        if (mDrawerBuilder.mDrawerContentRoot != null) {
            mDrawerBuilder.mDrawerContentRoot.setEnabled(!fullscreen);
        }
    }

    /**
     * Set the color for the statusBar
     *
     * @param statusBarColor
     */
    public void setStatusBarColor(int statusBarColor) {
        if (mDrawerBuilder.mDrawerContentRoot != null) {
            mDrawerBuilder.mDrawerContentRoot.setInsetForeground(statusBarColor);
            mDrawerBuilder.mDrawerContentRoot.invalidate();
        }
    }

    /**
     * get the drawerContentRoot Layout (ScrimInsetsFrameLayout)
     *
     * @return
     */
    public ScrimInsetsFrameLayout getScrimInsetsFrameLayout() {
        return mDrawerBuilder.mDrawerContentRoot;
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
    public ListView getListView() {
        return mDrawerBuilder.mListView;
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
        return mDrawerBuilder.mDrawerItems;
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
    public void setHeader(View view) {
        if (getListView() != null) {
            BaseDrawerAdapter adapter = getAdapter();
            getListView().setAdapter(null);
            if (getHeader() != null) {
                getListView().removeHeaderView(getHeader());
            }
            getListView().addHeaderView(view);
            getListView().setAdapter(adapter);
            mDrawerBuilder.mHeaderView = view;
            mDrawerBuilder.mHeaderOffset = 1;
        }
    }

    /**
     * method to remove the header of the list
     */
    public void removeHeader() {
        if (getListView() != null && getHeader() != null) {
            getListView().removeHeaderView(getHeader());
            mDrawerBuilder.mHeaderView = null;
            mDrawerBuilder.mHeaderOffset = 0;
        }
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
        return DrawerUtils.getPositionFromIdentifier(mDrawerBuilder, identifier);
    }

    /**
     * calculates the position of an drawerItem. searching by it's identifier
     *
     * @param drawerItem
     * @return
     */
    public int getFooterPositionFromIdentifier(IDrawerItem drawerItem) {
        return getFooterPositionFromIdentifier(drawerItem.getIdentifier());
    }

    /**
     * calculates the position of an drawerItem inside the footer. searching by it's identfier
     *
     * @param identifier
     * @return
     */
    public int getFooterPositionFromIdentifier(int identifier) {
        return DrawerUtils.getFooterPositionFromIdentifier(mDrawerBuilder, identifier);
    }

    /**
     * get the current selection
     *
     * @return
     */
    public int getCurrentSelection() {
        return mDrawerBuilder.mCurrentSelection;
    }

    /**
     * get the current footer selection
     *
     * @return
     */
    public int getCurrentFooterSelection() {
        return mDrawerBuilder.mCurrentFooterSelection;
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view!
     *
     * @param identifier
     */
    public boolean setSelectionByIdentifier(int identifier) {
        return setSelection(getPositionFromIdentifier(identifier), true);
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param identifier
     * @param fireOnClick
     */
    public boolean setSelectionByIdentifier(int identifier, boolean fireOnClick) {
        return setSelection(getPositionFromIdentifier(identifier), fireOnClick);
    }

    /**
     * set the current selection in the footer of the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param identifier
     * @param fireOnClick
     */
    public void setFooterSelectionByIdentifier(int identifier, boolean fireOnClick) {
        setFooterSelection(getPositionFromIdentifier(identifier), fireOnClick);
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view!
     *
     * @param drawerItem
     */
    public boolean setSelection(IDrawerItem drawerItem) {
        return setSelection(getPositionFromIdentifier(drawerItem), true);
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param drawerItem
     * @param fireOnClick
     */
    public boolean setSelection(IDrawerItem drawerItem, boolean fireOnClick) {
        return setSelection(getPositionFromIdentifier(drawerItem), fireOnClick);
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view!
     *
     * @param position the position to select
     */
    public boolean setSelection(int position) {
        return setSelection(position, true);
    }

    /*
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param position
     * @param fireOnClick
     * @return true if the event was consumed
     */
    public boolean setSelection(int position, boolean fireOnClick) {
        if (mDrawerBuilder.mListView != null) {
            return DrawerUtils.setListSelection(mDrawerBuilder, position, fireOnClick, mDrawerBuilder.getDrawerItem(position, false));
        }
        return false;
    }

    /**
     * set the current selection in the footer of the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view!
     *
     * @param position the position to select
     */
    public void setFooterSelection(int position) {
        setFooterSelection(position, true);
    }

    /**
     * set the current selection in the footer of the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param position
     * @param fireOnClick
     */
    public void setFooterSelection(int position, boolean fireOnClick) {
        DrawerUtils.setFooterSelection(mDrawerBuilder, position, fireOnClick);
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
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            mDrawerBuilder.mDrawerItems.set(position, drawerItem);
            mDrawerBuilder.mAdapter.dataUpdated();
        }
    }

    /**
     * Add a drawerItem at the end
     *
     * @param drawerItem
     */
    public void addItem(IDrawerItem drawerItem) {
        if (mDrawerBuilder.mDrawerItems != null) {
            mDrawerBuilder.mDrawerItems.add(drawerItem);
            mDrawerBuilder.mAdapter.dataUpdated();
        }
    }

    /**
     * Add a drawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void addItem(IDrawerItem drawerItem, int position) {
        if (mDrawerBuilder.mDrawerItems != null) {
            mDrawerBuilder.mDrawerItems.add(position, drawerItem);
            mDrawerBuilder.mAdapter.dataUpdated();
        }
    }

    /**
     * Set a drawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void setItem(IDrawerItem drawerItem, int position) {
        if (mDrawerBuilder.mDrawerItems != null) {
            mDrawerBuilder.mDrawerItems.set(position, drawerItem);
            mDrawerBuilder.mAdapter.dataUpdated();
        }
    }

    /**
     * Remove a drawerItem at a specific position
     *
     * @param position
     */
    public void removeItem(int position) {
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            mDrawerBuilder.mDrawerItems.remove(position);
            mDrawerBuilder.mAdapter.dataUpdated();
        }
    }

    /**
     * Removes all items from drawer
     */
    public void removeAllItems() {
        mDrawerBuilder.mDrawerItems.clear();
        mDrawerBuilder.mAdapter.dataUpdated();
    }

    /**
     * add new Items to the current DrawerItem List
     *
     * @param drawerItems
     */
    public void addItems(IDrawerItem... drawerItems) {
        if (mDrawerBuilder.mDrawerItems != null) {
            Collections.addAll(mDrawerBuilder.mDrawerItems, drawerItems);
            mDrawerBuilder.mAdapter.dataUpdated();
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
        mDrawerBuilder.mDrawerItems = drawerItems;

        //if we are currently at a switched list set the new reference
        if (originalDrawerItems != null && !switchedItems) {
            originalDrawerItems = drawerItems;
        } else {
            mDrawerBuilder.mAdapter.setDrawerItems(mDrawerBuilder.mDrawerItems);
        }

        mDrawerBuilder.mAdapter.dataUpdated();
    }

    /**
     * Update the name of a drawer item if its an instance of nameable
     *
     * @param nameRes
     * @param position
     */
    public void updateName(int nameRes, int position) {
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            IDrawerItem drawerItem = mDrawerBuilder.mDrawerItems.get(position);

            if (drawerItem instanceof Nameable) {
                ((Nameable) drawerItem).setName(null);
                ((Nameable) drawerItem).setNameRes(nameRes);
            }

            mDrawerBuilder.mDrawerItems.set(position, drawerItem);
            mDrawerBuilder.mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Update the name of a drawer item if its an instance of nameable
     *
     * @param name
     * @param position
     */
    public void updateName(String name, int position) {
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            IDrawerItem drawerItem = mDrawerBuilder.mDrawerItems.get(position);

            if (drawerItem instanceof Nameable) {
                ((Nameable) drawerItem).setNameRes(-1);
                ((Nameable) drawerItem).setName(name);
            }

            mDrawerBuilder.mDrawerItems.set(position, drawerItem);
            mDrawerBuilder.mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Update the badge of a drawer item if its an instance of badgeable
     *
     * @param badge
     * @param position
     */
    public void updateBadge(String badge, int position) {
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            IDrawerItem drawerItem = mDrawerBuilder.mDrawerItems.get(position);

            if (drawerItem instanceof Badgeable) {
                ((Badgeable) drawerItem).setBadge(badge);
            }

            mDrawerBuilder.mDrawerItems.set(position, drawerItem);
            mDrawerBuilder.mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Update the icon of a drawer item if its an instance of iconable
     *
     * @param icon
     * @param position
     */
    public void updateIcon(Drawable icon, int position) {
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            IDrawerItem drawerItem = mDrawerBuilder.mDrawerItems.get(position);

            if (drawerItem instanceof Iconable) {
                ((Iconable) drawerItem).setIcon(icon);
            }

            mDrawerBuilder.mDrawerItems.set(position, drawerItem);
            mDrawerBuilder.mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Update the icon of a drawer item from an iconRes
     *
     * @param iconRes
     * @param position
     */
    public void updateIcon(int iconRes, int position) {
        if (mDrawerBuilder.mRootView != null && mDrawerBuilder.checkDrawerItem(position, false)) {
            IDrawerItem drawerItem = mDrawerBuilder.mDrawerItems.get(position);

            if (drawerItem instanceof Iconable) {
                ((Iconable) drawerItem).setIcon(UIUtils.getCompatDrawable(mDrawerBuilder.mRootView.getContext(), iconRes));
            }

            mDrawerBuilder.mDrawerItems.set(position, drawerItem);
            mDrawerBuilder.mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Update the icon of a drawer item if its an instance of iconable
     *
     * @param icon
     * @param position
     */
    public void updateIcon(IIcon icon, int position) {
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            IDrawerItem drawerItem = mDrawerBuilder.mDrawerItems.get(position);

            if (drawerItem instanceof Iconable) {
                ((Iconable) drawerItem).setIIcon(icon);
            }

            mDrawerBuilder.mDrawerItems.set(position, drawerItem);
            mDrawerBuilder.mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * update a specific footerDrawerItem :D
     * automatically identified by it's id
     *
     * @param drawerItem
     */
    public void updateFooterItem(IDrawerItem drawerItem) {
        updateFooterItem(drawerItem, getFooterPositionFromIdentifier(drawerItem));
    }

    /**
     * update a footerDrawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void updateFooterItem(IDrawerItem drawerItem, int position) {
        if (mDrawerBuilder.mStickyDrawerItems != null && mDrawerBuilder.mStickyDrawerItems.size() > position) {
            mDrawerBuilder.mStickyDrawerItems.set(position, drawerItem);
        }

        DrawerUtils.rebuildFooterView(mDrawerBuilder);
    }


    /**
     * Add a footerDrawerItem at the end
     *
     * @param drawerItem
     */
    public void addFooterItem(IDrawerItem drawerItem) {
        if (mDrawerBuilder.mStickyDrawerItems == null) {
            mDrawerBuilder.mStickyDrawerItems = new ArrayList<>();
        }
        mDrawerBuilder.mStickyDrawerItems.add(drawerItem);

        DrawerUtils.rebuildFooterView(mDrawerBuilder);
    }

    /**
     * Add a footerDrawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void addFooterItem(IDrawerItem drawerItem, int position) {
        if (mDrawerBuilder.mStickyDrawerItems == null) {
            mDrawerBuilder.mStickyDrawerItems = new ArrayList<>();
        }
        mDrawerBuilder.mStickyDrawerItems.add(position, drawerItem);

        DrawerUtils.rebuildFooterView(mDrawerBuilder);
    }

    /**
     * Set a footerDrawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void setFooterItem(IDrawerItem drawerItem, int position) {
        if (mDrawerBuilder.mStickyDrawerItems != null && mDrawerBuilder.mStickyDrawerItems.size() > position) {
            mDrawerBuilder.mStickyDrawerItems.set(position, drawerItem);
        }

        DrawerUtils.rebuildFooterView(mDrawerBuilder);
    }


    /**
     * Remove a footerDrawerItem at a specific position
     *
     * @param position
     */
    public void removeFooterItem(int position) {
        if (mDrawerBuilder.mStickyDrawerItems != null && mDrawerBuilder.mStickyDrawerItems.size() > position) {
            mDrawerBuilder.mStickyDrawerItems.remove(position);
        }

        DrawerUtils.rebuildFooterView(mDrawerBuilder);
    }

    /**
     * Removes all footerItems from drawer
     */
    public void removeAllFooterItems() {
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

            mDrawerBuilder.mAdapter.resetAnimation();

            if (getStickyFooter() != null) {
                getStickyFooter().setVisibility(View.GONE);
            }
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

            mDrawerBuilder.mAdapter.resetAnimation();

            if (getStickyFooter() != null) {
                getStickyFooter().setVisibility(View.VISIBLE);
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
            savedInstanceState.putInt(BUNDLE_SELECTION, mDrawerBuilder.mCurrentSelection);
            savedInstanceState.putInt(BUNDLE_FOOTER_SELECTION, mDrawerBuilder.mCurrentFooterSelection);
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
         * @param parent
         * @param view
         * @param position
         * @param id
         * @param drawerItem
         * @return true if the event was consumed
         */
        boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem);
    }

    public interface OnDrawerItemLongClickListener {
        /**
         * @param parent
         * @param view
         * @param position
         * @param id
         * @param drawerItem
         * @return true if the event was consumed
         */
        boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem);
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
