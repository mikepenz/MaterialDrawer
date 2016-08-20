package com.mikepenz.materialdrawer.model.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.util.StateSet;

import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.ColorHolder;

/**
 * Created by mikepenz on 02.07.15.
 */
public class BadgeDrawableBuilder {
    private BadgeStyle mStyle;

    public BadgeDrawableBuilder(BadgeStyle style) {
        this.mStyle = style;
    }

    public StateListDrawable build(Context ctx) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        GradientDrawable normal = (GradientDrawable) ContextCompat.getDrawable(ctx, mStyle.getGradientDrawable());
        GradientDrawable selected = (GradientDrawable) normal.getConstantState().newDrawable().mutate();

        ColorHolder.applyToOrTransparent(mStyle.getColor(), ctx, normal);
        if (mStyle.getColorPressed() == null) {
            ColorHolder.applyToOrTransparent(mStyle.getColor(), ctx, selected);
        } else {
            ColorHolder.applyToOrTransparent(mStyle.getColorPressed(), ctx, selected);
        }

        if (mStyle.getCorners() != null) {
            normal.setCornerRadius(mStyle.getCorners().asPixel(ctx));
            selected.setCornerRadius(mStyle.getCorners().asPixel(ctx));
        }

        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, selected);
        stateListDrawable.addState(StateSet.WILD_CARD, normal);

        return stateListDrawable;
    }
}
