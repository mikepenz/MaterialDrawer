package com.mikepenz.materialdrawer.util;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * http://stackoverflow.com/questions/7979440/android-cloning-a-drawable-in-order-to-make-a-statelistdrawable-with-filters
 * http://stackoverflow.com/users/2075875/malachiasz
 */

@SuppressLint("InlinedApi")
public class PressedEffectStateListDrawable extends StateListDrawable {

    private int color;
    private int selectionColor;

    public PressedEffectStateListDrawable(Drawable drawable, int color, int selectionColor) {
        super();

        drawable = drawable.mutate();

        addState(new int[]{android.R.attr.state_selected}, drawable);
        addState(new int[]{}, drawable);

        this.color = color;
        this.selectionColor = selectionColor;
    }

    @Override
    protected boolean onStateChange(int[] states) {
        boolean isStatePressedInArray = false;
        for (int state : states) {
            if (state == android.R.attr.state_selected) {
                isStatePressedInArray = true;
            }
        }
        if (isStatePressedInArray) {
            super.setColorFilter(selectionColor, PorterDuff.Mode.SRC_IN);
        } else {
            super.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
        return super.onStateChange(states);
    }

    @Override
    public boolean isStateful() {
        return true;
    }
}