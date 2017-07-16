package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.OnPostBindViewListener;
import com.mikepenz.materialdrawer.model.interfaces.Selectable;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mikepenz on 14.07.15.
 */
public abstract class AbstractDrawerItem<T, VH extends RecyclerView.ViewHolder> implements IDrawerItem<T, VH>, Selectable<T>, Tagable<T> {
    // the identifier for this item
    protected long mIdentifier = -1;

    /**
     * set the identifier of this item
     *
     * @param identifier
     * @return
     */
    public T withIdentifier(long identifier) {
        this.mIdentifier = identifier;
        return (T) this;
    }

    /**
     * returns the identifier of this item
     * -1 is the default not set state
     *
     * @return
     */
    @Override
    public long getIdentifier() {
        return mIdentifier;
    }

    // the tag for this item
    protected Object mTag;

    /**
     * set the tag of this item
     *
     * @param object
     * @return
     */
    public T withTag(Object object) {
        this.mTag = object;
        return (T) this;
    }

    /**
     * @return the tag of this item
     */
    @Override
    public Object getTag() {
        return mTag;
    }

    // defines if this item is enabled
    protected boolean mEnabled = true;

    /**
     * set if this item is enabled
     *
     * @param enabled true if this item is enabled
     * @return
     */
    public T withEnabled(boolean enabled) {
        this.mEnabled = enabled;
        return (T) this;
    }

    /**
     * @return if this item is enabled
     */
    @Override
    public boolean isEnabled() {
        return mEnabled;
    }

    // defines if the item is selected
    protected boolean mSelected = false;

    /**
     * set if this item is selected
     *
     * @param selected true if this item is selected
     * @return
     */
    @Override
    public T withSetSelected(boolean selected) {
        this.mSelected = selected;
        return (T) this;
    }

    /**
     * @return if this item is selected
     */
    @Override
    public boolean isSelected() {
        return mSelected;
    }

    // defines if this item is selectable
    protected boolean mSelectable = true;

    /**
     * set if this item is selectable
     *
     * @param selectable true if this item is selectable
     * @return
     */
    @Override
    public T withSelectable(boolean selectable) {
        this.mSelectable = selectable;
        return (T) this;
    }

    /**
     * @return if this item is selectable
     */
    @Override
    public boolean isSelectable() {
        return mSelectable;
    }

    // defines if the item's background' change should be animated when it is (de)selected
    protected boolean mSelectedBackgroundAnimated = true;

    /**
     * set if this item is selectable
     *
     * @param selectedBackgroundAnimated true if this item's background should fade when it is (de) selected
     * @return
     */
    public T withSelectedBackgroundAnimated(boolean selectedBackgroundAnimated) {
        this.mSelectedBackgroundAnimated = selectedBackgroundAnimated;
        return (T) this;
    }

    /**
     * @return if this item is selectable
     */
    public boolean isSelectedBackgroundAnimated() {
        return mSelectedBackgroundAnimated;
    }

    public Drawer.OnDrawerItemClickListener mOnDrawerItemClickListener = null;

    public Drawer.OnDrawerItemClickListener getOnDrawerItemClickListener() {
        return mOnDrawerItemClickListener;
    }

    /**
     * this listener is called when an item is clicked in the drawer.
     * WARNING: don't overwrite this in the Switch / Toggle drawerItems if you want the toggle / switch to be selected
     * if the item is clicked and the item is not selectable.
     *
     * @param onDrawerItemClickListener
     * @return
     */
    public T withOnDrawerItemClickListener(Drawer.OnDrawerItemClickListener onDrawerItemClickListener) {
        this.mOnDrawerItemClickListener = onDrawerItemClickListener;
        return (T) this;
    }

    protected OnPostBindViewListener mOnPostBindViewListener = null;

    public OnPostBindViewListener getOnPostBindViewListener() {
        return mOnPostBindViewListener;
    }

    /**
     * add this listener and hook in if you want to modify a drawerItems view without creating a custom drawer item
     *
     * @param onPostBindViewListener
     * @return
     */
    public T withPostOnBindViewListener(OnPostBindViewListener onPostBindViewListener) {
        this.mOnPostBindViewListener = onPostBindViewListener;
        return (T) this;
    }

    /**
     * is called after bindView to allow some post creation setps
     *
     * @param drawerItem the drawerItem which is bound to the view
     * @param view       the currently view which will be bound
     */
    public void onPostBindView(IDrawerItem drawerItem, View view) {
        if (mOnPostBindViewListener != null) {
            mOnPostBindViewListener.onBindView(drawerItem, view);
        }
    }

