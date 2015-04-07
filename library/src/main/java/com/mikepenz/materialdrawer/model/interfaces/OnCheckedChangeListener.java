package com.mikepenz.materialdrawer.model.interfaces;

import android.widget.CompoundButton;

/**
 * Interface definition for a callback to be invoked when the checked state
 * of a compound button changed.
 */
public interface OnCheckedChangeListener {
    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked);
}