package com.mikepenz.materialdrawer.adapter;

import android.widget.BaseAdapter;

import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDrawerAdapter extends BaseAdapter {

    public void dataUpdated() {
        mapTypes();
        notifyDataSetChanged();
    }

    public void mapTypes() {
        if (getTypeMapper() == null) {
            setTypeMapper(new ArrayList<String>());
        }

        if (getDrawerItems() != null) {
            for (IDrawerItem drawerItem : getDrawerItems()) {
                getTypeMapper().add(drawerItem.getType());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getDrawerItems() != null && getDrawerItems().size() > position && getTypeMapper() != null) {
            return getTypeMapper().indexOf(getDrawerItems().get(position).getType());
        } else {
            return -1;
        }

    }

    @Override
    public int getViewTypeCount() {
        if (getTypeMapper() != null) {
            return getTypeMapper().size();
        } else {
            return -1;
        }
    }


    public abstract ArrayList<IDrawerItem> getDrawerItems();

    public abstract void setDrawerItems(ArrayList<IDrawerItem> drawerItems);

    public abstract List<String> getTypeMapper();

    public abstract void setTypeMapper(List<String> typeMapper);
}
