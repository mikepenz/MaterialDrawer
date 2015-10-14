package com.mikepenz.materialdrawer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mikepenz.materialdrawer.adapter.BaseDrawerAdapter;
import com.mikepenz.materialdrawer.adapter.DrawerAdapter;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.MiniDrawerItem;
import com.mikepenz.materialdrawer.model.MiniProfileDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialize.util.UIUtils;

import java.util.ArrayList;

/**
 * Created by mikepenz on 15.07.15.
 * Don't count this for real yet. it's just a quick try on creating a Gmail like panel
 */
public class MiniDrawer {
    public static final int PROFILE = 1;
    public static final int ITEM = 2;

    private LinearLayout mContainer;
    private RecyclerView mRecyclerView;
    private DrawerAdapter mDrawerAdapter;

    private Drawer mDrawer;

    public MiniDrawer withDrawer(@NonNull Drawer drawer) {
        this.mDrawer = drawer;
        return this;
    }

    private AccountHeader mAccountHeader;

    public MiniDrawer withAccountHeader(@NonNull AccountHeader accountHeader) {
        this.mAccountHeader = accountHeader;
        return this;
    }

    private ICrossfader mCrossFader;

    public MiniDrawer withCrossFader(@NonNull ICrossfader crossFader) {
        this.mCrossFader = crossFader;
        return this;
    }

    private boolean mInnerShadow = false;

    public MiniDrawer withInnerShadow(boolean innerShadow) {
        this.mInnerShadow = innerShadow;
        return this;
    }

    private boolean mInRTL = false;

    public MiniDrawer withInRTL(boolean inRTL) {
        this.mInRTL = inRTL;
        return this;
    }

    private boolean mIncludeSecondaryDrawerItems = false;

    public MiniDrawer withIncludeSecondaryDrawerItems(boolean includeSecondaryDrawerItems) {
        this.mIncludeSecondaryDrawerItems = includeSecondaryDrawerItems;
        return this;
    }

    private boolean mEnableSelectedMiniDrawerItemBackground = false;

