package com.mikepenz.materialdrawer.model.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public interface ViewHolderFactory<T extends RecyclerView.ViewHolder> {
    T factory(View v);
}