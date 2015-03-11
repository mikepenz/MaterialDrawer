package com.mikepenz.materialdrawer.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;
import com.mikepenz.materialdrawer.util.UIUtils;


/**
 * Created at 11.03.15 21:06
 * @author RustamG
 */
public class SectionDividerDrawerItem
        implements IDrawerItem, Tagable<SectionDividerDrawerItem> {

    private Object tag;


    public SectionDividerDrawerItem withTag(Object object) {

        this.tag = object;
        return this;
    }


    @Override
    public Object getTag() {

        return tag;
    }

    @Override
    public void setTag(Object tag) {

        this.tag = tag;
    }

    @Override
    public int getIdentifier() {

        return -1;
    }

    @Override
    public boolean isEnabled() {

        return false;
    }

    @Override
    public String getType() {

        return "SECTION_DIVIDER_ITEM";
    }

    @Override
    public int getLayoutRes() {

        return R.layout.material_drawer_divider_item_section;
    }

    @Override
    public View convertView(LayoutInflater inflater, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(getLayoutRes(), parent, false);
            convertView.setBackgroundColor(
                    UIUtils.getThemeColorFromAttrOrRes(parent.getContext(), R.attr.material_drawer_divider,
                            R.color.material_drawer_divider));
        }

        return convertView;
    }
}