    public MiniDrawer withEnableSelectedMiniDrawerItemBackground(boolean enableSelectedMiniDrawerItemBackground) {
        this.mEnableSelectedMiniDrawerItemBackground = enableSelectedMiniDrawerItemBackground;
        return this;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public DrawerAdapter getDrawerAdapter() {
        return mDrawerAdapter;
    }

    public Drawer getDrawer() {
        return mDrawer;
    }

    public AccountHeader getAccountHeader() {
        return mAccountHeader;
    }

    public ICrossfader getCrossFader() {
        return mCrossFader;
    }

    /**
     * generates a MiniDrawerItem from a IDrawerItem
     *
     * @param drawerItem
     * @return
     */
    public IDrawerItem generateMiniDrawerItem(IDrawerItem drawerItem) {
        if (drawerItem instanceof PrimaryDrawerItem) {
            return new MiniDrawerItem((PrimaryDrawerItem) drawerItem).withEnableSelectedBackground(mEnableSelectedMiniDrawerItemBackground);
        } else if (drawerItem instanceof SecondaryDrawerItem && mIncludeSecondaryDrawerItems) {
            return new MiniDrawerItem((SecondaryDrawerItem) drawerItem).withEnableSelectedBackground(mEnableSelectedMiniDrawerItemBackground);
        }
        return null;
    }

    /**
     * gets the type of a IDrawerItem
     *
     * @param drawerItem
     * @return
     */
    public int getMiniDrawerType(IDrawerItem drawerItem) {
        if (drawerItem instanceof MiniProfileDrawerItem) {
            return PROFILE;
        } else if (drawerItem instanceof MiniDrawerItem) {
            return ITEM;
        }
        return -1;
    }

    /**
     * build the MiniDrawer
     *
     * @param ctx
     * @return
     */
    public View build(Context ctx) {
        mContainer = new LinearLayout(ctx);
        if (mInnerShadow) {
            if (!mInRTL) {
                mContainer.setBackgroundResource(R.drawable.material_drawer_shadow_left);
            } else {
                mContainer.setBackgroundResource(R.drawable.material_drawer_shadow_right);
            }
        }

        //create and append recyclerView
        mRecyclerView = new RecyclerView(ctx);
        mContainer.addView(mRecyclerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //set the itemAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //some style improvements on older devices
        mRecyclerView.setFadingEdgeLength(0);
        //set the drawing cache background to the same color as the slider to improve performance
        //mRecyclerView.setDrawingCacheBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(mActivity, R.attr.material_drawer_background, R.color.material_drawer_background));
        mRecyclerView.setClipToPadding(false);
        //additional stuff
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        //adapter
        mDrawerAdapter = new DrawerAdapter();
        mRecyclerView.setAdapter(mDrawerAdapter);

        //if the activity with the drawer should be fullscreen add the padding for the statusbar
        if (mDrawer != null && mDrawer.mDrawerBuilder != null && (mDrawer.mDrawerBuilder.mFullscreen || mDrawer.mDrawerBuilder.mTranslucentStatusBar)) {
            mRecyclerView.setPadding(mRecyclerView.getPaddingLeft(), UIUtils.getStatusBarHeight(ctx), mRecyclerView.getPaddingRight(), mRecyclerView.getPaddingBottom());
        }

        //if the activity with the drawer should be fullscreen add the padding for the navigationBar
        if (mDrawer != null && mDrawer.mDrawerBuilder != null && (mDrawer.mDrawerBuilder.mFullscreen || mDrawer.mDrawerBuilder.mTranslucentNavigationBar)) {
            mRecyclerView.setPadding(mRecyclerView.getPaddingLeft(), mRecyclerView.getPaddingTop(), mRecyclerView.getPaddingRight(), UIUtils.getNavigationBarHeight(ctx));
        }

        //set the adapter with the items
        createItems();

        return mContainer;
    }

    /**
     * call this method to trigger the onProfileClick on the MiniDrawer
     */
    public void onProfileClick() {
        //crossfade if we are cross faded
        if (mCrossFader != null) {
            if (mCrossFader.isCrossfaded()) {
                mCrossFader.crossfade();
            }
        }

        //update the current profile
        if (mAccountHeader != null) {
            IProfile profile = mAccountHeader.getActiveProfile();
            if (profile instanceof ProfileDrawerItem) {
                mDrawerAdapter.setDrawerItem(0, new MiniProfileDrawerItem((ProfileDrawerItem) profile));
            }
        }
    }

    /**
     * call this method to trigger the onItemClick on the MiniDrawer
     *
     * @param selectedDrawerItem
     * @return
     */
    public boolean onItemClick(IDrawerItem selectedDrawerItem) {
        //We only need to clear if the new item is selectable
        if (selectedDrawerItem.isSelectable()) {
            //crossfade if we are cross faded
            if (mCrossFader != null) {
                if (mCrossFader.isCrossfaded()) {
                    mCrossFader.crossfade();
                }
            }

            //get the identifier
            int identifier = selectedDrawerItem.getIdentifier();

            //update everything
            setSelection(identifier);

            return false;
        } else {
            return true;
        }
    }

    /**
     * set the selection of the MiniDrawer
     *
     * @param identifier the identifier of the item which should be selected (-1 for none)
     */
    public void setSelection(int identifier) {
        for (IDrawerItem drawerItem : mDrawerAdapter.getDrawerItems()) {
            drawerItem.withSetSelected(drawerItem.getIdentifier() == identifier);
        }
        mDrawerAdapter.notifyDataSetChanged();
    }

    /**
     * update a MiniDrawerItem (after updating the main Drawer) via its identifier
     *
     * @param identifier the identifier of the item which was updated
     */
    public void updateItem(int identifier) {
        if (mDrawer != null && mDrawerAdapter != null && mDrawerAdapter.getDrawerItems() != null && identifier != -1) {
            IDrawerItem drawerItem = mDrawer.getDrawerItem(identifier);

            for (int i = 0; i < mDrawerAdapter.getDrawerItems().size(); i++) {
                if (mDrawerAdapter.getDrawerItems().get(i).getIdentifier() == drawerItem.getIdentifier()) {
                    IDrawerItem miniDrawerItem = generateMiniDrawerItem(drawerItem);
                    if (miniDrawerItem != null) {
                        mDrawerAdapter.setDrawerItem(i, miniDrawerItem);
                    }
                }
            }
        }
    }

    /**
     * creates the items for the MiniDrawer
     */
    public void createItems() {
        mDrawerAdapter.clearDrawerItems();

        if (mAccountHeader != null) {
            IProfile profile = mAccountHeader.getActiveProfile();
            if (profile instanceof ProfileDrawerItem) {
                mDrawerAdapter.addDrawerItem(new MiniProfileDrawerItem((ProfileDrawerItem) profile));
            }
        }

        if (mDrawer != null) {
            if (mDrawer.getDrawerItems() != null) {
                ArrayList<IDrawerItem> drawerItems = mDrawer.getDrawerItems();
                if (mDrawer.switchedDrawerContent()) {
                    drawerItems = mDrawer.getOriginalDrawerItems();
                }

                //migrate to miniDrawerItems
                for (IDrawerItem drawerItem : drawerItems) {
                    IDrawerItem miniDrawerItem = generateMiniDrawerItem(drawerItem);
                    if (miniDrawerItem != null) {
                        mDrawerAdapter.addDrawerItem(miniDrawerItem);
                    }
                }
            }
        }

        //listener
        mDrawerAdapter.setOnClickListener(new BaseDrawerAdapter.OnClickListener() {
            @Override
            public void onClick(View v, int position, IDrawerItem item) {
                int type = getMiniDrawerType(item);
                if (type == ITEM) {
                    //fire the onClickListener also if the specific drawerItem is not Selectable
                    if (item.isSelectable()) {
                        mDrawer.setSelection(item, true);
                    } else if (mDrawer.getOnDrawerItemClickListener() != null) {
                        //get the original `DrawerItem` from the Drawer as this one will contain all information
                        mDrawer.getOnDrawerItemClickListener().onItemClick(v, position, mDrawer.getDrawerItem(item.getIdentifier()));
                    }
                } else if (type == PROFILE) {
                    if (mAccountHeader != null && !mAccountHeader.isSelectionListShown()) {
                        mAccountHeader.toggleSelectionList(v.getContext());
                    }
                    if (mCrossFader != null) {
                        mCrossFader.crossfade();
                    }
                }
            }
        });
        mRecyclerView.scrollToPosition(0);
    }
}
