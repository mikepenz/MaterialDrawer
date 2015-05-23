package com.mikepenz.materialdrawer.adapter;

import android.widget.BaseAdapter;

import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public abstract class BaseDrawerAdapter extends BaseAdapter {

    public void dataUpdated() {
        mapTypes();
        notifyDataSetChanged();
    }

    public void mapTypes() {
        if (getTypeMapper() == null) {
            setTypeMapper(new LinkedHashSet<String>());
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
            int i = 0;
            for (String type : getTypeMapper()) {
                if (type.equals(getDrawerItems().get(position).getType())) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    @Override
    public int getViewTypeCount() {
        //WTF this is only called once ?!
        //This means for now i only allow 50 viewtTypes^
        return 50;
        /*
        if (getTypeMapper() != null) {
            return getTypeMapper().size();
        } else {
            return -1;
        }
        */
    }


    public abstract ArrayList<IDrawerItem> getDrawerItems();

    public abstract void setDrawerItems(ArrayList<IDrawerItem> drawerItems);

    public abstract LinkedHashSet<String> getTypeMapper();

    public abstract void setTypeMapper(LinkedHashSet<String> typeMapper);

    public abstract void resetAnimation();
}
