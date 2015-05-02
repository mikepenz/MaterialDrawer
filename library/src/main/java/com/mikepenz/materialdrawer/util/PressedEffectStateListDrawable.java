package com.mikepenz.materialdrawer.util;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * http://stackoverflow.com/questions/7979440/android-cloning-a-drawable-in-order-to-make-a-statelistdrawable-with-filters
 * http://stackoverflow.com/users/2075875/malachiasz
 */

@SuppressLint("InlinedApi")
public class PressedEffectStateListDrawable extends StateListDrawable {

    private int selectionColor;

    public PressedEffectStateListDrawable(Drawable drawable, int selectionColor) {
        super();
        this.selectionColor = selectionColor;
        addState(new int[]{android.R.attr.state_activated}, drawable);
        addState(new int[]{}, drawable);
    }

    @Override
    protected boolean onStateChange(int[] states) {
        boolean isStatePressedInArray = false;
        for (int state : states) {
            if (state == android.R.attr.state_activated) {
                isStatePressedInArray = true;
            }
        }
        if (isStatePressedInArray) {
            super.setColorFilter(selectionColor, PorterDuff.Mode.SRC_ATOP);
            super.setAlpha(Color.alpha(selectionColor));
        } else {
            super.clearColorFilter();
            super.setAlpha(255);
        }
        return super.onStateChange(states);
    }

    @Override
    public boolean isStateful() {
        return true;
    }
}