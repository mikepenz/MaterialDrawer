package com.mikepenz.materialdrawer.model.interfaces;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.IItem;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface IDrawerItem<T, VH extends RecyclerView.ViewHolder> extends IItem<T, VH> {

    Object getTag();

    boolean isEnabled();

    boolean isSelected();

    T withSetSelected(boolean selected);

    boolean isSelectable();

    T withSelectable(boolean selectable);

    int getType();

    int getLayoutRes();

    View generateView(Context ctx);

    View generateView(Context ctx, ViewGroup parent);

    VH getViewHolder(ViewGroup parent);

    void bindView(VH holder);

    boolean equals(long id);
}