    // the parent of this item
    private IDrawerItem mParent;

    /**
     * @return the parent of this item
     */
    @Override
    public IDrawerItem getParent() {
        return mParent;
    }

    /**
     * the parent for this item
     *
     * @param parent it's parent
     * @return this
     */
    @Override
    public IDrawerItem withParent(IDrawerItem parent) {
        this.mParent = parent;
        return this;
    }

    // the subItems to expand for this item
    protected List<IDrawerItem> mSubItems;

    /**
     * a list of subItems
     * **WARNING** Make sure the subItems provided already have identifiers
     *
     * @param subItems
     * @return
     */
    public T withSubItems(List<IDrawerItem> subItems) {
        this.mSubItems = subItems;
        return (T) this;
    }

    /**
     * an array of subItems
     * **WARNING** Make sure the subItems provided already have identifiers
     *
     * @param subItems
     * @return
     */
    public T withSubItems(IDrawerItem... subItems) {
        if (mSubItems == null) {
            mSubItems = new ArrayList<>();
        }
        Collections.addAll(mSubItems, subItems);
        return (T) this;
    }

    /**
     * @return the subItems for this item
     */
    @Override
    public List<IDrawerItem> getSubItems() {
        return mSubItems;
    }

    //if the this item is currently expanded
    private boolean mExpanded = false;

    /**
     * @param expanded defines if this item is now expanded or not
     * @return this
     */
    @Override
    public T withIsExpanded(boolean expanded) {
        mExpanded = expanded;
        return (T) this;
    }

    /**
     * @return if this item is currently expaneded
     */
    @Override
    public boolean isExpanded() {
        return mExpanded;
    }


    /**
     * overwrite this method and return true if the item should auto expand on click, false if you want to disable this
     *
     * @return true if this item should auto expand in the adapter
     */
    @Override
    public boolean isAutoExpanding() {
        return true;
    }

    /**
     * generates a view by the defined LayoutRes
     *
     * @param ctx
     * @return
     */
    @Override
    public View generateView(Context ctx) {
        VH viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), null, false));
        bindView(viewHolder, Collections.emptyList());
        return viewHolder.itemView;
    }

    /**
     * generates a view by the defined LayoutRes and pass the LayoutParams from the parent
     *
     * @param ctx
     * @param parent
     * @return
     */
    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        VH viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false));
        bindView(viewHolder, Collections.emptyList());
        return viewHolder.itemView;
    }

    @CallSuper
    @Override
    public void bindView(VH holder, List<Object> payloads) {
        holder.itemView.setTag(this);
    }

    /**
     * called when the view is unbound
     *
     * @param holder
     */
    @Override
    public void unbindView(VH holder) {

    }

    /**
     * View got attached to the window
     *
     * @param holder
     */
    @Override
    public void attachToWindow(VH holder) {

    }

    /**
     * View got detached from the window
     *
     * @param holder
     */
    @Override
    public void detachFromWindow(VH holder) {

    }

    /**
     * is called when the ViewHolder is in a transient state. return true if you want to reuse
     * that view anyways
     *
     * @param holder the viewHolder for the view which failed to recycle
     * @return true if we want to recycle anyways (false - it get's destroyed)
     */
    @Override
    public boolean failedToRecycle(VH holder) {
        return false;
    }

    /**
     * This method returns the ViewHolder for our item, using the provided View.
     *
     * @param parent
     * @return the ViewHolder for this Item
     */
    @Override
    public VH getViewHolder(ViewGroup parent) {
        return getViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
    }

    /**
     * This method returns the ViewHolder for our item, using the provided View.
     *
     * @param v
     * @return the ViewHolder for this Item
     */
    public abstract VH getViewHolder(View v);

    /**
     * If this item equals to the given identifier
     *
     * @param id
     * @return
     */
    @Override
    public boolean equals(long id) {
        return id == mIdentifier;
    }

    public boolean equals(int id) {
        return id == mIdentifier;
    }

    /**
     * If this item equals to the given object
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDrawerItem<?, ?> that = (AbstractDrawerItem<?, ?>) o;
        return mIdentifier == that.mIdentifier;
    }

    /**
     * the hashCode implementation
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Long.valueOf(mIdentifier).hashCode();
    }
}
