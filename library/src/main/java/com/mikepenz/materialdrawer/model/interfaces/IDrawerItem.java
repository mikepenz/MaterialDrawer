package com.mikepenz.materialdrawer.model.interfaces;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface IDrawerItem<VH extends RecyclerView.ViewHolder> extends IItem<VH>, IExpandable<VH> {

    Object getTag();

    boolean isEnabled();

    boolean isSelected();

    void setSelected(boolean selected);

    boolean isSelectable();

    void setSelectable(boolean selectable);

    int getType();

    int getLayoutRes();

    View generateView(Context ctx);

    View generateView(Context ctx, ViewGroup parent);

    VH getViewHolder(ViewGroup parent);

    void unbindView(VH holder);

    void bindView(VH holder, List<Object> payloads);

    boolean equals(long id);
}
