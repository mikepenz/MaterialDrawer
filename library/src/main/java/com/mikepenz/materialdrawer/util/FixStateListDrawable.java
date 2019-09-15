package com.mikepenz.materialdrawer.util;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * http://stackoverflow.com/questions/7979440/android-cloning-a-drawable-in-order-to-make-a-statelistdrawable-with-filters
 * http://stackoverflow.com/users/2075875/malachiasz
 */

@SuppressLint("InlinedApi")
public class FixStateListDrawable extends StateListDrawable {

    private ColorStateList color;

    public FixStateListDrawable(Drawable drawable, ColorStateList color) {
        super();

        drawable = drawable.mutate();

        addState(new int[]{android.R.attr.state_selected}, drawable);
        addState(new int[]{}, drawable);

        this.color = color;
    }

    public FixStateListDrawable(Drawable drawable, Drawable selectedDrawable, ColorStateList color) {
        super();

        drawable = drawable.mutate();
        selectedDrawable = selectedDrawable.mutate();

        addState(new int[]{android.R.attr.state_selected}, selectedDrawable);
        addState(new int[]{}, drawable);

        this.color = color;
    }

    @Override
    protected boolean onStateChange(int[] states) {
        super.setColorFilter(color.getColorForState(states, color.getDefaultColor()), PorterDuff.Mode.SRC_IN);
        return super.onStateChange(states);
    }

    @Override
    public boolean isStateful() {
        return true;
    }
}