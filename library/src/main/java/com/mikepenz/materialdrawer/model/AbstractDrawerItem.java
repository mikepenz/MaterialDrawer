package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Identifyable;
import com.mikepenz.materialdrawer.model.interfaces.Selectable;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;
import com.mikepenz.materialdrawer.model.utils.ViewHolderFactory;

/**
 * Created by mikepenz on 14.07.15.
 */
public abstract class AbstractDrawerItem<T> implements IDrawerItem, Identifyable<T>, Selectable<T>, Tagable<T> {
    private int mIdentifier = -1;

    public T withIdentifier(int identifier) {
        this.mIdentifier = identifier;
        return (T) this;
    }

    @Override
    public int getIdentifier() {
        return mIdentifier;
    }

    private Object mTag;

    public T withTag(Object object) {
        this.mTag = object;
        return (T) this;
    }

    @Override
    public Object getTag() {
        return mTag;
    }

    private boolean mEnabled = true;

    public T withEnabled(boolean enabled) {
        this.mEnabled = enabled;
        return (T) this;
    }

    @Override
    public boolean isEnabled() {
        return mEnabled;
    }

    private boolean mSelected = false;

    @Override
    public void withSetSelected(boolean selected) {
        this.mSelected = selected;
    }

    @Override
    public boolean isSelected() {
        return mSelected;
    }

    private boolean mSelectable = true;

    @Override
    public T withSelectable(boolean selectable) {
        this.mSelectable = selectable;
        return (T) this;
    }

    @Override
    public boolean isSelectable() {
        return mSelectable;
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
}
