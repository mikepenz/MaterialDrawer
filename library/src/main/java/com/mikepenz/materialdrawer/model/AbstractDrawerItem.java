package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.OnPostBindViewListener;
import com.mikepenz.materialdrawer.model.interfaces.Selectable;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;
import com.mikepenz.materialdrawer.model.utils.ViewHolderFactory;

/**
 * Created by mikepenz on 14.07.15.
 */
public abstract class AbstractDrawerItem<T> implements IDrawerItem<T>, Selectable<T>, Tagable<T> {
    protected int mIdentifier = -1;

    public T withIdentifier(int identifier) {
        this.mIdentifier = identifier;
        return (T) this;
    }

    @Override
    public int getIdentifier() {
        return mIdentifier;
    }

    protected Object mTag;

    public T withTag(Object object) {
        this.mTag = object;
        return (T) this;
    }

    @Override
    public Object getTag() {
        return mTag;
    }

    protected boolean mEnabled = true;

    public T withEnabled(boolean enabled) {
        this.mEnabled = enabled;
        return (T) this;
    }

    @Override
    public boolean isEnabled() {
        return mEnabled;
    }

    protected boolean mSelected = false;

    @Override
    public T withSetSelected(boolean selected) {
        this.mSelected = selected;
        return (T) this;
    }

    @Override
    public boolean isSelected() {
        return mSelected;
    }

    protected boolean mSelectable = true;

    @Override
    public T withSelectable(boolean selectable) {
        this.mSelectable = selectable;
        return (T) this;
    }

    @Override
    public boolean isSelectable() {
        return mSelectable;
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

    public abstract ViewHolderFactory getFactory();

    @Override
    public View generateView(Context ctx) {
        RecyclerView.ViewHolder viewHolder = getFactory().factory(LayoutInflater.from(ctx).inflate(getLayoutRes(), null, false));
        bindView(viewHolder);
        return viewHolder.itemView;
    }

    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        RecyclerView.ViewHolder viewHolder = getFactory().factory(LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false));
        bindView(viewHolder);
        return viewHolder.itemView;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
        return getFactory().factory(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
    }

    public boolean equals(Integer id) {
        return id != null && id == mIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDrawerItem<?> that = (AbstractDrawerItem<?>) o;
        return mIdentifier == that.mIdentifier;
    }

    @Override
    public int hashCode() {
        return mIdentifier;
    }
}
