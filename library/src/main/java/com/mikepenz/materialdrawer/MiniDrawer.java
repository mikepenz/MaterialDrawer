package com.mikepenz.materialdrawer;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter.listeners.OnLongClickListener;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.MiniDrawerItem;
import com.mikepenz.materialdrawer.model.MiniProfileDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialize.util.UIUtils;

import java.util.List;

/**
 * Created by mikepenz on 15.07.15.
 * Don't count this for real yet. it's just a quick try on creating a Gmail like panel
 */
public class MiniDrawer {
    public static final int PROFILE = 1;
    public static final int ITEM = 2;

    private LinearLayout mContainer;
    private RecyclerView mRecyclerView;
    protected FastAdapter<IDrawerItem> mAdapter;
    protected ItemAdapter<IDrawerItem> mItemAdapter;

    private Drawer mDrawer;

    /**
     * Provide the Drawer which will be used as dataSource for the drawerItems
     *
     * @param drawer
     * @return
     */
    public MiniDrawer withDrawer(@NonNull Drawer drawer) {
        this.mDrawer = drawer;
        return this;
    }

    private AccountHeader mAccountHeader;

    /**
     * Provide the AccountHeader which will be used as the dataSource for the profiles
     *
     * @param accountHeader
     * @return
     */
    public MiniDrawer withAccountHeader(@NonNull AccountHeader accountHeader) {
        this.mAccountHeader = accountHeader;
        return this;
    }

    private ICrossfader mCrossFader;

    /**
     * Provide the Crossfader implementation which is used with this MiniDrawer
     *
     * @param crossFader
     * @return
     */
    public MiniDrawer withCrossFader(@NonNull ICrossfader crossFader) {
        this.mCrossFader = crossFader;
        return this;
    }

    private boolean mInnerShadow = false;

    /**
     * set to true if you want to show the innerShadow on the MiniDrawer
     *
     * @param innerShadow
     * @return
     */
    public MiniDrawer withInnerShadow(boolean innerShadow) {
        this.mInnerShadow = innerShadow;
        return this;
    }

    private boolean mInRTL = false;

    /**
     * set to true if you want the MiniDrawer in RTL mode
     *
     * @param inRTL
     * @return
     */
    public MiniDrawer withInRTL(boolean inRTL) {
        this.mInRTL = inRTL;
        return this;
    }

    private boolean mIncludeSecondaryDrawerItems = false;

    /**
     * set to true if you also want to display secondaryDrawerItems
     *
     * @param includeSecondaryDrawerItems
     * @return
     */
    public MiniDrawer withIncludeSecondaryDrawerItems(boolean includeSecondaryDrawerItems) {
        this.mIncludeSecondaryDrawerItems = includeSecondaryDrawerItems;
        return this;
    }

    private boolean mEnableSelectedMiniDrawerItemBackground = false;

    /**
     * set to true if you want to display the background for the miniDrawerItem
     *
     * @param enableSelectedMiniDrawerItemBackground
     * @return
     */
    public MiniDrawer withEnableSelectedMiniDrawerItemBackground(boolean enableSelectedMiniDrawerItemBackground) {
        this.mEnableSelectedMiniDrawerItemBackground = enableSelectedMiniDrawerItemBackground;
        return this;
    }

    private boolean mEnableProfileClick = true;

    /**
     * set to false if you do not want the profile image to toggle to the normal drawers profile selection
     *
     * @param enableProfileClick
     * @return this
     */
    public MiniDrawer withEnableProfileClick(boolean enableProfileClick) {
        this.mEnableProfileClick = enableProfileClick;
        return this;
    }

    private OnMiniDrawerItemClickListener mOnMiniDrawerItemClickListener;

    /**
     * Define the onMiniDrawerItemClickListener called before any logic in the MiniDrawer is run, allows you to intercept the default behavior
     *
     * @param onMiniDrawerItemClickListener
     * @return this
     */
    public MiniDrawer withOnMiniDrawerItemClickListener(OnMiniDrawerItemClickListener onMiniDrawerItemClickListener) {
        this.mOnMiniDrawerItemClickListener = onMiniDrawerItemClickListener;
        return this;
    }


    private OnClickListener<IDrawerItem> mOnMiniDrawerItemOnClickListener;

    /**
     * Define an onClickListener for the MiniDrawer item adapter. WARNING: this will completely overwrite the default behavior
     * You may want to check the `OnMiniDrawerItemClickListener` (withOnMiniDrawerItemClickListener) which just hooks into the default behavior
     *
     * @param onMiniDrawerItemOnClickListener
     * @return this
     */
    public MiniDrawer withOnMiniDrawerItemOnClickListener(OnClickListener<IDrawerItem> onMiniDrawerItemOnClickListener) {
        this.mOnMiniDrawerItemOnClickListener = onMiniDrawerItemOnClickListener;
        return this;
    }


