package com.mikepenz.materialdrawer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Selectable;

import java.util.ArrayList;
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
            notifyItemRangeInserted(length, length + drawerItems.length);
        }
        mapPossibleTypes(mDrawerItems);
    }

    public void setDrawerItem(int position, IDrawerItem drawerItem) {
        mDrawerItems.set(position, drawerItem);
        notifyItemChanged(position);
        mapPossibleType(drawerItem);
    }

    public void addDrawerItem(IDrawerItem drawerItem) {
        mDrawerItems.add(drawerItem);
        notifyItemInserted(mDrawerItems.size());
        mapPossibleType(drawerItem);
    }

    public void addDrawerItem(int position, IDrawerItem drawerItem) {
        mDrawerItems.add(position, drawerItem);
        notifyItemInserted(position);
        mapPossibleType(drawerItem);
    }

    public void removeDrawerItem(int position) {
        mDrawerItems.remove(position);
        notifyItemRemoved(position);
    }

    public void clearDrawerItems() {
        int count = mDrawerItems.size();
        mDrawerItems.clear();
        notifyItemRangeRemoved(getHeaderItemCount(), count);
    }

    public ArrayList<IDrawerItem> getHeaderDrawerItems() {
        return mHeaderDrawerItems;
    }

    public void setHeaderDrawerItems(ArrayList<IDrawerItem> mHeaderDrawerItems) {
        this.mHeaderDrawerItems = mHeaderDrawerItems;
        mapPossibleTypes(mHeaderDrawerItems);
    }

    public void addHeaderDrawerItems(IDrawerItem... drawerItems) {
        if (drawerItems != null) {
            Collections.addAll(mHeaderDrawerItems, drawerItems);
        }
        mapPossibleTypes(mHeaderDrawerItems);
    }

    public ArrayList<IDrawerItem> getFooterDrawerItems() {
        return mFooterDrawerItems;
    }

    public void setFooterDrawerItems(ArrayList<IDrawerItem> mFooterDrawerItems) {
        this.mFooterDrawerItems = mFooterDrawerItems;
        mapPossibleTypes(mFooterDrawerItems);
    }

    public void addFooterDrawerItems(IDrawerItem... drawerItems) {
        if (drawerItems != null) {
            Collections.addAll(mFooterDrawerItems, drawerItems);
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
        return mTypeInstances.get(mTypeIds.get(viewType)).getViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        getItem(position).bindView(holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                IDrawerItem drawerItem = getItem(pos);

                if (drawerItem.isEnabled()) {
                    if (drawerItem instanceof Selectable) {
                        if (((Selectable) drawerItem).isSelectable()) {
                            handleSelection(v, pos);
                        }
                    }

                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(v, pos, drawerItem);
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

    public void handleSelection(View v, int pos) {
        if (previousSelection == pos) {
            return;
        }

        //deselect the previous item
        if (previousSelection > -1) {
            IDrawerItem prev = getItem(previousSelection);
            if (prev != null) {
                prev.withSetSelected(false);
            }
            notifyItemChanged(previousSelection);
        }

        //highlight the new item
        if (pos > -1) {
            IDrawerItem cur = getItem(pos);
            if (cur != null) {
                cur.withSetSelected(true);
            }
            notifyItemChanged(pos);
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
        if (position < 0 || position > getItemCount()) {
            return null;
        }

        if (position < getHeaderItemCount()) {
            return mHeaderDrawerItems.get(position);
        } else if (position < getHeaderItemCount() + getDrawerItemCount()) {
            return mDrawerItems.get(position - getHeaderItemCount());
        } else {
            return mFooterDrawerItems.get(position - getHeaderItemCount() - getDrawerItemCount());
        }
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

    public int getHeaderItemCount() {
        return mHeaderDrawerItems == null ? 0 : mHeaderDrawerItems.size();
    }

    public int getDrawerItemCount() {
        return mDrawerItems == null ? 0 : mDrawerItems.size();
    }

    public int getFooterItemCount() {
        return mFooterDrawerItems == null ? 0 : mFooterDrawerItems.size();
    }

    public interface OnClickListener {
        void onClick(View v, int position, IDrawerItem item);
    }

    public interface OnLongClickListener {
        boolean onLongClick(View v, int position, IDrawerItem item);
    }
}
