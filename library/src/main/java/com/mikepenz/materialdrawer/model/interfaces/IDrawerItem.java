package com.mikepenz.materialdrawer.model.interfaces;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface IDrawerItem<T> extends Identifyable<T> {

    Object getTag();

    boolean isEnabled();

    boolean isSelected();

    T withSetSelected(boolean selected);

    boolean isSelectable();

    T withSelectable(boolean selectable);

    String getType();

    int getLayoutRes();

    View generateView(Context ctx);

    View generateView(Context ctx, ViewGroup parent);

    RecyclerView.ViewHolder getViewHolder(ViewGroup parent);

    void bindView(RecyclerView.ViewHolder holder);

    boolean equals(Integer id);

    boolean equals(Object o);
}
