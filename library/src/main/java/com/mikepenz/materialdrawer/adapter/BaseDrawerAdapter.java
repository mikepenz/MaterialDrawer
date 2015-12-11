package com.mikepenz.materialdrawer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.materialdrawer.model.AbstractDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Selectable;
import com.mikepenz.materialdrawer.util.RecyclerViewCacheUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mikepenz on 14.07.15.
 */
public abstract class BaseDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<IDrawerItem> mHeaderDrawerItems = new ArrayList<>();
    private ArrayList<IDrawerItem> mDrawerItems = new ArrayList<>();
    private ArrayList<IDrawerItem> mFooterDrawerItems = new ArrayList<>();

    private LinkedList<String> mTypeIds = new LinkedList<>();
    private LinkedHashMap<String, IDrawerItem> mTypeInstances = new LinkedHashMap<>();

    private int previousSelection = -1;

    private OnClickListener mOnClickListener;
    private OnLongClickListener mOnLongClickListener;

    public BaseDrawerAdapter() {
    }

    public BaseDrawerAdapter(ArrayList<IDrawerItem> drawerItems) {
        setDrawerItems(drawerItems);
    }

    public void setOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener mOnLongClickListener) {
        this.mOnLongClickListener = mOnLongClickListener;
    }

    public ArrayList<IDrawerItem> getDrawerItems() {
        return mDrawerItems;
    }

    public void setDrawerItems(ArrayList<IDrawerItem> drawerItems) {
        mDrawerItems = drawerItems;
        mapPossibleTypes(drawerItems);
        notifyItemRangeChanged(getHeaderItemCount(), getDrawerItemCount());
    }

    public void addDrawerItems(IDrawerItem... drawerItems) {
        int length = mDrawerItems.size();
        if (drawerItems != null) {
            Collections.addAll(mDrawerItems, drawerItems);
            mapPossibleTypes(mDrawerItems);
            notifyItemRangeInserted(length, drawerItems.length);
        }
    }

    public void addDrawerItems(int position, IDrawerItem... drawerItems) {
        if (drawerItems != null) {
            mDrawerItems.addAll(position, Arrays.asList(drawerItems));
            mapPossibleTypes(mDrawerItems);
            notifyItemRangeInserted(position + 1, drawerItems.length);

            //fix wrong remembered position
            if (position < previousSelection) {
                previousSelection = previousSelection + drawerItems.length;
            }
        }
    }

    public void setDrawerItem(int position, IDrawerItem drawerItem) {
        mDrawerItems.set(position - getHeaderItemCount(), drawerItem);
        mapPossibleType(drawerItem);
        notifyItemChanged(position);
    }

    public void addDrawerItem(IDrawerItem drawerItem) {
        mDrawerItems.add(drawerItem);
        mapPossibleType(drawerItem);
        notifyItemInserted(mDrawerItems.size());
    }

    public void addDrawerItem(int position, IDrawerItem drawerItem) {
        mDrawerItems.add(position - getHeaderItemCount(), drawerItem);
        mapPossibleType(drawerItem);
        notifyItemInserted(position);

        //fix wrong remembered position
        if (position < previousSelection) {
            previousSelection = previousSelection + 1;
        }
    }

    public void removeDrawerItem(int position) {
        mDrawerItems.remove(position - getHeaderItemCount());
        notifyItemRemoved(position);

        //fix wrong remembered position
        if (position < previousSelection) {
            previousSelection = previousSelection - 1;
        }
    }

    public void clearDrawerItems() {
        int count = mDrawerItems.size();
        mDrawerItems.clear();
        notifyItemRangeRemoved(getHeaderItemCount(), count);

        //fix wrong remembered position
        previousSelection = -1;
    }

    public void clearHeaderItems() {
        int size = mHeaderDrawerItems.size();
        mHeaderDrawerItems.clear();
        if (size > 0) {
            notifyItemRemoved(0);
        }
    }

    public void clearFooterItems() {
        int count = mFooterDrawerItems.size();
        mFooterDrawerItems.clear();
        notifyItemRangeRemoved(getHeaderItemCount() + getDrawerItemCount(), count);
    }

    public ArrayList<IDrawerItem> getHeaderDrawerItems() {
        return mHeaderDrawerItems;
    }

    public void setHeaderDrawerItems(ArrayList<IDrawerItem> mHeaderDrawerItems) {
        this.mHeaderDrawerItems = mHeaderDrawerItems;
        notifyItemRangeInserted(0, mHeaderDrawerItems.size());
        mapPossibleTypes(mHeaderDrawerItems);
    }

    public void addHeaderDrawerItems(IDrawerItem... drawerItems) {
        if (drawerItems != null) {
            Collections.addAll(mHeaderDrawerItems, drawerItems);
            notifyItemRangeInserted(0, drawerItems.length);
        }
        mapPossibleTypes(mHeaderDrawerItems);
    }

    public ArrayList<IDrawerItem> getFooterDrawerItems() {
        return mFooterDrawerItems;
    }

    public void setFooterDrawerItems(ArrayList<IDrawerItem> mFooterDrawerItems) {
        this.mFooterDrawerItems = mFooterDrawerItems;
        notifyItemRangeInserted(0, mFooterDrawerItems.size());
        mapPossibleTypes(mFooterDrawerItems);
    }

    public void addFooterDrawerItems(IDrawerItem... drawerItems) {
        if (drawerItems != null) {
            Collections.addAll(mFooterDrawerItems, drawerItems);
            notifyItemRangeInserted(0, drawerItems.length);
        }
        mapPossibleTypes(mFooterDrawerItems);
    }

    /**
     * internal mapper to remember and add possible types for the RecyclerView
     */
    private void mapPossibleTypes(List<IDrawerItem> drawerItemList) {
        if (drawerItemList != null) {
            for (IDrawerItem drawerItem : drawerItemList) {
                mapPossibleType(drawerItem);
            }
        }
    }

    /**
     * internal mapper to remember and add possible types for the RecyclerView
     */
    private void mapPossibleType(IDrawerItem drawerItem) {
        if (!mTypeInstances.containsKey(drawerItem.getType())) {
            mTypeIds.add(drawerItem.getType());
            mTypeInstances.put(drawerItem.getType(), drawerItem);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mTypeIds.indexOf(getItem(position).getType());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //first check if we (probably) have this item in the cache
        RecyclerView.ViewHolder vh = RecyclerViewCacheUtil.getInstance().obtain(mTypeIds.get(viewType));
        if (vh == null) {
            return mTypeInstances.get(mTypeIds.get(viewType)).getViewHolder(parent);
        } else {
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        getItem(position).bindView(holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                IDrawerItem drawerItem = getItem(pos);

                //make sure there is a DrawerItem for the specific position
                if (drawerItem != null) {
                    //if we are enabled allow the selection and call the onClick
                    if (drawerItem.isEnabled()) {
                        if (drawerItem instanceof Selectable) {
                            if (drawerItem.isSelectable()) {
                                handleSelection(v, pos);
                            }
                        }

                        if (mOnClickListener != null) {
                            mOnClickListener.onClick(v, pos, drawerItem);
                        }

                        //if this is an abstractDrawerItem and it has an onDrawerItemClickListener call it
                        if (drawerItem instanceof AbstractDrawerItem) {
                            AbstractDrawerItem adi = (AbstractDrawerItem) drawerItem;
                            if (adi.getOnDrawerItemClickListener() != null) {
                                adi.getOnDrawerItemClickListener().onItemClick(v, pos, drawerItem);
                            }
                        }
                    }
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnLongClickListener != null) {
                    int pos = holder.getAdapterPosition();
                    return mOnLongClickListener.onLongClick(v, pos, getItem(pos));
                }
                return false;
            }
        });
    }

    /**
     * handles the selection on click and deselects previous selected items
     *
     * @param v
     * @param pos
     */
    public void handleSelection(View v, int pos) {
        //deselect the previous item
        if (previousSelection > -1) {
            IDrawerItem prev = getItem(previousSelection);
            if (prev != null) {
                prev.withSetSelected(false);
            }
            notifyItemChanged(previousSelection);
        } else {
            //if there was no previous selection we have to iterate over all so we can deselect the previous item
            for (int i = 0; i < getItemCount(); i++) {
                if (getItem(i).isSelected()) {
                    getItem(i).withSetSelected(false);
                    notifyItemChanged(i);
                    break;
                }
            }
        }

        //highlight the new item
        if (pos > -1) {
            IDrawerItem cur = getItem(pos);
            if (cur != null) {
                cur.withSetSelected(true);
            }
            notifyItemChanged(pos);

            if (v != null) {
                v.setSelected(true);
                v.invalidate();
            }
        }
        previousSelection = pos;
    }

    public boolean isEnabled(int position) {
        IDrawerItem item = getItem(position);
        return item != null && item.isEnabled();
    }

    public boolean isSelected(int position) {
        IDrawerItem item = getItem(position);
        return item != null && item.isSelected();
    }

    public IDrawerItem getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }

        if (position < getHeaderItemCount()) {
            return mHeaderDrawerItems.get(position);
        } else if (position < (getHeaderItemCount() + getDrawerItemCount())) {
            return mDrawerItems.get(position - getHeaderItemCount());
        } else {
            return mFooterDrawerItems.get(position - getHeaderItemCount() - getDrawerItemCount());
        }
    }

    @Override
    public long getItemId(int position) {
        IDrawerItem item = getItem(position);
        if (item != null && item.getIdentifier() != -1) {
            return item.getIdentifier();
        }
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;

        itemCount = itemCount + getHeaderItemCount();
        itemCount = itemCount + getDrawerItemCount();
        itemCount = itemCount + getFooterItemCount();

        return itemCount;
    }

    public int getHeaderOffset() {
        return getHeaderItemCount();
    }

    protected int getHeaderItemCount() {
        return mHeaderDrawerItems == null ? 0 : mHeaderDrawerItems.size();
    }

    protected int getDrawerItemCount() {
        return mDrawerItems == null ? 0 : mDrawerItems.size();
    }

    protected int getFooterItemCount() {
        return mFooterDrawerItems == null ? 0 : mFooterDrawerItems.size();
    }

    public interface OnClickListener {
        void onClick(View v, int position, IDrawerItem item);
    }

    public interface OnLongClickListener {
        boolean onLongClick(View v, int position, IDrawerItem item);
    }
}