    private OnLongClickListener<IDrawerItem> mOnMiniDrawerItemLongClickListener;

    /**
     * Define an onLongClickListener for the MiniDrawer item adapter
     *
     * @param onMiniDrawerItemLongClickListener
     * @return
     */
    public MiniDrawer withOnMiniDrawerItemLongClickListener(OnLongClickListener<IDrawerItem> onMiniDrawerItemLongClickListener) {
        this.mOnMiniDrawerItemLongClickListener = onMiniDrawerItemLongClickListener;
        return this;
    }

    /**
     * get the RecyclerView of this MiniDrawer
     *
     * @return
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * get the FastAdapter of this MiniDrawer
     *
     * @return
     */
    public FastAdapter<IDrawerItem> getAdapter() {
        return mAdapter;
    }

    /**
     * get the ItemAdapter of this MiniDrawer
     *
     * @return
     */
    public ItemAdapter<IDrawerItem> getItemAdapter() {
        return mItemAdapter;
    }

    /**
     * get the Drawer used to fill this MiniDrawer
     *
     * @return
     */
    public Drawer getDrawer() {
        return mDrawer;
    }

    /**
     * get the AccountHeader used to fill the this MiniDrawer
     *
     * @return
     */
    public AccountHeader getAccountHeader() {
        return mAccountHeader;
    }

    /**
     * get the Crossfader used for this MiniDrawer
     *
     * @return
     */
    public ICrossfader getCrossFader() {
        return mCrossFader;
    }


    /**
     * the defined FastAdapter.OnClickListener which completely replaces the original behavior
     *
     * @return
     */
    public OnClickListener getOnMiniDrawerItemOnClickListener() {
        return mOnMiniDrawerItemOnClickListener;
    }

    /**
     * @return
     */
    public OnLongClickListener getOnMiniDrawerItemLongClickListener() {
        return mOnMiniDrawerItemLongClickListener;
    }


