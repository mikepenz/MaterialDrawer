package com.mikepenz.materialdrawer.app.customMiniDrawer;

import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.model.CustomMiniDrawerItem;
import com.mikepenz.materialdrawer.model.MiniProfileDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by mikepenz on 07.10.15.
 */
public class CustomMiniDrawer extends MiniDrawer {
    @Override
    public IDrawerItem generateMiniDrawerItem(IDrawerItem drawerItem) {
        if (drawerItem instanceof PrimaryDrawerItem) {
            return new CustomMiniDrawerItem((PrimaryDrawerItem) drawerItem);
        }
        return null;
    }

    @Override
    public int getMiniDrawerType(IDrawerItem drawerItem) {
        if (drawerItem instanceof MiniProfileDrawerItem) {
            return PROFILE;
        } else if (drawerItem instanceof CustomMiniDrawerItem) {
            return ITEM;
        }
        return -1;
    }
}