    /**
     * generates a MiniDrawerItem from a IDrawerItem
     *
     * @param drawerItem
     * @return
     */
    public IDrawerItem generateMiniDrawerItem(IDrawerItem drawerItem) {
        if (drawerItem instanceof SecondaryDrawerItem) {
            return mIncludeSecondaryDrawerItems ? new MiniDrawerItem((SecondaryDrawerItem) drawerItem).withEnableSelectedBackground(mEnableSelectedMiniDrawerItemBackground).withSelectedBackgroundAnimated(false) : null;
        } else if (drawerItem instanceof PrimaryDrawerItem) {
            return new MiniDrawerItem((PrimaryDrawerItem) drawerItem).withEnableSelectedBackground(mEnableSelectedMiniDrawerItemBackground).withSelectedBackgroundAnimated(false);
        } else if (drawerItem instanceof ProfileDrawerItem) {
            MiniProfileDrawerItem mpdi = new MiniProfileDrawerItem((ProfileDrawerItem) drawerItem);
            mpdi.withEnabled(mEnableProfileClick);
            return mpdi;
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
        mItemAdapter = new ItemAdapter<>();
        mAdapter = FastAdapter.with(mItemAdapter);
        mAdapter.withSelectable(true);
        mAdapter.withAllowDeselection(false);
        mRecyclerView.setAdapter(mAdapter);

        //if the activity with the drawer should be fullscreen add the padding for the statusbar
        if (mDrawer != null && mDrawer.mDrawerBuilder != null && (mDrawer.mDrawerBuilder.mFullscreen || mDrawer.mDrawerBuilder.mTranslucentStatusBar)) {
            mRecyclerView.setPadding(mRecyclerView.getPaddingLeft(), UIUtils.getStatusBarHeight(ctx), mRecyclerView.getPaddingRight(), mRecyclerView.getPaddingBottom());
        }

        //if the activity with the drawer should be fullscreen add the padding for the navigationBar
        if (mDrawer != null && mDrawer.mDrawerBuilder != null && (mDrawer.mDrawerBuilder.mFullscreen || mDrawer.mDrawerBuilder.mTranslucentNavigationBar) && ctx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
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
            if (profile instanceof IDrawerItem) {
                mItemAdapter.set(0, generateMiniDrawerItem((IDrawerItem) profile));
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
            //update everything
            setSelection(selectedDrawerItem.getIdentifier());

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
    public void setSelection(long identifier) {
        if (identifier == -1) {
            mAdapter.deselect();
        }
        int count = mAdapter.getItemCount();
        for (int i = 0; i < count; i++) {
            IDrawerItem item = mAdapter.getItem(i);
            if (item.getIdentifier() == identifier && !item.isSelected()) {
                mAdapter.deselect();
                mAdapter.select(i);
            }
        }
    }

    /**
     * update a MiniDrawerItem (after updating the main Drawer) via its identifier
     *
     * @param identifier the identifier of the item which was updated
     */
    public void updateItem(long identifier) {
        if (mDrawer != null && mAdapter != null && mItemAdapter.getAdapterItems() != null && identifier != -1) {
            IDrawerItem drawerItem = DrawerUtils.getDrawerItem(getDrawerItems(), identifier);
            for (int i = 0; i < mItemAdapter.getAdapterItems().size(); i++) {
                if (mItemAdapter.getAdapterItems().get(i).getIdentifier() == drawerItem.getIdentifier()) {
                    IDrawerItem miniDrawerItem = generateMiniDrawerItem(drawerItem);
                    if (miniDrawerItem != null) {
                        mItemAdapter.set(i, miniDrawerItem);
                    }
                }
            }
        }
    }

    /**
     * creates the items for the MiniDrawer
     */
    public void createItems() {
        mItemAdapter.clear();

        int profileOffset = 0;
        if (mAccountHeader != null && mAccountHeader.getAccountHeaderBuilder().mProfileImagesVisible) {
            IProfile profile = mAccountHeader.getActiveProfile();
            if (profile instanceof IDrawerItem) {
                mItemAdapter.add(generateMiniDrawerItem((IDrawerItem) profile));
                profileOffset = 1;
            }
        }

        int select = -1;
        if (mDrawer != null) {
            if (getDrawerItems() != null) {
                //migrate to miniDrawerItems
                int length = getDrawerItems().size();

                int position = 0;
                for (int i = 0; i < length; i++) {
                    IDrawerItem miniDrawerItem = generateMiniDrawerItem(getDrawerItems().get(i));
                    if (miniDrawerItem != null) {
                        if (miniDrawerItem.isSelected()) {
                            select = position;
                        }
                        mItemAdapter.add(miniDrawerItem);
                        position = position + 1;
                    }
                }

                if (select >= 0) {
                    //+1 because of the profile
                    mAdapter.select(select + profileOffset);
                }
            }
        }

        //listener
        if (mOnMiniDrawerItemOnClickListener != null) {
            mAdapter.withOnClickListener(mOnMiniDrawerItemOnClickListener);
        } else {
            mAdapter.withOnClickListener(new OnClickListener<IDrawerItem>() {
                @Override
                public boolean onClick(View v, IAdapter<IDrawerItem> adapter, final IDrawerItem item, final int position) {
                    int type = getMiniDrawerType(item);

                    //if a listener is defined and we consume the event return
                    if (mOnMiniDrawerItemClickListener != null && mOnMiniDrawerItemClickListener.onItemClick(v, position, item, type)) {
                        return false;
                    }

                    if (type == ITEM) {
                        //fire the onClickListener also if the specific drawerItem is not Selectable
                        if (item.isSelectable()) {
                            //make sure we are on the original drawerItemList
                            if (mAccountHeader != null && mAccountHeader.isSelectionListShown()) {
                                mAccountHeader.toggleSelectionList(v.getContext());
                            }
                            IDrawerItem drawerItem = mDrawer.getDrawerItem(item.getIdentifier());
                            if (drawerItem != null && !drawerItem.isSelected()) {
                                //set the selection
                                mDrawer.setSelection(item, true);
                            }
                        } else if (mDrawer.getOnDrawerItemClickListener() != null) {
                            //get the original `DrawerItem` from the Drawer as this one will contain all information
                            mDrawer.getOnDrawerItemClickListener().onItemClick(v, position, DrawerUtils.getDrawerItem(getDrawerItems(), item.getIdentifier()));
                        }
                    } else if (type == PROFILE) {
                        if (mAccountHeader != null && !mAccountHeader.isSelectionListShown()) {
                            mAccountHeader.toggleSelectionList(v.getContext());
                        }
                        if (mCrossFader != null) {
                            mCrossFader.crossfade();
                        }
                    }
                    return false;
                }
            });
        }
        mAdapter.withOnLongClickListener(mOnMiniDrawerItemLongClickListener);
        mRecyclerView.scrollToPosition(0);
    }

    /**
     * returns always the original drawerItems and not the switched content
     *
     * @return
     */
    private List<IDrawerItem> getDrawerItems() {
        return mDrawer.getOriginalDrawerItems() != null ? mDrawer.getOriginalDrawerItems() : mDrawer.getDrawerItems();
    }


    public interface OnMiniDrawerItemClickListener {
        /**
         * @param view
         * @param position
         * @param drawerItem
         * @param type       either MiniDrawer.PROFILE or MiniDrawer.ITEM
         * @return true if the event was consumed
         */
        boolean onItemClick(View view, int position, IDrawerItem drawerItem, int type);
    }
}
